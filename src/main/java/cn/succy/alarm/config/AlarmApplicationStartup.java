package cn.succy.alarm.config;

import cn.succy.alarm.dao.AdminDao;
import cn.succy.alarm.dao.ContactDao;
import cn.succy.alarm.dao.ProdLineDao;
import cn.succy.alarm.dao.SysConfDao;
import cn.succy.alarm.entity.Admin;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.entity.ProdLine;
import cn.succy.alarm.entity.SysConf;
import cn.succy.alarm.mq.AlarmMqConsumerStarter;
import cn.succy.alarm.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 当程序启动时，做一些操作
 */
@Component
@Slf4j
public class AlarmApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("application context has been startup");

        ApplicationContext ctx = event.getApplicationContext();
        // 获取dao层
        SysConfDao sysConfDao = ctx.getBean(SysConfDao.class);
        AdminDao adminDao = ctx.getBean(AdminDao.class);
        // system_config目前只支持一条数据，保存整个系统的配置
        SysConf sysConf = sysConfDao.findOne(Constants.SYS_CONF_ID);
        if (sysConf == null) {
            // 初始化一些数据
            sysConf = new SysConf();
            log.info("system_config is empty");
            sysConf.setId(Constants.SYS_CONF_ID);
            sysConf.setMessageQueueSize(100);
            sysConf.setSender("wechat");
            sysConf.setEmailCharset("utf-8");
            sysConf.setSmtpPort(25);
            sysConf.setSmtpSSL(false);
            sysConf.setSmtpSSLPort(443);
            sysConf.setWechatAgentId(1000002);
            sysConf.setWechatCorpId("");
            sysConf.setWechatCorpSecret("");
            sysConfDao.save(sysConf);
        }

        Admin admin = adminDao.findOne(Constants.ADMIN_ID);
        if (admin == null) {
            admin = new Admin();
            admin.setId(Constants.ADMIN_ID);
            admin.setDesc("系统超级管理员，具备最高权限");
            admin.setName("SuperAdmin");
            admin.setPwd(Constants.ADMIN_DEFAULT_PWD);
            adminDao.save(admin);
        }

        // 启动消息队列消费者线程，监听消息队列
        new AlarmMqConsumerStarter().start();
    }
}
