package com.myapp.crawer;

import com.myapp.entity.ProxyIp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;

/**
 * Created by gaorui on 16/12/26.
 */
public abstract class ProxyIpCrawer implements Runnable {

    public static ConcurrentSkipListSet<ProxyIp> workProxyIps = new ConcurrentSkipListSet<ProxyIp>();// 测试之后可用的 proxyip
    public    String         notIp; // 测试时过滤掉的本机ip
    public    String         website;
    protected CountDownLatch countDownLatch;

    public ProxyIpCrawer(String website) {
        super();
        this.website = website;
    }

    /**
     * 从数据库加载代理ip ,
     */
    public void loadDB() {
        /* 数据库 使用 Ip+port两个字段作为 主键 限制重复 */
    }

    /**
     * workProxyIps 存入数据库
     */
    public void persistWorkProxyIpsDB() {

    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            fetchProxyIpOnePage();
        } catch (Exception ex) {

        } finally {
            this.countDownLatch.countDown();
        }

    }

    public abstract void fetchProxyIpOnePage();
}
