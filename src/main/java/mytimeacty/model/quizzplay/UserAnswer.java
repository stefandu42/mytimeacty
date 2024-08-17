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
    @Column(name = "id_user_answer")
    private Integer idUserAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_quizz_play", nullable = false)
    private QuizzPlay quizzPlay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_answer", nullable = false)
    private QuizzAnswer answer;
    
}