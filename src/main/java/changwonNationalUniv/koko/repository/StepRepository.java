package changwonNationalUniv.koko.repository;

import changwonNationalUniv.koko.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StepRepository extends JpaRepository<Step, Long> {

    List<Step> findAllByOrderByLevelAsc();

    @Query("SELECT s.level FROM Step s ORDER BY s.level ASC")
    List<Integer> findLevelsSortedAsc();

}
