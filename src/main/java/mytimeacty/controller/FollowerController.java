package mytimeacty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mytimeacty.exception.UserNotFoundException;
import mytimeacty.model.users.UserDTO;
import mytimeacty.service.FollowerService;
import mytimeacty.utils.SecurityUtils;

@RestController
@RequestMapping("/followers")
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestParam Integer idFollower, @RequestParam Integer idUserFollowed) {
        if (!idFollower.equals(SecurityUtils.getCurrentUser().getIdUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You must be the follower, the identifiers must be identical");
        }
        
        if (idFollower.equals(idUserFollowed)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You cannot follow yourself");
        }
        
    	try {
            followerService.followUser(idFollower, idUserFollowed);
            return ResponseEntity.status(HttpStatus.CREATED).build(); 
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");  
        }
    }
    
    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestParam Integer idFollower, @RequestParam Integer idUserFollowed) {
        if (!idFollower.equals(SecurityUtils.getCurrentUser().getIdUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You must be the follower, the identifiers must be identical");
        }
    	
        followerService.unfollowUser(idFollower, idUserFollowed);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
}