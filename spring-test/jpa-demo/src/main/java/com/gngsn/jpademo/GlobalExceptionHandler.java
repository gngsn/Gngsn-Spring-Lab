package com.gngsn.jpademo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public String handleLineException(final Exception exception) {
        exception.printStackTrace();
        log.error(exception.getMessage());
        return "error/404";
    }


}
