package changwonNationalUniv.koko.controller.dto;

import changwonNationalUniv.koko.entity.Step;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class StepResponse {

    private Integer level;

    private String title;

    private String description;

    @Builder
    public StepResponse(Integer level, String title, String description) {
        this.level = level;
        this.title = title;
        this.description = description;
    }

    public static StepResponse of(Step step) {
        return StepResponse
                .builder()
                .level(step.getLevel())
                .title(step.getTitle())
                .description(step.getDescription())
                .build();
    }
}
