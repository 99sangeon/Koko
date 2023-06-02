package changwonNationalUniv.koko.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SuccessCntResponse {

    private Date dat;
    private int cnt;

    public SuccessCntResponse(Date date, int cnt) {
        this.dat = date;
        this.cnt = cnt;
    }
}
