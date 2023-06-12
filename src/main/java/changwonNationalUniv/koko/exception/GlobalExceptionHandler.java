package changwonNationalUniv.koko.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
@ControllerAdvice(basePackages = "changwonNationalUniv.koko.controller")
public class GlobalExceptionHandler {


    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String memberNotFound(StepNotFoundException e){
        log.error("MemberNotFoundException {}", e.getMessage());
        log.error("MemberNotFoundException reason : {}", e.getCause());

        return "/login";
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String fileNotFound(FileNotFoundException e) {
        return e.getMessage();
    }

}
