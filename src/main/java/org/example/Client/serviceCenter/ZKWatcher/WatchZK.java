package org.example.Client.serviceCenter.ZKWatcher;

import lombok.AllArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.example.Client.cache.ServiceCache;

@AllArgsConstructor
public class WatchZK {

    private CuratorFramework client;

    private ServiceCache cache;

    //  监听当前节点及其子节点的创建，删除，更新
    public void watchToUpdate(String path) throws InterruptedException {
        CuratorCache curatorCache = CuratorCache.build(client, "/");
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData oldChildData, ChildData newChildData) {
                switch (type) {
                    case Type.NODE_CREATED :
                        String[] pathList = parsePath(newChildData);
                        //  初次创建触发，不理睬
                        if (pathList.length <= 2) {
                            break;
                        } else {
                            String serviceName = pathList[1];
                            String address = pathList[2];
                            cache.addServicetoCache(serviceName, address);
                        }
                        break;
                        //  节点更新
                    case Type.NODE_CHANGED:
                        if (oldChildData.getData() != null) {
                            System.out.println("节点原数据为：" + new String(oldChildData.getData()));
                        } else {
                            System.out.println("节点第一次创建");
                        }

                        String[] newPathList = parsePath(newChildData);
                        String[] oldPathList = parsePath(oldChildData);
                        cache.replaceServiceAddress(newPathList[1], oldPathList[2], newPathList[2]);
                        System.out.println("节点修改后的数据：" + new String(newChildData.getData()));
                        break;
                    case Type.NODE_DELETED :
                        String[] deletePathList = parsePath(oldChildData);
                        if (deletePathList.length <= 2) {
                            break;
                        }
                        cache.delete(deletePathList[1], deletePathList[2]);
                        System.out.println("删除节点数据" + new String(oldChildData.getData()));

                    default:
                        break;

                }
            }
        });
        curatorCache.start();
    }

    private String[] parsePath(ChildData data) {
        String path = new String(data.getData());
        //  按照格式读取
        return path.split("/");
    }


}
