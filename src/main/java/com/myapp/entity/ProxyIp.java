package com.myapp.entity;

import lombok.Data;

/**
 * Created by gaorui on 16/12/26.
 */
@Data
public class ProxyIp {

    private String ip;
    private int port;
    private String desc;// 描述：透明，高匿
    private String area; // 地区
    private String type; // http | https
    private double responseTime;
    private boolean work;

    public ProxyIp(String ip, int port, String area, String type) {
        super();
        this.ip = ip;
        this.port = port;
        this.area = area;
        this.type = type;
        this.work = false; // 默认即为false；
    }
}
