package mytimeacty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mytimeacty.annotation.RolesAllowed;
import mytimeacty.model.quizzes.dto.QuizzDTO;
import mytimeacty.model.quizzes.dto.creation.QuizzCreateDTO;
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
	
	@PostMapping
    public ResponseEntity<QuizzDTO> createQuizz(@Valid @RequestBody QuizzCreateDTO quizzCreationDTO) {
        QuizzDTO createdQuizz = quizzService.createQuizz(quizzCreationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuizz);
    }
	
	@GetMapping
    public ResponseEntity<Page<QuizzDTO>> getAllQuizzes(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "15") int size,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String nickname,
        @RequestParam(required = false) String categoryLabel,
        @RequestParam(required = false) String levelLabel) {
        
        Page<QuizzDTO> quizzes = quizzService.getQuizzes(page, size, title, nickname, categoryLabel, levelLabel);
        return ResponseEntity.status(HttpStatus.OK).body(quizzes);
    }
	
	@GetMapping("/likes/users/{idUser}")
	public ResponseEntity<Page<QuizzDTO>> getLikedQuizzes(
		@PathVariable int idUser,
	    @RequestParam(defaultValue = "0") int page, 
	    @RequestParam(defaultValue = "15") int size,
	    @RequestParam(required = false) String title,
	    @RequestParam(required = false) String categoryLabel,
	    @RequestParam(required = false) String levelLabel) {
	    
	    Page<QuizzDTO> quizzes = quizzService.getLikedQuizzes(idUser, page, size, title, categoryLabel, levelLabel);
	    
	    return ResponseEntity.status(HttpStatus.OK).body(quizzes);
	}
	
	@GetMapping("/favourites/users/{idUser}")
	public ResponseEntity<Page<QuizzDTO>> getFavouriteQuizzes(
		@PathVariable int idUser,
	    @RequestParam(defaultValue = "0") int page, 
	    @RequestParam(defaultValue = "15") int size,
	    @RequestParam(required = false) String title,
	    @RequestParam(required = false) String categoryLabel,
	    @RequestParam(required = false) String levelLabel) {
	    
	    Page<QuizzDTO> quizzes = quizzService.getFavouriteQuizzes(idUser, page, size, title, categoryLabel, levelLabel);
	    
	    return ResponseEntity.status(HttpStatus.OK).body(quizzes);
	}
	
	@PostMapping("/favourite/{idQuizz}")
	public ResponseEntity<String> favouriteQuizz(@PathVariable Integer idQuizz) {
	    quizzFavouriteService.favouriteQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}

    @DeleteMapping("/favourite/{idQuizz}")
    public ResponseEntity<Void> unfavouriteQuizz(@PathVariable Integer idQuizz) {
        quizzFavouriteService.unfavouriteQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    
    
    @PostMapping("/like/{idQuizz}")
    public ResponseEntity<String> likeQuizz(@PathVariable Integer idQuizz) {
    	quizzLikeService.likeQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/like/{idQuizz}")
    public ResponseEntity<Void> unlikeQuizz(@PathVariable Integer idQuizz) {
        quizzLikeService.unlikeQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    
    @RolesAllowed({"admin", "chief"})
    @GetMapping("/view")
    public ResponseEntity<String> viewDashboard() {
        return ResponseEntity.ok("Welcome to the Dashboard accessible by Admins and Chiefs");
    }
	
    @RolesAllowed({"admin", "chief"})
    @PutMapping("/{quizId}/hide")
    public ResponseEntity<String> hideQuiz(@PathVariable int quizId) {
        quizzService.markQuizAsHidden(quizId);
        return ResponseEntity.status(HttpStatus.OK).body("Quiz hidden successfully");
    }
	
}
