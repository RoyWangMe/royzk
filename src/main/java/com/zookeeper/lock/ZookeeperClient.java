package com.zookeeper.lock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Roy on 16/7/10.
 *
 * Zookeeper client to connect and operate with zookeeper server.
 */
public class ZookeeperClient implements Watcher {

    // the zookeeper instance
    private ZooKeeper zooKeeper;

    private String hostName;

    // hard code
    private int timeout = 60000;

    private CountDownLatch signal = new CountDownLatch(1);

    public ZookeeperClient(){}

    // the hostName put in application
    public ZookeeperClient(String hostName) {
        this.hostName = hostName;
    }

    public void connect() throws IOException {
        zooKeeper = new ZooKeeper(hostName, timeout, this);
    }

    public void process(WatchedEvent watchedEvent) {
        // ensure that the zookeeper finishing the connection with zk server
        if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
            signal.countDown();
        }
    }

    // close the connection
    public void close() throws InterruptedException {
        zooKeeper.close();
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }
}
