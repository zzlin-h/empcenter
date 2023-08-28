package com.zzl.empcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzl.empcenter.common.BaseResponse;
import com.zzl.empcenter.common.ErrorEnum;
import com.zzl.empcenter.entity.domain.User;
import com.zzl.empcenter.entity.request.UserLoginRequest;
import com.zzl.empcenter.entity.request.UserRegisterRequest;
import com.zzl.empcenter.exception.BusinessException;
import com.zzl.empcenter.service.UserService;
import com.zzl.empcenter.utils.BackUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zzl.empcenter.service.UserService.COMMON_USER;
import static com.zzl.empcenter.service.UserService.USER_LOGIN_STATE;

/**
 * @version 1.0
 * @Author Zhangzl
 * @Date 2023/8/19 22:51
 * @注释
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        }
        long userRegister = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return BackUtils.success(userRegister);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorEnum.PARAMS_ERROR);
        }
        User userLogin = userService.userLogin(userAccount, userPassword, request);
        return BackUtils.success(userLogin);
    }

    /**
     * 获取当前用户
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorEnum.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return BackUtils.success(safetyUser);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if(request == null) {
            throw new BusinessException(ErrorEnum.PARAMS_ERROR);
        }
        int userLogout = userService.userLogout(request);
        return BackUtils.success(userLogout);
    }

    @GetMapping("/query")
    public BaseResponse<List<User>> queryUser(String userName, HttpServletRequest request){
        //仅管理员可查询，获取登录态信息
        User queryUser = (User)request.getSession().getAttribute(USER_LOGIN_STATE);
        if(queryUser == null || queryUser.getUserRole() == COMMON_USER){
            throw new BusinessException(ErrorEnum.NO_AUTH, "没有管理员权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNoneBlank(userName)){
            queryWrapper.like("user_name",userName);
        }
        List<User> listUser = userService.list(queryWrapper);
        List<User> userList = listUser.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return BackUtils.success(userList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request){

        //仅管理员可删除，获取登录态信息
        User queryUser = (User)request.getSession().getAttribute(USER_LOGIN_STATE);
        if(queryUser.getUserRole() == COMMON_USER){
            throw new BusinessException(ErrorEnum.NO_AUTH, "没有管理员权限");
        }
        boolean removeById = userService.removeById(id);
        return BackUtils.success(removeById);
    }
}
