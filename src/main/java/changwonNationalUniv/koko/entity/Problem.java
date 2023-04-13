package changwonNationalUniv.koko.entity;

import changwonNationalUniv.koko.dto.request.UploadFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Problem extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "problem_id")
    private Long id;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private String korean;

    @Column(nullable = false)
    private String english;

    private Integer clearCnt = 0;

    private Integer challengeCnt = 0;

    @Embedded
    private UploadFile uploadFile;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private Step step;


    @Builder
    public Problem(Integer level, String korean, String english, UploadFile uploadFile) {
        this.level = level;
        this.korean = korean;
        this.english = english;
        this.uploadFile = uploadFile;
    }
}
