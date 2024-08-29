package mytimeacty.model.quizzes.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QuizzDTO {
    private Integer idQuizz;
    private String title;
    private Integer creatorId;
    private String creatorNickname;
    private QuizzCategoryDTO category;
    private QuizzLevelDTO level;   
    private Instant createdAt;
}
