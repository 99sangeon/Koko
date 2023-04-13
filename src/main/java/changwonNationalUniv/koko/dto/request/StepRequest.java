package changwonNationalUniv.koko.dto.request;

import changwonNationalUniv.koko.entity.Step;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
public class StepRequest {

    @NotNull
    private Integer level;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @Builder
    public StepRequest(Integer level, String title, String description) {
        this.level = level;
        this.title = title;
        this.description = description;
    }

    public Step toEntity() {
        return Step
                .builder()
                .level(level)
                .title(title)
                .description(description)
                .build();
    }

    public static StepRequest of(Step step) {
        return StepRequest
                .builder()
                .level(step.getLevel())
                .title(step.getTitle())
                .description(step.getDescription())
                .build();
    }
}
