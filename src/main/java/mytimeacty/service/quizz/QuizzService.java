package mytimeacty.service.quizz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mytimeacty.exception.NotFoundException;
import mytimeacty.exception.UserNotFoundException;
import mytimeacty.mapper.QuizzMapper;
import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.QuizzAnswer;
import mytimeacty.model.quizzes.QuizzCategory;
import mytimeacty.model.quizzes.QuizzLevel;
import mytimeacty.model.quizzes.QuizzQuestion;
import mytimeacty.model.quizzes.dto.QuizzDTO;
import mytimeacty.model.quizzes.dto.QuizzWithLikeAndFavouriteDTO;
import mytimeacty.model.quizzes.dto.creation.AnswerCreateDTO;
import mytimeacty.model.quizzes.dto.creation.QuestionCreateDTO;
import mytimeacty.model.quizzes.dto.creation.QuizzCreateDTO;
import mytimeacty.model.users.User;
import mytimeacty.model.users.dto.UserDTO;
import mytimeacty.repository.UserRepository;
import mytimeacty.repository.quizz.QuizzAnswerRepository;
import mytimeacty.repository.quizz.QuizzCategoryRepository;
import mytimeacty.repository.quizz.QuizzFavouriteRepository;
import mytimeacty.repository.quizz.QuizzLevelRepository;
import mytimeacty.repository.quizz.QuizzLikeRepository;
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
    private QuizzLikeRepository quizzLikeRepository;
    
    @Autowired
    private QuizzFavouriteRepository quizzFavouriteRepository;

    @Autowired
    private QuizzCategoryRepository quizzCategoryRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(QuizzService.class);
    
    /**
     * Marks a quizz as hidden by setting the `isVisible` attribute to false.
     * 
     * @param quizzId the ID of the quizz to be hidden
     * @throws NotFoundException if the quizz is not found
     */
    @Transactional
    public void markQuizzAsHidden(int quizzId) {
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method markQuizzAsHidden: User '{}'", currentUserNickname);
    	
    	Quizz quizz = quizzRepository.findById(quizzId)
                .orElseThrow(() -> {
                	logger.warn("Method markQuizzAsHidden: Quizz with ID {} not found. Current User nickname: {}",
                			quizzId, currentUserNickname);
                	return new NotFoundException("Quizz not found");
                });
        quizz.setIsVisible(false);
        quizzRepository.save(quizz);
        
        logger.info("Method markQuizzAsHidden: Quizz with ID {} marked as hidden sucessfully. Current User nickname: {}",
        		quizz.getIdQuizz(), currentUserNickname);
    }
    
    /**
     * Creates a new quizz based on the provided `QuizzCreateDTO` data.
     * This method handles the creation of the quizz, its questions, and the associated answers.
     * The quizz is marked as visible by default.
     * 
     * @param quizzCreationDTO the data transfer object containing the quizz details
     * @return the created `QuizzDTO`
     * @throws UserNotFoundException if the creator (current user) is not found
     * @throws NotFoundException if the level or category is not found
     */
    @Transactional
    public QuizzDTO createQuizz(QuizzCreateDTO quizzCreationDTO) {
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method createQuizz: User '{}'", currentUserNickname);
    	
        // Get entities binded
        User creator = userRepository.findById(SecurityUtils.getCurrentUser().getIdUser())
		        .orElseThrow(() -> {
		        	logger.warn("Method createQuizz: User with ID {} not found. Current User nickname: {}",
		        			SecurityUtils.getCurrentUser().getIdUser(), currentUserNickname);
		        	return new UserNotFoundException("User not found");
		        });
        QuizzLevel level = quizzLevelRepository.findById(quizzCreationDTO.getLevelId())
		        .orElseThrow(() -> {
		        	logger.warn("Method createQuizz: Level with ID {} not found. Current User nickname: {}",
		        			quizzCreationDTO.getLevelId(), currentUserNickname);
		        	return new NotFoundException("Level not found");
		        });
        QuizzCategory category = quizzCategoryRepository.findById(quizzCreationDTO.getCategoryId())
		        .orElseThrow(() -> {
		        	logger.warn("Method createQuizz: Category with ID {} not found. Current User nickname: {}",
		        			quizzCreationDTO.getCategoryId(), currentUserNickname);
		        	return new NotFoundException("Category not found");
		        });

        // Build the quizz
        Quizz quizz = Quizz.builder()
                .title(quizzCreationDTO.getTitle())
                .creator(creator)
                .level(level)
                .category(category)
                .isVisible(true)
                .img(quizzCreationDTO.getImg())
                .build();

        quizz = quizzRepository.save(quizz);
        logger.info("Method createQuizz: Quizz with ID {} created sucessfully. Current User nickname: {}",
        		quizz.getIdQuizz(), currentUserNickname);

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
        QuizzDTO quizzDTO = QuizzMapper.toDTO(quizz);
        logger.info("Method createQuizz: Questions and answers of quizz with ID {} created sucessfully. Current User nickname: {}",
        		quizz.getIdQuizz(), currentUserNickname);
        return quizzDTO;
    }
    
    
    
    /**
     * Retrieves a paginated list of quizzes based on various filters.
     * Filters include title, creator nickname, category, and level. 
     * Only quizzes marked as visible are included.
     * 
     * @param page the page number to retrieve
     * @param size the size of the page
     * @param title the title filter (optional)
     * @param nickname the creator's nickname filter (optional)
     * @param categoryLabel the category label filter (optional)
     * @param levelLabel the level label filter (optional)
     * @return a page of `QuizzDTO` objects
     */
    public Page<QuizzWithLikeAndFavouriteDTO> getQuizzes(int page, int size, String title, String nickname, String categoryLabel, String levelLabel) {
    	UserDTO currentUserDTO = SecurityUtils.getCurrentUser();
    	logger.info("Entering method getQuizzes: User '{}'", currentUserDTO.getNickname());
    	
    	Pageable pageable = PaginationUtils.createPageableSortByDesc(page, size, "createdAt");
        
        Specification<Quizz> spec = applyCommonSpecifications(title, categoryLabel, levelLabel);
        
        if (nickname != null && spec!=null && !nickname.isBlank()) {
            spec = spec.and(QuizzSpecifications.hasCreatorWithNickname(nickname));
        }
        
        Page<Quizz> quizzes = quizzRepository.findAll(spec, pageable);
        
        User currentUser = userRepository.findById(SecurityUtils.getCurrentUser().getIdUser())
		        .orElseThrow(() -> {
		        	logger.warn("Method getQuizzes: User with ID {} not found. Current User nickname: {}",
		        			SecurityUtils.getCurrentUser().getIdUser(), currentUserDTO.getNickname());
		        	return new UserNotFoundException("User not found");
		        });
        
        Page<QuizzWithLikeAndFavouriteDTO> pageQuizzWithLikeAndFavouriteDTO = quizzes.map(quizz -> {
            boolean isLiked = this.quizzLikeRepository.existsByQuizzAndUser(quizz, currentUser);
            boolean isFavourite = this.quizzFavouriteRepository.existsByQuizzAndUser(quizz, currentUser);

            return QuizzMapper.toDTO(quizz, isLiked, isFavourite);
        });
        logger.info("Method getQuizzes: Get quizzes sucessfully. Current User nickname: {}",
        		currentUserDTO.getNickname());
        return pageQuizzWithLikeAndFavouriteDTO;
    }
    
    /**
     * Retrieves a paginated list of quizzes liked by the specified user.
     * 
     * @param userId the ID of the user who liked the quizzes
     * @param page the page number to retrieve
     * @param size the size of the page
     * @param title the title filter (optional)
     * @param categoryLabel the category label filter (optional)
     * @param levelLabel the level label filter (optional)
     * @return a page of `QuizzDTO` objects
     */
    public Page<QuizzDTO> getLikedQuizzes(int userId, int page, int size, String title, String categoryLabel, String levelLabel) {
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method getLikedQuizzes: User '{}'", currentUserNickname);
    	
    	Page<QuizzDTO> pageQuizzDTO = getFilteredQuizzes(QuizzSpecifications.isLikedByUser(userId), page, size, title, categoryLabel, levelLabel);
        logger.info("Method getLikedQuizzes: Get liked quizzes sucessfully. Current User nickname: {}",
        		currentUserNickname);
        return pageQuizzDTO;
    }

    /**
     * Retrieves a paginated list of quizzes favorited by the specified user.
     * 
     * @param userId the ID of the user who favorited the quizzes
     * @param page the page number to retrieve
     * @param size the size of the page
     * @param title the title filter (optional)
     * @param categoryLabel the category label filter (optional)
     * @param levelLabel the level label filter (optional)
     * @return a page of `QuizzDTO` objects
     */
    public Page<QuizzDTO> getFavouriteQuizzes(int userId, int page, int size, String title, String categoryLabel, String levelLabel) {
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method getFavouriteQuizzes: User '{}'", currentUserNickname);
    	
    	Page<QuizzDTO> pageQuizzDTO =  getFilteredQuizzes(QuizzSpecifications.isFavouritedByUser(userId), page, size, title, categoryLabel, levelLabel);
    	logger.info("Method getFavouriteQuizzes: Get favourite quizzes sucessfully. Current User nickname: {}",
        		currentUserNickname);
        return pageQuizzDTO;
    }
    
    /**
     * A private helper method to retrieve quizzes based on common filters (title, category, level)
     * and additional specifications (such as liked or favorited quizzes).
     * 
     * @param additionalSpec additional specification to apply (e.g., liked or favorited by the user)
     * @param page the page number to retrieve
     * @param size the size of the page
     * @param title the title filter (optional)
     * @param categoryLabel the category label filter (optional)
     * @param levelLabel the level label filter (optional)
     * @return a page of `QuizzDTO` objects
     */
    private Page<QuizzDTO> getFilteredQuizzes(Specification<Quizz> additionalSpec, int page, int size, String title, String categoryLabel, String levelLabel) {
    	Pageable pageable = PaginationUtils.createPageableSortByDesc(page, size, "createdAt");
        Specification<Quizz> spec = applyCommonSpecifications(title, categoryLabel, levelLabel);
        
        if (additionalSpec != null) {
            spec = spec.and(additionalSpec);
        }
        
        Page<Quizz> quizzes = quizzRepository.findAll(spec, pageable);
        return quizzes.map(QuizzMapper::toDTO);
    }
    
    /**
     * Applies common specifications for filtering quizzes based on title, category label, and level label.
     * Also filters quizzes to only include those marked as visible.
     * 
     * @param title the title filter (optional)
     * @param categoryLabel the category label filter (optional)
     * @param levelLabel the level label filter (optional)
     * @return a `Specification<Quizz>` object that can be used in repository queries
     */
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
        
        Specification<Quizz> isVisibleSpec = QuizzSpecifications.isVisible();
        spec = (spec == null) ? isVisibleSpec : spec.and(isVisibleSpec);
        
        return spec;
    }
}