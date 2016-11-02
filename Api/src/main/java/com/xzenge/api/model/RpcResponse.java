package com.xzenge.api.model;

import lombok.Data;

/**
 * Created by Administrator on 2016/11/1.
 */
@Data
public class RpcResponse {
    private String requestId;
    private Throwable error;
    private Object result;

    public boolean isError(){
        return error == null ? false : true;
    }
}
