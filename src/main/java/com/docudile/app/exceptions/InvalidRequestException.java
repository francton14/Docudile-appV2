package com.docudile.app.exceptions;

import org.springframework.validation.Errors;

/**
 * Created by franc on 5/24/2016.
 */
@SuppressWarnings("serial")
public class InvalidRequestException extends RuntimeException {

    private Errors errors;

    public InvalidRequestException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }

}
