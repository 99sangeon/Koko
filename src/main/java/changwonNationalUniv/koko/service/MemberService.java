package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.dto.request.MemberRequest;
import changwonNationalUniv.koko.entity.Member;


public interface MemberService {

    void save(MemberRequest memberRequest);

    Member getCurrentMember();

    int getMyRank(String userId);


}
