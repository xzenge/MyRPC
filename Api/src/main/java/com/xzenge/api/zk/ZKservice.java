package com.xzenge.api.zk;


import com.xzenge.api.utils.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ZKservice {
    private static final Logger logger = Logger.getLogger(ZKservice.class);

    /**
     * 获取zookeeper链接
     *
     * @return
     */
    public static ZooKeeper connectServer(final String timeOut, final String registryAddress, final CountDownLatch latch) {
        ZooKeeper zk = null;
        int zkSessionTimeout = "".equals(timeOut) ? 5000 : StringUtils.toInt(timeOut);

        try {
            new ZooKeeper(registryAddress, zkSessionTimeout, new Watcher() {
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
        } catch (InterruptedException iee) {
            logger.error("connectServer InterruptedException:" + iee);
        }
        return zk;
    }

    public static void watchNode(final ZooKeeper zk, final String zkRegistryPath, final List<String> dataList) {
        try {
            List<String> nodeList = zk.getChildren(zkRegistryPath, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    watchNode(zk, zkRegistryPath, dataList);
                }
            });

            for (String node : nodeList) {
                byte[] data = zk.getData(zkRegistryPath + "/" + node, false, null);
                dataList.add(new String(data));
            }
            logger.info("node data:" + dataList);
        } catch (KeeperException e) {
            logger.error("watchNode KeeperException error:" + e);
        } catch (InterruptedException e) {
            logger.error("watchNode InterruptedException error:" + e);
        }

    }

}