package org.example.Client.serviceCenter.balance.impl;

import org.example.Client.serviceCenter.balance.LoadBalance;

import java.util.List;

public class RoundLoadBalance implements LoadBalance {

    private int choose = -1;
    /**
     * 实现具体算法，返回特定地址
     *
     * @param addressList
     * @return
     */
    @Override
    public String balance(List<String> addressList) {
        choose = choose % addressList.size();
        String address = addressList.get(choose);
        choose++;
        System.out.println("负载均衡选择轮询算法，选择 " + address + " 地址的服务器");
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
