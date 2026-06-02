package com.projects.diningreview.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidScoreNameException extends RuntimeException {
    public InvalidScoreNameException(String message) {
        super("Error: " + message + "is not a valid score name. It must be: dairy, egg or peanut.");
    }
}
