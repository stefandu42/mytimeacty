package mytimeacty.controller;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import mytimeacty.exception.UserNotFoundException;
import mytimeacty.model.auth.LoginDTO;
import mytimeacty.model.users.dto.creation.UserCreateDTO;
import mytimeacty.service.AuthenticationService;
import mytimeacty.service.JWT.JWTService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private JWTService jwtService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	/**
     * Authenticates a user and returns an authentication token.
     * 
     * This endpoint validates the user's credentials and provides an authentication token upon successful login.
     * If the provided credentials are incorrect or the user does not exist, an unauthorized error response is returned.
     * 
     * @param loginDTO the login details of the user, including nickname/email and password.
     * @return a ResponseEntity containing the authentication token if login is successful.
     *         Returns a 401 Unauthorized status with an error message if the login fails.
     */
	@PostMapping("/login")
	public ResponseEntity<String> getToken(@Valid @RequestBody LoginDTO loginDTO) {
		try {
            String token = authenticationService.authenticateUser(loginDTO);
            logger.info("User with nickname/email '{}' sucessfully connected with token '{}'", loginDTO.getNicknameOrEmail(), token);
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (UserNotFoundException | AuthenticationException e) {
        	logger.warn("Unauthorized: Invalid nickname/email or wrong password for user '{}'", loginDTO.getNicknameOrEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid nickname/email or wrong password");
        }
	}

	/**
     * Registers a new user and returns an authentication token.
     * 
     * This endpoint creates a new user based on the provided registration details and issues an authentication token for the newly created user.
     * 
     * @param userCreateDTO the details of the user to be registered, including email, nickname, and password.
     * @return a ResponseEntity containing the authentication token for the newly registered user.
     */
	@PostMapping("/register")
    public ResponseEntity<String> createUserAndGetToken(@Valid @RequestBody UserCreateDTO userCreateDTO) {
		authenticationService.registerUser(userCreateDTO);
		logger.info("User with nickname '{}'sucessfully registered, check your emails to activate your account", userCreateDTO.getNickname());
	    return ResponseEntity.status(HttpStatus.OK).body("User sucessfully registered, check your emails to activate your account");
    }
	
	
	@PutMapping("/verify/{token}")
	public ResponseEntity<String> verifyUser(@PathVariable("token") String token) {
        String email = jwtService.validateTokenAndGetEmail(token);
        
        authenticationService.activateUser(email);
        
        logger.info("User with email '{}' successfully verified.", email);
        return ResponseEntity.status(HttpStatus.OK).body("Account successfully verified");
 
	}

}
