package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED)
public class CollectionAccessDeniedException extends RuntimeException {
  public CollectionAccessDeniedException(String message) {
    super(message);
  }
}
