package com.zzl.empcenter.service;

import com.zzl.empcenter.entity.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Zhangzl
* @description 针对表【user】的数据库操作Service
* @createDate 2023-08-19 14:03:48
*/
public interface UserService extends IService<User> {

    /**
     * 登录状态
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     *管理员
     */
    int ADMIN_USER = 1;

    /**
     * 普通管理员
     */
    int COMMON_USER = 0;

    /**
     * 用户注册
     * @param userAccount 用户名
     * @param userPassword 密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount  用户名
     * @param userPassword 密码
     * @param request 用户信息
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request 登录信息
     * @return 1表示成功
     */
    int userLogout(HttpServletRequest request);

    User getSafetyUser(User user);
}
