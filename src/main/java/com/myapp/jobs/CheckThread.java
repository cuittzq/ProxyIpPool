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

    private ProxyIp proxyIp;

    private ConcurrentSkipListSet<ProxyIp> allProxyIps;
    private CountDownLatch                 countDownLatch;

    public CheckThread(ProxyIp proxyIp, ConcurrentSkipListSet<ProxyIp> allProxyIps, CountDownLatch countDownLatch) {
        this.proxyIp = proxyIp;
        this.allProxyIps = allProxyIps;
        this.countDownLatch = countDownLatch;
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
            if (HttpStatus.SC_OK.getCode() == ProxyIpCheck.Check(new HttpProxy(proxyIp.getIp(), proxyIp.getPort()).getProxy()).getCode()) {
                this.allProxyIps.add(proxyIp);
            }
        } catch (Exception ex) {

        } finally {
            this.countDownLatch.countDown();
            System.out.println(this.countDownLatch.getCount());
        }
    }
}
