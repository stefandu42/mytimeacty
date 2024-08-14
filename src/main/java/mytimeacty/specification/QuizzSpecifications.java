package mytimeacty.specification;

import org.springframework.data.jpa.domain.Specification;
import mytimeacty.model.quizzes.Quizz;

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
}
