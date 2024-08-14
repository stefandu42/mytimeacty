package mytimeacty.model.quizzes;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quizz_answers", schema = "mytimeacty")
public class QuizzAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_answer")
    private Integer idAnswer;

    @Column(name = "answer", nullable = false)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_question", nullable = false)
    private QuizzQuestion question;

    @Column(name = "num_answer", nullable = false)
    private Integer numAnswer;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;
}