package com.myapp.crawer.impl;

import com.myapp.crawer.ProxyIpCrawer;
import com.myapp.entity.ProxyIp;
import com.myapp.util.CrawerBase;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.concurrent.CountDownLatch;

public class ProxyIpForkxdailiCrawerImpl extends ProxyIpCrawer {

    private CountDownLatch countDownLatch;

    public ProxyIpForkxdailiCrawerImpl(CountDownLatch countDownLatch) {
        super("http://www.kxdaili.com/dailiip/2/%d.html");
        this.countDownLatch = countDownLatch;
    }

    public void fetchProxyIpOnePage() {
        for (int k = 0; k < 10; k++) {
            System.out.println("开始爬取" + String.format(this.website, k + 1) + "。。。。。");
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
                    ProxyIpCrawer.workProxyIps.add(proxyIp);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

            System.out.println(String.format(this.website, k + 1)+ "爬取完毕。。。。。");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
}
