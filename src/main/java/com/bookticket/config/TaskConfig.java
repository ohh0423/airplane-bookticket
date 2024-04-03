package com.bookticket.config;

import com.bookticket.service.TripsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;


/**
 * 定时任务
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@EnableAsync        // 3.开启多线程
public class TaskConfig implements CommandLineRunner {

    @Autowired
    private TripsService tripsService;

    public static final Logger logger= LoggerFactory.getLogger(TaskConfig.class);
    /**
     * 每天0点对超过起飞日期的班次进行逻辑删除
     * @return void
     */

    @Async
    @Scheduled(cron = "0 14 0 * * ?")
    public void tripsDeleteTask() {
        int count = tripsService.deleteEveryDay();
        if (count != 0) {
            logger.info("---------------成功删除" + count + "条超时班次---------------------");
        } else {
            logger.info("------------------------无定时任务需操作,时间:" + new Date() + "-------------------------");
        }
    }
    // 在项目启动时执行逻辑删除操作
    @Override
    public void run(String... args) throws Exception {

        int count = tripsService.deleteEveryDay();
        if (count != 0) {
            logger.info("---------------成功删除" + count + "条超时班次---------------------");
        } else {
            logger.info("------------------------无定时任务需操作,时间:" + new Date() + "-------------------------");
        }
    }

}
