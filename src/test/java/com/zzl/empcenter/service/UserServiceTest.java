package com.zzl.empcenter.service;

import com.zzl.empcenter.entity.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @version 1.0
 * @Author Zhangzl
 * @Date 2023/8/19 14:35
 * @注释
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void test(){
        User user = new User();
        user.setUserName("zzl");
        user.setGender(0);
        user.setUserAccount("123");
        user.setUserPassword("456");
        user.setAvatarUrl("qwert");
        user.setPhone("1234567");
        user.setEmail("erweg");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    public void registerTest(){
        String userAccount = "dazzle";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        String planetCode = "123";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertTrue(result > 0);

        userAccount = "zzlww";
        userPassword = "";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "zzl";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "zzlzz";
        userPassword = "1234567";
        checkPassword = "1234567";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "dazzle";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "zzl*";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "zzlz";
        userPassword = "12345679";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1,result);

    }

}