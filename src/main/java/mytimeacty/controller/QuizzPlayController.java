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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mytimeacty.model.quizzplay.dto.QuizzPlayDTO;
import mytimeacty.model.quizzplay.dto.creation.QuizzPlayCreateDTO;
import mytimeacty.model.quizzplay.dto.creation.UserAnswerCreateDTO;
import mytimeacty.service.quizzplay.QuizzPlayService;
import mytimeacty.service.quizzplay.UserAnswerService;

@RestController
@RequestMapping("/quizz-play")
public class QuizzPlayController {
	
	@Autowired
    private QuizzPlayService quizzPlayService;
    
    
    @PostMapping("/quizzes/{quizzId}/answers")
    public ResponseEntity<String> submitUserAnswers(
            @PathVariable Integer quizzId, 
            @RequestBody List<UserAnswerCreateDTO> userAnswerCreateDTOs) {

        quizzPlayService.handleUserAnswers(quizzId, userAnswerCreateDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body("Quizz play and answers created successfully.");
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<QuizzPlayDTO>> getUserQuizzPlays(@PathVariable Integer userId) {
        List<QuizzPlayDTO> quizzPlayDTOs = quizzPlayService.getQuizzPlaysByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(quizzPlayDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizzPlayDTO> getQuizzPlayById(@PathVariable Integer id) {
        QuizzPlayDTO quizzPlayDTO = quizzPlayService.getQuizzPlayById(id);
        return ResponseEntity.status(HttpStatus.OK).body(quizzPlayDTO);
    }
}
