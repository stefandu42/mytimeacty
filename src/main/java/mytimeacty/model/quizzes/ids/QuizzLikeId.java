package mytimeacty.model.quizzes.ids;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class QuizzLikeId implements Serializable {
    private Integer idQuizz;
    private Integer idUser;
}