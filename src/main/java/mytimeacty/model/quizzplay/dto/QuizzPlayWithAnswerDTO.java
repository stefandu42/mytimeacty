package mytimeacty.model.quizzplay.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true) 
public class QuizzPlayWithAnswerDTO extends QuizzPlayDTO {
    private List<UserAnswerDTO> userAnswers;
}
