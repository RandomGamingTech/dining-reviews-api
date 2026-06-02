package com.projects.diningreview.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidZipCodeException extends RuntimeException {
    public InvalidZipCodeException() {
        super("Invalid zip code");
    }
}
