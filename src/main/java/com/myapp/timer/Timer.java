package com.myapp.timer;

/**
 * Created by gaorui on 17/1/9.
 */


import com.myapp.jobs.SpiderJob;
import com.myapp.jobs.CheckJob;
import com.myapp.jobs.LoadMemoryJob;


public class Timer {
    final public static String job_name_1 = "task_Client";
    final public static String job_name_2 = "task2_Main";
    final public static String job_name_3 = "task3_Redis";

    public Timer() {
    }

    public static void main(String[] args) throws InterruptedException {
        QuartzManager.addJob(job_name_1, SpiderJob.class, "0 0 * * * ?");
        Thread.sleep(1000 * 10);
        QuartzManager.addJob(job_name_2, CheckJob.class, "*/1 * * * * ?");
        QuartzManager.addJob(job_name_3, LoadMemoryJob.class, "0 0 2 * * ?");


    }
}

