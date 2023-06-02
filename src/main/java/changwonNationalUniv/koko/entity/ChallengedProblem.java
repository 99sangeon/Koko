package changwonNationalUniv.koko.entity;

import changwonNationalUniv.koko.enums.ClearState;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "challenged_problem")
public class ChallengedProblem extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "challenged_problem_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ClearState clearState;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "challengedProblem")
    private List<ChallengedProblemHistory> challengedProblemHistories = new ArrayList<>();

    public void addChallengedProblemHistory(ChallengedProblemHistory challengedProblemHistory) {
        this.getChallengedProblemHistories().add(challengedProblemHistory);
        challengedProblemHistory.setChallengedProblem(this);
    }

}
