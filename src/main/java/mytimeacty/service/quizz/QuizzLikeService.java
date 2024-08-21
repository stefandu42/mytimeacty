package mytimeacty.service.quizz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import mytimeacty.utils.SecurityUtils;

@Service
public class QuizzLikeService {

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizzLikeRepository quizzLikeRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(QuizzLikeService.class);

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
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method likeQuizz: User '{}'", currentUserNickname);
    	
    	Quizz quizz = quizzRepository.findById(quizzId)
                .orElseThrow(() -> {
                	logger.warn("Method likeQuizz: Quizz with ID {} not found. Current User nickname: {}",
                			quizzId, currentUserNickname);
                	return new NotFoundException("Quizz not found");
                });
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                	logger.warn("Method likeQuizz: User with ID {} not found. Current User nickname: {}",
                			userId, currentUserNickname);
                	return new UserNotFoundException("User not found");
                });
    	
    	
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
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method unlikeQuizz: User '{}'", currentUserNickname);
    	
        QuizzLikeId quizzLikeId = new QuizzLikeId(quizzId, userId);
        quizzLikeRepository.deleteById(quizzLikeId);
    }
}