package com.docudile.app.controllers;

import com.docudile.app.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by franc on 5/24/2016.
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({InvalidRequestException.class})
    protected ResponseEntity<?> handleInvalidRequest(RuntimeException runtimeException, WebRequest webRequest) {
        InvalidRequestException invalidRequestException = (InvalidRequestException) runtimeException;
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : invalidRequestException.getErrors().getFieldErrors()) {
            errors.put(fieldError.getField(), messageSource.getMessage(fieldError, null));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return handleExceptionInternal(runtimeException, errors, headers, HttpStatus.UNPROCESSABLE_ENTITY, webRequest);
    }

}
