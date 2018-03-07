package cn.succy.alarm.sender.impl;

import cn.hutool.core.util.StrUtil;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.entity.SysConf;
import cn.succy.alarm.sender.Sender;
import cn.succy.alarm.util.ContactsHelper;
import cn.succy.alarm.util.SysConfHelper;
import cn.succy.alarm.util.TemplateManager;
import cn.succy.alarm.util.TemplateModel;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 邮件发送器实现类
 *
 * @author Succy
 * @date 2017-10-13 11:36
 **/

public class EmailSenderImpl implements Sender {
    private static final Logger logger = LoggerFactory.getLogger(EmailSenderImpl.class);
    //private Setting emailSetting = SettingManager.getSetting(Constants.SETTING_GROUP_EMAIL);
    private SysConf sysConf = SysConfHelper.getSysConf();
    private Map<String, List<Contact>> recvMap = ContactsHelper.getReceiverMap();



    @Override
    public void send(TemplateModel model) {
        String appName = model.getAppName();
        if (!recvMap.containsKey(appName)) {
            logger.warn("could not found modelName:{} in recvMap", appName);
            return;
        }

        HtmlEmail email = new HtmlEmail();
        // 邮件服务器域名
        email.setHostName(sysConf.getSmtpHost());
        // 邮件服务器smtp协议端口
        email.setSmtpPort(sysConf.getSmtpPort());
        // 邮箱账户
        email.setAuthentication(sysConf.getEmailUser(), sysConf.getEmailPwd());
        // 邮件的字符集
        email.setCharset(sysConf.getEmailCharset());
        boolean useSSL = sysConf.getSmtpSSL();
        // 是否开启ssl
        email.setSSLOnConnect(useSSL);
        if (useSSL) {
            email.setSslSmtpPort(String.valueOf(sysConf.getSmtpSSLPort()));
        }
        Set<String> emailSet = recvMap.get(appName).stream().map(Contact::getEmail).collect(Collectors.toSet());

        try {
            email.setFrom(sysConf.getEmailUser(), sysConf.getEmailAddressor());
            for (String to : emailSet) {
                email.addTo(to);
            }
            String template = TemplateManager.getTemplateMsg(model);
            template = template.replaceAll(StrUtil.LF, "<br/>").replaceAll("\\t", "&nbsp;&nbsp;");
            email.setSubject(model.getAlarmName());
            email.setHtmlMsg(template);
            email.send();
            logger.debug("EmailSender has send a email");
        } catch (EmailException e) {
            logger.error("send email failure", e);
            throw new RuntimeException(e);
        }
    }
}
