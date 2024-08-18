package mytimeacty.controller;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import mytimeacty.exception.UserNotFoundException;
import mytimeacty.model.auth.LoginDTO;
import mytimeacty.model.users.dto.creation.UserCreateDTO;
import mytimeacty.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("/login")
	public ResponseEntity<String> getToken(@Valid @RequestBody LoginDTO loginDTO) {
		try {
            String token = authenticationService.authenticateUser(loginDTO);
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (UserNotFoundException | AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid nickname/email or wrong password");
        }
	}

	
	@PostMapping("/register")
    public ResponseEntity<String> createUserAndGetToken(@Valid @RequestBody UserCreateDTO userCreateDTO) {
		String token = authenticationService.registerUser(userCreateDTO);
	    return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
