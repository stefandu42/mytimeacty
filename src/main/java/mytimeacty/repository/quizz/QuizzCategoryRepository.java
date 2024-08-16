package mytimeacty.repository.quizz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzes.QuizzCategory;

@Repository
public interface QuizzCategoryRepository extends JpaRepository<QuizzCategory, Integer> {}