package cn.succy.alarm.entity;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "system_config")
@Entity
@Data
public class SysConf implements Serializable {
    private static final long serialVersionUID = 5383110636547258025L;
    @Id
    private String id;

    /**
     * 发送器，可以配置邮箱发送或者微信发送
     */
    @Column(nullable = false, columnDefinition = "varchar default 'wechat'")
    private String sender;

    @Column(columnDefinition = "int default 100", nullable = false)
    private Integer messageQueueSize;

    /**
     * 发件邮箱登录用户名
     */
    @Column(name = "email_user")
    private String emailUser;
    /**
     * 邮箱发件人，即指定一个名字代替邮箱号
     */
    private String emailAddressor;

    /**
     * 发件邮箱登录用户密码
     */
    private String emailPwd;

    private String emailCharset;

    private String smtpHost;

    @Column(name = "smtp_port", columnDefinition = "int default 25")
    private Integer smtpPort;

    @Column(name = "smtp_ssl", columnDefinition = "tinyint default 0")
    private Boolean smtpSSL;

    @Column(name = "smtp_ssl_port", columnDefinition = "int default 443")
    private Integer smtpSSLPort;

    private String wechatCorpId;

    private String wechatCorpSecret;

    @Column(name = "wechat_agent_id", columnDefinition = "int default 1000002")
    private Integer wechatAgentId;

    /**
     * 针对String类型的属性，当赋值为""时，调用该方法
     */
    public void blank2Null() {
        if (StrUtil.isBlank(sender)) {
            sender = null;
        }
        if (StrUtil.isBlank(emailUser)) {
            emailUser = null;
        }
        if (StrUtil.isBlank(emailAddressor)) {
            emailAddressor = null;
        }
        if (StrUtil.isBlank(emailCharset)) {
            emailCharset = null;
        }
        if (StrUtil.isBlank(emailPwd)) {
            emailPwd = null;
        }
        if (StrUtil.isBlank(smtpHost)) {
            smtpHost = null;
        }
        if (StrUtil.isBlank(wechatCorpId)) {
            wechatCorpId = null;
        }
        if (StrUtil.isBlank(wechatCorpSecret)) {
            wechatCorpSecret = null;
        }
    }
}
