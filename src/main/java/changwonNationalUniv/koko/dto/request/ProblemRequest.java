package changwonNationalUniv.koko.dto.request;

import changwonNationalUniv.koko.entity.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class ProblemRequest {

    @NotNull(message = "레벨을 선택해주세요.")
    private Integer level;

    @NotNull(message = "경험치를 입력해주세요.")
    private Float exp;

    @NotEmpty(message = "한글을 입력해주세요.")
    private String korean;

    @NotEmpty(message = "영어(번역)을 입력해주세요.")
    private String english;

    private MultipartFile audioFile;

    private UploadFile uploadFile;

    @Builder
    public ProblemRequest(int level, Float exp, String korean, String english, MultipartFile audioFile) {
        this.level = level;
        this.exp = exp;
        this.korean = korean;
        this.english = english;
        this.audioFile = audioFile;
    }

    public Problem toEntity() {
        return Problem
                .builder()
                .level(level)
                .exp(exp)
                .korean(korean)
                .english(english)
                .uploadFile(uploadFile)
                .build();
    }
}
