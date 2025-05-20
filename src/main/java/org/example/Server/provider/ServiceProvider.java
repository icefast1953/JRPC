package org.example.Server.provider;

import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {
    private Map<String, Object> interfaceProvider;

    // 初始化
    public ServiceProvider() {
        interfaceProvider = new HashMap<>();
    }


    public void provideServiceInterface(Object service) {
        String serviceName = service.getClass().getName();

        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class<?> i : interfaces) {
            interfaceProvider.put(i.getName(), service);
        }
    }

    public Object getService(String interfaceName) {
        return interfaceProvider.get(interfaceName);
    }

}
