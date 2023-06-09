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
    private String koPronunciation;

    @Column(nullable = false)
    private String enPronunciation;

    private Integer clearCnt = 0;

    private Integer challengedCnt = 0;

    @Embedded
    private UploadFile uploadFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id")
    private Step step;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "problem")
    private List<ChallengedProblem> challengedProblems;

    @Builder
    public Problem(Integer level, Float exp,String korean, String english, UploadFile uploadFile, String koPronunciation, String enPronunciation) {
        this.level = level;
        this.exp = exp;
        this.korean = korean;
        this.english = english;
        this.uploadFile = uploadFile;
        this.koPronunciation = koPronunciation;
        this.enPronunciation = enPronunciation;

    }

    public void addChallengedProblem(ChallengedProblem challengedProblem) {
        this.getChallengedProblems().add(challengedProblem);
        challengedProblem.setProblem(this);
    }

    public void increaseClearCnt() {
        this.clearCnt += 1;
    }

    public void increaseChallengedCnt() {
        this.challengedCnt += 1;
    }
}
