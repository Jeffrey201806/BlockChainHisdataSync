package pers.jeffrey.blockchain.hisdatasync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zengjianfeng
 * @name
 * @desc
 * @jdk
 * @group
 * @os
 * @date 2018-06-08
 */
@SpringBootApplication
public class Application {
    public static void main ( String[] args )
    {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        HisDataSyncService bean = ctx.getBean(HisDataSyncService.class);
        bean.init();

    }
}
