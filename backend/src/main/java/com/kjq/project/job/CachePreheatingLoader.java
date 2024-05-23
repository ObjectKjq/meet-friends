package com.kjq.project.job;

import com.kjq.project.manager.Finder;
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
        //这里的敏感词可以存储到数据库
        String[] sensitiveWords = {"杀人", "抢劫"};
        // 设置敏感词库
        Finder.addSensitiveWords(sensitiveWords);
    }
}
