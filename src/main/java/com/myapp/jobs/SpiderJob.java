package com.myapp.jobs;


import com.google.gson.Gson;
import com.myapp.crawer.ProxyIpCrawer;
import com.myapp.crawer.impl.ProxyIpCrawerImpl;
import com.myapp.crawer.impl.ProxyIpForkxdailiCrawerImpl;
import com.myapp.entity.ProxyIp;
import com.myapp.proxy.ProxyPool;
import com.myapp.redis.RedisStorage;
import com.myapp.util.ProxyIpCheck;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by gaorui on 16/12/26.
 */
public class SpiderJob implements Job {

    public         ProxyIpCrawer                  proxyIpCrawerone = new ProxyIpCrawerImpl();
    public         ProxyIpCrawer                  proxyIpCrawerTow = new ProxyIpForkxdailiCrawerImpl();
    private static int                            count            = 0;
    public static  ConcurrentSkipListSet<ProxyIp> allProxyIps      = new ConcurrentSkipListSet<ProxyIp>();// 解析页面获取的所有proxyip
    public static  ProxyPool                      proxyPool        = new ProxyPool();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        count++;
        System.out.println("#####第" + count + "次开始爬取#####");
        this.proxyIpCrawerone.fetchProxyIp();
        this.proxyIpCrawerTow.fetchProxyIp();
        allProxyIps.addAll(proxyIpCrawerone.workProxyIps);
        allProxyIps.addAll(proxyIpCrawerTow.workProxyIps);
        long index = 0;

        for (ProxyIp proxyip : allProxyIps) {
            try {
                String rediskey = "ipproxy";
                RedisStorage.getInstance().lpush(rediskey, Gson.class.newInstance().toJson(proxyip));
                Thread.sleep(100);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            index++;
        }
        ConcurrentSkipListSet<ProxyIp> allProxyIps = this.allProxyIps;
        for (ProxyIp Proxyip : allProxyIps) {
            System.out.println("proxyPool:" + Proxyip.getIp() + ":" + Proxyip.getPort());
        }

        System.out.println("#####爬取并更新redis完毕#####");
    }
}
