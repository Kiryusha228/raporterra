package org.example.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidReportQueryException extends RuntimeException {
    public InvalidReportQueryException(String message) {
        super(message);
    }
}
