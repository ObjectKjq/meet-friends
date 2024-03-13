package com.kjq.project.service;
import java.util.Date;

import com.kjq.project.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
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
}