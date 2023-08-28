package com.zzl.empcenter.exception;

import com.zzl.empcenter.common.ErrorEnum;

/**
 * @version 1.0
 * @Author Zhangzl
 * @Date 2023/8/27 11:11
 * @注释
 */
public class BusinessException extends RuntimeException{

    private int code;
    private String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
        this.description = errorEnum.getDescription();
    }

    public BusinessException(ErrorEnum errorEnum, String description) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    // https://t.zsxq.com/0emozsIJh

    public String getDescription() {
        return description;
    }
}
