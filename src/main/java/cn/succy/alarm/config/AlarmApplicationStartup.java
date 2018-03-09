package cn.succy.alarm.config;

import cn.succy.alarm.dao.ContactDao;
import cn.succy.alarm.dao.ProdLineDao;
import cn.succy.alarm.dao.SysConfDao;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.entity.ProdLine;
import cn.succy.alarm.entity.SysConf;
import cn.succy.alarm.mq.AlarmMqConsumerStarter;
import cn.succy.alarm.util.Constants;
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
public class AlarmApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(AlarmApplicationStartup.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("application context has been startup");

        ApplicationContext ctx = event.getApplicationContext();
        // 获取dao层
        ProdLineDao prodLineDao = ctx.getBean(ProdLineDao.class);
        ContactDao contactDao = ctx.getBean(ContactDao.class);
        SysConfDao sysConfDao = ctx.getBean(SysConfDao.class);
        // system_config目前只支持一条数据，保存整个系统的配置
        SysConf sysConf = sysConfDao.findOne(Constants.SYS_CONF_ID);
        if (sysConf == null) {
            // 初始化一些数据
            sysConf = new SysConf();
            logger.info("system_config is empty");
            sysConf.setId(Constants.SYS_CONF_ID);
            sysConf.setMessageQueueSize(100);
            sysConf.setSender("wechat");
            sysConf.setEmailCharset("utf-8");
            sysConf.setSmtpPort(25);
            sysConf.setSmtpSSL(false);
            sysConf.setSmtpSSLPort(443);
            sysConf.setWechatAgentId(1000002);

            sysConfDao.save(sysConf);
        }

        List<Contact> contactList = contactDao.findAll();
        if (contactList == null || contactList.size() <= 0) {
            logger.info("contact is empty");
            contactList = new ArrayList<>();
            Contact contact1 = new Contact();


            Contact contact2 = new Contact();


            contactList.add(contact1);
            contactList.add(contact2);

            contactDao.save(contactList);
        }

        List<ProdLine> prodLineList = prodLineDao.findAll();
        if (prodLineList == null || prodLineList.size() <= 0) {
            logger.info("prod_line is empty");
            ProdLine prodLine = new ProdLine();
            prodLine.setContactList(contactList);
            prodLine.setName("炼钢系统");
            prodLine.setCode("lg");
            prodLine.setDescription("转炉炼钢产线质量数据系统");
            prodLineDao.save(prodLine);
        }

        // 启动消息队列消费者线程，监听消息队列
        new AlarmMqConsumerStarter().start();
    }
}
