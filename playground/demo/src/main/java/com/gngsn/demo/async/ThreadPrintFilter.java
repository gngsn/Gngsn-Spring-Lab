package com.gngsn.demo.async;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
public class ThreadPrintFilter implements Filter {

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