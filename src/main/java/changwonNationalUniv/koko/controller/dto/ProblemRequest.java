package changwonNationalUniv.koko.controller.dto;

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

    @NotNull
    private Integer level;

    @NotEmpty
    private String korean;

    @NotEmpty
    private String english;

    private MultipartFile audioFile;

    private UploadFile uploadFile;

    @Builder
    public ProblemRequest(int level, String korean, String english, MultipartFile audioFile) {
        this.level = level;
        this.korean = korean;
        this.english = english;
        this.audioFile = audioFile;
    }

    public Problem toEntity() {
        return Problem
                .builder()
                .level(level)
                .korean(korean)
                .english(english)
                .storeFileName(uploadFile.getStoreFileName())
                .uploadFileName(uploadFile.getUploadFileName())
                .build();
    }
}
