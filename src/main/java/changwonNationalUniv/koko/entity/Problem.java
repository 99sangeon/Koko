package changwonNationalUniv.koko.entity;

import changwonNationalUniv.koko.dto.request.UploadFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "problem")
public class Problem extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "problem_id")
    private Long id;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private String korean;

    @Column(nullable = false)
    private String english;

    @Column(nullable = false)
    private Float exp;

    @Column(nullable = false)
    private String pronunciation;

    private Integer clearCnt = 0;

    private Integer challengeCnt = 0;

    @Embedded
    private UploadFile uploadFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id")
    private Step step;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "problem")
    private List<ChallengedProblem> challengedProblems;

    @Builder
    public Problem(Integer level, Float exp,String korean, String english, UploadFile uploadFile, String pronunciation) {
        this.level = level;
        this.exp = exp;
        this.korean = korean;
        this.english = english;
        this.uploadFile = uploadFile;
        this.pronunciation = pronunciation;

    }

    public void addChallengedProblem(ChallengedProblem challengedProblem) {
        this.getChallengedProblems().add(challengedProblem);
        challengedProblem.setProblem(this);
    }

    public void increaseClearCnt() {
        this.clearCnt += 1;
    }

    public void increaseChallengeCnt() {
        this.challengeCnt += 1;
    }
}
