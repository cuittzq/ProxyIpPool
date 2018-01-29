package com.myapp.util;


import com.alibaba.fastjson.JSON;
import com.myapp.entity.ProxyIp;
import com.myapp.util.http.Https;
import sun.applet.Main;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by gaorui on 16/12/26.
 */
public class ProxyIpCheck {
    private final static int DEFAULT_REUSE_TIME_INTERVAL = 1500;// ms，从一次请求结束到再次可以请求的默认时间间隔
    private static final int HTTP_CONNECT_TIMEOUT        = 1000 * 15;
    private static final int HTTP_ReadTimeout            = 1000 * 5;

    public static HttpStatus Check(Proxy proxy) {


        URL url = null;

        try {

            //测试url
            url = new URL("http://www.baidu.com/");
            HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
            uc.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
            uc.setReadTimeout(HTTP_ReadTimeout);
            uc.connect();
            int code = uc.getResponseCode();
//          System.err.println(proxy+"resCode:"+code);

            if (code == 200)
                return HttpStatus.SC_OK;
            else if (code == 403)
                return HttpStatus.SC_FORBIDDEN;
            else
                return HttpStatus.SC_BAD_REQUEST;

        } catch (MalformedURLException e) {
//            e.printStackTrace();
            return HttpStatus.SC_BAD_REQUEST;
        } catch (IOException e) {
//            e.printStackTrace();
            return HttpStatus.SC_REQUEST_TIMEOUT;

        }

    }

    // http://www.xdaili.cn/ipagent//checkIp/ipList?ip_ports%5B%5D=39.134.93.11%3A8080&ip_ports%5B%5D=116.196.119.138%3A3128&ip_ports%5B%5D=223.241.116.95%3A8010&ip_ports%5B%5D=47.93.55.25%3A3128&ip_ports%5B%5D=180.118.73.187%3A9000
    // http://www.xdaili.cn/ipagent//checkIp/ipList?ip_ports[]=39.134.93.11:8080&ip_ports[]=116.196.119.138:3128&ip_ports[]=223.241.116.95:8010&ip_ports[]=47.93.55.25:3128&ip_ports[]=180.118.73.187:9000
    public static ConcurrentSkipListSet<ProxyIp> batchCheck(ConcurrentSkipListSet<ProxyIp> iplist) {
        StringBuilder baseurl = new StringBuilder("http://www.xdaili.cn/ipagent//checkIp/ipList?");
        for (ProxyIp proxyIp : iplist) {
            baseurl.append(String.format("ip_ports[]=%s", (proxyIp.getIp() + ":" + proxyIp.getPort())));
            baseurl.append("&");
        }
        baseurl.deleteCharAt(baseurl.length() - 1);
        String        responces = Https.get(baseurl.toString()).request();
        ConcurrentSkipListSet<ProxyIp> proxyIps  = new ConcurrentSkipListSet<>();
        Response      response  = JSON.parseObject(responces, Response.class);
        if (response.getERRORCODE().equals("0")) {
            if (response.getRESULT() != null) {
                response.getRESULT().forEach(iPinfo -> {
                    ProxyIp proxyIp = new ProxyIp(iPinfo.getIp(), Integer.parseInt(iPinfo.getPort()), iPinfo.getPosition(), iPinfo.getAnony());
                    proxyIps.add(proxyIp);
                });
            }
        }
        return proxyIps;
    }

    public static class Response {

        private String ERRORCODE;

        private List<IPinfo> RESULT;

        public String getERRORCODE() {
            return ERRORCODE;
        }

        public void setERRORCODE(String ERRORCODE) {
            this.ERRORCODE = ERRORCODE;
        }

        public List<IPinfo> getRESULT() {
            return RESULT;
        }

        public void setRESULT(List<IPinfo> RESULT) {
            this.RESULT = RESULT;
        }
    }

    public static class IPinfo {
        private String position;
        private String port;
        private String time;
        private String anony;
        private String ip;

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAnony() {
            return anony;
        }

        public void setAnony(String anony) {
            this.anony = anony;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }

}
