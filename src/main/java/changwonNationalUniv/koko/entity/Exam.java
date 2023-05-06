package changwonNationalUniv.koko.entity;

import changwonNationalUniv.koko.enums.ClearState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Exam extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "exam_id")
    private Long id;

    private Integer level;

    @Enumerated(EnumType.STRING)
    private ClearState clearState;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "exam")
    private List<ExamProblem> examProblems;

    public void addExamProblem(ExamProblem examProblem){
        this.getExamProblems().add(examProblem);
        examProblem.setExam(this);
    }


}
