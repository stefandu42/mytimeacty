package mytimeacty.repository;

import mytimeacty.model.followers.Follower;
import mytimeacty.model.followers.FollowerId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, FollowerId> {
   
	// To get subscribers (those who follow me)
    Page<Follower> findByUserFollowedIdUser(Integer idUserFollowed, Pageable pageable);

    // To get subscriptions (those that I follow)
    Page<Follower> findByFollowerIdUser(Integer idFollower, Pageable pageable);
    
    int countByUserFollowedIdUser(Integer idUserFollowed);  // count followers
    int countByFollowerIdUser(Integer idFollower);  // count subscriptions
}