package mytimeacty.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mytimeacty.model.users.UserDTO;
import mytimeacty.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    

	public UserController(UserService userService) {
	    this.userService = userService;
	}
	
	
	
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
}