package mytimeacty.service.quizzplay;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mytimeacty.exception.NotFoundException;
import mytimeacty.mapper.UserAnswerMapper;
import mytimeacty.model.quizzplay.dto.UserAnswerDTO;
import mytimeacty.repository.quizzplay.QuizzPlayRepository;
import mytimeacty.repository.quizzplay.UserAnswerRepository;

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
