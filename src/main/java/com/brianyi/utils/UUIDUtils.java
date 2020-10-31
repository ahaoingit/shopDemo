package com.brianyi.utils;

import java.util.UUID;

public class UUIDUtils {
    /**
     * 方法,实现返回不重复的32长度字符串
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-","");
    }
}
