package mytimeacty.service.quizz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import mytimeacty.exception.NotFoundException;
import mytimeacty.exception.UserNotFoundException;
import mytimeacty.mapper.QuizzMapper;
import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.QuizzAnswer;
import mytimeacty.model.quizzes.QuizzCategory;
import mytimeacty.model.quizzes.QuizzLevel;
import mytimeacty.model.quizzes.QuizzQuestion;
import mytimeacty.model.quizzes.dto.QuizzDTO;
import mytimeacty.model.quizzes.dto.creation.AnswerCreateDTO;
import mytimeacty.model.quizzes.dto.creation.QuestionCreateDTO;
import mytimeacty.model.quizzes.dto.creation.QuizzCreateDTO;
import mytimeacty.model.users.User;
import mytimeacty.repository.UserRepository;
import mytimeacty.repository.quizz.QuizzAnswerRepository;
import mytimeacty.repository.quizz.QuizzCategoryRepository;
import mytimeacty.repository.quizz.QuizzLevelRepository;
import mytimeacty.repository.quizz.QuizzQuestionRepository;
import mytimeacty.repository.quizz.QuizzRepository;
import mytimeacty.specification.QuizzSpecifications;
import mytimeacty.utils.PaginationUtils;
import mytimeacty.utils.SecurityUtils;

@Service
public class QuizzService {

    @Autowired
    private QuizzRepository quizzRepository;
    
    @Autowired
    private QuizzQuestionRepository quizzQuestionRepository;

    @Autowired
    private QuizzAnswerRepository quizzAnswerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizzLevelRepository quizzLevelRepository;

    @Autowired
    private QuizzCategoryRepository quizzCategoryRepository;
    
    
    public QuizzDTO createQuizz(QuizzCreateDTO quizzCreationDTO) {
        // Get entities binded
        User creator = userRepository.findById(SecurityUtils.getCurrentUser().getIdUser())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        QuizzLevel level = quizzLevelRepository.findById(quizzCreationDTO.getLevelId())
                .orElseThrow(() -> new NotFoundException("Quizz not found"));
        QuizzCategory category = quizzCategoryRepository.findById(quizzCreationDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        // Build the quizz
        Quizz quizz = Quizz.builder()
                .title(quizzCreationDTO.getTitle())
                .creator(creator)
                .level(level)
                .category(category)
                .img(quizzCreationDTO.getImg())
                .build();

        quizz = quizzRepository.save(quizz);

        // Build questions and answers
        for (QuestionCreateDTO questionDTO : quizzCreationDTO.getQuestions()) {
            QuizzQuestion question = QuizzQuestion.builder()
                    .question(questionDTO.getQuestion())
                    .numQuestion(questionDTO.getNumQuestion())
                    .quizz(quizz)
                    .build();

            question = quizzQuestionRepository.save(question);

            for (AnswerCreateDTO answerDTO : questionDTO.getAnswers()) {
                QuizzAnswer answer = QuizzAnswer.builder()
                        .answer(answerDTO.getAnswer())
                        .numAnswer(answerDTO.getNumAnswer())
                        .isCorrect(answerDTO.getIsCorrect())
                        .question(question)
                        .build();

                quizzAnswerRepository.save(answer);
            }
        }

        return QuizzMapper.toDTO(quizz);
    }

    public Page<QuizzDTO> getQuizzes(int page, int size, String title, String nickname, String categoryLabel, String levelLabel) {
    	Pageable pageable = PaginationUtils.createPageableSortByDesc(page, size, "createdAt");
        
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
        Pageable pageable = PaginationUtils.createPageableSortByDesc(page, size, "createdAt");
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
}