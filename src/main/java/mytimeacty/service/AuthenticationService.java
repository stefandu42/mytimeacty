package mytimeacty.service;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mytimeacty.exception.ForbiddenException;
import mytimeacty.exception.UserNotFoundException;
import mytimeacty.mapper.UserMapper;
import mytimeacty.model.auth.LoginDTO;
import mytimeacty.model.users.User;
import mytimeacty.model.users.dto.UserDTO;
import mytimeacty.model.users.dto.creation.UserCreateDTO;
import mytimeacty.model.users.enums.UserRole;
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
	
	/**
	 * Authenticates a user based on the provided login credentials.
	 *
	 * @param loginDTO contains the nickname or email and password for authentication
	 * @return a JWT token if authentication is successful
	 * @throws UserNotFoundException if the user with the given nickname or email is not found
	 * @throws AuthenticationException if the credentials are invalid or the user is banned
	 */
	public String authenticateUser(LoginDTO loginDTO) throws UserNotFoundException, AuthenticationException {
		// Trim the input to remove leading and trailing whitespace
		loginDTO.setNicknameOrEmail(loginDTO.getNicknameOrEmail().trim()); 
		
		// Attempt to find the user by email or nickname
		User user = userRepository.findByEmailIgnoreCase(loginDTO.getNicknameOrEmail())
                .orElseGet(() -> userRepository.findByNicknameIgnoreCase(loginDTO.getNicknameOrEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found")));
		
		// Check if the user is banned
		if(user.getUserRole().equals(UserRole.BANNED.getRole()))
			throw new ForbiddenException("You are banned");

		// Validate the provided password
        if (!bcryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
        	throw new AuthenticationException("Invalid credentials");
        
     // Generate and return a JWT token for the authenticated user
        return jwtService.generateToken(UserMapper.toDTO(user));
    }
	
	/**
	 * Registers a new user and generates a JWT token for the newly created user.
	 *
	 * @param createDTO contains the user details for registration
	 * @return a JWT token for the newly registered user
	 */
	public String registerUser(UserCreateDTO createDTO) {
		// Trim the email and nickname to remove leading and trailing whitespace
		createDTO.setEmail(createDTO.getEmail().trim());
		createDTO.setNickname(createDTO.getNickname().trim());
		
		// Create a new user and save it
		UserDTO userSaved = userService.createUser(createDTO);
		
		// Generate and return a JWT token for the newly registered user
		return jwtService.generateToken(userSaved);
	}
}
