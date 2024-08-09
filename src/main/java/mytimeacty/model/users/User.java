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
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;
    
    @Column(name = "pwd", nullable = false)
    private String password;
    
    @Column(name = "user_role", nullable = false)
    private String userRole;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant  createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = Instant.now();
    }
}