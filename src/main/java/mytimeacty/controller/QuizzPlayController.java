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
import mytimeacty.model.quizzplay.dto.creation.UserAnswerCreateDTO;
import mytimeacty.service.quizzplay.QuizzPlayService;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/quizz-play")
public class QuizzPlayController {
	
	@Autowired
    private QuizzPlayService quizzPlayService;
    
    
    @PostMapping("/quizzes/{quizzId}")
    public ResponseEntity<String> submitUserAnswers(
            @PathVariable Integer quizzId, 
            @RequestBody List<UserAnswerCreateDTO> userAnswerCreateDTOs) {

        quizzPlayService.handleUserAnswers(quizzId, userAnswerCreateDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body("Quizz play and answers created successfully.");
    }
    
    @GetMapping("/quizzes/{quizzId}/plays")
    public ResponseEntity<Page<QuizzPlayDTO>> getQuizzPlaysByQuizz(
            @PathVariable Integer quizzId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        Page<QuizzPlayDTO> quizzPlayDTOs = quizzPlayService.getQuizzPlaysByQuizz(quizzId, page, size);
        return ResponseEntity.ok(quizzPlayDTOs);
    }
}
