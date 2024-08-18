package mytimeacty.model.quizzplay.dto.creation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UserAnswerCreateDTO {
    @NotNull(message = "Answer ID is required")
    @PositiveOrZero(message = "Answer ID must be positive or zero")
    private Integer answerId;
}
