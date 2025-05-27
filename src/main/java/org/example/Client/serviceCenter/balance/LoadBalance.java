package org.example.Client.serviceCenter.balance;

import java.util.List;

public interface LoadBalance {

    /**
     * 实现具体算法，返回特定地址
     * @param addressList
     * @return
     */
    String balance(List<String> addressList);

    /**
     * 添加节点
     * @param node
     */
    void addNode(String node);

    /**
     * 删除节点
     * @param node
     */
    void deleteNode(String node);
}
