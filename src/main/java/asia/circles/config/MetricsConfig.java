package asia.circles.config;

import asia.circles.service.metrics.ArrayProcessingMetrics;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class MetricsConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
    
    @Bean
    public ArrayProcessingMetrics arrayProcessingMetrics(MeterRegistry registry) {
        return new ArrayProcessingMetrics(registry);
    }
}