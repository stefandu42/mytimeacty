package mytimeacty.mapper;

import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.dto.QuizzCategoryDTO;
import mytimeacty.model.quizzes.dto.QuizzDTO;
import mytimeacty.model.quizzes.dto.QuizzLevelDTO;

public class QuizzMapper {

	public static QuizzDTO toDTO(Quizz quizz) {
        if (quizz == null) {
            return null;
        }
        
        QuizzCategoryDTO categoryDTO = QuizzCategoryDTO.builder()
        		.idCategory(quizz.getCategory().getIdCategory())
        		.label(quizz.getCategory().getLabel())
        		.build();
        
        QuizzLevelDTO levelDTO = QuizzLevelDTO.builder()
        		.idLevel(quizz.getLevel().getIdLevel())
        		.label(quizz.getLevel().getLabel())
        		.build();

        return QuizzDTO.builder()
                .idQuizz(quizz.getIdQuizz())
                .title(quizz.getTitle())
                .creatorId(quizz.getCreator().getIdUser())
                .creatorNickname(quizz.getCreator().getNickname())
                .category(categoryDTO)
                .level(levelDTO)
                .createdAt(quizz.getCreatedAt())
                .build();
    }
}