package mytimeacty.mapper;

import mytimeacty.model.quizzplay.UserAnswer;
import mytimeacty.model.quizzplay.dto.UserAnswerDTO;

public class UserAnswerMapper {

	public static UserAnswerDTO toDTO(UserAnswer userAnswer) {
		if (userAnswer == null) {
            return null;
        }
        return UserAnswerDTO.builder()
        		.idUserAnswer(userAnswer.getIdUserAnswer())
        		.quizzPlayId(userAnswer.getQuizzPlay().getIdQuizzPlay())
        		.answerId(userAnswer.getAnswer().getIdAnswer())
        		.build();
    }
}
