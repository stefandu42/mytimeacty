package mytimeacty.service.quizzplay;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mytimeacty.exception.NotFoundException;
import mytimeacty.mapper.UserAnswerMapper;
import mytimeacty.model.quizzplay.dto.UserAnswerDTO;
import mytimeacty.repository.quizzplay.QuizzPlayRepository;
import mytimeacty.repository.quizzplay.UserAnswerRepository;
import mytimeacty.utils.SecurityUtils;

@Service
public class UserAnswerService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;
    
    @Autowired
    private QuizzPlayRepository quizzPlayRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(UserAnswerService.class);

    /**
     * Retrieves a list of `UserAnswerDTO` objects associated with a specific quizz play.
     *
     * @param quizzPlayId the ID of the quizz play for which answers are to be retrieved
     * @return a list of `UserAnswerDTO` objects
     * @throws NotFoundException if the quizz play is not found
     */
    public List<UserAnswerDTO> getAnswersByQuizzPlay(int quizzPlayId) {
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method getQuizzPlaysByQuizz: User '{}'", currentUserNickname);
    	
        if (!quizzPlayRepository.existsById(quizzPlayId)) {
        	logger.warn("Method getAnswersByQuizzPlay: Quizz play with ID {} not found. Current User nickname: {}",
        			quizzPlayId, currentUserNickname);
            throw new NotFoundException("Quizz play not found");
        }
    	
        List<UserAnswerDTO> answers = userAnswerRepository.findByQuizzPlayIdQuizzPlay(quizzPlayId)
        		.stream()
        		.map(UserAnswerMapper::toDTO)
        		.collect(Collectors.toList());
        
        logger.info("Method getAnswersByQuizzPlay: Answers for quizz play with ID {} retrieved sucessfully. Current User nickname: {}",
        		quizzPlayId, currentUserNickname);
        
        return answers;
    }
    
    
}
