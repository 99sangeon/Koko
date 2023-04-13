package changwonNationalUniv.koko.repository;


import changwonNationalUniv.koko.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByLevel(int level);

    @Query("SELECT p.uploadFile.storeFileName FROM Problem p WHERE p.id = :id")
    Optional<String> findFilename(Long id);

}
