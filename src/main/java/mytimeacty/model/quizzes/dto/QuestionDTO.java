package mytimeacty.model.quizzes.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionDTO {
    private Integer idQuestion;
    private String question;
    private Integer numQuestion;
    private List<AnswerDTO> answers;
}