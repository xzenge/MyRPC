package com.xzenge.client.rpc.proxy;

import com.xzenge.client.rpc.discovery.ServiceDiscovery;

/**
 * Created by Administrator on 2016/11/2.
 */
public class RpcProxy {
    private String serverAddress;
    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public RpcProxy(String serverAddress, ServiceDiscovery serviceDiscovery) {
        this.serverAddress = serverAddress;
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(Class<?> interfaceClass) {





    }




}
