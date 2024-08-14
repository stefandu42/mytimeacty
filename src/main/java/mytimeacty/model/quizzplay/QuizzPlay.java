package mytimeacty.model.quizzplay;

import jakarta.persistence.*;
import lombok.*;
import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.users.User;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quizz_play", schema = "mytimeacty")
public class QuizzPlay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_quizz_play")
    private Integer idQuizzPlay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_quizz", nullable = false)
    private Quizz quizz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_player", nullable = false)
    private User player;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "played_at", nullable = false)
    private Instant playedAt;
}