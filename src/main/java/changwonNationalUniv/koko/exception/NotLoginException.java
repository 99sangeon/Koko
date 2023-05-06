package changwonNationalUniv.koko.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotLoginException extends RuntimeException{

    public NotLoginException(String message) {
        super(message);
    }

}
