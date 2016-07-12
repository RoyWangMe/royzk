package com.zookeeper.lock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hzwangyujie on 2016/7/12.
 */
public class Initializer {

    private final static Logger INIT_LOG = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    private ZooKeeperClientFactory zooKeeperClientFactory;

    public void init(){

        ZookeeperClient zkClient = zooKeeperClientFactory.getClient();
        ZooKeeper zk = zkClient.getZooKeeper();

        try {
            zk.create(ZkDistributedConstant.LOCK_MASTER_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            INIT_LOG.error("INIT LOCK PATH ERROR: path= {}", ZkDistributedConstant.LOCK_MASTER_PATH, e);
        }
    }

}
