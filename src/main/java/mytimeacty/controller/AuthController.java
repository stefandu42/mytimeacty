package mytimeacty.controller;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mytimeacty.exception.UserAlreadyExistsException;
import mytimeacty.exception.UserNotFoundException;
import mytimeacty.model.auth.LoginDTO;
import mytimeacty.model.users.UserCreateDTO;
import mytimeacty.model.users.UserDTO;
import mytimeacty.service.AuthenticationService;
import mytimeacty.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("/login")
	public ResponseEntity<String> getToken(@RequestBody LoginDTO loginDTO) {
		try {
            String token = authenticationService.authenticateUser(loginDTO);
            return ResponseEntity.ok(token);
        } catch (UserNotFoundException | AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid nickname/email or wrong password");
        }
	}

	
	@PostMapping("/register")
    public ResponseEntity<String> createUserAndGetToken(@RequestBody UserCreateDTO userCreateDTO) {
		try {
			String token = authenticationService.registerUser(userCreateDTO);
	        return ResponseEntity.ok(token);
		} catch (UserAlreadyExistsException e) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	    }
    }
}
