package changwonNationalUniv.koko.repository;


import changwonNationalUniv.koko.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByLevel(int level);

}
