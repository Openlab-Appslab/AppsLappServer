package org.appslapp.AppsLappServer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LabAlreadyExistsException extends RuntimeException{
    public LabAlreadyExistsException(String message) {
        super(message);
    }
}
