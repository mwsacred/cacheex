package cc.before30.metricex.cacheex.config;

import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedThreadPoolBulkheadMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedTimeLimiterMetrics;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * CircuitBreakerConfig
 *
 * @author before30
 * @since 2020/07/18
 */
@Configuration
public class CacheExCircuitBreakerConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(MeterRegistry registry) {
        CircuitBreakerConfig defaultConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(100)
                .minimumNumberOfCalls(10)
                .permittedNumberOfCallsInHalfOpenState(5)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(defaultConfig);
        TaggedCircuitBreakerMetrics
                .ofCircuitBreakerRegistry(circuitBreakerRegistry)
                .bindTo(registry);

        return circuitBreakerRegistry;
    }

    @Bean
    public TimeLimiterRegistry timeLimiterRegistry(MeterRegistry registry) {
        TimeLimiterConfig defaultConfig = TimeLimiterConfig.custom()
                .cancelRunningFuture(true)
                .timeoutDuration(Duration.ofMillis(500))
                .build();

        TimeLimiterRegistry timeLimiterRegistry = TimeLimiterRegistry.of(defaultConfig);
        TaggedTimeLimiterMetrics
                .ofTimeLimiterRegistry(timeLimiterRegistry)
                .bindTo(registry);

        return timeLimiterRegistry;
    }

    @Bean
    public ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry(MeterRegistry registry) {
        ThreadPoolBulkheadConfig defaultConfig = ThreadPoolBulkheadConfig.ofDefaults();

        ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry = ThreadPoolBulkheadRegistry.of(defaultConfig);
        TaggedThreadPoolBulkheadMetrics
                .ofThreadPoolBulkheadRegistry(threadPoolBulkheadRegistry)
                .bindTo(registry);

        return threadPoolBulkheadRegistry;
    }
}