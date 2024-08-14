package mytimeacty.model.quizzes.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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
