package com.myapp.controller;

import com.alibaba.fastjson.JSON;
import com.myapp.entity.ProxyIp;
import com.myapp.jobs.SpiderJob;
import com.myapp.redis.RedisStorage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/spider")
public class TestController {
    @RequestMapping(value = "/getIPProxy", method = RequestMethod.GET)
    public String getIPProxy() {
        String rediskey = "ipproxy";
        String result   = "";
        try {
            ProxyIp proxyIp = SpiderJob.allProxyIps.pollLast();
            if (proxyIp != null) {
                result = JSON.toJSONString(proxyIp);
            } else {
                result = RedisStorage.getInstance().rpop(rediskey);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }
}
