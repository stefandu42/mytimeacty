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

    public List<UserAnswerDTO> getAnswersByQuizzPlay(Integer quizzPlayId) {
        if (!quizzPlayRepository.existsById(quizzPlayId)) {
            throw new NotFoundException("Quizz play not found");
        }
    	
        return userAnswerRepository.findByQuizzPlayIdQuizzPlay(quizzPlayId)
        		.stream()
        		.map(UserAnswerMapper::toDTO)
        		.collect(Collectors.toList());
    }
    
    
}
