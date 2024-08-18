package mytimeacty.repository.quizz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzes.QuizzLike;
import mytimeacty.model.quizzes.ids.QuizzLikeId;
import mytimeacty.model.users.User;

@Repository
public interface QuizzLikeRepository extends JpaRepository<QuizzLike, QuizzLikeId> {
	long countByUser(User user);
}