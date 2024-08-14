package mytimeacty.model.quizzes;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quizz_questions", schema = "mytimeacty")
public class QuizzQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_question")
    private Integer idQuestion;

    @Column(name = "question", nullable = false)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_quizz", nullable = false)
    private Quizz quizz;

    @Column(name = "num_question", nullable = false)
    private Integer numQuestion;
}