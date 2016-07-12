package com.zookeeper.lock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Roy on 16/7/10.
 *
 * Zookeeper client to connect and operate with zookeeper server.
 */
public class ZookeeperClient implements Watcher {

    private final static Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

    // the zookeeper instance
    private ZooKeeper zooKeeper;

    private String hostName;

    // hard code
    private int timeout = 60000;

    // connect retry count
    private int retryCount = 6;

    private CountDownLatch signal = new CountDownLatch(1);

    public ZookeeperClient(){}

    // the hostName put in application
    public ZookeeperClient(String hostName) {
        this.hostName = hostName;
        this.connect();
    }

    public void connect() {
        try {
            zooKeeper = new ZooKeeper(hostName, timeout, this);
        } catch (Exception e) {
            logger.error("Zookeeper create error: connect to {} zk server with exception ={}", hostName, e);
            this.retryClient();
        }
    }

    private void retryClient(){
        int count = 0;
        while(count <= retryCount){
            try {
                zooKeeper = new ZooKeeper(hostName, timeout, this);
            } catch (IOException e) {
                logger.error("Zookeeper create error: connect to {} zk server with exception ={}", hostName, e);
            }
            break;
        }
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
