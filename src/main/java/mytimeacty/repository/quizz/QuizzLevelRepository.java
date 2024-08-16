package mytimeacty.repository.quizz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzes.QuizzLevel;

@Repository
public interface QuizzLevelRepository extends JpaRepository<QuizzLevel, Integer> {}