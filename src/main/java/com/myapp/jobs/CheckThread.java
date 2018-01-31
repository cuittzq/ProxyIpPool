package com.myapp.jobs;

import com.myapp.entity.ProxyIp;
import com.myapp.proxy.HttpProxy;
import com.myapp.util.HttpStatus;
import com.myapp.util.ProxyIpCheck;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;

/**
 * 功能描述
 *
 * @Author tzq24955
 * @Created on 2018/1/29
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
public class CheckThread implements Runnable {

    private CountDownLatch countDownLatch;

    private ConcurrentSkipListSet<ProxyIp> allProxyIps;

    public ConcurrentSkipListSet<ProxyIp> workProxyIps;

    private String threadName;

    public CheckThread(String threadName, ConcurrentSkipListSet<ProxyIp> allProxyIps, ConcurrentSkipListSet<ProxyIp> workProxyIps, CountDownLatch countDownLatch) {
        this.allProxyIps = allProxyIps;
        this.countDownLatch = countDownLatch;
        this.workProxyIps = workProxyIps;
        this.threadName = threadName;
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
        while (!workProxyIps.isEmpty()) {
            try {
                System.out.println(threadName + "-------" + workProxyIps.size());
                ProxyIp proxyIp = workProxyIps.pollLast();
                if (proxyIp == null) {
                    break;
                }
                if (HttpStatus.SC_OK.getCode() == ProxyIpCheck.Check(new HttpProxy(proxyIp.getIp(), proxyIp.getPort()).getProxy()).getCode()) {
                    this.allProxyIps.add(proxyIp);
                }
            } catch (Exception ex) {

            }
        }

        this.countDownLatch.countDown();
        System.out.println(this.countDownLatch.getCount());
    }
}
