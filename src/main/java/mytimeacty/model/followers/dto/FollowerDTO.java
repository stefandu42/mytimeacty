package mytimeacty.model.followers.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowerDTO {
    private Integer followerId;  // user's id that follows me
    private String followerUsername; // user's nickname that follows me
}