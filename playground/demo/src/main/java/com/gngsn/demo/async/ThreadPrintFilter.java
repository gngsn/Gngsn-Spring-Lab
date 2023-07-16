package com.gngsn.demo.async;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ThreadPrintFilter implements Filter {

    public static final Logger log = LoggerFactory.getLogger(ThreadPrintFilter.class);

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        threadLocal.set("Bye");

        filterChain.doFilter(servletRequest, servletResponse);

        if (servletRequest.isAsyncStarted()) {
            log.debug("### [{}] async called CheckFilter", Thread.currentThread().getName());
        } else {
            log.debug("### [{}] called CheckFilter", Thread.currentThread().getName());
        }
    }
}