package changwonNationalUniv.koko.entity;

import changwonNationalUniv.koko.enums.ClearState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ExamProblem extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "exam_problem_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ClearState clearState;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

}
