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
   
	/**
	 * Retrieves a paginated list of Follower entities for users who follow a specific user.
	 * 
	 * @param idUserFollowed the ID of the user being followed.
	 * @param pageable the Pageable object containing pagination and sorting information.
	 * @return a Page containing a list of Follower entities where the specified user is followed.
	 */
    Page<Follower> findByUserFollowedIdUser(Integer idUserFollowed, Pageable pageable);

    /**
     * Retrieves a paginated list of Follower entities for users followed by a specific user.
     * 
     * @param idFollower the ID of the user who is following others.
     * @param pageable the Pageable object containing pagination and sorting information.
     * @return a Page containing a list of Follower entities where the specified user is following.
     */
    Page<Follower> findByFollowerIdUser(Integer idFollower, Pageable pageable);
    
    /**
     * Counts the number of followers for a given user entity.
     * 
     * @param user the User entity for which to count followers.
     * @return the number of followers associated with the specified user entity.
     */
    long countByUserFollowed(User user);
    
    /**
     * Counts the number of subscribers (followings) for a given user entity.
     * 
     * @param user the User entity for which to count followings.
     * @return the number of followings associated with the specified user entity.
     */
    long countByFollower(User user);
    
    /**
     * Checks if there is an existing follower relationship between two users.
     * 
     * @param follower the User entity who is following.
     * @param userFollowed the User entity who is being followed.
     * @return true if a follower relationship exists between the specified users, otherwise false.
     */
    boolean existsByFollowerAndUserFollowed(User follower, User userFollowed);
}