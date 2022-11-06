package com.gngsn;

import com.gngsn.config.CircuitBreakerConfiguration;
import com.gngsn.service.TestService;
import com.gngsn.service.delay.NoDelay;
import com.gngsn.service.failure.SucceedNTimesAndThenFail;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class CircuitBreakerTest {

    Logger log = LoggerFactory.getLogger(CircuitBreakerTest.class);

    TestController testController;
    TestService testService;

    @Test
    void countBasedSlidingWindow() {
        CircuitBreakerConfiguration conf = new CircuitBreakerConfiguration();
        CircuitBreakerRegistry registry = conf.circuitBreakerRegistry(conf.circuitBreakerConfig());
        CircuitBreaker circuitBreaker = conf.circuitBreaker(registry);

        testService = new TestService(new SucceedNTimesAndThenFail(3), new NoDelay());
        testController = new TestController(testService, circuitBreaker);

        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(registry)
            .bindTo(meterRegistry);

        for (int i = 0; i < 20; i++) {
            try {
                testController.randomLimiter(21);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        printMetricDetails(meterRegistry);
    }

    void printMetricDetails(MeterRegistry meterRegistry) {
        Consumer<Meter> meterConsumer = meter -> {
            String desc = meter.getId().getDescription();
            String metricName = meter.getId().getName();
            String tagName = "";
            String tagValue = "";
            if (metricName.equals("resilience4j.circuitbreaker.state")) {
                tagName = "state";
                tagValue = meter.getId().getTag(tagName);
            }
            if (metricName.equals("resilience4j.circuitbreaker.calls")) {
                tagName = "kind";
                tagValue = meter.getId().getTag(tagName);
            }
            Double metricValue = StreamSupport.stream(meter.measure().spliterator(), false)
                .filter(m -> {
                    return m.getStatistic().name().equals("VALUE");
                })
                .findFirst()
                .map(m -> m.getValue())
                .orElse(0.0);
            System.out.print(desc + " - " + metricName + ": " + metricValue);
            if (!tagValue.isEmpty()) {
                System.out.println(", " + tagName + ": " + tagValue);
            }
            else {
                System.out.println();
            }
        };
        meterRegistry.forEachMeter(meterConsumer);
    }
}
