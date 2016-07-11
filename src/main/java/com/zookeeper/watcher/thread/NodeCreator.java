package com.zookeeper.watcher.thread;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Roy on 16/7/3.
 */
public class NodeCreator {

    private ZooKeeper zooKeeper;

    private boolean isLeader;

    public NodeCreator(){}

    public NodeCreator(ZooKeeper zooKeeper){
        this.zooKeeper = zooKeeper;
    }

    public ZooKeeper getZooKeeper(){
        return zooKeeper;
    }

    public void createZKNodeLoop(String path, Object data){

        this.createMasterZKNode(path, data);

    }

    /**
     * 创建master节点
     *
     * @param path
     * @param data
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void createMasterZKNode(String path, Object data)  {
        byte[] dataByte = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(data);
            oos.flush();
            dataByte = bos.toByteArray();
        } catch (IOException e) {
           e.printStackTrace();
        }

        try {
            zooKeeper.create(path, dataByte, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException.NodeExistsException e) {
            isLeader = false;
            return;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if(checkZookeeperPath(path, data)) {
                return;
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean checkZookeeperPath(String path, Object source) throws KeeperException, InterruptedException {
        while (true) {
            Stat stat = new Stat();
            byte data[] =  zooKeeper.getData(path, false, stat);
            isLeader = new String(data).equals(source);
            return true;
        }
    }

}
