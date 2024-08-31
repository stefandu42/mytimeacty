package mytimeacty.service.quizz;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mytimeacty.mapper.QuizzLevelMapper;
import mytimeacty.model.quizzes.QuizzLevel;
import mytimeacty.model.quizzes.dto.QuizzLevelDTO;
import mytimeacty.repository.quizz.QuizzLevelRepository;
import mytimeacty.utils.SecurityUtils;

@Service
public class QuizzLevelService {
	
	@Autowired
	private QuizzLevelRepository levelRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(QuizzCategoryService.class);

	public List<QuizzLevelDTO> getAllLevels() {
		String currentUserNickname= SecurityUtils.getCurrentUser().getNickname();
    	logger.info("Entering method getAllLevels: User '{}'", currentUserNickname);
    	
		List<QuizzLevel> levels = levelRepository.findAll();
		return levels.stream().map(QuizzLevelMapper::toDTO).collect(Collectors.toList());
	}
	
	


}
