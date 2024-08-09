package mytimeacty.service;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mytimeacty.exception.UserNotFoundException;
import mytimeacty.mapper.UserMapper;
import mytimeacty.model.auth.LoginDTO;
import mytimeacty.model.users.User;
import mytimeacty.model.users.UserCreateDTO;
import mytimeacty.model.users.UserDTO;
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
        User user = userRepository.findByEmailOrNickname(loginDTO.getNicknameOrEmail(), loginDTO.getNicknameOrEmail())
                                  .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!bcryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
        	throw new AuthenticationException("Invalid credentials");
        
        return jwtService.generateToken(UserMapper.toDTO(user));
    }
	
	public String registerUser(UserCreateDTO createDTO) {
		UserDTO userSaved = userService.createUser(createDTO);
		
			
		return jwtService.generateToken(userSaved);
	}
}
