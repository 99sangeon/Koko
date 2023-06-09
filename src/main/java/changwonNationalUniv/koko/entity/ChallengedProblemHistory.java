package changwonNationalUniv.koko.entity;

import changwonNationalUniv.koko.dto.request.UploadFile;
import changwonNationalUniv.koko.enums.ClearState;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengedProblemHistory extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "challenged_problem_history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "challenged_problem_id")
    private ChallengedProblem challengedProblem;

    private Float score;

    private String korean;

    @Column(length = 1000)
    private String feedback;

    @Enumerated(EnumType.STRING)
    private ClearState clearState;

    @Embedded
    private UploadFile uploadFile;

}
