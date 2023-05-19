package com.gngsn.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class BaseAsyncConfiguration implements AsyncConfigurer {

    final private Logger log = LoggerFactory.getLogger(BaseAsyncConfiguration.class);

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("async-threads-");
        executor.initialize();
        return executor;
    }

    /**
     * override the getAsyncUncaughtExceptionHandler() method to return our custom asynchronous exception handler:
     * !!! All the exception happened in @async will be handler here.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable ex, Method method, Object... params) -> {

            try {
                log.error("[Exception-Async-Handler] Class-Name: {}-{}\nType: {}\nException: {}",
                        method.getDeclaringClass().getName(),method.getName(),
                        ex.getClass().getName(),
                        ex.getMessage());
            } catch (Throwable nex) {
                log.error("Catch Async Exception: {}", nex.getMessage());
            }
        };
    }

}
