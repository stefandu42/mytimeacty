package mytimeacty.model.quizzes.dto.creation;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateDTO {
	@NotBlank(message = "Question is required")
	@NotNull(message = "Question is required")
	@Size(max = 255, message = "Question must be max 255 characters")
    private String question;
	
	@NotNull(message = "Number of question is required")
	@PositiveOrZero(message = "Number of question must be positive or zero")
    private Integer numQuestion;
	
	@NotNull(message = "Answers are required")
	@NotEmpty(message = "Answers cannot be empty")
	@Valid
    private List<AnswerCreateDTO> answers;
}