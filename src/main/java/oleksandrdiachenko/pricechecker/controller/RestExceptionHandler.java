package oleksandrdiachenko.pricechecker.controller;

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

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice()
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing.";
        return new ResponseEntity<>(new ErrorResponse<>(Collections.singletonList(error)), BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex,
                                                                   HttpServletRequest request) {
        try {
            List<String> messages = ex.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(toList());
            return new ResponseEntity<>(new ErrorResponse<>(messages), BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse<>(Collections.singletonList(ex.getMessage())),
                    INTERNAL_SERVER_ERROR);
        }
    }
}