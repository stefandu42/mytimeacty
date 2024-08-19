package mytimeacty.service.quizz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mytimeacty.exception.NotFoundException;
import mytimeacty.exception.UserNotFoundException;
import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.QuizzFavourite;
import mytimeacty.model.quizzes.ids.QuizzFavouriteId;
import mytimeacty.model.users.User;
import mytimeacty.repository.UserRepository;
import mytimeacty.repository.quizz.QuizzFavouriteRepository;
import mytimeacty.repository.quizz.QuizzRepository;

@Service
public class QuizzFavouriteService {
	
	@Autowired
	private QuizzRepository quizzRepository;

	@Autowired
	private UserRepository userRepository;

    @Autowired
    private QuizzFavouriteRepository quizzFavouriteRepository;

    /**
     * Adds a quizz to the user's list of favourites.
     * 
     * This method checks if the quizz and user exist in the database. If both are valid and the quizz
     * is not already marked as a favourite by the user, it creates a new `QuizzFavourite` entity
     * and saves it to the repository.
     * 
     * @param userId the ID of the user who wants to favourite the quizz
     * @param quizzId the ID of the quizz to be favourited
     * @throws NotFoundException if the quizz is not found
     * @throws UserNotFoundException if the user is not found
     */
    public void favouriteQuizz(Integer userId, Integer quizzId) {
    	Quizz quizz = quizzRepository.findById(quizzId)
                .orElseThrow(() -> new NotFoundException("Quizz not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    	
        QuizzFavouriteId quizzFavouriteId = new QuizzFavouriteId(quizzId, userId);
        if (!quizzFavouriteRepository.existsById(quizzFavouriteId)) {
            QuizzFavourite quizzFavourite = new QuizzFavourite(quizzFavouriteId, quizz, user);
            quizzFavouriteRepository.save(quizzFavourite);
        }
    }

    /**
     * Removes a quizz from the user's list of favourites.
     * 
     * This method deletes the `QuizzFavourite` entity identified by the user ID and quizz ID,
     * effectively removing the quizz from the user's favourites list.
     * 
     * @param userId the ID of the user who wants to unfavourite the quizz
     * @param quizzId the ID of the quizz to be unfavourited
     */
    public void unfavouriteQuizz(Integer userId, Integer quizzId) {
        QuizzFavouriteId quizzFavouriteId = new QuizzFavouriteId(quizzId, userId);
        quizzFavouriteRepository.deleteById(quizzFavouriteId);
    }
}