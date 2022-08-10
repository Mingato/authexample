package com.redcompany.receita.infra.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by vntclol on 04/09/2014.
 */
@ControllerAdvice
public class BadRequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BadRequestHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        LOGGER.warn("Returning HTTP 400 Bad Request", e);
    }

}
