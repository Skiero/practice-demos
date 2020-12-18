package com.hikvision.fireprotection.hikalarm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wangjinchang5
 * @date 2020/12/18 16:00
 * @since 1.0.100
 */
@SpringBootApplication
@MapperScan("com/hikvision/fireprotection/hikalarm/module/mapper")
public class HikAlarmApplication {

    public static void main(String[] args) {
        SpringApplication.run(HikAlarmApplication.class, args);
    }

}

