import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;


public class ZooKeeperTest {

    private CuratorFramework client;

    @Before
    public void testCreateCurator() {

        // 超时策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);

//        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", 60 * 1000, 15 * 1000, retryPolicy);
//
//        client.start();

         client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(retryPolicy)
                .namespace("icefast")
                .build();

        client.start();

    }

    @Test
    public void testCreate() throws Exception {
        client.create().forPath("/app1/p1");
        client.create().forPath("/app1/p2");
        client.create().forPath("/app1/p3");
    }

    @Test
    public void testgetData() throws Exception {
        Stat stat = new Stat();

        byte[] bytes = client.getData().storingStatIn(stat).forPath("/app1/p1");

        System.out.println(new String(bytes));
        System.out.println(stat.getVersion());
    }

    @Test
    public void testGet2() throws Exception {
        List<String> strings = client.getChildren().forPath("/app1");

        System.out.println(strings);

    }


    @Test
    public void testSet1() throws Exception {
        client.setData().forPath("/app1/p1", "icefast".getBytes());
    }

    @Test
    public void testSetWithVersion() throws Exception {
        int version = 1;

        client.setData().withVersion(version).forPath("/app1/p1", "icefast1953".getBytes());
    }


    @Test
    public void testDelete1() throws Exception {
        client.delete().forPath("/app2");
    }


    @Test
    public void testDelete2() throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath("/app2");
    }


    @Test
    public void testDelete3() throws Exception {
        client.delete().guaranteed().forPath("/app2");
    }

    @Test
    public void testDelete4() throws Exception {
        client.delete().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println("删除成功！");
                System.out.println(curatorEvent);
            }
        }).forPath("/app2");
    }

    @After
    public void close() {
        if (client != null) {
            client.close();
        }
    }

    @Test
    public void testWatcher() throws Exception {
        // 1.创建NodeCache对象
        NodeCache nodeCache = new NodeCache(client, "/app1");
        // 2.注册监听
        nodeCache.getListenable().addListener(new NodeCacheListener() {

            @Override
            public void nodeChanged() throws Exception {
                System.out.println("节点变化！");
            }
        });
        // 3.开启监听

        nodeCache.start();

        while (true) {

        }
    }
}
