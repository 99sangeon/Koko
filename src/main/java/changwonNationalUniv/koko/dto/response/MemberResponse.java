package changwonNationalUniv.koko.dto.response;

import changwonNationalUniv.koko.entity.Member;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

    private Integer rank;

    private String name;

    private String userId;

    private String password;

    private String mobile;

    private String email;

    private Integer challengedCnt;

    private Integer successCnt;

    private Integer failureCnt;

    private Integer successProblemCnt;

    private Integer failureProblemCnt;

    private Float cumulativeExp;

    public static MemberResponse of(Member member) {
        return MemberResponse
                .builder()
                .name(member.getName())
                .userId(member.getUserId())
                .password(member.getPassword())
                .mobile(member.getMobile())
                .email(member.getEmail())
                .challengedCnt(member.getChallengedCnt())
                .successCnt(member.getSuccessCnt())
                .failureCnt(member.getFailureCnt())
                .successProblemCnt(member.getSuccessProblemCnt())
                .failureProblemCnt(member.getFailureProblemCnt())
                .cumulativeExp(member.getCumulativeExp())
                .build();
    }
}
