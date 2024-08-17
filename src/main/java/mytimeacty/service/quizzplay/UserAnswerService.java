package mytimeacty.service.quizzplay;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mytimeacty.exception.ConflictException;
import mytimeacty.exception.ForbiddenException;
import mytimeacty.exception.IllegalArgumentException;
import mytimeacty.exception.NotFoundException;
import mytimeacty.mapper.UserAnswerMapper;
import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.QuizzAnswer;
import mytimeacty.model.quizzes.QuizzQuestion;
import mytimeacty.model.quizzplay.QuizzPlay;
import mytimeacty.model.quizzplay.UserAnswer;
import mytimeacty.model.quizzplay.dto.UserAnswerDTO;
import mytimeacty.model.quizzplay.dto.creation.UserAnswerCreateDTO;
import mytimeacty.repository.quizz.QuizzAnswerRepository;
import mytimeacty.repository.quizzplay.QuizzPlayRepository;
import mytimeacty.repository.quizzplay.UserAnswerRepository;
import mytimeacty.utils.SecurityUtils;

@Service
public class UserAnswerService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private QuizzPlayRepository quizzPlayRepository;

    @Autowired
    private QuizzAnswerRepository quizzAnswerRepository;

    
    public void addUserAnswers(Integer quizzPlayId, List<UserAnswerCreateDTO> userAnswerCreateDTOs) {
        QuizzPlay quizzPlay = quizzPlayRepository.findById(quizzPlayId)
        		.orElseThrow(() -> new NotFoundException("Quizz Play not found"));
        
        if(!quizzPlay.getPlayer().getIdUser().equals(SecurityUtils.getCurrentUser().getIdUser())) {
        	throw new ForbiddenException("You must be the player of the game");
        }
        
        Quizz quizz = quizzPlay.getQuizz();
        
        try {
	        List<UserAnswer> userAnswers = userAnswerCreateDTOs
	        		.stream()
	        		.map(dto -> {
	                   QuizzAnswer answer = quizzAnswerRepository.findById(dto.getAnswerId())
	                		   .orElseThrow(() -> new NotFoundException("Answer not found"));
	                   QuizzQuestion question = answer.getQuestion();
	                   
	                   if (!quizz.getQuizzQuestions().contains(question)) 
	                       throw new IllegalArgumentException("The answer (id:"+ answer.getIdAnswer() + ") does not belong to the quizz played");
	                   
	                   boolean exists = userAnswerRepository.existsByQuizzPlayAndAnswer(quizzPlay, answer);
	                   
	                   if (exists) {
	                       throw new ConflictException("This answer (id:"+answer.getIdAnswer()+") has already been added to the quiz play.");
	                   }
	                   
	                   return UserAnswer.builder()
	                		   .quizzPlay(quizzPlay)
	                		   .answer(answer)
	                		   .build();
	               })
	        		.collect(Collectors.toList());

	        userAnswerRepository.saveAll(userAnswers);
        } catch (Exception e) {
            quizzPlayRepository.delete(quizzPlay);
            throw e; 
        }
    }

    public List<UserAnswerDTO> getAnswersByQuizzPlay(Integer quizzPlayId) {
        return userAnswerRepository.findByQuizzPlayIdQuizzPlay(quizzPlayId)
        		.stream()
        		.map(UserAnswerMapper::toDTO)
        		.collect(Collectors.toList());
    }
    
    
}
