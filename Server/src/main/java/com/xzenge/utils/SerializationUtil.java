package com.xzenge.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.apache.log4j.Logger;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/11/1.
 */
public class SerializationUtil {
    private static final Logger logger = Logger.getLogger(SerializationUtil.class);

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    private SerializationUtil() {

    }

    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);

        if(schema == null){
            schema = RuntimeSchema.createFrom(cls);

            if(schema != null){
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>)obj.getClass();

        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

        try{
            Schema<T> schema = getSchema(cls);
            return ProtobufIOUtil.toByteArray(obj, schema, buffer);
        }catch (Exception e){
            logger.error("serialize error:" + e);
            throw new IllegalStateException(e.getMessage(), e);
        }finally {
            buffer.clear();
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            T message = (T) objenesis.newInstance(cls);

            Schema<T> schema = getSchema(cls);

            ProtostuffIOUtil.mergeFrom(data, message, schema);

            return message;

        } catch (Exception e) {
            logger.error("deserialize error:" + e);
            throw new IllegalStateException(e.getMessage(), e);
        }
    }





}
