package com.xzenge.api.model;

import com.xzenge.api.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Administrator on 2016/11/1.
 */
public class RpcEncoder extends MessageToByteEncoder{
    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if(genericClass.isInstance(o)){
            byte[] data = SerializationUtil.serialize(o);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
