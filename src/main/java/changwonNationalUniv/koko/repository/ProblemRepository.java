package changwonNationalUniv.koko.repository;


import changwonNationalUniv.koko.dto.response.ProblemResponse;
import changwonNationalUniv.koko.entity.Member;
import changwonNationalUniv.koko.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByLevel(int level);

    @Query("SELECT p.uploadFile.storeFileName FROM Problem p WHERE p.id = :id")
    Optional<String> findFilename(Long id);

    @Query("SELECT " +
            "NEW changwonNationalUniv.koko.dto.response.ProblemResponse(p.id, p.korean, p.challengedCnt, p.clearCnt, cp.clearState) " +
            "FROM Problem p " +
            "left join fetch ChallengedProblem cp on p=cp.problem AND cp.member= :member " +
            "WHERE p.level= :level")
    List<ProblemResponse> findProblemResponsesWithCpByMemberAndLevel(Member member, int level);


}
