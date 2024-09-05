package mytimeacty.model.quizzes;

import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import mytimeacty.model.quizzplay.UserAnswer;

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
    
    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserAnswer> userAnswers;
}