package com.projects.diningreview.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAllergyException extends RuntimeException {
    public InvalidAllergyException() {
        super("Invalid Allergy: Allergy must be Peanut, Egg or Dairy");
    }
}
