package com.gngsn.config;

import com.gngsn.ConstServerName;
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

    /**
     * [third step]
     * Create circuit breaker via registry
     *
     * @param circuitBreakerRegistry
     * @return
     */
    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return circuitBreakerRegistry.circuitBreaker(ConstServerName.BERLIN_SERVER);
    }

    /**
     * [second step]
     * Create a CircuitBreakerRegistry with a custom global configuration
     *
     * @param circuitBreakerConfig
     * @return
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig circuitBreakerConfig) {
        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }

    /**
     * [first step]
     * : Create a custom configuration for a CircuitBreaker
     *
     * @return
     */
    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        // using default setting
        CircuitBreakerConfig.ofDefaults();

        // customizing setting
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
