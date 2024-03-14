package com.kjq.project.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.kjq.project.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class UserServiceTest {

    @Resource
    private UserService userService;

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
}