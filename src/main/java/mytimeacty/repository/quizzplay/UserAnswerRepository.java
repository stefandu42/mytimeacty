package mytimeacty.repository.quizzplay;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mytimeacty.model.quizzplay.UserAnswer;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
	
	/**
	 * Finds a list of UserAnswer entities associated with a specific QuizzPlay.
	 * 
	 * @param quizzPlayId the ID of the QuizzPlay for which to find UserAnswer entities.
	 * @return a List of UserAnswer entities associated with the specified QuizzPlay ID.
	 */
    List<UserAnswer> findByQuizzPlayIdQuizzPlay(Integer quizzPlayId);
}
