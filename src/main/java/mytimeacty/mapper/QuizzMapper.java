package mytimeacty.mapper;

import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.dto.QuizzDTO;
import mytimeacty.model.quizzes.dto.QuizzWithLikeAndFavouriteDTO;

public class QuizzMapper {

	/**
	 * Converts a Quizz entity to a QuizzDTO.
	 * 
	 * @param quizz the Quizz entity to be converted.
	 * @return a QuizzDTO representing the quizz's information, or null if the input quizz is null.
	 */
	public static QuizzDTO toDTO(Quizz quizz) {
        if (quizz == null) {
            return null;
        }

        // Build and return the QuizzDTO
        return QuizzDTO.builder()
                .idQuizz(quizz.getIdQuizz())
                .title(quizz.getTitle())
                .creatorId(quizz.getCreator().getIdUser())
                .creatorNickname(quizz.getCreator().getNickname())
                .category(QuizzCategoryMapper.toDTO(quizz.getCategory()))
                .level(QuizzLevelMapper.toDTO(quizz.getLevel()))
                .createdAt(quizz.getCreatedAt())
                .build();
    }
	
	public static QuizzWithLikeAndFavouriteDTO toDTO(Quizz quizz, boolean isLiked, boolean isFavourite) {
        if (quizz == null) {
            return null;
        }

        // Build and return the QuizzWithLikeAndFavouriteDTO
        return QuizzWithLikeAndFavouriteDTO.builder()
        		.idQuizz(quizz.getIdQuizz())
                .title(quizz.getTitle())
                .creatorId(quizz.getCreator().getIdUser())
                .creatorNickname(quizz.getCreator().getNickname())
                .category(QuizzCategoryMapper.toDTO(quizz.getCategory()))
                .level(QuizzLevelMapper.toDTO(quizz.getLevel()))
                .createdAt(quizz.getCreatedAt())
        	    .isLiked(isLiked)
        	    .isFavourite(isFavourite)
        	    .build();
    }
}