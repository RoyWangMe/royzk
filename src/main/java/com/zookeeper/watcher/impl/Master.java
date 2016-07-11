package com.zookeeper.watcher.impl;

import com.zookeeper.watcher.Watcher;
import junit.framework.Assert;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by Roy on 16/7/2.
 *
 * The implementation for Watcher interface.
 */
public class Master implements Watcher {

    private ZooKeeper zk;
    private String hostPort;
    private Integer timeout;

    public Master(String hostPort, Integer timeout) {
        this.hostPort = hostPort;
        this.timeout = timeout;
    }

    private void initZookeeper() throws IOException {
        zk = new ZooKeeper(hostPort, timeout, this);
    }

    public void process(WatchedEvent event) {
        System.out.println(event);
    }

    public void stop() throws InterruptedException {
        zk.close();
    }

    /**
     * return zookeeper instance.
     *
     * @return
     */
    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public static void main(String[] args) throws Exception {

        Assert.assertEquals(args.length, 2);

        Master master = new Master(args[0], Integer.parseInt(args[1]));

        master.initZookeeper();

        Thread.sleep(60000);

        master.stop();


    }
}
