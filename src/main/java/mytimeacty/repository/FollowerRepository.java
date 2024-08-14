package mytimeacty.repository;

import mytimeacty.model.followers.Follower;
import mytimeacty.model.followers.FollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, FollowerId> {
   
}