package org.example.Client.circuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;

public class CircuitBreaker {
    //  默认为关闭状态，这里的关闭指的是关闭熔断
    private CircuitBreakerState state = CircuitBreakerState.CLOSED;
    private AtomicInteger failureCount = new AtomicInteger(0);
    private AtomicInteger successCount = new AtomicInteger(0);
    private AtomicInteger requestCount = new AtomicInteger(0);
    //  失败次数阈值
    private final int failureThreshold;
    //  半开启到关闭状态的成功次数比例
    private final double halfOpenSuccessRate;
    //  恢复时间
    private final long retryTimePeriod;
    //  上次失败时间
    private long lastFailureTime = 0;

    public CircuitBreaker(int failureThreshold, double halfOpenSuccessRate, long retryTimePeriod) {
        this.failureThreshold = failureThreshold;
        this.halfOpenSuccessRate = halfOpenSuccessRate;
        this.retryTimePeriod = retryTimePeriod;
    }

    /**
     * 查看当前熔断器是否允许请求通过
     * @return
     */
    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        System.out.println("熔断swtich之前!!!!!!!+failureNum==" + failureCount);

        switch (state) {
            case OPEN:
                if (currentTime - lastFailureTime > retryTimePeriod) {
                    state = CircuitBreakerState.HALF_OPEN;
                    resetCounts();
                    return true;
                }
                //  否则不允许请求通过
                return false;
            case HALF_OPEN:
                requestCount.incrementAndGet();
                return true;

            case CLOSED:
                default:
                requestCount.decrementAndGet();
                return true;
        }
    }

    //  记录一次成功的请求
    public synchronized void recordSuccess() {
        if (state == CircuitBreakerState.HALF_OPEN) {
            successCount.incrementAndGet();
            if (successCount.get() >= halfOpenSuccessRate * requestCount.get()) {
                state = CircuitBreakerState.CLOSED;
                resetCounts();
            }
        } else {
            resetCounts();
        }
    }

    //  记录一次失败的请求
    public synchronized void recordFailure() {
        failureCount.incrementAndGet();
        lastFailureTime = System.currentTimeMillis();
        if (failureCount.get() >= failureThreshold) {
            state = CircuitBreakerState.OPEN;

        }
    }

    private void resetCounts() {
        failureCount.set(0);
        successCount.set(0);
        requestCount.set(0);
    }


    enum CircuitBreakerState {
        //关闭    开启   半开启
        CLOSED, OPEN, HALF_OPEN
    }
}
