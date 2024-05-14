package com.kjq.project.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kjq.project.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class UserServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testAddUser(){
        User user = new User();
        user.setUsername("kjq");
        user.setUserAccount("123");
        user.setAvatarUrl("https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("12312312312");
        user.setEmail("12@qq.com");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        //断言用于测试
        Assertions.assertTrue(result);
    }

    @Test
    void userRegister() {
        long l = userService.userRegister("qwe", "qwe", "qwe");
        System.out.println(l);
    }

    @Test
    void systemText(){
        // 定义一个集合
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.stream().map(Integer::parseInt).collect(Collectors.toList());
        System.out.println(list);
    }

    @Test
    void searchUserByTags() {
        List<String> asList = new ArrayList<>();
        asList.add("javas");
        asList.add("pythons");
        List<User> users = userService.searchUserByTags(asList);
        log.info("返回结果", users);
        Assert.assertNotNull(users);
    }

    @Test
    void test(){
        RLock lock = redissonClient.getLock("kjq.precachejob:docache:lock");
        try {
            //获取锁，如果有锁返回false
            if(lock.tryLock(0, -1L, TimeUnit.MILLISECONDS)){
                Thread.sleep(30000000L);
                 System.out.println("========="+Thread.currentThread().getId());
                //从数据库中遍历所有用户，查询他们的列表执行，预热到缓存中
                if (!("List" == "dfs")) {

                }
            }
        } catch (InterruptedException e) {
            log.error("redis set key error", e);
        } finally {
            //判断是不是当前线程加的锁
            if(lock.isHeldByCurrentThread()){
                //System.out.println("========="+Thread.currentThread().getId());
                //释放锁
                lock.unlock();
            }
        }
    }


}