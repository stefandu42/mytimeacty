package mytimeacty.service.quizzplay;

import java.time.Instant;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mytimeacty.exception.NotFoundException;
import mytimeacty.exception.IllegalArgumentException;
import mytimeacty.mapper.QuizzPlayMapper;
import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.QuizzAnswer;
import mytimeacty.model.quizzes.QuizzQuestion;
import mytimeacty.model.quizzplay.QuizzPlay;
import mytimeacty.model.quizzplay.UserAnswer;
import mytimeacty.model.quizzplay.dto.QuizzPlayDTO;
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
    public Page<QuizzPlayDTO> getQuizzPlaysByQuizz(Integer quizzId, int page, int size) {
        Pageable pageable = PaginationUtils.createPageableSortByDesc(page, size, "playedAt");
        Page<QuizzPlay> quizzPlays = quizzPlayRepository.findByQuizzIdQuizz(quizzId, pageable);

        return quizzPlays.map(QuizzPlayMapper::toDTO);
    }

    /**
     * Handles the process of recording user answers for a specific quizz.
     * This method validates that all questions have been answered and calculates the score for the quizz play.
     * It then saves the user's quizz play and the corresponding answers.
     *
     * @param quizzId the ID of the quizz being played
     * @param userAnswerCreateDTOs a list of `UserAnswerCreateDTO` objects representing the user's answers
     * @throws NotFoundException if the quizz or any answer is not found
     * @throws IllegalArgumentException if not all questions have been answered or if an answer does not belong to the quizz
     */
    @Transactional
    public void handleUserAnswers(Integer quizzId, List<UserAnswerCreateDTO> userAnswerCreateDTOs) {
        Quizz quizz = quizzRepository.findById(quizzId)
                .orElseThrow(() -> new NotFoundException("Quizz not found"));
        
        User currentUser = userRepository.findById(SecurityUtils.getCurrentUser().getIdUser())
                .orElseThrow(() -> new NotFoundException("User not found"));
        
        // Get quizz's questions
        Set<QuizzQuestion> quizQuestions = quizz.getQuizzQuestions();
        
        // Map each UserAnswerCreateDTO to a QuizzAnswer
        Map<Integer, QuizzAnswer> quizzAnswerMap = userAnswerCreateDTOs.stream()
                .map(dto -> quizzAnswerRepository.findById(dto.getAnswerId())
                        .orElseThrow(() -> new NotFoundException("Answer not found")))
                .collect(Collectors.toMap(QuizzAnswer::getIdAnswer, answer -> answer));
        
        // Group answers by their corresponding question
        Map<QuizzQuestion, List<QuizzAnswer>> answersByQuestion = quizzAnswerMap.values().stream()
                .collect(Collectors.groupingBy(QuizzAnswer::getQuestion));
        
        // Check if all questions are answered
        if (answersByQuestion.size() != quizQuestions.size()) {
            throw new IllegalArgumentException("Not all questions have been answered.");
        }

        // Calculate the number of correct answers
        long correctAnswerCount = answersByQuestion.values().stream()
                .flatMap(List::stream)
                .filter(QuizzAnswer::getIsCorrect)
                .count();

        // Calculate the score
        double score = (double) correctAnswerCount / userAnswerCreateDTOs.size() * 100.0;

        // Build QuizzPlay
        QuizzPlay quizzPlayTemp = QuizzPlay.builder()
                .quizz(quizz)
                .player(currentUser)
                .score(score)
                .playedAt(Instant.now())
                .build();
        
        QuizzPlay quizzPlay = quizzPlayRepository.save(quizzPlayTemp);
        
        // Bind answers to QuizzPlay
        List<UserAnswer> userAnswers = userAnswerCreateDTOs.stream()
                .map(dto -> {
                	QuizzAnswer answer = quizzAnswerMap.get(dto.getAnswerId());
                	
                	// If the answer doesn't belong to the Quizz
                	QuizzQuestion question = answer.getQuestion();
                    if (!quizz.getQuizzQuestions().contains(question)) {
                        throw new IllegalArgumentException("The answer (id:" + answer.getIdAnswer() + ") does not belong to the quizz played");
                    }
                    
                    return UserAnswer.builder()
                            .quizzPlay(quizzPlay)
                            .answer(answer)
                            .build();
                })
                .collect(Collectors.toList());
        
        userAnswerRepository.saveAll(userAnswers);
    }

    
}
