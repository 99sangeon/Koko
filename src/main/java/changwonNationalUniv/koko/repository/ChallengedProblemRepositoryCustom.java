package changwonNationalUniv.koko.repository;

import changwonNationalUniv.koko.dto.response.SuccessCntResponse;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ChallengedProblemRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    public List<SuccessCntResponse> findSuccessCntForVisualChart(Long memberId){
        String sql =  "SELECT DATE(created_time) AS date, COUNT(*) AS cnt" +
                " FROM Challenged_Problem" +
                " WHERE member_id =:memberId" +
                " AND clear_state='Y' AND created_time >= DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY) " +
                "GROUP BY DATE(created_time), clear_state " +
                "ORDER BY DATE(created_time) ASC";

        List<Object[]> resultList = em.createNativeQuery(sql)
                .setParameter("memberId", memberId)
                .getResultList();

        List<SuccessCntResponse> responseList = new ArrayList<>();
        for (Object[] obj : resultList) {
            Date date = (Date) obj[0];
            BigInteger count = (BigInteger) obj[1];
            int cnt = count.intValue();
            SuccessCntResponse response = new SuccessCntResponse(date, cnt);
            responseList.add(response);
        }

        return responseList;

    }
}
