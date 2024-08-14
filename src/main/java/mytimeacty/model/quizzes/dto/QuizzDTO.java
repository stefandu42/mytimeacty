package mytimeacty.model.quizzes.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mytimeacty.model.quizzes.Quizz;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizzDTO {
    private Integer idQuizz;
    private String title;
    private String creatorNickname;
    private Instant createdAt;
    
    public QuizzDTO(Quizz quizz) {
        this.idQuizz = quizz.getIdQuizz();
        this.title = quizz.getTitle();
        this.creatorNickname = quizz.getCreator().getNickname();
        this.createdAt = quizz.getCreatedAt();
    }
}
