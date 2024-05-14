package com.kjq.project.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置，分布式锁
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private String host;

    private String port;

    private String password;

    private Integer database;

    @Bean
    public RedissonClient redissonClient(){
        //创建配置
        Config config = new Config();
        System.out.println(host+" "+port+" "+password);
        String redisAddress = String.format("redis://%s:%s", host, port);
        if (password == null || password.equals("")){
            config.useSingleServer()
                    .setAddress(redisAddress)
                    .setDatabase(database);
        }else{
            config.useSingleServer()
                    .setAddress(redisAddress)
                    .setPassword(password)
                    .setDatabase(database);
        }
        //创建实例
        return Redisson.create(config);
    }

}
