package mytimeacty.mapper;

import mytimeacty.model.quizzplay.QuizzPlay;
import mytimeacty.model.quizzplay.dto.QuizzPlayDTO;

public class QuizzPlayMapper {

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
