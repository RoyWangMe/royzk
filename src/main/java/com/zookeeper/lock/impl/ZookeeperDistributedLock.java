package com.zookeeper.lock.impl;

import com.zookeeper.lock.DistributedLock;
import com.zookeeper.lock.ZkDistributedConstant;
import com.zookeeper.lock.ZooKeeperClientFactory;
import com.zookeeper.watcher.Watcher;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Roy on 16/7/10.
 *
 * The distributed lock implementation with Zookeeper.
 */
public class ZookeeperDistributedLock implements DistributedLock{

    private static Logger LOCK_LOGGER = LoggerFactory.getLogger(ZookeeperDistributedLock.class);

    @Autowired
    private ZooKeeperClientFactory zooKeeperClientFactory;

    public boolean tryLock() {
        ZooKeeper zk = zooKeeperClientFactory.getClient().getZooKeeper();

        try {
            this.join(ZkDistributedConstant.LOCK_MASTER_PATH);
        } catch (Exception e) {
            LOCK_LOGGER.error("Lock path created failed.", e);
            return false;
        }


        return false;
    }

    /**
     * Create sub node in Zookeeper server.
     *
     * @param masterPath
     * @throws KeeperException
     * @throws InterruptedException
     */
    private Long join(String masterPath) throws KeeperException, InterruptedException {
        ZooKeeper zk = zooKeeperClientFactory.getClient().getZooKeeper();
        Long zkLockIndex = zk.getSessionId();
        String path = masterPath + "/lock-" + zkLockIndex;
        zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        return zkLockIndex;
    }

    /**
     * Check the created node before is the last node.
     *
     * @param masterPath
     * @param currentLockIndex
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    private boolean checkLastestNodeAndWatch(String masterPath, Long currentLockIndex) throws KeeperException, InterruptedException {
        ZooKeeper zk = zooKeeperClientFactory.getClient().getZooKeeper();

        // Get the node's name which under lock path
        List<String> zkLockNames = zk.getChildren(masterPath, false);
        Long nextNodeIndex = -1l;
        Long minSub = Long.MAX_VALUE;
        for(String lockName : zkLockNames){
            String[] arr = lockName.split("-");
            Long index = Long.parseLong(arr[1]);
            if(index > currentLockIndex){
                if(minSub > (index - currentLockIndex)){
                    minSub = index - currentLockIndex;
                    nextNodeIndex = index;
                }
            }
        }
        if(nextNodeIndex > -1l){
            String nextNodePath = masterPath + "/lock-" + nextNodeIndex;
            zk.exists(nextNodePath, new Watcher() {
                public void process(WatchedEvent watchedEvent) {

                }
            });
            return false;
        }else{
            return true;
        }
    }
}
