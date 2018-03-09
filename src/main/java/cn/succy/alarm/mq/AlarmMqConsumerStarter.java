package cn.succy.alarm.mq;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.succy.alarm.entity.SysConf;
import cn.succy.alarm.sender.Sender;
import cn.succy.alarm.sender.SenderFactory;
import cn.succy.alarm.util.SysConfHelper;
import cn.succy.alarm.util.TemplateModel;

import java.util.List;

/**
 * 警报消费者启动器，启动一个线程，专门监听消息队列，当队列中有可消费的警报时，进行发送
 * @author Succy
 * Created on 18-3-7.
 */
public class AlarmMqConsumerStarter extends Thread {
    private static final Log logger = LogFactory.get();
    private SysConf sysConf = SysConfHelper.getSysConf();

    public AlarmMqConsumerStarter() {
        super("alarm-consumer-thread");
    }

    @Override
    public void run() {
        logger.info("alarm mq consumer startup...");

        while (true) {
            try {
                TemplateModel model = AlarmMessageQueue.me().pull();
                if (model != null) {
                    List<String> senderKeys = StrUtil.split(sysConf.getSender(), StrUtil.C_COMMA);
                    for (String key : senderKeys) {
                        Sender sender = SenderFactory.getSender(key);
                        sender.send(model);
                    }
                }
            } catch (Exception e) {
                // 该出捕获异常是因为：当邮件发送失败时会抛出异常，如果不捕获，这里将跳出循环
                // 队列中其他等待发送的消息就不会继续发送了。
                logger.warn(e.getMessage(), e);
            }
        }
    }
}

