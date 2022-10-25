package com.gngsn;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

public class CircuitBreakerConfiguration {

    public CircuitBreaker circuitBreakerConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("berlin-server1");
        String result = circuitBreaker.executeSupplier(TestController::limit);

        return circuitBreaker;
    }

    public CircuitBreakerConfig circuitBreakerConfig() {
        // Create a custom configuration for a CircuitBreaker
        return CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .permittedNumberOfCallsInHalfOpenState(2)
            .slidingWindowSize(2)
            .recordExceptions(IOException.class, TimeoutException.class)
//            .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
            .build();
    }

    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig circuitBreakerConfig) {
        // Create a CircuitBreakerRegistry with a custom global configuration
        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }
}
