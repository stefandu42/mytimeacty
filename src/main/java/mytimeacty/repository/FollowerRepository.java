package mytimeacty.repository;

import mytimeacty.model.followers.Follower;
import mytimeacty.model.followers.FollowerId;
import mytimeacty.model.users.User;

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
    
    // To count followers
    int countByUserFollowedIdUser(Integer idUserFollowed);  
    
    // To count subscriptions
    int countByFollowerIdUser(Integer idFollower);  
    
    long countByUserFollowed(User user);
    
    boolean existsByFollowerAndUserFollowed(User follower, User userFollowed);
}