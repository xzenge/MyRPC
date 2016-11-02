package com.xzenge.api.model;

import lombok.Data;

/**
 * Created by Administrator on 2016/11/1.
 */
@Data
public class RpcRequest {
    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
}
