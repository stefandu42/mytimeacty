package mytimeacty.repository.quizzplay;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mytimeacty.model.quizzplay.QuizzPlay;

@Repository
public interface QuizzPlayRepository extends JpaRepository<QuizzPlay, Integer> {
    List<QuizzPlay> findByPlayerIdUser(Integer playerId);
}
