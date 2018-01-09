package com.myapp.util;


import com.myapp.util.http.Https;
import sun.applet.Main;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Map;

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
    public static Map<String, HttpStatus> batchCheck(List<String> iplist) {
        StringBuilder baseurl = new StringBuilder("http://www.xdaili.cn/ipagent//checkIp/ipList?");
        for (int i = 0; i < iplist.size(); i++) {
            baseurl.append(String.format("ip_ports[]=%s", iplist.get(i)));
            if (i < iplist.size() - 1) {
                baseurl.append("&");
            }
        }
        //{"ERRORCODE":"0","RESULT":[{"position":"中国 移动","port":"8080","time":"89ms","anony":"\"高匿\"","ip":"39.134.93.11"},{"position":"中国北京市北京市 电信","port":"3128","time":"3035ms","anony":"\"透明\"","ip":"116.196.119.138"},{"position":"中国安徽省芜湖市 电信","port":"8010","ip":"223.241.116.95"},{"position":"中国北京市北京市 阿里云","port":"3128","time":"236ms","anony":"\"透明\"","ip":"47.93.55.25"},{"position":"中国江苏省镇江市 电信","port":"9000","ip":"180.118.73.187"}]}
        String responces = Https.get(baseurl.toString()).request();
        return null;
    }


}
