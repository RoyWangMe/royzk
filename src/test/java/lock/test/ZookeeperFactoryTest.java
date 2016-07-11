package lock.test;

import com.zookeeper.lock.ZooKeeperClientFactory;
import com.zookeeper.lock.ZookeeperClient;
import org.junit.Test;

/**
 * Created by Roy on 16/7/10.
 */
public class ZookeeperFactoryTest{

    @Test
    public void testClientBuild(){
        String hostName = "127.0.0.1:2181";
        ZooKeeperClientFactory factory = new ZooKeeperClientFactory();
        factory.setHostName(hostName);
        ZookeeperClient zookeeperClient = factory.getClient();
        System.out.println(zookeeperClient.getZooKeeper());
    }
}
