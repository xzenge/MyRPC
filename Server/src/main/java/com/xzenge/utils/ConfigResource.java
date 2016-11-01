package com.xzenge.utils;


import org.apache.log4j.Logger;

import java.util.ResourceBundle;

/**
 * Created by Administrator on 2016/11/1.
 */
public class ConfigResource {
    private static final Logger logger = Logger.getLogger(ConfigResource.class);

    public static final ResourceBundle config = ResourceBundle.getBundle("config");

    /**
     * 获取系统属性
     * @param key
     * @return
     */
    public static String getSystemProperty(String key) {
        try{
            return config.getString(key);
        }catch(Exception e){
            logger.error("getSystemProperty error:" + e);
        }
        return "";
    }
}
