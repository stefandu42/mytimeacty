package mytimeacty.model.quizzes.dto.creation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateDTO {
	@NotBlank(message = "Answer is required")
	@NotNull(message = "Answer is required")
	@Size(max = 255, message = "Answer must be max 255 characters")
    private String answer;
	
	@NotNull(message = "Number of answer is required")
	@PositiveOrZero(message = "Number of answer must be positive or zero")
    private Integer numAnswer;
	
	@NotNull(message = "Is correct field is required")
    private Boolean isCorrect;
}