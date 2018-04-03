package com.myapp.crawer.impl;


import com.myapp.crawer.ProxyIpCrawer;
import com.myapp.entity.ProxyIp;
import com.myapp.util.CrawerBase;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;


/**
 * Created by gaorui on 16/12/26.
 */
@Service("proxyIpForIP181Crawer")
public class ProxyIpForIP181CrawerImpl extends ProxyIpCrawer {

    public ProxyIpForIP181CrawerImpl() {
        super("http://www.ip181.com/");
    }

    @Override
    public void fetchProxyIpOnePage() {
        System.out.println("开始爬取" + this.website + "。。。。。");
        Document doc = CrawerBase.get(this.website);
        Elements trs = doc.select("table").select("tr");
        if (trs.size() <= 0) {
            return;
        }
        for (int i = 1; i <= 100; i++) {
            try {
                Elements tds     = trs.get(i).select("td");
                String   ip      = tds.get(0).text();
                int      port    = Integer.parseInt(tds.get(1).text());
                String   desc    = tds.get(2).text();// 描述：透明，高匿
                String   type    = tds.get(3).text(); // http | https
                String   area    = tds.get(5).text();// 地区
                ProxyIp  proxyIp = new ProxyIp(ip, port, area, type);
                workProxyIps.add(proxyIp);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println(this.website + "爬取完毕。。。。。");
    }
}
