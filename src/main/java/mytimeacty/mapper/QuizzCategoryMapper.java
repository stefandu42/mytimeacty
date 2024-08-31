package mytimeacty.mapper;

import mytimeacty.model.quizzes.QuizzCategory;
import mytimeacty.model.quizzes.dto.QuizzCategoryDTO;

public class QuizzCategoryMapper {

	public static QuizzCategoryDTO toDTO(QuizzCategory category) {
        if (category == null) {
            return null;
        }

        return QuizzCategoryDTO.builder()
                .idCategory(category.getIdCategory())
                .label(category.getLabel())
                .build();
    }
}
