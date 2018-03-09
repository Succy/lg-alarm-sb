package cn.succy.alarm.util;

import cn.succy.alarm.enums.ResultEnum;
import cn.succy.alarm.vo.Result;

public class ResultUtil<T> {
    public static <T> Result success(ResultEnum resultEnum, T data) {
        return new Result();
    }
}
