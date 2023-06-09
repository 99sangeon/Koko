package changwonNationalUniv.koko.entity;

import changwonNationalUniv.koko.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Member extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 30)
    private String userId;

    @Column(length = 100)
    private String password;

    @Column(length = 20)
    private String mobile;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, length = 100)
    private String email;

    private Integer challengedCnt;

    private Integer successCnt;

    private Integer failureCnt;

    private Integer successProblemCnt;

    private Integer failureProblemCnt;

    private Float cumulativeExp;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "member")
    private List<ChallengedProblem> challengedProblems = new ArrayList<>();


    @Builder
    public Member(String name, String userId, String password, String mobile, String email) {
        this.challengedCnt = 0;
        this.successCnt = 0;
        this.failureCnt = 0;
        this.successProblemCnt = 0;
        this.failureProblemCnt = 0;
        this.cumulativeExp = 0f;
        this.role = Role.MEMBER;
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
    }

    public void addChallengedProblem(ChallengedProblem challengedProblem) {
        this.getChallengedProblems().add(challengedProblem);
        challengedProblem.setMember(this);
    }

    public void increaseChallengeCnt() {
        this.challengedCnt += 1;
    }

    public void increaseSuccessCnt() {
        this.successCnt += 1;
    }

    public void increaseFailureCnt() {
        this.failureCnt += 1;
    }

    public void increaseSuccessProblemCnt() {
        this.successProblemCnt += 1;
    }

    public void increaseFailureProblemCnt() {
        this.failureProblemCnt += 1;
    }

    public void decreaseFailureProblemCnt() {
        this.failureProblemCnt -= 1;
    }
    public void increaseCumulativeExp(float exp) {
        this.cumulativeExp += exp;
    }
}
