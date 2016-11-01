package com.xzenge.rpc;

import com.xzenge.internal.RpcService;
import com.xzenge.registry.ServiceRegistry;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/1.
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {
    private static final Logger logger = Logger.getLogger(RpcServer.class);

    private String serverAdders;

    private ServiceRegistry serviceRegistry;
    //存放接口名与服务对象之间的映射关系
    private Map<String,Object> handlerMap = new HashMap<String,Object>();

    public RpcServer(String serverAdders) {
        this.serverAdders = serverAdders;
    }

    public RpcServer(String serverAdders, ServiceRegistry serviceRegistry) {
        this.serverAdders = serverAdders;
        this.serviceRegistry = serviceRegistry;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    public void afterPropertiesSet() throws Exception {

    }
}
