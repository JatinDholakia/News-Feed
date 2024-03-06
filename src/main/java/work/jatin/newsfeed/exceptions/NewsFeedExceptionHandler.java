package work.jatin.newsfeed.exceptions;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                List.of(ex.getMessage()),
                request.getDescription(false)), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ErrorMessage> handleMissingRequestValueException(MissingRequestValueException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                List.of(ex.getMessage()),
                request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nullable HttpHeaders headers, @Nullable HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                errors, request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleLocationNotFoundException(LocationNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                List.of(ex.getMessage()),
                request.getDescription(false)), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobalExceptionHandler(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                List.of(ex.getMessage()),
                request.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
