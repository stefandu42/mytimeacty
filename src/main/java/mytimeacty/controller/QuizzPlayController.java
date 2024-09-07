package mytimeacty.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mytimeacty.model.quizzplay.dto.QuizzPlayDTO;
import mytimeacty.model.quizzplay.dto.QuizzPlayWithAnswerDTO;
import mytimeacty.model.quizzplay.dto.creation.UserAnswerCreateDTO;
import mytimeacty.service.quizzplay.QuizzPlayService;
import mytimeacty.utils.SecurityUtils;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/quizz-play")
public class QuizzPlayController {
	
	@Autowired
    private QuizzPlayService quizzPlayService;
	
	private static final Logger logger = LoggerFactory.getLogger(QuizzPlayController.class);
    
	/**
     * Submits the user's answers for a given quizz.
     * 
     * @param quizzId the ID of the quizz being played.
     * @param userAnswerCreateDTOs the list of UserAnswerCreateDTO containing the user's answers.
     * @return a ResponseEntity with a success message and HTTP status 201 Created.
     */
    @PostMapping("/quizzes/{quizzId}")
    public ResponseEntity<QuizzPlayDTO> submitUserAnswers(
            @PathVariable int quizzId, 
            @RequestBody List<UserAnswerCreateDTO> userAnswerCreateDTOs) {

        QuizzPlayDTO quizzPlay= quizzPlayService.handleUserAnswers(quizzId, userAnswerCreateDTOs);
        logger.info("User with the nickname '{}' has successfully answered the quizz with id '{}'", 
        		SecurityUtils.getCurrentUser().getNickname(), quizzId);
        return ResponseEntity.status(HttpStatus.CREATED).body(quizzPlay);
    }
    
    /**
     * Retrieves a paginated list of quizz plays associated with a specific quizz.
     * 
     * @param quizzId the ID of the quizz for which the plays are being retrieved.
     * @param page the page number to retrieve (zero-based).
     * @param size the number of items per page.
     * @return a ResponseEntity containing a Page of QuizzPlayDTO objects and HTTP status 200 OK.
     */
    @GetMapping("/quizzes/{quizzId}/plays")
    public ResponseEntity<Page<QuizzPlayDTO>> getQuizzPlaysByQuizz(
            @PathVariable int quizzId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        Page<QuizzPlayDTO> quizzPlayDTOs = quizzPlayService.getQuizzPlaysByQuizz(quizzId, page, size);
        logger.info("User with the nickname '{}' has successfully retrieved the quizz plays for the quizz with id '{}' using "
        		+ "params page '{}' and size '{}'", 
        		SecurityUtils.getCurrentUser().getNickname(), quizzId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(quizzPlayDTOs);
    }
    
    /**
     * Retrieves the quizz play with its answers
     * 
     * @param quizzPlayId the ID of the quizz play for which the quizz play and its answers are being retrieved.
     * @return a ResponseEntity containing the quizz dto and HTTP status 200 OK.
     */
    @GetMapping("/{quizzPlayId}/answers")
    public ResponseEntity<QuizzPlayWithAnswerDTO> getQuizzPlayWithAnswers(
            @PathVariable int quizzPlayId) {

        QuizzPlayWithAnswerDTO quizzPlayDTO = quizzPlayService.getQuizzPlayByIdWithDetails(quizzPlayId);
        logger.info("User with the nickname '{}' has successfully retrieved the user answers for the quizz play with id '{}'", 
        		SecurityUtils.getCurrentUser().getNickname(), quizzPlayId);
        return ResponseEntity.status(HttpStatus.OK).body(quizzPlayDTO);
    }
}
