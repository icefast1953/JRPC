package org.example.Client.serviceCenter;

import java.net.InetSocketAddress;

public interface ServiceCenter {

    /**
     * 根据服务名查找地址
     * @param serviceName
     * @return
     */
    InetSocketAddress serviceDiscovery(String serviceName);
}
