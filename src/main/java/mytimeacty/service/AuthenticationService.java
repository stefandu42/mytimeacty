package mytimeacty.service;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mytimeacty.exception.UserNotFoundException;
import mytimeacty.mapper.UserMapper;
import mytimeacty.model.auth.LoginDTO;
import mytimeacty.model.users.User;
import mytimeacty.model.users.dto.UserDTO;
import mytimeacty.model.users.dto.creation.UserCreateDTO;
import mytimeacty.repository.UserRepository;
import mytimeacty.service.JWT.JWTService;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@Autowired
    private JWTService jwtService;
	
	public String authenticateUser(LoginDTO loginDTO) throws UserNotFoundException, AuthenticationException {
		loginDTO.setNicknameOrEmail(loginDTO.getNicknameOrEmail().trim()); 
		
		User user = userRepository.findByEmailIgnoreCase(loginDTO.getNicknameOrEmail())
                .orElseGet(() -> userRepository.findByNicknameIgnoreCase(loginDTO.getNicknameOrEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found")));

        if (!bcryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
        	throw new AuthenticationException("Invalid credentials");
        
        return jwtService.generateToken(UserMapper.toDTO(user));
    }
	
	public String registerUser(UserCreateDTO createDTO) {
		createDTO.setEmail(createDTO.getEmail().trim());
		createDTO.setNickname(createDTO.getNickname().trim());
		
		UserDTO userSaved = userService.createUser(createDTO);
		
			
		return jwtService.generateToken(userSaved);
	}
}
