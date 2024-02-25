package work.jatin.newsfeed.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import work.jatin.newsfeed.dto.ErrorMessage;

import java.util.List;

@RestControllerAdvice
public class NewsFeedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                List.of(ex.getMessage()),
                request.getDescription(false)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateLikeException.class)
    public ResponseEntity<ErrorMessage> handleDuplicateLikeException(DuplicateLikeException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                List.of(ex.getMessage()),
                request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ErrorMessage> handleMissingRequestValueException(MissingRequestValueException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                List.of(ex.getMessage()),
                request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleLocationNotFoundException(LocationNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                List.of(ex.getMessage()),
                request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ErrorMessage handleGlobalExceptionHandler(Exception ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                List.of(ex.getMessage()),
                request.getDescription(false));
    }
}
