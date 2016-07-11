package com.zookeeper.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Roy on 16/7/3.
 *
 * The factory to build completed zookeeper connection instance.
 */
public class ZooKeeperClientFactory {

    private final static Logger logger = LoggerFactory.getLogger(ZooKeeperClientFactory.class);

    private String hostName;

    // connect retry count
    private int retryCount = 6;

    public ThreadLocal<ZookeeperClient> zookeeperClientConstant = new ThreadLocal<ZookeeperClient>();

    /**
     * Thread get zk client.
     * if connect to zk server fail will retry @{retryCount} times.
     *
     * @return
     */
    public ZookeeperClient getClient(){
        // get zk from cache
        ZookeeperClient zkClient = zookeeperClientConstant.get();
        if(zkClient == null){
            zkClient = new ZookeeperClient(hostName);
            try {
                zkClient.connect();
            } catch (Exception e) {
                logger.error("Zookeeper create error: connect to {} zk server with exception ={}", hostName, e);
                this.retryClient(zkClient);
            }
            // success build client
            zookeeperClientConstant.set(zkClient);
            return zkClient;
        }else{
            return zkClient;
        }
    }

    private void retryClient(ZookeeperClient zookeeperClient){
        int count = 0;
        while(count <= retryCount){
            try {
                zookeeperClient.connect();
                break;
            } catch (IOException e) {
                logger.warn("Zookeeper create warn: retry time zk server with exception ={}", count, e);
            }
        }
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
