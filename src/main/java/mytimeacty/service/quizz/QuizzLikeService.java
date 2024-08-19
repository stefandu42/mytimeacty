package mytimeacty.service.quizz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mytimeacty.exception.NotFoundException;
import mytimeacty.exception.UserNotFoundException;
import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.QuizzLike;
import mytimeacty.model.quizzes.ids.QuizzLikeId;
import mytimeacty.model.users.User;
import mytimeacty.repository.UserRepository;
import mytimeacty.repository.quizz.QuizzLikeRepository;
import mytimeacty.repository.quizz.QuizzRepository;

@Service
public class QuizzLikeService {

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizzLikeRepository quizzLikeRepository;

    /**
     * Adds a "like" to a quizz by the specified user.
     * 
     * This method verifies that both the quizz and user exist. If the quizz is not already liked by the user,
     * it creates a new `QuizzLike` entity and saves it to the repository.
     * 
     * @param userId the ID of the user who wants to like the quizz
     * @param quizzId the ID of the quizz to be liked
     * @throws NotFoundException if the quizz is not found
     * @throws UserNotFoundException if the user is not found
     */
    public void likeQuizz(Integer userId, Integer quizzId) {
    	Quizz quizz = quizzRepository.findById(quizzId)
                .orElseThrow(() -> new NotFoundException("Quizz not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    	
    	
        QuizzLikeId quizzLikeId = new QuizzLikeId(quizzId, userId);
        if (!quizzLikeRepository.existsById(quizzLikeId)) {
            QuizzLike quizzLike = new QuizzLike(quizzLikeId, quizz, user);
            quizzLikeRepository.save(quizzLike);
        }
    }

    /**
     * Removes a "like" from a quizz by the specified user.
     * 
     * This method deletes the `QuizzLike` entity identified by the user ID and quizz ID,
     * effectively removing the like from the quizz.
     * 
     * @param userId the ID of the user who wants to unlike the quizz
     * @param quizzId the ID of the quizz to be unliked
     */
    public void unlikeQuizz(Integer userId, Integer quizzId) {
        QuizzLikeId quizzLikeId = new QuizzLikeId(quizzId, userId);
        quizzLikeRepository.deleteById(quizzLikeId);
    }
}