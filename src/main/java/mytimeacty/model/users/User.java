package mytimeacty.model.users;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "mytimeacty")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "nickname", nullable = false, unique = true, length = 20)
    private String nickname;
    
    @Column(name = "pwd", nullable = false, length = 60)
    private String password;
    
    @Column(name = "user_role", nullable = false)
    private String userRole;
    
    @Column(name = "user_previous_role", nullable = false)
    private String userPreviousRole;
    
    @Column(name = "is_activated", nullable = false)
    private Boolean isActivated;
    
    @Column(name = "actual_connexion_token", nullable = false)
    private String actualConnexionToken;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant  createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = Instant.now();
    }
}