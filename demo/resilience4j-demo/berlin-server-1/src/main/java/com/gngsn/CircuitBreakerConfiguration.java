package com.gngsn;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(ConstServerNm.BERLIN_SERVER_1);
        String result = circuitBreaker.executeSupplier(() -> "Berlin Server 1's circuit breaker is active");

        return circuitBreaker;
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig circuitBreakerConfig) {
        // Create a CircuitBreakerRegistry with a custom global configuration
        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        // Create a custom configuration for a CircuitBreaker
        return CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .permittedNumberOfCallsInHalfOpenState(2)
            .slidingWindowSize(2)
            .recordExceptions(RuntimeException.class, IOException.class, TimeoutException.class)
            .recordException(e -> {
                if (e instanceof RuntimeException) {
                    // additional logic when occur RuntimeException
                    return true;
                }
                return true;
            })
//            .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
            .build();
    }

}
