package org.example.Server.serviceRegister.Impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.example.Server.serviceRegister.ServiceRegister;

import java.net.InetSocketAddress;

public class ZKServiceRegister implements ServiceRegister {

    private static final String ROOT_PATH = "MyRPC";

    private static final String RETRY = "CanRetry";

    private  CuratorFramework client;

    public ZKServiceRegister() {
        client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(ROOT_PATH)
                .sessionTimeoutMs(40 * 1000)
                .build();
        client.start();
        System.out.println("zookeeper连接建立成功！");
    }


    @Override
    public void register(String serviceName, InetSocketAddress address, boolean canRetry) {
        try {
            if (canRetry) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/" + RETRY + "/" + serviceName, serviceName.getBytes());
            }

            if (client.checkExists().forPath("/" + serviceName) == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath( "/" + serviceName);
            }

            String s = AddressToString(address);

            client.create().withMode(CreateMode.EPHEMERAL).forPath("/" + serviceName + "/" + s, s.getBytes());
            System.out.println(serviceName  + "注册成功！");
        } catch (Exception e) {
            System.out.println("服务已被注册！");
            throw new RuntimeException(e);

        }


    }


    private String AddressToString(InetSocketAddress address) {
        return address.getHostName() + ":" + address.getPort();
    }

    public InetSocketAddress parseAddress(String address) {
        String host = address.split(":")[0];
        int port = Integer.parseInt(address.split(":")[1]);
        return new InetSocketAddress(host, port);
    }
}
