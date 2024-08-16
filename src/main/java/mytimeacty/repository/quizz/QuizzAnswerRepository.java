package mytimeacty.repository.quizz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzes.QuizzAnswer;

@Repository
public interface QuizzAnswerRepository extends JpaRepository<QuizzAnswer, Integer> {}