package mytimeacty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mytimeacty.model.followers.dto.FollowerDTO;
import mytimeacty.model.followers.dto.FollowingDTO;
import mytimeacty.service.FollowerService;
import mytimeacty.utils.SecurityUtils;

@RestController
@RequestMapping("/followers")
public class FollowerController {

    @Autowired
    private FollowerService followerService;
    
    // Route to get a user's followers
    @GetMapping("/users/{userId}/followers")
    public ResponseEntity<Page<FollowerDTO>> getFollowers(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        Page<FollowerDTO> followers = followerService.getFollowersByUserId(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(followers);
    }

    // Route to get a user's subscriptions
    @GetMapping("/users/{userId}/followings")
    public ResponseEntity<Page<FollowingDTO>> getFollowings(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        Page<FollowingDTO> followings = followerService.getFollowingsByUserId(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(followings);
    }

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