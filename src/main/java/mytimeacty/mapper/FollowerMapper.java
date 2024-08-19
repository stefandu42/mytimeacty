package mytimeacty.mapper;

import mytimeacty.model.followers.Follower;
import mytimeacty.model.followers.dto.FollowerDTO;
import mytimeacty.model.followers.dto.FollowingDTO;

public class FollowerMapper {

	/**
	 * Converts a Follower entity to a FollowerDTO.
	 * 
	 * @param follower the Follower entity to be converted.
	 * @return a FollowerDTO representing the follower's information, or null if the input follower is null.
	 */
	public static FollowerDTO convertToFollowerDTO(Follower follower) {
		if (follower == null) {
            return null;
        }
		
        return FollowerDTO.builder()
        		.followerId(follower.getFollower().getIdUser())
                .followerUsername(follower.getFollower().getNickname())
                .build();
    }

	/**
	 * Converts a Follower entity to a FollowingDTO.
	 * 
	 * @param following the Follower entity to be converted.
	 * @return a FollowingDTO representing the user's information being followed, or null if the input following is null.
	 */
    public static FollowingDTO convertToFollowingDTO(Follower following) {
    	if (following == null) {
            return null;
        }
    	
        return FollowingDTO.builder()
                .followedUserId(following.getUserFollowed().getIdUser())
                .followedUsername(following.getUserFollowed().getNickname())
                .build();
    }
}
