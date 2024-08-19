package mytimeacty.mapper;

import mytimeacty.model.quizzplay.QuizzPlay;
import mytimeacty.model.quizzplay.dto.QuizzPlayDTO;

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
        		.playerId(quizzPlay.getPlayer().getIdUser())
        		.score(quizzPlay.getScore())
        		.playedAt(quizzPlay.getPlayedAt())
        		.build();
    }
}
