package com.zzl.empcenter.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzl.empcenter.common.BaseResponse;
import com.zzl.empcenter.common.ErrorEnum;
import com.zzl.empcenter.entity.domain.User;
import com.zzl.empcenter.exception.BusinessException;
import com.zzl.empcenter.mapper.UserMapper;
import com.zzl.empcenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Zhangzl
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-08-19 14:03:48
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "zzl";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCOde) {

        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "参数不能为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "账户名长度不能小于4位");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "密码长度不能小于8位");
        }
        if (planetCOde.length() > 5){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "星球编号不能超过5位");
        }
        //账户不能包含特殊字符
        String validPattern = "[`~!#$%^&*()+=|{}':;,\\\\.<>/?！@￥…（）—【】\"‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "账户名不能包含特殊字符");
        }
        //查看账户名是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "账户名重复");
        }
        //查看星球编号是否重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planet_code",planetCOde);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "星球编号重复");
        }
        //检查校验密码是否一致
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "密码与校验码不一致");
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult){
            throw new BusinessException(ErrorEnum.SYSTEM_ERROR);
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "账户名或密码错误");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "账户名或密码错误");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "账户名或密码错误");
        }

        //账户不能包含特殊字符
        String validPattern = "[`~!#$%^&*()+=|{}':;,\\\\.<>/?！@￥…（）—【】\"‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "账户名或密码错误");
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //查看账号与密码是否对应
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorEnum.PARAMS_ERROR, "账户名或密码错误");
        }

        //3.脱敏
        User newUser = getSafetyUser(user);
        //34.记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, newUser);

        return newUser;
    }

    /**
     * 用户注销
     *
     * @param request 登录信息
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public User getSafetyUser(User user){
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setGender(user.getGender());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setIsVail(user.getIsVail());

        return safetyUser;
    }
}




