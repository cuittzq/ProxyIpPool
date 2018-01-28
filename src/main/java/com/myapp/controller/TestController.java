package com.myapp.controller;

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
        String result = "";
        try {
            result = RedisStorage.getInstance().rpop(rediskey);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }
}
