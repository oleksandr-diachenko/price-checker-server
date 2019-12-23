package oleksandrdiachenko.pricechecker.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ErrorResponse<T> {

    private List<T> errors;

    public ErrorResponse(List<T> errors) {
        this.errors = errors;
    }

    public List<T> getErrors() {
        return errors;
    }
}
