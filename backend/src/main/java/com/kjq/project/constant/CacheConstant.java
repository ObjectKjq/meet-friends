package com.kjq.project.constant;

/**
 * 关于缓存的常量类
 */
public interface CacheConstant {

    /**
     * 缓存心动用户的数量
     */
    String HEARTBEAT_USER = "100";

    /**
     * 定时任务的分布式锁常量id
     */
    String SCHEDULED_HEARTBEAT_USER_LOCK = "kjq.precachejob:docache:lock";

    /**
     * 根据标签缓存心动用户的常量，用于存储该用户在缓存中为标识id
     */
    String CACHE_HEARTBEAT_USER = "kjq.user.matchUsers:%s";

    /**
     * 缓存所有标签到redis的唯一标识
     */
    String CACHE_TAG = "kjq.user.listTags";

}
