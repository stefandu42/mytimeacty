package mytimeacty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import mytimeacty.service.FollowerService;
import mytimeacty.utils.SecurityUtils;

@RestController
@RequestMapping("/followers")
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    @PostMapping("/follow/{idUserFollowed}")
    public ResponseEntity<String> followUser(@PathVariable Integer idUserFollowed) {
        if (SecurityUtils.getCurrentUser().getIdUser().equals(idUserFollowed)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You cannot follow yourself");
        }
        
    	followerService.followUser(SecurityUtils.getCurrentUser().getIdUser(), idUserFollowed);
        return ResponseEntity.status(HttpStatus.CREATED).build(); 
    }
    
    @DeleteMapping("/unfollow/{idUserFollowed}")
    public ResponseEntity<String> unfollowUser(@PathVariable Integer idUserFollowed) {
        followerService.unfollowUser(SecurityUtils.getCurrentUser().getIdUser(), idUserFollowed);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
}