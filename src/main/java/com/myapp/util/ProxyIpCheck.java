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

    public static ConcurrentSkipListSet<ProxyIp> batchCheck(ConcurrentSkipListSet<ProxyIp> iplist) {
        StringBuilder baseurl = new StringBuilder("http://www.xdaili.cn/ipagent//checkIp/ipList?");
        for (ProxyIp proxyIp : iplist) {
            baseurl.append(String.format("ip_ports[]=%s", (proxyIp.getIp() + ":" + proxyIp.getPort())));
            baseurl.append("&");
        }
        baseurl.deleteCharAt(baseurl.length() - 1);
        ConcurrentSkipListSet<ProxyIp> proxyIps = new ConcurrentSkipListSet<>();
        try {
            String   responces = Https.get(baseurl.toString()).request();
            Response response  = JSON.parseObject(responces, Response.class);
            if (response.getERRORCODE().equals("0")) {
                if (response.getRESULT() != null) {
                    ConcurrentSkipListSet<ProxyIp> finalProxyIps = proxyIps;
                    response.getRESULT().forEach(iPinfo -> {
                        ProxyIp proxyIp = new ProxyIp(iPinfo.getIp(), Integer.parseInt(iPinfo.getPort()), iPinfo.getPosition(), iPinfo.getAnony());
                        finalProxyIps.add(proxyIp);
                    });
                }
            }
        } catch (Exception ex) {
            return iplist;
        }

        if (proxyIps.size() == 0) {
            proxyIps = iplist;
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
