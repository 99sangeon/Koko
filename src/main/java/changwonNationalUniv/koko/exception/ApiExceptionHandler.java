package changwonNationalUniv.koko.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "changwonNationalUniv.koko.controller")
public class ApiExceptionHandler{

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> fileNotFound(FileNotFoundException e) {
        log.error("MemberNotFoundException {}", e.getMessage());
        log.error("MemberNotFoundException reason : {}", e.getCause());

        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<String> notLogin(NotLoginException e) {
        log.error("MemberNotFoundException {}", e.getMessage());
        log.error("MemberNotFoundException reason : {}", e.getCause());

        return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
