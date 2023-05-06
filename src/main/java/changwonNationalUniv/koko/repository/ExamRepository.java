package changwonNationalUniv.koko.repository;

import changwonNationalUniv.koko.dto.response.ExamResponse;
import changwonNationalUniv.koko.entity.Exam;
import changwonNationalUniv.koko.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT " +
            "NEW changwonNationalUniv.koko.dto.response.ExamResponse(s.id, s.level, s.title, s.description, e.clearState) " +
            "FROM Step s " +
            "left join Exam e on s=e.step AND e.member= :member " +
            "ORDER BY s.level")
    List<ExamResponse> findExamWithStepByMember(Member member);

}
