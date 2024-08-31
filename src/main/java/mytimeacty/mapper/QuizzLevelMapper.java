package mytimeacty.mapper;

import mytimeacty.model.quizzes.QuizzLevel;
import mytimeacty.model.quizzes.dto.QuizzLevelDTO;

public class QuizzLevelMapper {

	public static QuizzLevelDTO toDTO(QuizzLevel level) {
        if (level == null) {
            return null;
        }

        return QuizzLevelDTO.builder()
                .idLevel(level.getIdLevel())
                .label(level.getLabel())
                .build();
    }
}
