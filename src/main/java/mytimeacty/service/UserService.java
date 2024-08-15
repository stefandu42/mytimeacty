package mytimeacty.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mytimeacty.exception.UserAlreadyExistsException;
import mytimeacty.model.users.User;
import mytimeacty.model.users.UserCreateDTO;
import mytimeacty.model.users.UserDTO;
import mytimeacty.repository.UserRepository;
import mytimeacty.service.Bcrypt.BcryptService;
import mytimeacty.mapper.UserMapper;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BcryptService bcryptService;
    
    

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

}
