package mytimeacty.service.quizz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.dto.QuizzDTO;
import mytimeacty.repository.quizz.QuizzRepository;

@Service
public class QuizzService {

    @Autowired
    private QuizzRepository quizzRepository;

    public Page<QuizzDTO> getQuizzes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Quizz> quizzes = quizzRepository.findAll(pageable);
        
        return quizzes.map(QuizzDTO::new);
    }
}