package changwonNationalUniv.koko.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
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

}
