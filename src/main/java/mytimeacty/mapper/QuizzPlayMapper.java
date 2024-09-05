package mytimeacty.mapper;

import java.util.List;

import mytimeacty.model.quizzplay.QuizzPlay;
import mytimeacty.model.quizzplay.dto.QuizzPlayDTO;
import mytimeacty.model.quizzplay.dto.QuizzPlayWithAnswerDTO;
import mytimeacty.model.quizzplay.dto.UserAnswerDTO;

public class QuizzPlayMapper {

	/**
	 * Converts a QuizzPlay entity to a QuizzPlayDTO.
	 * 
	 * @param quizzPlay the QuizzPlay entity to be converted.
	 * @return a QuizzPlayDTO representing the quizz play's information, or null if the input quizzPlay is null.
	 */
	public static QuizzPlayDTO toDTO(QuizzPlay quizzPlay) {
		if(quizzPlay == null )
			return null;
		
        return QuizzPlayDTO.builder()
        		.idQuizzPlay(quizzPlay.getIdQuizzPlay())
        		.quizzId(quizzPlay.getQuizz().getIdQuizz())
        		.player(UserMapper.toDTO(quizzPlay.getPlayer()))
        		.score(quizzPlay.getScore())
        		.playedAt(quizzPlay.getPlayedAt())
        		.build();
    }
	
	/**
	 * Converts a QuizzPlay entity to a QuizzPlayDTO with answers.
	 * 
	 * @param quizzPlay the QuizzPlay entity to be converted.
	 * @return a QuizzPlayDTO representing the quizz play's information, or null if the input quizzPlay is null.
	 */
	public static QuizzPlayWithAnswerDTO withAnswerstoDTO(QuizzPlay quizzPlay, List<UserAnswerDTO> userAnswers) {
		if(quizzPlay == null )
			return null;
		
        return QuizzPlayWithAnswerDTO.builder()
        		.idQuizzPlay(quizzPlay.getIdQuizzPlay())
        		.quizzId(quizzPlay.getQuizz().getIdQuizz())
        		.player(UserMapper.toDTO(quizzPlay.getPlayer()))
        		.score(quizzPlay.getScore())
        		.playedAt(quizzPlay.getPlayedAt())
        		.userAnswers(userAnswers)
        		.build();
    }
}
