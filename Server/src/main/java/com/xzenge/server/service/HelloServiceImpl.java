package com.xzenge.server.service;

import com.xzenge.api.client.HelloService;
import com.xzenge.server.rpc.internal.RpcService;

/**
 * Created by Administrator on 2016/11/1.
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {
    public String hello(String name) {
        return "hello! " + name;
    }
}
