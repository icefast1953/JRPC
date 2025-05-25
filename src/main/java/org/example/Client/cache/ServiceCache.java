package org.example.Client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCache {
    //  存储服务名：服务地址
    private static Map<String, List<String>> cache = new HashMap<>();
    //  添加服务缓存
    public void addServicetoCache(String serviceName, String address) {

        List<String> list = null;
        //  若在cache中存在服务名，则取出地址列表
        if (cache.containsKey(serviceName)) {
            list = cache.get(serviceName);
            //  若地址列表中包含该地址，则什么都不做，反之，添加服务
            if (!list.contains(address)) {
                list.add(address);
            }

        } else {
            list = new ArrayList<>();
            list.add(address);
            cache.put(serviceName, list);
        }

        System.out.println("将服务名为：" + serviceName + "的服务地址：" + address + "成功注册！");


    }


    public void addServicetoCache(String serviceName, List<String> addresses) {

        List<String> list = null;
        //  若在cache中存在服务名，则取出地址列表
        if (cache.containsKey(serviceName)) {
            list = cache.get(serviceName);
            list.addAll(addresses);
        } else {
            list = new ArrayList<>();
            list.addAll(addresses);
            cache.put(serviceName, list);
        }


        System.out.println("将服务名为：" + serviceName + "的服务地址：" + addresses + "成功注册！");


    }

    //  使用新地址替换旧地址
    public void replaceServiceAddress(String serviceName, String oldAddress, String newAddress) {
        //  若缓存中不存在服务名，则报错
        if (!cache.containsKey(serviceName)) {
            System.out.println("服务不存在！无法替换！");
            return;
        }
        List<String> list = cache.get(serviceName);
        //  若地址列表中不存在旧地址，则报错
        list.remove(oldAddress);
        list.add(newAddress);

    }

    //  从缓存中获取服务
    public List<String> getServiceFromCache(String serviceName) {
        if (!cache.containsKey(serviceName)) {
            return null;
        }
        return cache.get(serviceName);
    }

    //  删除服务中的地址
    public void delete(String serviceName, String address) {
        if (cache.containsKey(serviceName)) {
            cache.get(serviceName).remove(address);
            System.out.println(serviceName + address + " 删除成功！");
        }
    }
}
