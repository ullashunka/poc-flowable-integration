package asia.circles.service.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ArrayProcessingMetrics {
    private final MeterRegistry registry;
    private final Counter arraysProcessedCounter;
    private final Counter elementsProcessedCounter;
    private final Counter stringConversionsCounter;
    private final Counter objectCreationsCounter;
    private final Counter errorsCounter;
    private final Timer columnWidthCalculationTimer;
    private final AtomicInteger currentBatchSize = new AtomicInteger(0);
    private final AtomicLong lastProcessingTimeMs = new AtomicLong(0);

    public ArrayProcessingMetrics(MeterRegistry registry) {
        this.registry = registry;

        // Create counters
        this.arraysProcessedCounter = Counter.builder("app.array.processing.arrays")
                .description("Number of arrays processed")
                .register(registry);

        this.elementsProcessedCounter = Counter.builder("app.array.processing.elements")
                .description("Number of elements processed")
                .register(registry);

        this.stringConversionsCounter = Counter.builder("app.array.processing.string.conversions")
                .description("Number of string conversions performed")
                .register(registry);

        this.objectCreationsCounter = Counter.builder("app.array.processing.object.creations")
                .description("Number of objects created during processing")
                .register(registry);

        this.errorsCounter = Counter.builder("app.array.processing.errors")
                .description("Number of errors during processing")
                .register(registry);

        // Create timers
        this.columnWidthCalculationTimer = Timer.builder("app.array.processing.column.width.calculation")
                .description("Time spent calculating column widths")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);

        // Create gauges
        Gauge.builder("app.array.processing.current.batch.size", currentBatchSize, AtomicInteger::get)
                .description("Current batch size being processed")
                .register(registry);

        Gauge.builder("app.array.processing.last.processing.time.ms", lastProcessingTimeMs, AtomicLong::get)
                .description("Last processing time in milliseconds")
                .register(registry);
    }

    public void recordArrayProcessed() {
        arraysProcessedCounter.increment();
    }

    public void recordElementsProcessed(int count) {
        elementsProcessedCounter.increment(count);
    }

    public void recordStringConversions(int count) {
        stringConversionsCounter.increment(count);
    }

    public void recordObjectCreations(int count) {
        objectCreationsCounter.increment(count);
    }

    public void recordError() {
        errorsCounter.increment();
    }

    public void setBatchSize(int size) {
        currentBatchSize.set(size);
    }

    public void setLastProcessingTime(long timeMs) {
        lastProcessingTimeMs.set(timeMs);
    }

    public Timer getColumnWidthCalculationTimer() {
        return columnWidthCalculationTimer;
    }
}