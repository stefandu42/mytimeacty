package mytimeacty.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    
    private User getUserByIdData(int userId) {
    	return userRepository.findById(userId)
    			.orElseThrow(() -> new NotFoundException("User not found"));
    }
    
    public UserDTO getUserById(int userId) {
	    return UserMapper.toDTO(this.getUserByIdData(userId));
    }
    
    public UserProfileDTO getUserProfile(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // get the number of followers and subscriptions
        int followersCount = followerRepository.countByUserFollowedIdUser(userId);
        int followingCount = followerRepository.countByFollowerIdUser(userId);

        return UserProfileDTO.builder()
                .userId(user.getIdUser())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .followersCount(followersCount)
                .followingCount(followingCount)
                .build();
    }
    
    public void banUser(int userId) {
        User user = this.getUserByIdData(userId);
        User currentUser = this.getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
        String userCurrentRole = user.getUserRole();
        
        if(user.getIdUser().equals(currentUser.getIdUser()))
        	throw new ForbiddenException("You cannot change your own role");
        
        if (currentUser.getUserRole().equals("user")) {
            throw new ForbiddenException("Users are not allowed to ban users");
        }
        
        if(userCurrentRole.equals("banned"))
        	throw new ForbiddenException("User is already banned");

        // cannot change the role of admins or chiefs if I am admin
        if (currentUser.getUserRole().equals("admin") && (userCurrentRole.equals("admin") || userCurrentRole.equals("chief"))) {
            throw new ForbiddenException("Admins can only ban users");
        }
        
        // cannot change role of another chief if I am chief
        if (currentUser.getUserRole().equals("chief") && userCurrentRole.equals("chief")) {
            throw new ForbiddenException("Chiefs can only ban users and admins");
        }
        
        user.setUserPreviousRole(userCurrentRole);
        user.setUserRole("banned");
        userRepository.save(user);
    }

	public void unbanUser(int userId) {
		User user = this.getUserByIdData(userId);
        User currentUser = this.getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
        
        String userPreviousRole = user.getUserPreviousRole();
        String userCurrentRole = user.getUserRole();
        
        if(user.getIdUser().equals(currentUser.getIdUser()))
        	throw new ForbiddenException("You cannot change your own role");
        
        if (currentUser.getUserRole().equals("user")) {
            throw new ForbiddenException("Users are not allowed to unban users");
        }
        
        if(!userCurrentRole.equals("banned"))
        	throw new ForbiddenException("User is not banned");
        
        // admin can only unban previous user
        if (currentUser.getUserRole().equals("admin") && !userPreviousRole.equals("user")) {
            throw new ForbiddenException("Admins can only ban users");
        }
        
        user.setUserPreviousRole("banned");
        user.setUserRole(userPreviousRole);
        userRepository.save(user);
	}
	
	public void promoteUserToAdmin(int userId) {
        User user = getUserByIdData(userId);
        User currentUser = getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
        
        if(user.getIdUser().equals(currentUser.getIdUser()))
        	throw new ForbiddenException("You cannot change your own role");

        if (!currentUser.getUserRole().equals("chief")) {
            throw new ForbiddenException("Only chiefs can promote users to admin");
        }

        if (user.getUserRole().equals("admin") || user.getUserRole().equals("chief")) {
            throw new ForbiddenException("User is already an admin or chief");
        }

        user.setUserPreviousRole(user.getUserRole());
        user.setUserRole("admin");
        userRepository.save(user);
    }

    public void promoteAdminToChief(int userId) {
        User user = getUserByIdData(userId);
        User currentUser = getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
        
        if(user.getIdUser().equals(currentUser.getIdUser()))
        	throw new ForbiddenException("You cannot change your own role");

        if (!currentUser.getUserRole().equals("chief")) {
            throw new ForbiddenException("Only chiefs can promote admins to chief");
        }

        if (!user.getUserRole().equals("admin")) {
            throw new ForbiddenException("Only admins can be promoted to chief");
        }

        user.setUserPreviousRole(user.getUserRole());
        user.setUserRole("chief");
        userRepository.save(user);
    }

    public void demoteAdminToUser(int userId) {
        User user = getUserByIdData(userId);
        User currentUser = getUserByIdData(SecurityUtils.getCurrentUser().getIdUser());
        
        if(user.getIdUser().equals(currentUser.getIdUser()))
        	throw new ForbiddenException("You cannot change your own role");

        if (!currentUser.getUserRole().equals("chief")) {
            throw new ForbiddenException("Only chiefs can demote admins to user");
        }

        if (!user.getUserRole().equals("admin")) {
            throw new ForbiddenException("Only admins can be demoted to user");
        }

        user.setUserPreviousRole(user.getUserRole());
        user.setUserRole("user");
        userRepository.save(user);
    }

}
