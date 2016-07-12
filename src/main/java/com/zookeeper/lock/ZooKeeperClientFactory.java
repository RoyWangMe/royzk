package com.zookeeper.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by Roy on 16/7/3.
 *
 * The factory to build completed zookeeper connection instance.
 */
@Component
public class ZooKeeperClientFactory {

    private final static Logger logger = LoggerFactory.getLogger(ZooKeeperClientFactory.class);

    private String hostName;



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

            // success build client
            zookeeperClientConstant.set(zkClient);
            return zkClient;
        }else{
            return zkClient;
        }
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
