package mytimeacty.mapper;

import mytimeacty.model.quizzplay.UserAnswer;
import mytimeacty.model.quizzplay.dto.UserAnswerDTO;

public class UserAnswerMapper {

	/**
	 * Converts a UserAnswer entity to a UserAnswerDTO.
	 * 
	 * @param userAnswer the UserAnswer entity to be converted.
	 * @return a UserAnswerDTO representing the user's answer information, or null if the input userAnswer is null.
	 */
	public static UserAnswerDTO toDTO(UserAnswer userAnswer) {
		if (userAnswer == null) {
            return null;
        }
        return UserAnswerDTO.builder()
        		.idUserAnswer(userAnswer.getIdUserAnswer())
        		.answerId(userAnswer.getAnswer().getIdAnswer())
        		.build();
    }
}
