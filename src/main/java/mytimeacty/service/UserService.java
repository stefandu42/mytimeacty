package mytimeacty.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
                        .build();
        
        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }
    
    public Page<UserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PaginationUtils.createPageable(page, size);
        Page<User> users = userRepository.findAll(pageable);

        return users.map(UserMapper::toDTO);
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
    			.orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }
    
    public UserDTO getUserById(int userId) {
	    return UserMapper.toDTO(this.getUserByIdData(userId));
    }
    
    public UserDTO getUserByNickname(String nickname) {
	    return UserMapper.toDTO(userRepository.findByNickname(nickname)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found with nickname: " + nickname)));
    }
    
    public UserProfileDTO getUserProfile(Integer userId) {
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

}
