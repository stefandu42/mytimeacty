package mytimeacty.model.quizzes;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import mytimeacty.model.users.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quizzes", schema = "mytimeacty")
public class Quizz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_quizz")
    private Integer idQuizz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_creator", nullable = false)
    private User creator;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_level", nullable = false)
    private QuizzLevel level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", nullable = false)
    private QuizzCategory category;
    
    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible;

    @Column(name = "img", nullable = true)
    private String img;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = Instant.now();
    }
    
    @OneToMany(mappedBy = "quizz", fetch = FetchType.LAZY)
    private Set<QuizzLike> quizzLikes;
    
    @OneToMany(mappedBy = "quizz", fetch = FetchType.LAZY)
    private Set<QuizzFavourite> quizzFavourites;
    
    @OneToMany(mappedBy = "quizz")
    private Set<QuizzQuestion> quizzQuestions;

}