package com.zookeeper.lock.impl;

import com.zookeeper.lock.DistributedLock;

/**
 * Created by Roy on 16/7/10.
 */
public class ZookeeperDistributedLock implements DistributedLock{


    public boolean tryLock() {
        return false;
    }
}
