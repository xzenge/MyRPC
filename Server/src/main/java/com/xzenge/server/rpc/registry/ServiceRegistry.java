package com.xzenge.server.rpc.registry;

import com.xzenge.api.utils.ConfigResource;
import com.xzenge.api.utils.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2016/11/1.
 */
public class ServiceRegistry {
    private static final Logger logger = Logger.getLogger(ServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void register(String data) {
        if(data != null){
            ZooKeeper zk = this.connectServer();

            if (zk != null) {
                createNode(zk, data);
            }
        }
    }

    /**
     * 获取zookeeper链接
     * @return
     */
    private ZooKeeper connectServer() {
        ZooKeeper zk = null;

        //ZooKeeper 链接获取超时时间
        String timeOut = ConfigResource.getSystemProperty("zookeeper.session.timeout");
        int zkSessionTimeout = "".equals(timeOut) ? 5000 : StringUtils.toInt(timeOut);

        try {
            new ZooKeeper(this.registryAddress,zkSessionTimeout,new Watcher(){
                public void process(WatchedEvent watchedEvent) {
                    //
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (IOException ioe) {
            logger.error("connectServer IOException:" + ioe);
        }catch (InterruptedException iee){
            logger.error("connectServer InterruptedException:" + iee);
        }
        return zk;
    }

    /**
     * 创建节点
     * @param zk
     * @param data
     */
    private void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();

            String registryPath = ConfigResource.getSystemProperty("zookeeper.registry.path");

            String path = zk.create(registryPath, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            logger.info("create node path:" + path + ",date:" + data);
        } catch (KeeperException e) {
            logger.error("createNode KeeperException: " + e);
        } catch (InterruptedException e) {
            logger.error("createNode InterruptedException: " + e);
        }
    }







}
