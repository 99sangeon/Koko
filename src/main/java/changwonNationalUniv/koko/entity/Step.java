package changwonNationalUniv.koko.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Step extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "step_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer level;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Builder
    public Step(Integer level, String title, String description) {
        this.level = level;
        this.title = title;
        this.description = description;
    }
}
