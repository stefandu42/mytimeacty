package mytimeacty.model.quizzes.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true) 
public class QuizzWithLikeAndFavouriteDTO extends QuizzDTO {
    private boolean isLiked;
    private boolean isFavourite;
    

}