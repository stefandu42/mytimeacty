package mytimeacty.repository.quizz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.QuizzFavourite;
import mytimeacty.model.quizzes.ids.QuizzFavouriteId;
import mytimeacty.model.users.User;

@Repository
public interface QuizzFavouriteRepository extends JpaRepository<QuizzFavourite, QuizzFavouriteId> {
	boolean existsByQuizzAndUser(Quizz quizz, User user);
}