package com.clesun.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 雪花分布式id获取
 *
 * @author liuk
 */
@Slf4j
public class SnowFlake {

    private static Snowflake snowflake;

    /**
     * 初始化配置
     *
     * @param workerId
     * @param datacenterId
     */
    public static void initialize(long workerId, long datacenterId) {
        snowflake = IdUtil.getSnowflake(workerId, datacenterId);
    }

    public static String getIdStr() {
        return snowflake.nextId() + "";
    }
}
