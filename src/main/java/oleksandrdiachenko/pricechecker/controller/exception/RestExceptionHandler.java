package oleksandrdiachenko.pricechecker.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice()
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing.";
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(new ErrorResponse<>(Collections.singletonList(error)), BAD_REQUEST);
        log.error("Error occurred: {}", responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        try {
            List<String> messages = ex.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(toList());
            ResponseEntity<ErrorResponse<String>> responseEntity = new ResponseEntity<>(new ErrorResponse<>(messages), BAD_REQUEST);
            log.error("Error occurred: {}", responseEntity);
            return responseEntity;
        } catch (Exception e) {
            ResponseEntity<ErrorResponse<String>> responseEntity = new ResponseEntity<>(new ErrorResponse<>(Collections.singletonList(ex.getMessage())),
                    INTERNAL_SERVER_ERROR);
            log.error("Error occurred: {}", responseEntity);
            return responseEntity;
        }
    }
}