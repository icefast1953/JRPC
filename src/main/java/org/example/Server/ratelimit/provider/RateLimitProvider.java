package org.example.Server.ratelimit.provider;

import org.example.Server.ratelimit.RateLimit;
import org.example.Server.ratelimit.impl.TokenBucketRateLimitImpl;

import java.util.HashMap;
import java.util.Map;

public class RateLimitProvider {

    private Map<String, RateLimit> rateLimitProvider = new HashMap<>();

    public synchronized void rateLimitRegister(String serviceName, int rate, int capacity) {
        if (rateLimitProvider.containsKey(serviceName)) {
            System.out.println("服务 " + serviceName + " 的RateLimit 已被注册！");
            return;
        }
        // 当前只实现令牌桶算法
        RateLimit rateLimit = new TokenBucketRateLimitImpl(rate, capacity);
        rateLimitProvider.put(serviceName, rateLimit);

        System.out.println("服务 " + serviceName + " 的RateLimit 注册成功！");
    }


    public RateLimit getRateLimit(String serviceName) {
        if (!rateLimitProvider.containsKey(serviceName)) {
            System.out.println("服务 " + serviceName + " 的RateLimit 尚未注册！");
            //  默认参数
            rateLimitRegister(serviceName, 100, 10);
        }
        return rateLimitProvider.get(serviceName);
    }
}
