package mytimeacty.service;

import java.util.List;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import mytimeacty.model.users.enums.UserRole;
import mytimeacty.repository.FollowerRepository;
import mytimeacty.repository.UserRepository;
import mytimeacty.repository.quizz.QuizzLikeRepository;
import mytimeacty.service.Bcrypt.BcryptService;
import mytimeacty.utils.PaginationUtils;
import mytimeacty.utils.SecurityUtils;
import mytimeacty.mapper.UserMapper;
import java.util.concurrent.CompletableFuture;

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
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    

    /**
     * Creates a new user with the provided details.
     *
     * @param userCreateDTO the DTO containing the details of the user to be created
     * @return the created UserDTO
     * @throws UserAlreadyExistsException if the email or nickname is already in use
     */
    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        if (userRepository.findByEmailIgnoreCase(userCreateDTO.getEmail()).isPresent()) {
        	logger.warn("Method createUser: Email '{}' already in use)", userCreateDTO.getEmail());
            throw new UserAlreadyExistsException("Email already in use");
        }
        
        if (userRepository.findByNicknameIgnoreCase(userCreateDTO.getNickname()).isPresent()) {
        	logger.warn("Method createUser: Nickname '{}' already in use)", userCreateDTO.getNickname());
            throw new UserAlreadyExistsException("Nickname already in use");
        }
        
    	
        User user = User.builder()
                        .email(userCreateDTO.getEmail())
                        .nickname(userCreateDTO.getNickname())
                        .password(bcryptService.encodePassword(userCreateDTO.getPassword()))
                        .userRole("user")
                        .userPreviousRole("user")
                        .isActivated(false)
                        .build();
        
        User savedUser = userRepository.save(user);
        
        UserDTO userDTO = UserMapper.toDTO(savedUser);
        
        logger.info("Method createUser: User with nickname '{}' created sucessfully",
        		userCreateDTO.getNickname());
        
        return userDTO;
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
    	UserDTO currentUserDTO = SecurityUtils.getCurrentUser();
    	logger.info("Entering method getFilteredUsers: User '{}'", currentUserDTO.getNickname());
    	
        Pageable pageable = PaginationUtils.createPageableSortByAsc(page, size, "nickname");
        List<User> users = userRepository.findByNicknameContainingIgnoreCase(nickname.trim(), pageable);
        
        User currentUser = this.getUserByIdData(currentUserDTO.getIdUser());
        
        List<CompletableFuture<UserDetailsDTO>> futures = users.stream()
                .filter(user -> !user.getIdUser().equals(currentUser.getIdUser()))
                .map(user -> CompletableFuture.supplyAsync(() -> {
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
                }))
                .collect(Collectors.toList());
        
        List<UserDetailsDTO> userDetailsDTOs = futures.stream()
                .map(CompletableFuture::join) // Wait for all futures to complete
                .collect(Collectors.toList());
        
        Page<UserDetailsDTO> pageUserDetailsDTO = new PageImpl<>(userDetailsDTOs, pageable, userDetailsDTOs.size());
        
        logger.info("Method getFilteredUsers: Users retrieved sucessfully filtered by nickname '{}'", nickname);
        
        return pageUserDetailsDTO;
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
    			.orElseThrow(() -> {
    				logger.warn("Method getUserByIdData: User with ID {} not found",
    						userId);
    				return new UserNotFoundException("User not found");
    			});
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
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method getUserProfile: User '{}'", currentUserNickname);
    	
        User user = userRepository.findById(userId)
		        .orElseThrow(() -> {
					logger.warn("Method getUserProfile: User with ID {} not found. Current User nickname: {}",
							userId, currentUserNickname);
					return new UserNotFoundException("User not found");
				});

        // get the number of followers and subscriptions
        int followersCount = (int) followerRepository.countByUserFollowed(user);
        int followingCount = (int) followerRepository.countByFollower(user);

        UserProfileDTO userProfile = UserProfileDTO.builder()
                .userId(user.getIdUser())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .followersCount(followersCount)
                .followingCount(followingCount)
                .build();
        
        logger.info("Method getUserProfile: User profile with ID {} retrieved sucessfully. Current User nickname: {}", userId, currentUserNickname);
        
        return userProfile;
    }
    
    /**
     * Bans a user, changing their role to "banned".
     *
     * @param userId the ID of the user to ban
     * @throws ForbiddenException if the current user does not have permission to ban the target user
     */
    public void banUser(int userId) {
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method banUser: User '{}'", currentUserNickname);
    	
        User user = this.getUserByIdData(userId);
        User currentUser = this.getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
        
        validateRoleChangeBanAndUban(currentUser, user, "ban");
        
        updateUserRole(user, UserRole.BANNED.getRole());
        
        logger.info("Method banUser: User with ID {} banned sucessfully. Current User nickname: {}", userId, currentUserNickname);
    }

    /**
     * Unbans a user, restoring their previous role.
     *
     * @param userId the ID of the user to unban
     * @throws ForbiddenException if the current user does not have permission to unban the target user
     */
	public void unbanUser(int userId) {
		String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method unbanUser: User '{}'", currentUserNickname);
    	
		User user = this.getUserByIdData(userId);
        User currentUser = this.getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
        
        validateRoleChangeBanAndUban(currentUser, user, "unban");
        
        updateUserRole(user, UserRole.fromString(user.getUserPreviousRole()).getRole());
        
        logger.info("Method unbanUser: User with ID {} unbanned sucessfully and his previous role is restored. Current User nickname: {}", 
        		userId, currentUserNickname);
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
	    	logger.warn("Method validateRoleChangeBanAndUban: You cannot change your own role. Current User nickname: {}",
        			currentUser.getNickname());
	        throw new ForbiddenException("You cannot change your own role");
	    }

	    if (currentUser.getUserRole().equals(UserRole.USER.getRole())) {
	    	logger.warn("Method validateRoleChangeBanAndUban: Users are not allowed to {} users. Current User nickname: {}",
        			action, currentUser.getNickname());
	        throw new ForbiddenException("Users are not allowed to " + action + " users");
	    }

	    // Role-specific checks
	    if (action.equals("ban")) {
	    	if(targetUser.getUserRole().equals(UserRole.BANNED.getRole())) {
	    		logger.warn("Method validateRoleChangeBanAndUban: User with ID {} is already banned. Current User nickname: {}",
	        			targetUser.getIdUser(), currentUser.getNickname());
	        	throw new ForbiddenException("User is already banned");
	    	}
	    	
	        if (currentUser.getUserRole().equals(UserRole.ADMIN.getRole()) && 
	            (targetUser.getUserRole().equals(UserRole.ADMIN.getRole()) || targetUser.getUserRole().equals(UserRole.CHIEF.getRole()))) {
	        	logger.warn("Method validateRoleChangeBanAndUban: Admins can only ban users. Current User nickname: {}",
	        			currentUser.getNickname());
	            throw new ForbiddenException("Admins can only ban users");
	        }
	        
	        if (currentUser.getUserRole().equals(UserRole.CHIEF.getRole()) && targetUser.getUserRole().equals(UserRole.CHIEF.getRole())) {
	        	logger.warn("Method validateRoleChangeBanAndUban: Chiefs can only ban users and admins. Current User nickname: {}",
	        			currentUser.getNickname());
	            throw new ForbiddenException("Chiefs can only ban users and admins");
	        }
	    } else if (action.equals("unban")) {
	    	if(!targetUser.getUserRole().equals(UserRole.BANNED.getRole())) {
	    		logger.warn("Method validateRoleChangeBanAndUban: User with ID {} is not banned. Current User nickname: {}",
	    				targetUser.getIdUser(), currentUser.getNickname());
	        	throw new ForbiddenException("User is not banned");
	    	}
	    	
	        if (currentUser.getUserRole().equals(UserRole.ADMIN.getRole()) && !targetUser.getUserPreviousRole().equals(UserRole.USER.getRole())) {
	        	logger.warn("Method validateRoleChangeBanAndUban: Admins can only unban users. Current User nickname: {}",
	    				currentUser.getNickname());
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
		String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method promoteUserToAdmin: User '{}'", currentUserNickname);
    	
	    User user = getUserByIdData(userId);
	    User currentUser = getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
	    
	    validateRoleChangePromoteAndDemote(currentUser, user, UserRole.USER.getRole());
	    
	    updateUserRole(user, UserRole.ADMIN.getRole());
	    
	    logger.info("Method promoteUserToAdmin: User with ID {} promoted to admin sucessfully. Current User nickname: {}", 
        		userId, currentUserNickname);
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
		String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method promoteAdminToChief: User '{}'", currentUserNickname);
    	
	    User user = getUserByIdData(userId);
	    User currentUser = getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
	    
	    validateRoleChangePromoteAndDemote(currentUser, user, UserRole.ADMIN.getRole());
	    
	    updateUserRole(user, UserRole.CHIEF.getRole());
	    
	    logger.info("Method promoteAdminToChief: Admin with ID {} promoted to chief sucessfully. Current User nickname: {}", 
        		userId, currentUserNickname);
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
		String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method demoteAdminToUser: User '{}'", currentUserNickname);
    	
	    User user = getUserByIdData(userId);
	    User currentUser = getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
	    
	    validateRoleChangePromoteAndDemote(currentUser, user, UserRole.ADMIN.getRole());
	    
	    updateUserRole(user, UserRole.USER.getRole());
	    
	    logger.info("Method demoteAdminToUser: Admin with ID {} demoted to user sucessfully. Current User nickname: {}", 
        		userId, currentUserNickname);
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
        	logger.warn("Method validateRoleChangePromoteAndDemote: You cannot change your own role. Current User nickname: {}",
        			currentUser.getNickname());
            throw new ForbiddenException("You cannot change your own role");
        }

        if (!targetUser.getUserRole().equals(requiredRoleForTarget)) {
        	logger.warn("Method validateRoleChangePromoteAndDemote: Only "+ requiredRoleForTarget + "s can be promoted/demoted to this role. "
        			+ "Current User nickname: {}", currentUser.getNickname());
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
        user.setUserRole(UserRole.fromString(newRole).getRole());
        userRepository.save(user);
    }
    
    public void activateUser(String email) {
    	logger.info("Entering method activateUser: User '{}'", email);
    	
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                	logger.warn("Method activateUser: User not found with email: {}", email);
                	return new UserNotFoundException("User not found with email: " + email);
                });

        user.setIsActivated(true);

        userRepository.save(user);
        
        logger.info("Method activateUser: User '{}' marked as activated sucessfully", email);
    }

}
