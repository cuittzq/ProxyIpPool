package com.myapp.jobs;


import com.google.gson.Gson;
import com.myapp.crawer.ProxyIpCrawer;
import com.myapp.crawer.impl.ProxyIpCrawerImpl;
import com.myapp.crawer.impl.ProxyIpForData5UCrawerImpl;
import com.myapp.crawer.impl.ProxyIpForkxdailiCrawerImpl;
import com.myapp.crawer.impl.ProxyIpForxicidailiCrawerImpl;
import com.myapp.entity.ProxyIp;
import com.myapp.proxy.ProxyPool;
import com.myapp.redis.RedisStorage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gaorui on 16/12/26.
 */
public class SpiderJob implements Job {


    private static int count = 0;

    private final int                            CRAWERTHREAD = 4;
    private final int                            WORKTHREAD   = 100;
    public static ConcurrentSkipListSet<ProxyIp> allProxyIps  = new ConcurrentSkipListSet<ProxyIp>();// 解析页面获取的所有proxyip

    public static ProxyPool proxyPool = new ProxyPool();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("#####第" + count + "次开始爬取#####");
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        try {
            final CountDownLatch crawercountDownLatch = new CountDownLatch(CRAWERTHREAD);
            cachedThreadPool.execute(new ProxyIpCrawerImpl(crawercountDownLatch));
            cachedThreadPool.execute(new ProxyIpForkxdailiCrawerImpl(crawercountDownLatch));
            cachedThreadPool.execute(new ProxyIpForxicidailiCrawerImpl(crawercountDownLatch));
            cachedThreadPool.execute(new ProxyIpForData5UCrawerImpl(crawercountDownLatch));
            crawercountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final CountDownLatch           countDownLatch = new CountDownLatch(WORKTHREAD);
        ConcurrentSkipListSet<ProxyIp> checkProxyIps  = new ConcurrentSkipListSet<>();
        try {
            Long start = System.currentTimeMillis();
            for (int i = 0; i < WORKTHREAD; i++) {
                cachedThreadPool.execute(new CheckThread("线程" + i, checkProxyIps, ProxyIpCrawer.workProxyIps, countDownLatch));
            }
            countDownLatch.await();
            System.out.println(String.format("校验IP耗时%d", System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            cachedThreadPool.shutdown();
        }
        if (checkProxyIps.size() > 0) {
            allProxyIps = checkProxyIps;
        }


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
        }


        for (ProxyIp Proxyip : allProxyIps) {
            proxyPool.add(Proxyip.getIp(), Proxyip.getPort());
        }

        System.out.println("#####爬取并更新redis完毕,共" + allProxyIps.size() + "个IP#####");
        count++;
    }
}
