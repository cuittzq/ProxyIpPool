package com.myapp.crawer.impl;

import com.myapp.crawer.ProxyIpCrawer;
import com.myapp.entity.ProxyIp;
import com.myapp.util.CrawerBase;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * 功能描述
 *
 * @Author tzq24955
 * @Created on 2018/1/31
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
@Service("proxyIpForxicidailiCrawer")
public class ProxyIpForxicidailiCrawerImpl extends ProxyIpCrawer {

    public ProxyIpForxicidailiCrawerImpl() {
        super("http://www.xicidaili.com/wn/%d");
    }

    @Override
    public void fetchProxyIpOnePage() {
        int count = 10;
        for (int j = 1; j < 100; j++) {
            System.out.println("开始爬取" + String.format(this.website, j) + "。。。。。");
            Document doc = CrawerBase.get(String.format(this.website, j));
            Elements trs = doc.select("table").select("tr");
            if (trs.size() <= 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count--;
                if (count <= 0) {
                    return;
                }
                continue;
            }

            for (int i = 1; i < trs.size(); i++) {
                try {
                    Elements tds = trs.get(i).select("td");
                    if (tds.isEmpty()) {
                        continue;
                    }
                    String  ip      = tds.get(1).text();
                    int     port    = Integer.parseInt(tds.get(2).text());
                    String  desc    = tds.get(4).text();// 描述：透明，高匿
                    String  type    = tds.get(5).text(); // http | https
                    String  area    = tds.get(3).text();// 地区
                    ProxyIp proxyIp = new ProxyIp(ip, port, area, type);
                    ProxyIpCrawer.workProxyIps.add(proxyIp);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println(String.format(this.website, j) + "爬取完毕。。。。。");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
