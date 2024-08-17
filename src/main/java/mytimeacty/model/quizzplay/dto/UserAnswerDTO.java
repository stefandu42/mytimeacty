package mytimeacty.model.quizzplay.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAnswerDTO {
    private Integer idUserAnswer;
    private Integer quizzPlayId;
    private Integer answerId;
}

