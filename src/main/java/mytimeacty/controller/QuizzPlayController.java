package mytimeacty.controller;

import java.util.List;

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
import mytimeacty.model.quizzplay.dto.UserAnswerDTO;
import mytimeacty.model.quizzplay.dto.creation.UserAnswerCreateDTO;
import mytimeacty.service.quizzplay.QuizzPlayService;
import mytimeacty.service.quizzplay.UserAnswerService;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/quizz-play")
public class QuizzPlayController {
	
	@Autowired
    private QuizzPlayService quizzPlayService;
	
	@Autowired
    private UserAnswerService userAnswerService;
    
	/**
     * Submits the user's answers for a given quizz.
     * 
     * @param quizzId the ID of the quizz being played.
     * @param userAnswerCreateDTOs the list of UserAnswerCreateDTO containing the user's answers.
     * @return a ResponseEntity with a success message and HTTP status 201 Created.
     */
    @PostMapping("/quizzes/{quizzId}")
    public ResponseEntity<String> submitUserAnswers(
            @PathVariable Integer quizzId, 
            @RequestBody List<UserAnswerCreateDTO> userAnswerCreateDTOs) {

        quizzPlayService.handleUserAnswers(quizzId, userAnswerCreateDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body("Quizz play and answers created successfully.");
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
            @PathVariable Integer quizzId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        Page<QuizzPlayDTO> quizzPlayDTOs = quizzPlayService.getQuizzPlaysByQuizz(quizzId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(quizzPlayDTOs);
    }
    
    /**
     * Retrieves the list of user answers for a specific quizz play.
     * 
     * @param quizzPlayId the ID of the quizz play for which the answers are being retrieved.
     * @return a ResponseEntity containing a list of UserAnswerDTO objects and HTTP status 200 OK.
     */
    @GetMapping("/{quizzPlayId}/answers")
    public ResponseEntity<List<UserAnswerDTO>> getUserAnswersByQuizzPlay(
            @PathVariable Integer quizzPlayId) {

        List<UserAnswerDTO> userAnswerDTOs = userAnswerService.getAnswersByQuizzPlay(quizzPlayId);
        return ResponseEntity.status(HttpStatus.OK).body(userAnswerDTOs);
    }
}
