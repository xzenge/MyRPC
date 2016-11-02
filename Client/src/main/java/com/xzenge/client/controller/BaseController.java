package com.xzenge.client.controller;


import com.xzenge.api.client.HelloService;
import com.xzenge.client.rpc.proxy.RpcProxy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2016/11/2.
 */
@Controller
@RequestMapping(value = "/base")
public class BaseController {
    private static final Logger logger = Logger.getLogger(BaseController.class);

    @Autowired
    private RpcProxy rpcProxy;

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(){
        HelloService server = rpcProxy.create(HelloService.class);
        return server.hello("11111");
    }









}
