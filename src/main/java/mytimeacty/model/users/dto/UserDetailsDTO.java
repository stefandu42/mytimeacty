package mytimeacty.model.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsDTO {
    private Integer userId;
    private String nickname;
    private String email;
    private boolean isFollowedByCurrentUser;
    private int quizLikesCount;
    private int followersCount;
}
