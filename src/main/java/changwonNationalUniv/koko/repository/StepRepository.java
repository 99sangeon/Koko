package changwonNationalUniv.koko.repository;

import changwonNationalUniv.koko.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StepRepository extends JpaRepository<Step, Long> {

    List<Step> findAllByOrderByLevelAsc();

}
