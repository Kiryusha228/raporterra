package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnsupportedDatabaseException extends RuntimeException {
    public UnsupportedDatabaseException(String message) {
        super(message);
    }
}
