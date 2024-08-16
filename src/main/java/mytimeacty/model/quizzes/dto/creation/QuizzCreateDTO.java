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
public class QuizzCreateDTO {
	@NotBlank(message = "Title is required")
	@NotNull(message = "Title is required")
	@Size(max = 100, message = "Title must be max 100 characters")
    private String title;
	
	@NotNull(message = "Level id is required")
	@PositiveOrZero(message = "Level id must be positive or zero")
    private Integer levelId;
	
	@NotNull(message = "Category id is required")
	@PositiveOrZero(message = "Category id must be positive or zero")
    private Integer categoryId;
	
	@Size(max = 255, message = "Image must be max 255 characters")
    private String img;
    
	@NotNull(message = "Questions are required")
	@NotEmpty(message = "Questions cannot be empty")
	@Valid
    private List<QuestionCreateDTO> questions;
}