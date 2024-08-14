package mytimeacty.model.quizzes;

import jakarta.persistence.*;
import lombok.*;
import mytimeacty.model.quizzes.ids.QuizzFavouriteId;
import mytimeacty.model.users.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quizz_favourites", schema = "mytimeacty")
public class QuizzFavourite {

    @EmbeddedId
    private QuizzFavouriteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idQuizz")
    @JoinColumn(name = "id_quizz", nullable = false)
    private Quizz quizz;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUser")
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}