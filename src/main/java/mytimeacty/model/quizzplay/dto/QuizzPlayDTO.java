package mytimeacty.model.quizzplay.dto;

import java.time.Instant;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import mytimeacty.model.users.dto.UserDTO;

@Data
@SuperBuilder
public class QuizzPlayDTO {
    private Integer idQuizzPlay;
    private Integer quizzId;
    private UserDTO player;
    private Double score;
    private Instant playedAt;
}
