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

import mytimeacty.model.quizzes.dto.QuizzCategoryDTO;
import mytimeacty.service.quizz.QuizzCategoryService;
import mytimeacty.utils.SecurityUtils;

@RestController
@RequestMapping("/categories")
public class QuizzCategoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(FollowerController.class);

	@Autowired
    private QuizzCategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<QuizzCategoryDTO>> getAllCategories() {
        List<QuizzCategoryDTO> categories = categoryService.getAllCategories();
        logger.info("User with the nickname '{}' has successfully retrieved all quizzes category", 
        		SecurityUtils.getCurrentUser().getNickname());
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
