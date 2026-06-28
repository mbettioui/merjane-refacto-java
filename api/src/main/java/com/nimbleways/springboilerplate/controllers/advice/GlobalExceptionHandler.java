package com.nimbleways.springboilerplate.controllers.advice;

import com.nimbleways.springboilerplate.domain.exception.OrderNotFoundException;
import com.nimbleways.springboilerplate.domain.exception.UnsupportedProductTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleOrderNotFound(OrderNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({UnsupportedProductTypeException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleUnsupportedType(RuntimeException ex) {
        return ex.getMessage();
    }
}
