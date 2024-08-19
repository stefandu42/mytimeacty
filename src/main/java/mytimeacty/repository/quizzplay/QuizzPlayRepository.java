package mytimeacty.repository.quizzplay;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzplay.QuizzPlay;

@Repository
public interface QuizzPlayRepository extends JpaRepository<QuizzPlay, Integer> {
	
	/**
	 * Finds a paginated list of QuizzPlay entities associated with a specific quizz.
	 * 
	 * @param quizzId the ID of the quizz for which to find QuizzPlay entities.
	 * @param pageable the Pageable object containing pagination and sorting information.
	 * @return a Page containing a list of QuizzPlay entities associated with the specified quizz.
	 */
	Page<QuizzPlay> findByQuizzIdQuizz(Integer quizzId, Pageable pageable);
}
