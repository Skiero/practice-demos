package com.hikvision.fireprotection.alarm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wangjinchang5
 * @date 2020/12/18 16:00
 * @since 1.0.100
 */
@SpringBootApplication
@MapperScan("com/hikvision/fireprotection/alarm/module/mapper")
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
public class HikAlarmApplication {

    public static void main(String[] args) {
        SpringApplication.run(HikAlarmApplication.class, args);
    }

}
