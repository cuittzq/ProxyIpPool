package com.myapp.timer;

import com.myapp.jobs.ScheduledJob;
import com.myapp.jobs.SpiderJob;
import org.quartz.Job;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能描述
 *
 * @Author tzq24955
 * @Created on 2018/4/3
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
@Component
public class ScheduledTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private              Integer          count0     = 1;
    private              Integer          count1     = 1;
    private              Integer          count2     = 1;

    @Resource
    private ScheduledJob spiderJob;
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() throws InterruptedException {
        spiderJob.execute();
        System.out.println(String.format("---第%s次执行，当前时间为：%s", count0++, dateFormat.format(new Date())));
    }

    @Scheduled(fixedDelay = 5000)
    public void reportCurrentTimeAfterSleep() throws InterruptedException {
        System.out.println(String.format("===第%s次执行，当前时间为：%s", count1++, dateFormat.format(new Date())));
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void reportCurrentTimeCron() throws InterruptedException {
        System.out.println(String.format("+++第%s次执行，当前时间为：%s", count2++, dateFormat.format(new Date())));
    }
}
