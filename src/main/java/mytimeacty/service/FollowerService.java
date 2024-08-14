package mytimeacty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mytimeacty.exception.UserNotFoundException;
import mytimeacty.model.followers.Follower;
import mytimeacty.model.followers.FollowerId;
import mytimeacty.model.users.User;
import mytimeacty.repository.FollowerRepository;
import mytimeacty.repository.UserRepository;

@Service
public class FollowerService {

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserRepository userRepository;

    public Follower followUser(Integer idFollower, Integer idUserFollowed) {
        User follower = userRepository.findById(idFollower)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        User userFollowed = userRepository.findById(idUserFollowed)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FollowerId followerId = new FollowerId(idFollower, idUserFollowed);

        Follower followerEntity = Follower.builder()
                .id(followerId)
                .follower(follower)
                .userFollowed(userFollowed)
                .build();

        return followerRepository.save(followerEntity);
    }
    
    public void unfollowUser(Integer idFollower, Integer idUserFollowed) {
        FollowerId followerId = new FollowerId(idFollower, idUserFollowed);
        followerRepository.deleteById(followerId);
    }
}