package mytimeacty.service.quizzplay;

import java.time.Instant;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mytimeacty.exception.NotFoundException;
import mytimeacty.exception.IllegalArgumentException;
import mytimeacty.mapper.QuizzPlayMapper;
import mytimeacty.mapper.UserAnswerMapper;
import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.QuizzAnswer;
import mytimeacty.model.quizzes.QuizzQuestion;
import mytimeacty.model.quizzplay.QuizzPlay;
import mytimeacty.model.quizzplay.UserAnswer;
import mytimeacty.model.quizzplay.dto.QuizzPlayDTO;
import mytimeacty.model.quizzplay.dto.QuizzPlayWithAnswerDTO;
import mytimeacty.model.quizzplay.dto.UserAnswerDTO;
import mytimeacty.model.quizzplay.dto.creation.UserAnswerCreateDTO;
import mytimeacty.model.users.User;
import mytimeacty.repository.UserRepository;
import mytimeacty.repository.quizz.QuizzAnswerRepository;
import mytimeacty.repository.quizz.QuizzRepository;
import mytimeacty.repository.quizzplay.QuizzPlayRepository;
import mytimeacty.repository.quizzplay.UserAnswerRepository;
import mytimeacty.utils.PaginationUtils;
import mytimeacty.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;

@Service
public class QuizzPlayService {

    @Autowired
    private QuizzPlayRepository quizzPlayRepository;

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private QuizzAnswerRepository quizzAnswerRepository;
    
    @Autowired
    private UserAnswerRepository userAnswerRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(QuizzPlayService.class);
    
    /**
     * Retrieves a paginated list of `QuizzPlayDTO` objects associated with a specific quizz.
     * The results are sorted in descending order based on the `playedAt` timestamp.
     *
     * @param quizzId the ID of the quizz for which plays are to be retrieved
     * @param page the page number to retrieve
     * @param size the size of the page
     * @return a page of `QuizzPlayDTO` objects
     * @throws NotFoundException if the quizz is not found
     */
    public Page<QuizzPlayDTO> getQuizzPlaysByQuizz(int quizzId, int page, int size) {
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method getQuizzPlaysByQuizz: User '{}'", currentUserNickname);
    	
        Pageable pageable = PaginationUtils.createPageableSortByDesc(page, size, "playedAt");
        Page<QuizzPlay> quizzPlays = quizzPlayRepository.findByQuizzIdQuizz(quizzId, pageable);

        Page<QuizzPlayDTO> pageQuizzPlayDTO = quizzPlays.map(QuizzPlayMapper::toDTO);
        logger.info("Method getQuizzPlaysByQuizz: Get quizz plays of quizz with ID {} created sucessfully. Current User nickname: {}",
        		quizzId, currentUserNickname);
        return pageQuizzPlayDTO;
    }
    
    
    /**
     * Retrieves a quizz play with all its answers.
     *
     * @param quizzPlayId the ID of the quizz play to be retrieved
     * @return quizz play dto with its answers
     * @throws NotFoundException if the quizz play is not found
     */
    public QuizzPlayWithAnswerDTO getQuizzPlayByIdWithDetails(int quizzPlayId) {
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method getQuizzPlaysByQuizz: User '{}'", currentUserNickname);
    	
    	QuizzPlay quizzPlay = quizzPlayRepository.findById(quizzPlayId)
		        .orElseThrow(() -> {
		        	logger.warn("Method getAnswersByQuizzPlay: Quizz Play with ID {} not found. Current User nickname: {}",
		        			quizzPlayId, currentUserNickname);
		        	return new NotFoundException("Quizz Play not found");
		        });
    	
    	List<UserAnswerDTO> userAnswerDTO = userAnswerRepository.findByQuizzPlayIdQuizzPlay(quizzPlayId)
    			.stream()
    			.map(UserAnswerMapper::toDTO)
    			.collect(Collectors.toList());
    	
        
        logger.info("Method getAnswersByQuizzPlay: Answers for quizz play with ID {} retrieved sucessfully. Current User nickname: {}",
        		quizzPlayId, currentUserNickname);
        
        return QuizzPlayMapper.withAnswerstoDTO(quizzPlay, userAnswerDTO);
    }

    /**
     * Handles the process of recording user answers for a specific quizz.
     * This method validates that all questions have been answered and calculates the score for the quizz play.
     * It then saves the user's quizz play and the corresponding answers.
     *
     * @param quizzId the ID of the quizz being played
     * @param userAnswerCreateDTOs a list of `UserAnswerCreateDTO` objects representing the user's answers
     * @return 
     * @throws NotFoundException if the quizz or any answer is not found
     * @throws IllegalArgumentException if not all questions have been answered or if an answer does not belong to the quizz
     */
    @Transactional
    public QuizzPlayDTO handleUserAnswers(int quizzId, List<UserAnswerCreateDTO> userAnswerCreateDTOs) {
    	String currentUserNickname = SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method handleUserAnswers: User '{}'", currentUserNickname);
    	
    	// GETTERS /////
    	
        Quizz quizz = quizzRepository.findById(quizzId)
		        .orElseThrow(() -> {
		        	logger.warn("Method handleUserAnswers: Quizz with ID {} not found. Current User nickname: {}",
		        			quizzId, currentUserNickname);
		        	return new NotFoundException("Quizz not found");
		        });
        
        User currentUser = userRepository.findById(SecurityUtils.getCurrentUser().getIdUser())
                .orElseThrow(() -> new NotFoundException("User not found"));
        
        
        // CHECK IF ANSWERS BELONG TO THE QUIZZ AND IF ALL QUESTIONS HAS BEEN ANSWERED /////
        
        // Get quizz's questions
        Set<QuizzQuestion> quizzQuestions = quizz.getQuizzQuestions();
        
        // Map each UserAnswerCreateDTO to a QuizzAnswer
        Map<Integer, QuizzAnswer> quizzAnswerMap = userAnswerCreateDTOs.stream()
                .map(dto -> {
                    QuizzAnswer answer = quizzAnswerRepository.findById(dto.getAnswerId())
                            .orElseThrow(() -> {
                                logger.warn("Method handleUserAnswers: Answer with ID {} not found. Current User nickname: {}",
                                        quizzId, currentUserNickname);
                                return new NotFoundException("Answer not found");
                            });

                    // Verify if the answer belongs to the current quizz's questions
                    QuizzQuestion question = answer.getQuestion();
                    if (!quizzQuestions.contains(question)) {
                        logger.warn("Method handleUserAnswers: The answer (id: {}) does not belong to the quizz played (id: {}). Current User nickname: {}",
                                answer.getIdAnswer(), quizzId, currentUserNickname);
                        throw new IllegalArgumentException("The answer (id:" + answer.getIdAnswer() + ") does not belong to the quizz played");
                    }

                    return answer;
                })
                .collect(Collectors.toMap(QuizzAnswer::getIdAnswer, answer -> answer));
        
        // Group answers by their corresponding question
        Map<QuizzQuestion, List<QuizzAnswer>> answersByQuestion = quizzAnswerMap.values().stream()
                .collect(Collectors.groupingBy(QuizzAnswer::getQuestion));
        
        // Check if all questions are answered
        if (answersByQuestion.size() != quizzQuestions.size()) {
        	logger.warn("Method handleUserAnswers: Not all questions have been answered. Current User nickname: {}",
        			quizzId, currentUserNickname);
            throw new IllegalArgumentException("Not all questions have been answered.");
        }

        // CALCULATE SCORE /////
        
        // Calculate the number of correct answers
        long correctAnswerCount = answersByQuestion.values().stream()
                .flatMap(List::stream)
                .filter(QuizzAnswer::getIsCorrect)
                .count();

        // Calculate the score
        double score = (double) correctAnswerCount / userAnswerCreateDTOs.size() * 100.0;
        
        
        // SAVE THE QUIZZ PLAY /////

        // Build QuizzPlay
        QuizzPlay quizzPlayTemp = QuizzPlay.builder()
                .quizz(quizz)
                .player(currentUser)
                .score(score)
                .playedAt(Instant.now())
                .build();
        
        QuizzPlay quizzPlay = quizzPlayRepository.save(quizzPlayTemp);
        
        logger.info("Method handleUserAnswers: Quizz play for quizz with ID {} created sucessfully. Current User nickname: {}",
        		quizzId, currentUserNickname);
        
        
        // SAVE USER ANSWERS OF THE QUIZZ PLAY /////
        
        // Bind answers to QuizzPlay
        List<UserAnswer> userAnswers = userAnswerCreateDTOs.stream()
                .map(dto -> {
                	QuizzAnswer answer = quizzAnswerMap.get(dto.getAnswerId());
                    
                    return UserAnswer.builder()
                            .quizzPlay(quizzPlay)
                            .answer(answer)
                            .build();
                })
                .collect(Collectors.toList());
        
        userAnswerRepository.saveAll(userAnswers);
        
        logger.info("Method handleUserAnswers: Answers for quizz play with ID {} created sucessfully. Current User nickname: {}",
        		quizzPlay.getIdQuizzPlay(), currentUserNickname);
        
        return QuizzPlayMapper.toDTO(quizzPlay);
    }

    
}
