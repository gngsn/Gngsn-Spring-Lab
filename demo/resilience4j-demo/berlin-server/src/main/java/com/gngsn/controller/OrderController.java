package com.gngsn.controller;

import com.gngsn.ResDTO;
import com.gngsn.service.OrderService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController("/api/v1")
public class OrderController {

    private final OrderService orderService;
    private final CircuitBreaker circuitBreaker;

    private boolean isError = false;
    Logger log = LoggerFactory.getLogger(OrderService.class);
    public OrderController(OrderService orderService, CircuitBreaker circuitBreaker) {
        this.orderService = orderService;
        this.circuitBreaker = circuitBreaker;
    }

    @RequestMapping("/limit")
    public ResDTO limiter(@RequestParam int id) {
        String res = orderService.successOrErrorWhenNumGreaterThan20(id);

        log.info(res);
        return new ResDTO(200, "[Berlin Server] " + res);
    }

    @RequestMapping("/limit/random")
    public String randomLimiter(int delay) {
        return CircuitBreaker.decorateSupplier(circuitBreaker,
            () -> orderService.successOrErrorWhenNumGreaterThan20(delay))
            .get();
    }

    @RequestMapping("/limit/random")
    public String randomCheckedLimiter(int delay) {
        Random random = new Random();

        return CircuitBreaker.decorateCheckedSupplier(circuitBreaker,
            () -> orderService.successOrErrorWhenNumGreaterThan20(delay))
            .recover((throwable -> {
                if (throwable instanceof ClassCastException) {
                    return () -> "Ask to developer...";
                } else if (throwable instanceof RuntimeException) {
                    log.warn("### exception : {} ###", throwable.getMessage());
                    return () -> "Please try again after few minutes...";
                } else {
                    return () -> "Server is down...";
                }
            })).get();
    }
    @RequestMapping("/delay")
    public ResDTO delay(@RequestParam long delay) throws InterruptedException {
        System.out.println("delay: " + delay);
        Thread.sleep(delay);

        return new ResDTO(200, "[Berlin Server] Request Success");
    }
}
