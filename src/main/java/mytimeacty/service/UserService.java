package mytimeacty.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mytimeacty.exception.UserAlreadyExistsException;
import mytimeacty.exception.UserNotFoundException;
import mytimeacty.model.users.User;
import mytimeacty.model.users.UserCreateDTO;
import mytimeacty.model.users.UserDTO;
import mytimeacty.model.users.UserProfileDTO;
import mytimeacty.repository.FollowerRepository;
import mytimeacty.repository.UserRepository;
import mytimeacty.service.Bcrypt.BcryptService;
import mytimeacty.mapper.UserMapper;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BcryptService bcryptService;
    
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
    

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                             .map(UserMapper::toDTO)
                             .collect(Collectors.toList());
    }
    
    
    public UserDTO getUserById(int userId) {
	    return UserMapper.toDTO(userRepository.findById(userId)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId)));
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
