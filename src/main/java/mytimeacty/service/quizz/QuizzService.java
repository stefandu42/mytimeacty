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

    public Page<QuizzDTO> getQuizzes(int page, int size, String title, String nickname, String categoryLabel, String levelLabel) {
    	Pageable pageable = createPageable(page, size);
        
        Specification<Quizz> spec = applyCommonSpecifications(title, categoryLabel, levelLabel);
        
        if (nickname != null && spec!=null && !nickname.isBlank()) {
            spec = spec.and(QuizzSpecifications.hasCreatorWithNickname(nickname));
        }
        
        Page<Quizz> quizzes = quizzRepository.findAll(spec, pageable);
        
        return quizzes.map(QuizzMapper::toDTO);
    }
    
    public Page<QuizzDTO> getLikedQuizzes(int userId, int page, int size, String title, String categoryLabel, String levelLabel) {
        return getFilteredQuizzes(QuizzSpecifications.isLikedByUser(userId), page, size, title, categoryLabel, levelLabel);
    }

    public Page<QuizzDTO> getFavouriteQuizzes(int userId, int page, int size, String title, String categoryLabel, String levelLabel) {
        return getFilteredQuizzes(QuizzSpecifications.isFavouritedByUser(userId), page, size, title, categoryLabel, levelLabel);
    }
    
    private Page<QuizzDTO> getFilteredQuizzes(Specification<Quizz> additionalSpec, int page, int size, String title, String categoryLabel, String levelLabel) {
        Pageable pageable = createPageable(page, size);
        Specification<Quizz> spec = applyCommonSpecifications(title, categoryLabel, levelLabel);
        
        if (additionalSpec != null) {
            spec = spec.and(additionalSpec);
        }
        
        Page<Quizz> quizzes = quizzRepository.findAll(spec, pageable);
        return quizzes.map(QuizzMapper::toDTO);
    }
    
    
    private Specification<Quizz> applyCommonSpecifications(String title, String categoryLabel, String levelLabel) {
        Specification<Quizz> spec = Specification.where(null);
        
        if (categoryLabel != null && !categoryLabel.isBlank()) {
            spec = spec.and(QuizzSpecifications.hasCategoryLabel(categoryLabel));
        }

        if (levelLabel != null && !levelLabel.isBlank()) {
            spec = spec.and(QuizzSpecifications.hasLevelLabel(levelLabel));
        }
        
        if (title != null && !title.isBlank()) {
            spec = spec.and(QuizzSpecifications.hasTitleContaining(title));
        }
        
        return spec;
    }
    
    private Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}