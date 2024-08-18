package mytimeacty.mapper;

import mytimeacty.model.followers.Follower;
import mytimeacty.model.followers.dto.FollowerDTO;
import mytimeacty.model.followers.dto.FollowingDTO;

public class FollowerMapper {

	public static FollowerDTO convertToFollowerDTO(Follower follower) {
        return FollowerDTO.builder()
                .followerId(follower.getFollower().getIdUser())
                .followerUsername(follower.getFollower().getNickname())
                .build();
    }

    public static FollowingDTO convertToFollowingDTO(Follower following) {
        return FollowingDTO.builder()
                .followedUserId(following.getUserFollowed().getIdUser())
                .followedUsername(following.getUserFollowed().getNickname())
                .build();
    }
}
