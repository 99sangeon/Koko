package changwonNationalUniv.koko.controller.dto;

import changwonNationalUniv.koko.entity.Step;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class StepRequest {

    @NotNull
    private Integer level;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    public Step toEntity() {
        return Step
                .builder()
                .level(level)
                .title(title)
                .description(description)
                .build();
    }
}
