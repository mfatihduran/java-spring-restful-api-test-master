package uk.co.huntersix.spring.rest.advice;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.co.huntersix.spring.rest.exception.ExistingPersonException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class PersonControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { NullPointerException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<String> handle(NullPointerException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(value = { PersonNotFoundException.class })
    protected ResponseEntity<String> handle(PersonNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(value = { ExistingPersonException.class })
    @ResponseStatus(value = HttpStatus.CONFLICT)
    protected ResponseEntity<String> handle(ExistingPersonException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
