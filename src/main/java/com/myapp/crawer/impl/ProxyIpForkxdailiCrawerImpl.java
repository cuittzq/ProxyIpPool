package com.myapp.crawer.impl;

import com.myapp.crawer.ProxyIpCrawer;
import com.myapp.entity.ProxyIp;
import com.myapp.util.CrawerBase;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ProxyIpForkxdailiCrawerImpl extends ProxyIpCrawer {

    public ProxyIpForkxdailiCrawerImpl() {
        super("http://www.kxdaili.com/dailiip/2/%d.html");
    }

    @Override
    public void fetchProxyIp() {
        System.out.println("开始爬取" + this.website + "。。。。。");
        fetchProxyIpOnePage();
        System.out.println(this.website + "爬取完毕。。。。。");
    }

    public void fetchProxyIpOnePage() {
        for (int k = 0; k < 10; k++) {
            Document doc = CrawerBase.get(String.format(this.website, k + 1));
            Elements trs = doc.select("table").select("tr");
            if (trs.size() <= 0) {
                return;
            }

            for (int i = 1; i <= 10; i++) {
                try {
                    Elements tds     = trs.get(i).select("td");
                    String   ip      = tds.get(0).text();
                    int      port    = Integer.parseInt(tds.get(1).text());
                    String   desc    = tds.get(2).text();// 描述：透明，高匿
                    String   type    = tds.get(3).text(); // http | https
                    String   area    = tds.get(5).text();// 地区
                    ProxyIp  proxyIp = new ProxyIp(ip, port, area, type);
                    this.workProxyIps.add(proxyIp);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

    }
}
