package com.xzenge.model;

import lombok.Data;

/**
 * Created by Administrator on 2016/11/1.
 */
@Data
public class RpcResponse {
    private String requestId;
    private Throwable error;
    private Object result;
}