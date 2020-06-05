package oleksandrdiachenko.pricechecker.controller;

import static java.lang.String.format;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, long id) {
        super(format("Could not find %s with id: %s" + entityName, id));
    }
}
