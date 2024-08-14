package mytimeacty.repository.quizz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzes.Quizz;

@Repository
public interface QuizzRepository extends JpaRepository<Quizz, Integer> {
	Page<Quizz> findAll(Pageable pageable);
}
