package changwonNationalUniv.koko.entity;

import changwonNationalUniv.koko.enums.ClearState;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "challenged_problem_history")
public class ChallengedProblemHistory extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "challenged_problem_history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "challenged_problem_id")
    private ChallengedProblem challengedProblem;

    @Enumerated(EnumType.STRING)
    private ClearState clearState;

    private Float score;

    private String korean;

    private String feedback;
}
