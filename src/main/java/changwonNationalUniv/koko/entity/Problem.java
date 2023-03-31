package changwonNationalUniv.koko.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Problem {

    @Id @GeneratedValue
    @Column(name = "problem_id")
    private Long id;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private String korean;

    @Column(nullable = false)
    private String english;

    private String uploadFileName;

    private String storeFileName;

    @Builder
    public Problem(int level, String korean, String english, String uploadFileName, String storeFileName) {
        this.level = level;
        this.korean = korean;
        this.english = english;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
