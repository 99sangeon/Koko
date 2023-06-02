package changwonNationalUniv.koko.repository;

import changwonNationalUniv.koko.dto.response.SuccessCntResponse;
import changwonNationalUniv.koko.entity.ChallengedProblem;
import changwonNationalUniv.koko.entity.Member;
import changwonNationalUniv.koko.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChallengedProblemRepository extends JpaRepository<ChallengedProblem, Long> {

    Optional<ChallengedProblem> findChallengedProblemByMemberAndProblem(Member member, Problem problem);

}
