package mytimeacty.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.users.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * Retrieves a User entity by their email, ignoring case sensitivity.
	 * 
	 * @param email the email of the user to search for.
	 * @return an Optional containing the User entity if found, otherwise empty.
	 */
	Optional<User> findByEmailIgnoreCase(String email);

	/**
	 * Retrieves a User entity by their nickname, ignoring case sensitivity.
	 * 
	 * @param nickname the nickname of the user to search for.
	 * @return an Optional containing the User entity if found, otherwise empty.
	 */
    Optional<User> findByNicknameIgnoreCase(String nickname);
	
    /**
     * Retrieves a list of User entities whose nicknames contain the specified string, ignoring case sensitivity, and applies pagination.
     * 
     * @param nickname the string to search for within user nicknames.
     * @param pageable the Pageable object containing pagination and sorting information.
     * @return a List of User entities whose nicknames contain the specified string.
     */
	List<User> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);

}