package changwonNationalUniv.koko.controller.dto;

import changwonNationalUniv.koko.entity.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class ProblemResponse {

    private Long id;

    private Integer level;

    private String korean;

    private String english;

    private Integer clearCnt;

    private Integer challengeCnt;

    @Builder
    public ProblemResponse(Long id, Integer level, String korean, String english, Integer clearCnt, Integer challengeCnt) {
        this.id = id;
        this.level = level;
        this.korean = korean;
        this.english = english;
        this.clearCnt = clearCnt;
        this.challengeCnt = challengeCnt;

    }

    public static ProblemResponse of(Problem problem) {
        return ProblemResponse
                .builder()
                .id(problem.getId())
                .level(problem.getLevel())
                .korean(problem.getKorean())
                .english(problem.getEnglish())
                .clearCnt(problem.getClearCnt())
                .challengeCnt(problem.getChallengeCnt())
                .build();
    }

}
