package asia.circles.service.metrics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class ArrayProcessingAspect {
    private final ArrayProcessingMetrics metrics;

    public ArrayProcessingAspect(ArrayProcessingMetrics metrics) {
        this.metrics = metrics;
    }

    @Around("execution(* com.yourcompany.service.*.calculateColumnWidths(..))")
    public Object timeColumnWidthCalculation(ProceedingJoinPoint joinPoint) throws Throwable {
        return metrics.getColumnWidthCalculationTimer().record(() -> {
            try {
                Object result = joinPoint.proceed();
                
                // Count objects created (assuming result is a List)
                if (result instanceof List) {
                    metrics.recordObjectCreations(((List<?>) result).size());
                }
                
                return result;
            } catch (Throwable t) {
                metrics.recordError();
                throw new RuntimeException(t);
            }
        });
    }
}