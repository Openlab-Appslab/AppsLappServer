package org.appslapp.AppsLappServer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnsatisfyingPasswordException extends RuntimeException{
    public UnsatisfyingPasswordException(String message) {
        super(message);
    }
}
