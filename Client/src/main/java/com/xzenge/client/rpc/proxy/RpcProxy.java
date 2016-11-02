package com.xzenge.client.rpc.proxy;

import com.xzenge.api.model.RpcRequest;
import com.xzenge.api.model.RpcResponse;
import com.xzenge.api.utils.StringUtils;
import com.xzenge.client.rpc.client.RpcClient;
import com.xzenge.client.rpc.discovery.ServiceDiscovery;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.UUID;

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

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(interfaceClass);
        enhancer.setCallback(new MethodInterceptor(){
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                RpcRequest request = new RpcRequest();
                request.setRequestId(UUID.randomUUID().toString());
                request.setClassName(method.getDeclaringClass().getName());
                request.setMethodName(method.getName());
                request.setParameterTypes(method.getParameterTypes());
                request.setParameters(objects);

                if(serviceDiscovery != null){
                    serverAddress = serviceDiscovery.discover();
                }

                String[] array = serverAddress.split(":");
                String host = array[0];
                int port = StringUtils.toInt(array[1]);

                RpcClient client = new RpcClient(host, port);

                RpcResponse response = client.send(request);

                if(response.isError()){
                    throw response.getError();
                }else{
                    return response;
                }
            }
        });

        return (T)enhancer.create();
    }




}
