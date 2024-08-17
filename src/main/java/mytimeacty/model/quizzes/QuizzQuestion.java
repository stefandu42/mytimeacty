package mytimeacty.model.quizzes;

import java.util.Objects;
import java.util.Set;

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
    
    @OneToMany(mappedBy = "question")
    private Set<QuizzAnswer> quizzAnswers;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizzQuestion that = (QuizzQuestion) o;
        return Objects.equals(this.idQuestion, that.idQuestion); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idQuestion); 
    }
}