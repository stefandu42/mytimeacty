package mytimeacty.model.quizzplay;

import jakarta.persistence.*;
import lombok.*;
import mytimeacty.model.quizzes.QuizzAnswer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_answers", schema = "mytimeacty")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_player")
    private Long idPlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_answer", nullable = false)
    private QuizzAnswer answer;

    @Column(name = "trial_num", nullable = false)
    private Integer trialNum;
}