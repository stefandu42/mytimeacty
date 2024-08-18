package mytimeacty.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.QuizzFavourite;
import mytimeacty.model.quizzes.QuizzLike;

public class QuizzSpecifications {

	public static Specification<Quizz> hasCategoryLabel(String categoryLabel) {
        return (root, query, builder) -> 
            builder.equal(builder.lower(root.get("category").get("label")), categoryLabel.toLowerCase());
    }

    public static Specification<Quizz> hasLevelLabel(String levelLabel) {
        return (root, query, builder) -> 
            builder.equal(builder.lower(root.get("level").get("label")), levelLabel.toLowerCase());
    }
    
    public static Specification<Quizz> hasTitleContaining(String title) {
        return (root, query, builder) -> 
            builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }
    
    public static Specification<Quizz> hasCreatorWithNickname(String nickname) {
        return (root, query, builder) -> 
            builder.like(builder.lower(root.get("creator").get("nickname")), "%" + nickname.toLowerCase() + "%");
    }
    
    public static Specification<Quizz> isLikedByUser(int userId) {
        return (root, query, builder) -> {
            Join<Quizz, QuizzLike> likeJoin = root.join("quizzLikes");;
            return builder.equal(likeJoin.get("user").get("idUser"), userId);
        };
    }
    
    public static Specification<Quizz> isFavouritedByUser(int userId) {
        return (root, query, builder) -> {
            Join<Quizz, QuizzFavourite> favouriteJoin = root.join("quizzFavourites");
            return builder.equal(favouriteJoin.get("user").get("idUser"), userId);
        };
    }
    
    public static Specification<Quizz> isVisible() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isVisible"));
    }
}
