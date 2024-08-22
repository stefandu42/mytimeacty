package mytimeacty.service;

import java.util.concurrent.CompletableFuture;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import mytimeacty.service.mail.MailService;

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
	
	@Autowired
	private MailService mailService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	/**
	 * Authenticates a user based on the provided login credentials.
	 *
	 * @param loginDTO contains the nickname or email and password for authentication
	 * @return a JWT token if authentication is successful
	 * @throws UserNotFoundException if the user with the given nickname or email is not found
	 * @throws AuthenticationException if the credentials are invalid or the user is banned
	 */
	public String authenticateUser(LoginDTO loginDTO) throws UserNotFoundException, AuthenticationException {
    	logger.info("Entering method authenticateUser: Nickname/email '{}'", loginDTO.getNicknameOrEmail());
		
		// Trim the input to remove leading and trailing whitespace
		loginDTO.setNicknameOrEmail(loginDTO.getNicknameOrEmail().trim()); 
		
		// Attempt to find the user by email or nickname
		User user = userRepository.findByEmailIgnoreCase(loginDTO.getNicknameOrEmail())
                .orElseGet(() -> userRepository.findByNicknameIgnoreCase(loginDTO.getNicknameOrEmail())
                .orElseThrow(() -> {
                	logger.warn("Method authenticateUser: User with nickname or email '{}' not found",
                			loginDTO.getNicknameOrEmail());
                	return new UserNotFoundException("User not found");
                }));
		
		// Check if the user is banned
		if(user.getUserRole().equals(UserRole.BANNED.getRole())) {
			logger.warn("Method authenticateUser: User banned with nickname '{}' tried to login",
        			user.getNickname());
			throw new ForbiddenException("You are banned");
		}
		
		// Check if the user is banned
		if(!user.getIsActivated()) {
			logger.warn("Method authenticateUser: User not activated with nickname '{}' tried to login",
        			user.getNickname());
			throw new ForbiddenException("You must activate your account");
		}

		// Validate the provided password
        if (!bcryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
        	logger.warn("Method authenticateUser: User credentials are invalid for user with nickname '{}'",
        			user.getNickname());
        	throw new AuthenticationException("Invalid credentials");
        }
        
        // Generate and return a JWT token for the authenticated user
        String token = jwtService.generateToken(UserMapper.toDTO(user));
        
        logger.info("Method authenticateUser: Token for user with nickname {} created sucessfully.",
        		user.getNickname());
        
        return token;
    }
	
	/**
	 * Registers a new user and generates a JWT token for the newly created user.
	 *
	 * @param createDTO contains the user details for registration
	 * @return a JWT token for the newly registered user
	 */
	public String registerUser(UserCreateDTO createDTO) {
		logger.info("Entering method registerUser: Nickname '{}' and email '{}'", createDTO.getNickname(), createDTO.getEmail());
		
		// Trim the email and nickname to remove leading and trailing whitespace
		createDTO.setEmail(createDTO.getEmail().trim());
		createDTO.setNickname(createDTO.getNickname().trim());
		
		// Create a new user and save it
		UserDTO userSaved = userService.createUser(createDTO);
		
		logger.info("Method registerUser: User with nickname {} registered sucessfully.",
				createDTO.getNickname());
		
		// Generate and return a JWT token for the newly registered user
		String token = jwtService.generateVerificationToken(userSaved);
		
		logger.info("Method registerUser: Token for user with nickname {} created sucessfully.",
				createDTO.getNickname());

	    // Send email to verify
	    CompletableFuture.runAsync(() -> {
	    	mailService.sendVerificationEmail(userSaved.getEmail(), token);
	    });
		
		return token;
	}

	public void activateUser(String email) {
		logger.info("Entering method activateUser: User '{}'", email);
		
		userService.activateUser(email);
		
		logger.info("Method activateUser: User '{}' marked as activated sucessfully", email);
		
	}
}
