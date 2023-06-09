package changwonNationalUniv.koko.entity;

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
public class Step extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "step_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer level;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "step")
    private List<Problem> problem;

    @Builder
    public Step(Integer level, String title, String description) {
        this.level = level;
        this.title = title;
        this.description = description;
    }

    public void addProblem(Problem problem){
        this.getProblem().add(problem);
        problem.setStep(this);
    }

}
