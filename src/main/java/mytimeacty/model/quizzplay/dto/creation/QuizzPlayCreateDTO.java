package mytimeacty.model.quizzplay.dto.creation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class QuizzPlayCreateDTO {
    @NotNull(message = "Quizz ID is required")
    @PositiveOrZero(message = "Quizz ID must be positive or zero")
    private Integer quizzId;

    @NotNull(message = "Score is required")
    @PositiveOrZero(message = "Score must be positive or zero")
    private Double score;
}
