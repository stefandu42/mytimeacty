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

    public void unfavouriteQuizz(Integer userId, Integer quizzId) {
        QuizzFavouriteId quizzFavouriteId = new QuizzFavouriteId(quizzId, userId);
        quizzFavouriteRepository.deleteById(quizzFavouriteId);
    }
}