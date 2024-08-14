package mytimeacty.model.followers;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FollowerId implements Serializable {

    @Column(name = "id_follower")
    private Integer idFollower;

    @Column(name = "id_user_followed")
    private Integer idUserFollowed;
}
