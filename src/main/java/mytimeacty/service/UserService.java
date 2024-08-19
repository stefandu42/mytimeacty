package mytimeacty.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mytimeacty.exception.ForbiddenException;
import mytimeacty.exception.NotFoundException;
import mytimeacty.exception.UserAlreadyExistsException;
import mytimeacty.exception.UserNotFoundException;
import mytimeacty.model.users.User;
import mytimeacty.model.users.dto.UserDTO;
import mytimeacty.model.users.dto.UserDetailsDTO;
import mytimeacty.model.users.dto.UserProfileDTO;
import mytimeacty.model.users.dto.creation.UserCreateDTO;
import mytimeacty.repository.FollowerRepository;
import mytimeacty.repository.UserRepository;
import mytimeacty.repository.quizz.QuizzLikeRepository;
import mytimeacty.service.Bcrypt.BcryptService;
import mytimeacty.utils.PaginationUtils;
import mytimeacty.utils.SecurityUtils;
import mytimeacty.mapper.UserMapper;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BcryptService bcryptService;
    
    @Autowired
    private QuizzLikeRepository quizzLikeRepository;
    
    @Autowired
    private FollowerRepository followerRepository;
    

    /**
     * Creates a new user with the provided details.
     *
     * @param userCreateDTO the DTO containing the details of the user to be created
     * @return the created UserDTO
     * @throws UserAlreadyExistsException if the email or nickname is already in use
     */
    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        if (userRepository.findByEmailIgnoreCase(userCreateDTO.getEmail()).isPresent() || 
        		userRepository.findByNicknameIgnoreCase(userCreateDTO.getNickname()).isPresent()) {
            throw new UserAlreadyExistsException("Email or nickname already in use");
        }
        
    	
        User user = User.builder()
                        .email(userCreateDTO.getEmail())
                        .nickname(userCreateDTO.getNickname())
                        .password(bcryptService.encodePassword(userCreateDTO.getPassword()))
                        .userRole("user")
                        .userPreviousRole("user")
                        .build();
        
        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }
    
    /**
     * Get a paginated list of users filtered by nickname.
     *
     * @param nickname the nickname to filter users by
     * @param page the page number to retrieve (zero-based)
     * @param size the number of items per page
     * @return a page of UserDetailsDTO objects
     */
    public Page<UserDetailsDTO> getFilteredUsers(String nickname, int page, int size) {
        Pageable pageable = PaginationUtils.createPageableSortByAsc(page, size, "nickname");
        List<User> users = userRepository.findByNicknameContainingIgnoreCase(nickname.trim(), pageable);
        User currentUser = this.getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
        
        List<UserDetailsDTO> userDetailsDTOs = users.stream()
                .filter(user -> !user.getIdUser().equals(currentUser.getIdUser()))
                .map(user -> {
                    boolean isFollowedByCurrentUser = followerRepository.existsByFollowerAndUserFollowed(currentUser, user);
                    int quizLikesCount = (int) quizzLikeRepository.countByUser(user);
                    int followersCount = (int) followerRepository.countByUserFollowed(user);
                    
                    return UserDetailsDTO.builder()
                            .userId(user.getIdUser())
                            .nickname(user.getNickname())
                            .email(user.getEmail())
                            .isFollowedByCurrentUser(isFollowedByCurrentUser)
                            .quizLikesCount(quizLikesCount)
                            .followersCount(followersCount)
                            .build();
                })
                .collect(Collectors.toList());
        return new PageImpl<>(userDetailsDTOs, pageable, userDetailsDTOs.size());
    }
    
    /**
     * Get a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the User found
     * @throws NotFoundException if the user is not found
     */
    private User getUserByIdData(int userId) {
    	return userRepository.findById(userId)
    			.orElseThrow(() -> new NotFoundException("User not found"));
    }
    
    /**
     * Get a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the UserDTO of the user
     * @throws NotFoundException if the user is not found
     */
    public UserDTO getUserById(int userId) {
	    return UserMapper.toDTO(this.getUserByIdData(userId));
    }
    
    /**
     * Get the profile of a user by their ID.
     *
     * @param userId the ID of the user whose profile to retrieve
     * @return the UserProfileDTO containing profile details
     * @throws UserNotFoundException if the user is not found
     */
    public UserProfileDTO getUserProfile(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // get the number of followers and subscriptions
        int followersCount = (int) followerRepository.countByUserFollowed(user);
        int followingCount = (int) followerRepository.countByFollower(user);

        return UserProfileDTO.builder()
                .userId(user.getIdUser())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .followersCount(followersCount)
                .followingCount(followingCount)
                .build();
    }
    
    /**
     * Bans a user, changing their role to "banned".
     *
     * @param userId the ID of the user to ban
     * @throws ForbiddenException if the current user does not have permission to ban the target user
     */
    public void banUser(int userId) {
        User user = this.getUserByIdData(userId);
        User currentUser = this.getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
        
        validateRoleChangeBanAndUban(currentUser, user, "ban");
        
        updateUserRole(user, "banned");
    }

    /**
     * Unbans a user, restoring their previous role.
     *
     * @param userId the ID of the user to unban
     * @throws ForbiddenException if the current user does not have permission to unban the target user
     */
	public void unbanUser(int userId) {
		User user = this.getUserByIdData(userId);
        User currentUser = this.getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
        
        validateRoleChangeBanAndUban(currentUser, user, "unban");
        
        updateUserRole(user, user.getUserPreviousRole());
	}
	
	/**
     * Validates role changes for user management actions like ban and unban.
     *
     * @param currentUser the current authenticated user
     * @param targetUser the user whose role is being changed
     * @param action the action being performed (e.g., "ban", "unban")
     * @throws ForbiddenException if the current user does not have permission to perform the action
     */
	private void validateRoleChangeBanAndUban(User currentUser, User targetUser, String action) {
	    if (targetUser.getIdUser().equals(currentUser.getIdUser())) {
	        throw new ForbiddenException("You cannot change your own role");
	    }

	    if (currentUser.getUserRole().equals("user")) {
	        throw new ForbiddenException("Users are not allowed to " + action + " users");
	    }

	    // Role-specific checks
	    if (action.equals("ban")) {
	    	if(targetUser.getUserRole().equals("banned"))
	        	throw new ForbiddenException("User is already banned");
	    	
	        if (currentUser.getUserRole().equals("admin") && 
	            (targetUser.getUserRole().equals("admin") || targetUser.getUserRole().equals("chief"))) {
	            throw new ForbiddenException("Admins can only ban users");
	        }
	        
	        if (currentUser.getUserRole().equals("chief") && targetUser.getUserRole().equals("chief")) {
	            throw new ForbiddenException("Chiefs can only ban users and admins");
	        }
	    } else if (action.equals("unban")) {
	    	if(!targetUser.getUserRole().equals("banned"))
	        	throw new ForbiddenException("User is not banned");
	    	
	        if (currentUser.getUserRole().equals("admin") && !targetUser.getUserPreviousRole().equals("user")) {
	            throw new ForbiddenException("Admins can only unban users");
	        }
	    }
	}
	
	/**
	 * Promotes a user to the "admin" role.
	 * 
	 * This method checks if the target user is eligible to be promoted to "admin".
	 * 
	 * @param userId The ID of the user to be promoted to admin.
	 * @throws ForbiddenException If the target user is not eligible for promotion.
	 */
	public void promoteUserToAdmin(int userId) {
	    User user = getUserByIdData(userId);
	    User currentUser = getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
	    
	    validateRoleChangePromoteAndDemote(currentUser, user, "user");
	    
	    updateUserRole(user, "admin");
	}

	/**
	 * Promotes an admin to the "chief" role.
	 * 
	 * This method checks if the target admin is eligible to be promoted to "chief".
	 * 
	 * @param userId The ID of the admin to be promoted to chief.
	 * @throws ForbiddenException If the target admin is not eligible for promotion.
	 */
	public void promoteAdminToChief(int userId) {
	    User user = getUserByIdData(userId);
	    User currentUser = getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
	    
	    validateRoleChangePromoteAndDemote(currentUser, user, "admin");
	    
	    updateUserRole(user, "chief");
	}

	/**
	 * Demotes an admin to the "user" role.
	 * 
	 * This method checks if the target admin is eligible to be demoted to "user".
	 * 
	 * @param userId The ID of the admin to be demoted to user.
	 * @throws ForbiddenException If the target admin is not eligible for demotion.
	 */
	public void demoteAdminToUser(int userId) {
	    User user = getUserByIdData(userId);
	    User currentUser = getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
	    
	    validateRoleChangePromoteAndDemote(currentUser, user, "admin");
	    
	    updateUserRole(user, "user");
	}
    
    /**
     * Validates if the current user can change the target user's role.
     *
     * @param currentUser The current user performing the role change.
     * @param targetUser The target user whose role is being changed.
     * @param requiredRoleForTarget The role required for the target user.
     * @throws ForbiddenException If the role change conditions are not met.
     */
    private void validateRoleChangePromoteAndDemote(User currentUser, User targetUser, String requiredRoleForTarget) {
        if (targetUser.getIdUser().equals(currentUser.getIdUser())) {
            throw new ForbiddenException("You cannot change your own role");
        }

        if (!targetUser.getUserRole().equals(requiredRoleForTarget)) {
            throw new ForbiddenException("Only " + requiredRoleForTarget + "s can be promoted/demoted to this role");
        }
    }
    
    /**
     * Updates the user's role while saving the previous role.
     *
     * @param user The user whose role is being updated.
     * @param newRole The new role to assign to the user.
     */
    private void updateUserRole(User user, String newRole) {
        user.setUserPreviousRole(user.getUserRole());
        user.setUserRole(newRole);
        userRepository.save(user);
    }

}
