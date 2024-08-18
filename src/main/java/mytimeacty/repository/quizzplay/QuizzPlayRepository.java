package mytimeacty.repository.quizzplay;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzplay.QuizzPlay;

@Repository
public interface QuizzPlayRepository extends JpaRepository<QuizzPlay, Integer> {
	Page<QuizzPlay> findByQuizzIdQuizz(Integer quizzId, Pageable pageable);
}
