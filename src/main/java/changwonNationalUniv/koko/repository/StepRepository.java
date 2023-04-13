package changwonNationalUniv.koko.repository;

import changwonNationalUniv.koko.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StepRepository extends JpaRepository<Step, Long> {

    Optional<Step> findOneByLevel(int level);

    List<Step> findAllByOrderByLevelAsc();

    @Query("SELECT s.level FROM Step s ORDER BY s.level ASC")
    List<Integer> findLevelsSortedAsc();

}
