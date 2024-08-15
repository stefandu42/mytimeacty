package mytimeacty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.users.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByNicknameIgnoreCase(String nickname);

	Optional<User> findByNickname(String nickname);

}