package cn.succy.alarm.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.succy.alarm.entity.SysConf;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信token的工具类
 *
 * @author Succy
 * @date 2017-10-16 19:48
 **/
@Slf4j
public class TokenUtil {
    private static SysConf sysConf = SysConfHelper.getSysConf();
    private static String accessToken;
    private static long expiredate = -1L;


    private TokenUtil() {

    }

    /**
     * 初始化token
     */
    public static void initToken() {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.WeChat.CORP_ID, sysConf.getWechatCorpId());
        params.put(Constants.WeChat.CORP_SECRET, sysConf.getWechatCorpSecret());
        String result = HttpUtil.get(Constants.WeChat.URL_GET_TOKEN, params);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        accessToken = jsonObject.getStr(Constants.WeChat.ACCESS_TOKEN);
        if (log.isDebugEnabled()) {
            log.debug("access_token={}", accessToken);
        }
        expiredate = System.currentTimeMillis() + jsonObject.getInt(Constants.WeChat.EXPIRES_IN) * 1000;
    }

    private static Boolean isExpired() {
        return System.currentTimeMillis() > expiredate;
    }

    /**
     * 获取token
     * @return 获取到的token
     */
    public static String getAccessToken() {
        if (accessToken == null || isExpired()) {
            initToken();
        }
        return accessToken;
    }

}
