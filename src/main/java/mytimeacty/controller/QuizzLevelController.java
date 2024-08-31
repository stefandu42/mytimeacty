package mytimeacty.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mytimeacty.model.quizzes.dto.QuizzLevelDTO;
import mytimeacty.service.quizz.QuizzLevelService;
import mytimeacty.utils.SecurityUtils;

@RestController
@RequestMapping("/levels")
public class QuizzLevelController {

	private static final Logger logger = LoggerFactory.getLogger(FollowerController.class);
	
	@Autowired
    private QuizzLevelService levelService;

    @GetMapping
    public ResponseEntity<List<QuizzLevelDTO>> getAllLevels() {
        List<QuizzLevelDTO> levels = levelService.getAllLevels();
        logger.info("User with the nickname '{}' has successfully retrieved all quizzes level", 
        		SecurityUtils.getCurrentUser().getNickname());
        return ResponseEntity.status(HttpStatus.OK).body(levels);
    }
}
