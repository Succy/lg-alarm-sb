package cn.succy.alarm.sender.impl.wechat;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.entity.SysConf;
import cn.succy.alarm.exception.WeChatRuntimeException;
import cn.succy.alarm.sender.Sender;
import cn.succy.alarm.util.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 微信发送器实现类
 *
 * @author Succy
 * @date 2017-10-13 11:37
 **/
@Slf4j
public class WeChatSenderImpl implements Sender {
    @Override
    public void send(TemplateModel model) {
        Map<String, List<Contact>> recvMap = ContactsHelper.getReceiverMap();
        SysConf sysConf = SysConfHelper.getSysConf();
        String prodLineCode = model.getProdLineCode();
        if (!recvMap.containsKey(prodLineCode)) {
            return;
        }

        List<Contact> contacts = recvMap.get(prodLineCode);
        Set<String> weChatSet = contacts.stream().map(Contact::getWxId).collect(Collectors.toSet());

        String touser = CollUtil.join(weChatSet, "|");

        TextMessage message = new TextMessage();
        message.setAgentid(sysConf.getWechatAgentId());
        message.setTouser(touser);
        // 设置整个部门发送
        String partyId = "";
        if (StrUtil.isNotBlank(partyId)) {
            message.setToparty(partyId);
        }
        TextMessage.Text text = new TextMessage.Text();
        text.setContent(TemplateManager.getTemplateMsg(model));
        message.setText(text);

        JSON msgJson = JSONUtil.parse(message);
        String response = HttpRequest.post(Constants.WeChat.URL_SEND_MSG + "?access_token=" + TokenUtil.getAccessToken())
                .body(msgJson)
                .execute()
                .body();
        JSONObject respJson = JSONUtil.parseObj(response);
        // 如果不成功，则重试
        int errcode = respJson.getInt(Constants.WeChat.ERR_CODE);
        if (errcode != Constants.WeChat.SUCCESS_CODE) {
            // 如果是token失效，则刷新token
            if (errcode == Constants.WeChat.INVALID_TOKEN_CODE) {
                TokenUtil.initToken();
            }
            // 如果再一次重试不成功，直接抛出运行时异常
            retry(msgJson);
        } else {
            log.info("send wechat message success; resp={}", respJson);
        }

    }

    /**
     * 重试机制，如果遇到token失效，则刷新token，重试一次。或者其他异常，也重试一次
     * 如果重试一次之后，还是不成功，直接抛异常了。
     *
     * @param request 请求体json
     */
    private void retry(JSON request) {
        String response = HttpRequest.post(Constants.WeChat.URL_SEND_MSG)
                .body(request)
                .execute()
                .body();
        JSONObject respJson = JSONUtil.parseObj(response);

        if (respJson.getInt(Constants.WeChat.ERR_CODE) == Constants.WeChat.SUCCESS_CODE) {
            log.info("send wechat message success; resp=>{}", respJson);
        } else {
            log.error("send wechat message failure; resp={}", respJson);
            throw new WeChatRuntimeException("send wechat message failure");
        }
    }
}
