package changwonNationalUniv.koko.dto.response;

import changwonNationalUniv.koko.entity.ChallengedProblemHistory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengedProblemHistoryResponse {

    private Float score;
    private String korean;
    private String feedback;

    public static ChallengedProblemHistoryResponse of(ChallengedProblemHistory challengedProblemHistory){
        return ChallengedProblemHistoryResponse
                .builder()
                .score(challengedProblemHistory.getScore())
                .korean(challengedProblemHistory.getKorean())
                .feedback(challengedProblemHistory.getFeedback())
                .build();
    }
}
