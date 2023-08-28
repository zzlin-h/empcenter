package com.zzl.empcenter.entity.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author Zhangzl
 * @Date 2023/8/19 22:56
 * @注释
 */
@Data
public class UserRegisterRequest implements Serializable {
    //序列号
    private static final long serializableUTO = 3191241716373120793L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;

}
