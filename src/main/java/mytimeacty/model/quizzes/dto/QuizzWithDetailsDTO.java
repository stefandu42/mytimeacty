package mytimeacty.model.quizzes.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizzWithDetailsDTO {
    private QuizzDTO quizz;
    private List<QuestionDTO> questions;
}