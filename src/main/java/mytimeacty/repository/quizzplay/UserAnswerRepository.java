package mytimeacty.repository.quizzplay;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzes.QuizzAnswer;
import mytimeacty.model.quizzplay.QuizzPlay;
import mytimeacty.model.quizzplay.UserAnswer;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
    List<UserAnswer> findByQuizzPlayIdQuizzPlay(Integer quizzPlayId);
    
    boolean existsByQuizzPlayAndAnswer(QuizzPlay quizzPlay, QuizzAnswer answer);
}
