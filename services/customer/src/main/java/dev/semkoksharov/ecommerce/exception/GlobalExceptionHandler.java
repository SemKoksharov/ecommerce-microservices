package dev.semkoksharov.ecommerce.exception;

import dev.semkoksharov.ecommerce.dto.ErrorResponse;
import dev.semkoksharov.ecommerce.dto.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundExceptions(CustomerNotFoundException ex) {
        return generateErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> methodArgumentNotValidException(MethodArgumentNotValidException ex) {

        var errorsMap = new HashMap<String, String>();
        var status = HttpStatus.BAD_REQUEST;

        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError) error).getField();
                    var errorMessage = String.format("[Validation error] %s", error.getDefaultMessage());
                    errorsMap.put(fieldName, errorMessage);
                });

        return ResponseEntity.status(status).body(
                new ValidationError(errorsMap, status, getFormattedTimestamp()
                ));
    }

    private ResponseEntity<ErrorResponse> generateErrorResponse(Exception ex, HttpStatus status) {

        var exception = ex.getClass().toString().replace(".class", "");
        var message = ex.getMessage();
        var timestamp = this.getFormattedTimestamp();
        var stackTrace = Arrays.toString(ex.getStackTrace());
        var errorResponse = new ErrorResponse(status, exception, timestamp, message, stackTrace);

        log.error("::: Error :: Timestamp: {} :: Msg: {} Exception: {} :: Status: {}", timestamp, message, exception, status);
        log.error("::: Exception [{}] was handled by global exception handler :::", exception);
        log.error("::: Stack trace: {}", stackTrace);

        return ResponseEntity.status(status).body(errorResponse);
    }


    public String getFormattedTimestamp() {
        return DATE_FORMAT.format(new Date());
    }
}
