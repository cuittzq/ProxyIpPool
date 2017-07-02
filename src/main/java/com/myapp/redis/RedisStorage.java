package com.myapp.redis;

import com.myapp.proxy.HttpProxy;
import redis.clients.jedis.Jedis;

/**
 * Created by gaorui on 17/1/7.
 */
public class RedisStorage {

    //连接redis服务器 127.0.0.1:6379
    private static Jedis jedis = new Jedis("192.168.10.10", 6379);

    //权限认证
    //jedis.auth("admin");
    private RedisStorage() {
//        jedis.auth("123456");
    }

    public static Jedis getInstance() {
//        jedis.auth("123456");
        return jedis;
    }
}
