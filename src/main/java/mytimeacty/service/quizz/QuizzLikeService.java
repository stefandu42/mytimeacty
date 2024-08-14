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

    public void unlikeQuizz(Integer userId, Integer quizzId) {
        QuizzLikeId quizzLikeId = new QuizzLikeId(quizzId, userId);
        quizzLikeRepository.deleteById(quizzLikeId);
    }
}