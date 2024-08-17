package mytimeacty.model.quizzplay.dto;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizzPlayDTO {
    private Integer idQuizzPlay;
    private Integer quizzId;
    private Integer playerId;
    private Double score;
    private Instant playedAt;
}
