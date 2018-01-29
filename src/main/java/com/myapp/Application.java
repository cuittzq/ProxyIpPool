package com.myapp;

import com.myapp.jobs.SpiderJob;
import com.myapp.timer.QuartzManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-06-19 9:47
 **/
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    final public static String job_name_1 = "task_Client";
    final public static String job_name_2 = "task2_Main";
    final public static String job_name_3 = "task3_Redis";

    public static void main(String[] args) {
        QuartzManager.addJob(job_name_1, SpiderJob.class, "0 0/5 * * * ?");
        SpringApplication.run(Application.class, args);
    }
}
