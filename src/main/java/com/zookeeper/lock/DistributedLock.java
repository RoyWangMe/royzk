package com.zookeeper.lock;

/**
 * Created by Roy on 16/7/10.
 *
 * Interface for distributed lock implementation.
 */
public interface DistributedLock {

    boolean tryLock();

}
