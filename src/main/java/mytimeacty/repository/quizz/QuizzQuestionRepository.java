package mytimeacty.repository.quizz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzes.QuizzQuestion;

@Repository
public interface QuizzQuestionRepository extends JpaRepository<QuizzQuestion, Integer> {}