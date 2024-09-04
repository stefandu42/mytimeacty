package mytimeacty.model.quizzes.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDTO {
    private Integer idAnswer;
    private String answer;
    private Integer numAnswer;
    private Boolean isCorrect;
}