package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CollectionNotFoundException extends RuntimeException {
    public CollectionNotFoundException(String message) {
        super(message);
    }
}
