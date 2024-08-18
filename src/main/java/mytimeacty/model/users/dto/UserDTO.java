package mytimeacty.model.users.dto;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Integer idUser;
    private String email;
    private String nickname;
    private String userRole;
    private String previousRole;
    private Instant createdAt;
}
