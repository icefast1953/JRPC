package org.example.Server.serviceRegister;

import java.net.InetSocketAddress;

public interface ServiceRegister {

    /**
     * 保存服务与地址
     * @param serviceName
     * @param address
     */
    void register(String serviceName, InetSocketAddress address, boolean canRetry);
}
