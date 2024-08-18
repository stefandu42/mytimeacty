package mytimeacty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mytimeacty.exception.UserNotFoundException;
import mytimeacty.mapper.FollowerMapper;
import mytimeacty.model.followers.Follower;
import mytimeacty.model.followers.FollowerId;
import mytimeacty.model.followers.dto.FollowerDTO;
import mytimeacty.model.followers.dto.FollowingDTO;
import mytimeacty.model.users.User;
import mytimeacty.repository.FollowerRepository;
import mytimeacty.repository.UserRepository;
import mytimeacty.utils.PaginationUtils;

@Service
public class FollowerService {

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserRepository userRepository;

    public Follower followUser(Integer idFollower, Integer idUserFollowed) {
        User follower = userRepository.findById(idFollower)
                .orElseThrow(() -> new UserNotFoundException("User follower not found"));
        User userFollowed = userRepository.findById(idUserFollowed)
                .orElseThrow(() -> new UserNotFoundException("User followed not found"));

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
    
    public Page<FollowerDTO> getFollowersByUserId(Integer userId, int page, int size) {
    	userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    	
        Pageable pageable = PaginationUtils.createPageable(page, size);
        Page<Follower> followers = followerRepository.findByUserFollowedIdUser(userId, pageable);
        return followers.map(FollowerMapper::convertToFollowerDTO);
    }

    public Page<FollowingDTO> getFollowingsByUserId(Integer userId, int page, int size) {
    	userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    	
        Pageable pageable = PaginationUtils.createPageable(page, size);
        Page<Follower> followings = followerRepository.findByFollowerIdUser(userId, pageable);
        return followings.map(FollowerMapper::convertToFollowingDTO);
    }
    
    
}