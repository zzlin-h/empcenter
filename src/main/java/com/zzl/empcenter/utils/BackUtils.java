package com.zzl.empcenter.utils;

import com.zzl.empcenter.common.BaseResponse;

/**
 * @version 1.0
 * @Author Zhangzl
 * @Date 2023/8/27 10:02
 * @注释
 */
public class BackUtils {

    public static <T> BaseResponse<T> success(T date){
        return new BaseResponse<>(0, date, "操作成功");
    }

}
