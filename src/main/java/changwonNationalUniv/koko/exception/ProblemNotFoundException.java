package changwonNationalUniv.koko.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProblemNotFoundException extends RuntimeException{

    public ProblemNotFoundException(String message) {
        super(message);
    }
}
