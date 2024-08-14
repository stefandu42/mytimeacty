package mytimeacty.service.quizz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import mytimeacty.mapper.QuizzMapper;
import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.dto.QuizzDTO;
import mytimeacty.repository.quizz.QuizzRepository;
import mytimeacty.specification.QuizzSpecifications;

@Service
public class QuizzService {

    @Autowired
    private QuizzRepository quizzRepository;

    public Page<QuizzDTO> getQuizzes(int page, int size, String title, String categoryLabel, String levelLabel) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        
        Specification<Quizz> spec = Specification.where(null);
        
        if (categoryLabel != null && !categoryLabel.isEmpty()) {
            spec = spec.and(QuizzSpecifications.hasCategoryLabel(categoryLabel));
        }

        if (levelLabel != null && !levelLabel.isEmpty()) {
            spec = spec.and(QuizzSpecifications.hasLevelLabel(levelLabel));
        }
        
        if (title != null && !title.isEmpty()) {
            spec = spec.and(QuizzSpecifications.hasTitleContaining(title));
        }
        
        Page<Quizz> quizzes = quizzRepository.findAll(spec, pageable);
        
        return quizzes.map(QuizzMapper::toDTO);
    }
}