package com.zookeeper.watcher.thread;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Roy on 16/7/3.
 */
public class Application {

    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        ZooKeeperClientFactory factory = new ZooKeeperClientFactory();
        try {
            // init zookeeper instance.
            factory.createZKInstance(new Watcher() {
                public void process(WatchedEvent event) {
                    System.out.println("MyEvent:" + event);
                }
            });
            ZooKeeper zk = ZooKeeperClientFactory.getInstance();
            // create master node
            NodeCreator creator = new NodeCreator(ZooKeeperClientFactory.getInstance());
            String msg = "Create Master Node when = " + new Date();
            creator.createZKNodeLoop("/master1", msg);

            //取出子节点列表
            System.out.println("\ntestDir1的子节点列表为："+zk.getChildren("/testDir1", true));
            zk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        final CountDownLatch countDownLatch = new CountDownLatch(1);
//        // 创建一个与服务器的连接
//        ZooKeeper zk = new ZooKeeper("localhost:2181",
//                60000, new Watcher() {
//            // 监控所有被触发的事件
//            public void process(WatchedEvent event) {
//                //countDownLatch.countDown();
//                System.out.println("已经触发了" + event.getType() + "事件！");
//            }
//        });
//        //countDownLatch.await();
//
//        // 创建一个目录节点
//        zk.create("/wyjtest2", "testRootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
//                CreateMode.PERSISTENT);
//        // 创建一个子目录节点
//        zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
//                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//        System.out.println(new String(zk.getData("/testRootPath",false,null)));
//        // 取出子目录节点列表
//        System.out.println(zk.getChildren("/testRootPath",true));
//        // 修改子目录节点数据
//        zk.setData("/testRootPath/testChildPathOne","modifyChildDataOne".getBytes(),-1);
//        System.out.println("目录节点状态：["+zk.exists("/testRootPath",true)+"]");
//        // 创建另外一个子目录节点
//        zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(),
//                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//        System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo",true,null)));
//        // 删除子目录节点
//        zk.delete("/testRootPath/testChildPathTwo",-1);
//        zk.delete("/testRootPath/testChildPathOne",-1);
//        // 删除父目录节点
//        zk.delete("/testRootPath",-1);
        // 关闭连接

    }
}
