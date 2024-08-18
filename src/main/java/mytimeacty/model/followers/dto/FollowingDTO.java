package mytimeacty.model.followers.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowingDTO {
    private Integer followedUserId; // user's id that I follow
    private String followedUsername; // user's nickname that I follow
}