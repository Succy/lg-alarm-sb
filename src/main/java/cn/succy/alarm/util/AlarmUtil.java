package cn.succy.alarm.util;

import cn.hutool.core.util.StrUtil;

/**
 * Created by succy on 18-2-26.
 */
public final class AlarmUtil
{

    public static String getModelName(String appName) {
        String modelName = "";
        switch (appName) {
            case "lg":
                modelName = "炼钢系统";
                break;
            case "zb":
                modelName = "中板系统";
                break;
            case "rz":
                modelName = "热轧系统";
                break;
            case "bx":
                modelName = "棒线系统";
                break;
            case "lz":
                modelName = "冷轧系统";
                break;
        }

        return modelName;
    }

    public static String getHost(String host) {
        return StrUtil.isBlank(host) ? "未知主机" : host;
    }

    public static String getException(String ex) {
        return StrUtil.isBlank(ex) ? "暂未上报异常" : ex;
    }

}
