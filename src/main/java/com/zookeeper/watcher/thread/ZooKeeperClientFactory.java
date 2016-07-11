package com.zookeeper.watcher.thread;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by Roy on 16/7/3.
 */
public class ZooKeeperClientFactory {

    private String ZK_SERVER = "127.0.0.1";
    private Integer TIME_OUT = 60000;

    public ZooKeeper createZKInstance(Watcher watcher) throws IOException {
        ZooKeeper zk = new ZooKeeper(ZK_SERVER, TIME_OUT, watcher);
        ZookeeperContext.ZK_CONTEXT.set(zk);
        return zk;
    }

    public static ZooKeeper getInstance(){
        if(ZookeeperContext.ZK_CONTEXT.get() != null)
            return ZookeeperContext.ZK_CONTEXT.get();
        throw new RuntimeException("ZK SERVER SHOULD BE CREATED FIRST.");
    }

}
