package com.gngsn.jpademo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExecptionHandler {

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public String handleLineException(final Exception exception) {
        log.error(exception.getMessage());
        return "error/404";
    }


}
