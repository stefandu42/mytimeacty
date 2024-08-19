package mytimeacty.repository.quizz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzes.QuizzLike;
import mytimeacty.model.quizzes.ids.QuizzLikeId;
import mytimeacty.model.users.User;

@Repository
public interface QuizzLikeRepository extends JpaRepository<QuizzLike, QuizzLikeId> {
	
	/**
	 * Counts the number of occurrences associated with a specific user.
	 * 
	 * @param user the User entity for which to count occurrences.
	 * @return the number of occurrences associated with the specified user.
	 */
	long countByUser(User user);
}