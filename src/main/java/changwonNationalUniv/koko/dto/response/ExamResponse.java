package changwonNationalUniv.koko.dto.response;

import changwonNationalUniv.koko.enums.ClearState;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ExamResponse {

    private Long id;

    private Integer level;

    private String title;

    private String description;

    private ClearState clearState;

    public ExamResponse(Long id, Integer level, String title, String description, ClearState clearState) {
        this.id = id;
        this.level = level;
        this.title = title;
        this.description = description;
        this.clearState = clearState;
    }
}
