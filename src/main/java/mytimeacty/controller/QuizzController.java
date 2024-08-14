package mytimeacty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mytimeacty.exception.NotFoundException;
import mytimeacty.exception.UserNotFoundException;
import mytimeacty.model.quizzes.dto.QuizzDTO;
import mytimeacty.service.quizz.QuizzFavouriteService;
import mytimeacty.service.quizz.QuizzLikeService;
import mytimeacty.service.quizz.QuizzService;
import mytimeacty.utils.SecurityUtils;

@RestController
@RequestMapping("/quizzes")
public class QuizzController {

	@Autowired
    private QuizzLikeService quizzLikeService;
	
	@Autowired
    private QuizzFavouriteService quizzFavouriteService;
	
	@Autowired
    private QuizzService quizzService;
	
	@GetMapping
    public ResponseEntity<Page<QuizzDTO>> getAllQuizzes(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "15") int size) {
        
        Page<QuizzDTO> quizzes = quizzService.getQuizzes(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(quizzes);
    }
	
	@PostMapping("/favourite/{idQuizz}")
	public ResponseEntity<String> favouriteQuizz(@PathVariable Integer idQuizz) {
	    try {
	        quizzFavouriteService.favouriteQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
	        return ResponseEntity.status(HttpStatus.CREATED).build();
	    } catch (NotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quizz not found"); 
	    } catch (UserNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"); 
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

    @DeleteMapping("/favourite/{idQuizz}")
    public ResponseEntity<Void> unfavouriteQuizz(@PathVariable Integer idQuizz) {
        quizzFavouriteService.unfavouriteQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    
    
    @PostMapping("/like/{idQuizz}")
    public ResponseEntity<String> likeQuizz(@PathVariable Integer idQuizz) {
        try {
            quizzLikeService.likeQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quizz not found"); 
	    } catch (UserNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"); 
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
    }

    @DeleteMapping("/like/{idQuizz}")
    public ResponseEntity<Void> unlikeQuizz(@PathVariable Integer idQuizz) {
        quizzLikeService.unlikeQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
	
	
}
