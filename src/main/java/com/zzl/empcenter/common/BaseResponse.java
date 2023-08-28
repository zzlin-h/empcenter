package com.zzl.empcenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author Zhangzl
 * @Date 2023/8/27 10:15
 * @注释
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 需要返回的数据
     */
    private T date;

    /**
     * 返回的消息
     */
    private String message;

    /**
     * 返回消息的描述
     */
    private String description;

    public BaseResponse(int code, T date, String message, String description) {
        this.code = code;
        this.date = date;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T date, String message) {
        this.code = code;
        this.date = date;
        this.message = message;
        this.description = "";
    }

    public BaseResponse(int code, T date) {
        this.code = code;
        this.date = date;
        this.message = "";
        this.description = "";
    }
}
