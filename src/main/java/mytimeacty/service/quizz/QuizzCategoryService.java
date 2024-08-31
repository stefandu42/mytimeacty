package mytimeacty.service.quizz;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mytimeacty.mapper.QuizzCategoryMapper;
import mytimeacty.model.quizzes.QuizzCategory;
import mytimeacty.model.quizzes.dto.QuizzCategoryDTO;
import mytimeacty.repository.quizz.QuizzCategoryRepository;
import mytimeacty.utils.SecurityUtils;

@Service
public class QuizzCategoryService {

	@Autowired
	private QuizzCategoryRepository categoryRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(QuizzCategoryService.class);

	public List<QuizzCategoryDTO> getAllCategories() {
		String currentUserNickname= SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method getAllCategories: User '{}'", currentUserNickname);
    	
		List<QuizzCategory> categories = categoryRepository.findAll();
		return categories.stream().map(QuizzCategoryMapper::toDTO).collect(Collectors.toList());
	}
	
}
