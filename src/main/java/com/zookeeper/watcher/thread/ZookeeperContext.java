package com.zookeeper.watcher.thread;

import org.apache.zookeeper.ZooKeeper;

/**
 * Created by Roy on 16/7/3.
 */
public class ZookeeperContext {
    public static ThreadLocal<ZooKeeper> ZK_CONTEXT = new ThreadLocal<ZooKeeper>();
}
