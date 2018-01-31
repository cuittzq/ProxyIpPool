package com.myapp.crawer.impl;


import com.myapp.crawer.ProxyIpCrawer;
import com.myapp.entity.ProxyIp;
import com.myapp.util.CrawerBase;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.concurrent.CountDownLatch;


/**
 * Created by gaorui on 16/12/26.
 */
public class ProxyIpCrawerImpl extends ProxyIpCrawer {

    private CountDownLatch countDownLatch;

    public ProxyIpCrawerImpl(CountDownLatch countDownLatch) {
        super("http://www.ip181.com/");
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
        } catch (Exception ex) {

        } finally {
            this.countDownLatch.countDown();
        }
    }
}
