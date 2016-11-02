package com.xzenge.client.rpc.discovery;


import com.xzenge.api.utils.ConfigResource;
import com.xzenge.api.zk.ZKservice;
import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ServiceDiscovery {
    private static final Logger logger = Logger.getLogger(ServiceDiscovery.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<>();

    private String registryAddress;

    public ServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;

        ZooKeeper zk = ZKservice.connectServer(ConfigResource.getSystemProperty("zookeeper.session.timeout")
                , this.registryAddress
                , latch);

        if (zk != null) {
            ZKservice.watchNode(zk
                    , ConfigResource.getSystemProperty("zookeeper.registry.path")
                    , dataList);
        }
    }

    public String discover() {
        String data = null;
        int size = dataList.size();
        if (size > 0) {
            if (size == 1) {
                data = dataList.get(0);
                logger.info("using only data:" + data);
            }else{
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
                logger.info("using random data:" + data);
            }
        }
        return data;
    }


}
