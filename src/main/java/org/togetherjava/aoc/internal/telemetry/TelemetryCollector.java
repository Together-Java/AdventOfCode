package org.togetherjava.aoc.internal.telemetry;

import java.lang.management.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TelemetryCollector {

    private final Instant startTime;
    private final long startCpuTime;
    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private final List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

    private final Map<String, Long> counters = new ConcurrentHashMap<>();
    private final Map<String, Long> maxValues = new ConcurrentHashMap<>();
    private final Map<String, List<Long>> histograms = new ConcurrentHashMap<>();

    private long gcTimeStart;

    public TelemetryCollector() {
        this.startTime = Instant.now();
        this.startCpuTime = threadMXBean.getCurrentThreadCpuTime();
        this.gcTimeStart = totalGcTime();
    }

    // --- Metrics recording methods ---

    public void increment(String name) {
        counters.merge(name, 1L, Long::sum);
    }

    public void add(String name, long value) {
        counters.merge(name, value, Long::sum);
    }

    public void recordMax(String name, long value) {
        maxValues.merge(name, value, Math::max);
    }

    public void histogram(String name, long value) {
        histograms.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
    }

    // --- Final metrics snapshot ---

    public Map<String, Object> snapshot() {
        Map<String, Object> metrics = new LinkedHashMap<>();

        metrics.put("runtime_ms", Duration.between(startTime, Instant.now()).toMillis());
        metrics.put("cpu_time_ns", threadMXBean.getCurrentThreadCpuTime() - startCpuTime);
        metrics.put("peak_memory_mb", memoryMXBean.getHeapMemoryUsage().getUsed() / 1024 / 1024);
        metrics.put("gc_time_ms", totalGcTime() - gcTimeStart);
        metrics.put("thread_count", threadMXBean.getThreadCount());

        metrics.put("counters", counters);
        metrics.put("max_values", maxValues);
        metrics.put("histograms", histograms);

        return metrics;
    }

    public void printToConsole() {
        Map<String, Object> metrics = snapshot();
        System.out.println("--- Telemetry ---");
        metrics.forEach((k, v) -> System.out.printf("%-15s: %s%n", k, v));
    }

    private long totalGcTime() {
        return gcBeans.stream()
                .mapToLong(gc -> Optional.ofNullable(gc.getCollectionTime()).orElse(0L))
                .sum();
    }

    public static void main(String[] args) {
        TelemetryCollector collector = new TelemetryCollector();
        collector.snapshot();
        collector.histogram("test", 1L);
        collector.histogram("test", 2L);
        collector.histogram("test", 3L);
        collector.histogram("test", 4L);

        collector.printToConsole();
    }
}