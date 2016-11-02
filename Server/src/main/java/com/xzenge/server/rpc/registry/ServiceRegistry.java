package com.xzenge.server.rpc.registry;

import com.xzenge.api.utils.ConfigResource;
import com.xzenge.api.utils.StringUtils;
import com.xzenge.api.zk.ZKservice;
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

            ZooKeeper zk = ZKservice.connectServer(ConfigResource.getSystemProperty("zookeeper.session.timeout")
                    , this.registryAddress
                    , latch);

            if (zk != null) {
                ZKservice.createNode(zk, data);
            }
        }
    }









}
