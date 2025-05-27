package org.example.Client.serviceCenter;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.example.Client.cache.ServiceCache;
import org.example.Client.serviceCenter.ZKWatcher.WatchZK;
import org.example.Client.serviceCenter.balance.impl.ConsistencyHashBalance;

import java.net.InetSocketAddress;
import java.util.List;

public class ZKServiceCenter implements ServiceCenter{

    private ServiceCache cache;

    private CuratorFramework client;

    private static final String ROOT_PATH = "MyRPC";

    private static final String RETRY = "CanRetry";

    private WatchZK watchZK;


    public ZKServiceCenter() throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .retryPolicy(retryPolicy)
                .namespace(ROOT_PATH)
                .connectString("localhost:2181")
                .sessionTimeoutMs(40 * 1000)
                .build();
        client.start();
        System.out.println("连接成功！");
        cache = new ServiceCache();
        watchZK = new WatchZK(client, cache);
        watchZK.watchToUpdate(ROOT_PATH);
    }


    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            //  从缓存中取出地址列表
            List<String> strings = cache.getServiceFromCache(serviceName);
            //  若地址列表为空
            if (strings == null) {
                strings = client.getChildren().forPath("/" + serviceName);
            }
            //  从缓存中未能获取地址
            // 获取根节点的子节点的所有具有该服务名的节点的地址信息

            cache.addServicetoCache(serviceName, strings);
            // 这里默认第一个，后面加上负载均衡
            String server = new ConsistencyHashBalance().balance(strings);
            InetSocketAddress inetSocketAddress = parseAddress(server);

            return inetSocketAddress;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 检查是否能够重试
     *
     * @param serviceName
     * @return
     */
    @Override
    public boolean checkRetry(String serviceName) {
        boolean canRetry = false;
        try {
            List<String> list = client.getChildren().forPath("/" + RETRY);
            for (String str : list) {
                if (str.equals(serviceName)) {
                    canRetry = true;
                    System.out.println("服务：" + serviceName + " 在白名单上，可进行重试");
                    break;
                }
            }
            return canRetry;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return canRetry;
    }


    public String getServiceAddress(InetSocketAddress address) {
        if (address == null) {
            return null;
        }
        return address.getHostName() + ":" + address.getPort();
    }

    public InetSocketAddress parseAddress(String address) {
        String host = address.split(":")[0];
        int port = Integer.parseInt(address.split(":")[1]);
        return new InetSocketAddress(host, port);
    }
}
