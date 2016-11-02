package com.xzenge.client.rpc.client;

import com.xzenge.api.model.RpcDecoder;
import com.xzenge.api.model.RpcEncoder;
import com.xzenge.api.model.RpcRequest;
import com.xzenge.api.model.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2016/11/2.
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger logger = Logger.getLogger(RpcClient.class);

    private String host;
    private int port;
    private RpcResponse response;
    private final Object lock = new Object();

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        this.response = rpcResponse;

        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("client caught exception", cause);
        ctx.close();
    }

    public RpcResponse send(RpcRequest request) throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline()
                            .addLast(new RpcEncoder(RpcRequest.class))
                            .addLast(new RpcDecoder(RpcResponse.class))
                            .addLast(RpcClient.this);
                }
            }).option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture futurn = bootstrap.connect(host, port).sync();
            futurn.channel().writeAndFlush(request).sync();

            synchronized (lock) {
                lock.wait();
            }

            if (response != null) {
                futurn.channel().closeFuture().sync();
            }

            return response;
        }finally{
            group.shutdownGracefully();
        }
    }
}
