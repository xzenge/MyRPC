package com.xzenge.utils;

import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2016/11/1.
 */
public class StringUtils {
    private static final Logger logger = Logger.getLogger(StringUtils.class);

    public static int toInt(String s){
        int a = 0;

        try {
            if (!"".equals(s)) {
                a = Integer.parseInt(s);
            }
        } catch (Exception e) {
            logger.error("toInt error:" + e);
        }

        return a;
    }
}
