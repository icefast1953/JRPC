package org.example.Client.serviceCenter.balance.impl;

import org.example.Client.serviceCenter.balance.LoadBalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance {
    private Random random = new Random();

    /**
     * 实现具体算法，返回特定地址
     *
     * @param addressList
     * @return
     */
    @Override
    public String balance(List<String> addressList) {
        int choose = random.nextInt(addressList.size());
        String address = addressList.get(choose);
        System.out.println("负载均衡使用随机算法，选择 " + address + " 的地址服务器");

        return address;
    }

    /**
     * 添加节点
     *
     * @param node
     */
    @Override
    public void addNode(String node) {

    }

    /**
     * 删除节点
     *
     * @param node
     */
    @Override
    public void deleteNode(String node) {

    }
}
