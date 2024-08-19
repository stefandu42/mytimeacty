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

    /**
     * Allows a user to follow another user and returns the follow as a DTO.
     *
     * @param idFollower the ID of the user who is following
     * @param idUserFollowed the ID of the user being followed
     * @return a FollowerDTO representing the newly created follower relationship
     * @throws UserNotFoundException if either the follower or the followed user is not found
     */
    public FollowerDTO followUser(Integer idFollower, Integer idUserFollowed) {
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

        return FollowerMapper.convertToFollowerDTO(followerRepository.save(followerEntity));
    }
    
    /**
     * Allows a user to unfollow another user.
     *
     * @param idFollower the ID of the user who is unfollowing
     * @param idUserFollowed the ID of the user being unfollowed
     * @throws UserNotFoundException if the follower or the followed user does not exist
     */
    public void unfollowUser(Integer idFollower, Integer idUserFollowed) {
        FollowerId followerId = new FollowerId(idFollower, idUserFollowed);
        followerRepository.deleteById(followerId);
    }
    
    /**
     * Retrieves the followers of a specific user.
     *
     * @param userId the ID of the user whose followers are being retrieved
     * @param page the page number to retrieve
     * @param size the number of entries per page
     * @return a page of FollowerDTO objects representing the followers of the user
     * @throws UserNotFoundException if the user with the specified ID is not found
     */
    public Page<FollowerDTO> getFollowersByUserId(Integer userId, int page, int size) {
    	userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    	
        Pageable pageable = PaginationUtils.createPageable(page, size);
        Page<Follower> followers = followerRepository.findByUserFollowedIdUser(userId, pageable);
        return followers.map(FollowerMapper::convertToFollowerDTO);
    }

    /**
     * Retrieves the users that a specific user is following.
     *
     * @param userId the ID of the user whose followings are being retrieved
     * @param page the page number to retrieve
     * @param size the number of entries per page
     * @return a page of FollowingDTO objects representing the users being followed by the user
     * @throws UserNotFoundException if the user with the specified ID is not found
     */
    public Page<FollowingDTO> getFollowingsByUserId(Integer userId, int page, int size) {
    	userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    	
        Pageable pageable = PaginationUtils.createPageable(page, size);
        Page<Follower> followings = followerRepository.findByFollowerIdUser(userId, pageable);
        return followings.map(FollowerMapper::convertToFollowingDTO);
    }
    
    
}