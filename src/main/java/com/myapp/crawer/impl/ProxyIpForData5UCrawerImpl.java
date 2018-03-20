package com.myapp.crawer.impl;

import com.myapp.crawer.ProxyIpCrawer;
import com.myapp.entity.ProxyIp;
import com.myapp.util.CrawerBase;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 功能描述
 *
 * @Author tzq24955
 * @Created on 2018/2/2
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
public class ProxyIpForData5UCrawerImpl extends ProxyIpCrawer {

    private List<String> websites = new ArrayList<>();

    public ProxyIpForData5UCrawerImpl(CountDownLatch countDownLatch) {
        super("http://www.data5u.com/free/index.shtml");
        websites.add(this.website);
        websites.add("http://www.data5u.com/free/gngn/index.shtml");
        websites.add("http://www.data5u.com/free/gnpt/index.shtml");
        websites.add("http://www.data5u.com/free/gwgn/index.shtml");
        websites.add("http://www.data5u.com/free/gwpt/index.shtml");
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void fetchProxyIpOnePage() {
        websites.forEach(web->{
            System.out.println("开始爬取" + web+ "。。。。。");
            Document doc   = CrawerBase.get(web);
            Elements trs   = doc.select("li").addClass("text-align:center;").select("ul");
            if (trs.size() <= 0) {
                return;
            }
            for (int i = 1; i < trs.size(); i++) {
                try {
                    Elements tds = trs.get(i).select("li");
                    if (tds.isEmpty()) {
                        continue;
                    }
                    String  ip      = tds.get(0).text();
                    int     port    = Integer.parseInt(tds.get(1).text());
                    String  desc    = tds.get(2).select("a").text();// 描述：透明，高匿
                    String  type    = tds.get(3).select("a").text(); // http | https
                    String  area    = tds.get(4).select("a").text();// 地区
                    ProxyIp proxyIp = new ProxyIp(ip, port, area, type);
                    ProxyIpCrawer.workProxyIps.add(proxyIp);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println(web + "爬取完毕。。。。。");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
