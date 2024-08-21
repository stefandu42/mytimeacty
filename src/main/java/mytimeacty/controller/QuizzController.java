package mytimeacty.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(QuizzController.class);
	
	/**
     * Creates a new quizz.
     *
     * @param quizzCreationDTO the data transfer object containing quizz creation details.
     * @return a ResponseEntity containing the created QuizzDTO and HTTP status 201 Created.
     */
	@PostMapping
    public ResponseEntity<QuizzDTO> createQuizz(@Valid @RequestBody QuizzCreateDTO quizzCreationDTO) {
        QuizzDTO createdQuizz = quizzService.createQuizz(quizzCreationDTO);
        logger.info("User with the nickname '{}' has successfully created a quizz with id '{}'", 
        		SecurityUtils.getCurrentUser().getNickname(), createdQuizz.getIdQuizz());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuizz);
    }
	
	/**
     * Retrieves a paginated list of quizzes with optional filtering by title, nickname, category, or level.
     *
     * @param page the page number to retrieve (zero-based).
     * @param size the number of items per page.
     * @param title an optional filter by quizz title.
     * @param nickname an optional filter by user nickname.
     * @param categoryLabel an optional filter by quizz category.
     * @param levelLabel an optional filter by quizz difficulty level.
     * @return a ResponseEntity containing a Page of QuizzDTO objects.
     */
	@GetMapping
    public ResponseEntity<Page<QuizzDTO>> getAllQuizzes(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "15") int size,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String nickname,
        @RequestParam(required = false) String categoryLabel,
        @RequestParam(required = false) String levelLabel) {
        
        Page<QuizzDTO> quizzes = quizzService.getQuizzes(page, size, title, nickname, categoryLabel, levelLabel);
        
        logger.info("User with the nickname '{}' has successfully retrieved all quizzes with params page '{}', size '{}', "
        		+ "title '{}', nickname '{}', category label '{}' and level label '{}'", 
        		SecurityUtils.getCurrentUser().getNickname(), page, size, title, nickname, categoryLabel, levelLabel);
        return ResponseEntity.status(HttpStatus.OK).body(quizzes);
    }
	
	/**
     * Retrieves a paginated list of quizzes liked by a specific user.
     *
     * @param idUser the ID of the user whose liked quizzes are to be retrieved.
     * @param page the page number to retrieve (zero-based).
     * @param size the number of items per page.
     * @param title an optional filter by quizz title.
     * @param categoryLabel an optional filter by quizz category.
     * @param levelLabel an optional filter by quiz difficulty level.
     * @return a ResponseEntity containing a Page of QuizzDTO objects.
     */
	@GetMapping("/likes/users/{idUser}")
	public ResponseEntity<Page<QuizzDTO>> getLikedQuizzes(
		@PathVariable int idUser,
	    @RequestParam(defaultValue = "0") int page, 
	    @RequestParam(defaultValue = "15") int size,
	    @RequestParam(required = false) String title,
	    @RequestParam(required = false) String categoryLabel,
	    @RequestParam(required = false) String levelLabel) {
	    
	    Page<QuizzDTO> quizzes = quizzService.getLikedQuizzes(idUser, page, size, title, categoryLabel, levelLabel);
	    
	    logger.info("User with the nickname '{}' has successfully retrieved all quizzes liked by user with id '{}' using params page '{}', size '{}', "
        		+ "title '{}', category label '{}' and level label '{}'", 
        		SecurityUtils.getCurrentUser().getNickname(), idUser, page, size, title, categoryLabel, levelLabel);
	    return ResponseEntity.status(HttpStatus.OK).body(quizzes);
	}
	
	/**
     * Retrieves a paginated list of quizzes marked as favorites by a specific user.
     *
     * @param idUser the ID of the user whose favorite quizzes are to be retrieved.
     * @param page the page number to retrieve (zero-based).
     * @param size the number of items per page.
     * @param title an optional filter by quizz title.
     * @param categoryLabel an optional filter by quizz category.
     * @param levelLabel an optional filter by quiz difficulty level.
     * @return a ResponseEntity containing a Page of QuizzDTO objects.
     */
	@GetMapping("/favourites/users/{idUser}")
	public ResponseEntity<Page<QuizzDTO>> getFavouriteQuizzes(
		@PathVariable int idUser,
	    @RequestParam(defaultValue = "0") int page, 
	    @RequestParam(defaultValue = "15") int size,
	    @RequestParam(required = false) String title,
	    @RequestParam(required = false) String categoryLabel,
	    @RequestParam(required = false) String levelLabel) {
	    
	    Page<QuizzDTO> quizzes = quizzService.getFavouriteQuizzes(idUser, page, size, title, categoryLabel, levelLabel);
	    
	    logger.info("User with the nickname '{}' has successfully retrieved all quizzes favourited by user with id '{}' using params page '{}', size '{}', "
        		+ "title '{}', category label '{}' and level label '{}'", 
        		SecurityUtils.getCurrentUser().getNickname(), idUser, page, size, title, categoryLabel, levelLabel);
	    return ResponseEntity.status(HttpStatus.OK).body(quizzes);
	}
	
	/**
     * Allows the current user to mark a quizz as a favorite.
     *
     * @param idQuizz the ID of the quizz to be favorited.
     * @return a ResponseEntity with HTTP status 201 Created.
     */
	@PostMapping("/favourite/{idQuizz}")
	public ResponseEntity<String> favouriteQuizz(@PathVariable Integer idQuizz) {
	    quizzFavouriteService.favouriteQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
	    logger.info("User with the nickname '{}' has successfully marked the quizz with id '{}' as favourite", 
        		SecurityUtils.getCurrentUser().getNickname(), idQuizz);
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
     * Allows the current user to remove a quizz from their favorites.
     *
     * @param idQuizz the ID of the quizz to be removed from favorites.
     * @return a ResponseEntity with HTTP status 204 No Content.
     */
    @DeleteMapping("/favourite/{idQuizz}")
    public ResponseEntity<Void> unfavouriteQuizz(@PathVariable Integer idQuizz) {
        quizzFavouriteService.unfavouriteQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
        logger.info("User with the nickname '{}' has successfully remove the quizz with id '{}' as favourite", 
        		SecurityUtils.getCurrentUser().getNickname(), idQuizz);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    
    /**
     * Allows the current user to like a quizz.
     *
     * @param idQuizz the ID of the quizz to be liked.
     * @return a ResponseEntity with HTTP status 201 Created.
     */
    @PostMapping("/like/{idQuizz}")
    public ResponseEntity<String> likeQuizz(@PathVariable Integer idQuizz) {
    	quizzLikeService.likeQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
    	logger.info("User with the nickname '{}' has successfully marked the quizz with id '{}' as liked", 
        		SecurityUtils.getCurrentUser().getNickname(), idQuizz);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Allows the current user to unlike a quizz.
     *
     * @param idQuizz the ID of the quizz to be unliked.
     * @return a ResponseEntity with HTTP status 204 No Content.
     */
    @DeleteMapping("/like/{idQuizz}")
    public ResponseEntity<Void> unlikeQuizz(@PathVariable Integer idQuizz) {
        quizzLikeService.unlikeQuizz(SecurityUtils.getCurrentUser().getIdUser(), idQuizz);
        logger.info("User with the nickname '{}' has successfully remove the quizz with id '{}' as liked", 
        		SecurityUtils.getCurrentUser().getNickname(), idQuizz);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
	
    /**
     * Allows only users with the roles "admin" or "chief" to hide a specific quizz.
     *
     * @param quizzId the ID of the quizz to be hidden.
     * @return a ResponseEntity with a confirmation message and HTTP status 200 OK.
     */
    @RolesAllowed({"admin", "chief"})
    @PutMapping("/{quizzId}/hide")
    public ResponseEntity<String> hideQuiz(@PathVariable int quizzId) {
        quizzService.markQuizzAsHidden(quizzId);
        logger.info("User with the nickname '{}' (role: '{}') has successfully marked the quizz with id '{}' as hidden", 
        		SecurityUtils.getCurrentUser().getNickname(), SecurityUtils.getCurrentUser().getUserRole(), quizzId);
        return ResponseEntity.status(HttpStatus.OK).body("Quiz hidden successfully");
    }
	
}
