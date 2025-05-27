package org.example.Client.serviceCenter;

import java.net.InetSocketAddress;

public interface ServiceCenter {

    /**
     * 根据服务名查找地址
     * @param serviceName
     * @return
     */
    InetSocketAddress serviceDiscovery(String serviceName);

    /**
     * 检查是否能够重试
     * @param serviceName
     * @return
     */
    boolean checkRetry(String serviceName);
}
