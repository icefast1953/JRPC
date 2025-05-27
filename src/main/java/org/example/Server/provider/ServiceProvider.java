package org.example.Server.provider;

import org.example.Server.ratelimit.provider.RateLimitProvider;
import org.example.Server.serviceRegister.Impl.ZKServiceRegister;
import org.example.Server.serviceRegister.ServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {
    private Map<String, Object> interfaceProvider;

    private ServiceRegister serviceRegister;

    private RateLimitProvider rateLimitProvider;

    private String host;

    private int port;

    // 初始化
    public ServiceProvider(String host, int port) {
        interfaceProvider = new HashMap<>();
        serviceRegister = new ZKServiceRegister();
        rateLimitProvider = new RateLimitProvider();
        this.host = host;
        this.port = port;
    }


    public void provideServiceInterface(Object service, boolean canRetry) {
        String serviceName = service.getClass().getName();

        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class<?> i : interfaces) {
            interfaceProvider.put(i.getName(), service);
            serviceRegister.register(i.getName(), new InetSocketAddress(host, port), canRetry);
        }
    }

    public Object getService(String interfaceName) {

        return interfaceProvider.get(interfaceName);
    }

    public RateLimitProvider getRateLimitProvider() {
        return rateLimitProvider;
    }
}
