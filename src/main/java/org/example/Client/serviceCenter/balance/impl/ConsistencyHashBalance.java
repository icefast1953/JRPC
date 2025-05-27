package org.example.Client.serviceCenter.balance.impl;

import org.example.Client.serviceCenter.balance.LoadBalance;

import java.util.*;

public class ConsistencyHashBalance implements LoadBalance {
    // 每个服务器的虚拟节点个数
    private static final int VIRTUAL_NUM = 5;
    //  保有虚拟节点的哈希值（用于排序）， 与虚拟节点
    private SortedMap<Integer, String> shards = new TreeMap<Integer, String>();
    //  真实节点列表
    private List<String> realNodes = new LinkedList<String>();

    // 初始化虚拟节点
    private void init(List<String> addressList) {
        for (String server : addressList) {
            realNodes.add(server);
            System.out.println("真实节点[ " + server + " ]被添加");
            for (int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = server + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash, virtualNode);
                System.out.println("虚拟节点[" + virtualNode + "] hash: " + hash + ", 被添加" );
            }
        }
    }


    public String getServer(String node, List<String> serviceList) {
        init(serviceList);

        int hash = getHash(node);

        Integer key = null;

        SortedMap<Integer, String> subMap = shards.tailMap(hash);

        if (subMap.isEmpty()) {
            key = shards.firstKey();
        } else {
            key = subMap.firstKey();
        }

        String server = shards.get(key);

        return server.substring(0, server.indexOf("&&"));

    }

    /**
     * 实现具体算法，返回特定地址
     *
     * @param addressList
     * @return
     */
    @Override
    public String balance(List<String> addressList) {
        String random = UUID.randomUUID().toString();
        String server = getServer(random, addressList);
        return server;
    }

    /**
     * 添加节点
     *
     * @param node
     */
    @Override
    public void addNode(String node) {
        if (!realNodes.contains(node)) {
            realNodes.add(node);
            System.out.println("真实节点[ " + node + " ]被添加");
            for (int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash, virtualNode);
                System.out.println("虚拟节点[" + virtualNode + "] hash: " + hash + ", 被添加" );
            }
        }
    }

    /**
     * 删除节点
     *
     * @param node
     */
    @Override
    public void deleteNode(String node) {
        if (realNodes.contains(node)) {
            realNodes.remove(node);
            System.out.println("真实节点[ " + node + " ]被删除");
            for (int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.remove(hash);
                System.out.println("虚拟节点[" + virtualNode + "] hash: " + hash + ", 被删除" );
            }

        }
    }

    private static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
            hash += hash << 13;
            hash ^= hash >> 7;
            hash += hash << 3;
            hash ^= hash >> 17;
            hash += hash << 5;
            if (hash < 0) {
                hash = Math.abs(hash);

            }
        return hash;
    }


}
