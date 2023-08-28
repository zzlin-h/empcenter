package com.zzl.empcenter.entity.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author Zhangzl
 * @Date 2023/8/19 23:12
 * @注释
 */
@Data
public class UserLoginRequest implements Serializable {
    //序列号
    private static final long serializableUTO = 3191241716373120793L;

    private String userAccount;

    private String userPassword;
}
