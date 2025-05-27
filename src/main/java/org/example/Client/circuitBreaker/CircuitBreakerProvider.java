package org.example.Client.circuitBreaker;

import java.util.HashMap;
import java.util.Map;

public class CircuitBreakerProvider {

    private Map<String, CircuitBreaker> circuitBreakersProvider = new HashMap<>();

    public synchronized CircuitBreaker getCircuitBreaker(String ServiceName) {
        if (circuitBreakersProvider.containsKey(ServiceName)) {
            return circuitBreakersProvider.get(ServiceName);
        } else {
            System.out.println("serviceName: " + ServiceName + " 创建一个新的熔断器");
            CircuitBreaker circuitBreaker = new CircuitBreaker(1, 0.5, 10000);
            circuitBreakersProvider.put(ServiceName, circuitBreaker);
            return circuitBreaker;
        }
    }
}
