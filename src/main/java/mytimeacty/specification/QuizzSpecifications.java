package mytimeacty.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import mytimeacty.model.quizzes.Quizz;
import mytimeacty.model.quizzes.QuizzFavourite;
import mytimeacty.model.quizzes.QuizzLike;

public class QuizzSpecifications {

	/**
     * Creates a specification to filter quizzes by category label.
     *
     * @param categoryLabel the category label to search for (case-insensitive)
     * @return a specification that filters quizzes by category label
     */
	public static Specification<Quizz> hasCategoryLabel(String categoryLabel) {
        return (root, query, builder) -> 
            builder.equal(builder.lower(root.get("category").get("label")), categoryLabel.toLowerCase());
    }

	/**
     * Creates a specification to filter quizzes by level label.
     *
     * @param levelLabel the level label to search for (case-insensitive)
     * @return a specification that filters quizzes by level label
     */
    public static Specification<Quizz> hasLevelLabel(String levelLabel) {
        return (root, query, builder) -> 
            builder.equal(builder.lower(root.get("level").get("label")), levelLabel.toLowerCase());
    }
    
    /**
     * Creates a specification to filter quizzes where the title contains a given string.
     *
     * @param title the string to search for in the title (case-insensitive)
     * @return a specification that filters quizzes whose title contains the given string
     */
    public static Specification<Quizz> hasTitleContaining(String title) {
        return (root, query, builder) -> 
            builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }
    
    /**
     * Creates a specification to filter quizzes created by a user with a specific nickname.
     *
     * @param nickname the nickname of the creator to search for (case-insensitive)
     * @return a specification that filters quizzes created by a user with the given nickname
     */
    public static Specification<Quizz> hasCreatorWithNickname(String nickname) {
        return (root, query, builder) -> 
            builder.like(builder.lower(root.get("creator").get("nickname")), "%" + nickname.toLowerCase() + "%");
    }
    
    /**
     * Creates a specification to filter quizzes liked by a specific user.
     *
     * @param userId the ID of the user who liked the quizzes
     * @return a specification that filters quizzes liked by the user with the given ID
     */
    public static Specification<Quizz> isLikedByUser(int userId) {
        return (root, query, builder) -> {
            Join<Quizz, QuizzLike> likeJoin = root.join("quizzLikes");;
            return builder.equal(likeJoin.get("user").get("idUser"), userId);
        };
    }
    
    /**
     * Creates a specification to filter quizzes favorited by a specific user.
     *
     * @param userId the ID of the user who favorited the quizzes
     * @return a specification that filters quizzes favorited by the user with the given ID
     */
    public static Specification<Quizz> isFavouritedByUser(int userId) {
        return (root, query, builder) -> {
            Join<Quizz, QuizzFavourite> favouriteJoin = root.join("quizzFavourites");
            return builder.equal(favouriteJoin.get("user").get("idUser"), userId);
        };
    }
    
    /**
     * Creates a specification to filter quizzes that are visible.
     *
     * @return a specification that filters quizzes where the visibility flag is true
     */
    public static Specification<Quizz> isVisible() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isVisible"));
    }
}
