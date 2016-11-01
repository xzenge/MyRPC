package com.xzenge.rpc;

import com.xzenge.internal.RpcService;
import com.xzenge.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
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
        //获取所以带有RpcService的注解的Spring Bean
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);

        if (MapUtils.isNotEmpty(beans)) {
            for (Object serviceBean : beans.values()){
                String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                handlerMap.put(interfaceName, serviceBean);
            }
        }
    }

    public void afterPropertiesSet() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                // TODO: 2016/11/1  
            }
        });


    }
}
