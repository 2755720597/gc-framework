package com.allen.redisson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by xuguocai on 2021/3/15 9:29
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class RedissonApp {

    public static void main(String[] args) {
        SpringApplication.run(RedissonApp.class, args);
    }

}
