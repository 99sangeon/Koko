package changwonNationalUniv.koko.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
public class Time {
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Time() {
        createTime = LocalDateTime.now();
    }
}
