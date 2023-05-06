package changwonNationalUniv.koko.repository;


import changwonNationalUniv.koko.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByEmail(String email);

    @Query("SELECT COUNT(*)+1 FROM Member m WHERE m.cumulativeExp > (SELECT m2.cumulativeExp FROM Member m2 WHERE m2.userId = :userId)")
    Integer getMyRank(String userId);

}
