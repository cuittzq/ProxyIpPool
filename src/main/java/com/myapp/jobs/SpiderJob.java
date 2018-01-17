package com.myapp.jobs;


import com.google.gson.Gson;
import com.myapp.crawer.ProxyIpCrawer;
import com.myapp.crawer.impl.ProxyIpCrawerImpl;
import com.myapp.entity.ProxyIp;
import com.myapp.proxy.ProxyPool;
import com.myapp.redis.RedisStorage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created by gaorui on 16/12/26.
 */
public class SpiderJob implements Job {

    public ProxyIpCrawer proxyIpCrawer = new ProxyIpCrawerImpl();
    private static int count = 0;

    public static ProxyPool proxyPool = new ProxyPool();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        count++;
        System.out.println("#####第" + count + "次开始爬取#####");
        this.proxyIpCrawer.fetchProxyIp();

        long index = 0;
        for (ProxyIp proxyip : this.proxyIpCrawer.allProxyIps) {
            try {
                String rediskey = "ipproxy";
                RedisStorage.getInstance().lpush(rediskey, Gson.class.newInstance().toJson(proxyip));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            index++;
        }
        List<ProxyIp> allProxyIps = this.proxyIpCrawer.allProxyIps;
        for (ProxyIp Proxyip : allProxyIps) {
            System.out.println("proxyPool:" + Proxyip.getIp() + ":" + Proxyip.getPort());
        }

        System.out.println("#####爬取并更新redis完毕#####");
    }
}
