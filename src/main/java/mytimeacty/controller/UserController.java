package mytimeacty.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mytimeacty.model.users.dto.UserDTO;
import mytimeacty.model.users.dto.UserDetailsDTO;
import mytimeacty.model.users.dto.UserProfileDTO;
import mytimeacty.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    

	public UserController(UserService userService) {
	    this.userService = userService;
	}
	
	@GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Integer userId) {
        UserProfileDTO userProfileDTO = userService.getUserProfile(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileDTO);
    }
    
	@GetMapping
    public ResponseEntity<Page<UserDetailsDTO>> getFilteredUsers(
            @RequestParam String nickname, 
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "15") int size) {
        
        Page<UserDetailsDTO> userDetailsDTOs = userService.getFilteredUsers(nickname, page, size);
        return ResponseEntity.ok(userDetailsDTOs);
    }
    
}