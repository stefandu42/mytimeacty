package mytimeacty.model.followers;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Table(name = "followers", schema = "mytimeacty")
public class Follower {

    @EmbeddedId
    private FollowerId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idFollower")
    @JoinColumn(name = "id_follower", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUserFollowed")
    @JoinColumn(name = "id_user_followed", nullable = false)
    private User userFollowed;
}

