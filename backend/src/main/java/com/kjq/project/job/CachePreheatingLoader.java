package com.kjq.project.job;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CachePreheatingLoader implements CommandLineRunner {

    @Resource
    private ProCacheJob proCacheJob;

    @Override
    public void run(String... args) throws Exception {
        //开启进行缓存预热
        proCacheJob.doCacheRecommendUser();
    }
}
