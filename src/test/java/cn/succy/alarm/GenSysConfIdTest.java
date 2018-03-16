package cn.succy.alarm;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.succy.alarm.entity.SysConf;
import cn.succy.alarm.util.Constants;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class GenSysConfIdTest {
    @Test
    public void testGen() {
        String id = Constants.SYS_CONF_ID;
        System.out.println("id => " + id);
    }

    @Test
    public void testCopyProperties() {
        SysConf s1 = new SysConf();
        s1.setWechatCorpSecret("jsjgkjskgjskgjkslgjs");
        s1.setWechatCorpId("sjgkshseighkjs");

        SysConf s2 = new SysConf();
        s2.setWechatCorpId("");
        BeanUtil.copyProperties(s2, s1, CopyOptions.create().setIgnoreNullValue(true));
        System.out.println(s1);
        System.out.println(s2);
    }

    @Test
    public void testSendAlarm() {
        Map<String, Object> param = new HashMap<>();
        param.put("level", "一级");
        param.put("alarmName", "名字随便起");
        param.put("modelCode", "rz");
        param.put("content", "简单测试下，邮箱微信");
        String result = HttpUtil.post("http://127.0.0.1:8080/alarm/send", param);
        System.out.println(result);
    }


    @Test
    public void testContainsIgnoreCase() {
        String s = "email";

        boolean isContain = StrUtil.containsIgnoreCase(s, "wechat");
        System.out.println(isContain);
    }
}
