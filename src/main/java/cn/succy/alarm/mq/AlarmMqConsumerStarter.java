package cn.succy.alarm.mq;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.succy.alarm.exception.EmailRuntimeException;
import cn.succy.alarm.exception.WeChatRuntimeException;
import cn.succy.alarm.sender.Sender;
import cn.succy.alarm.sender.SenderFactory;
import cn.succy.alarm.util.SysConfHelper;
import cn.succy.alarm.util.TemplateModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 警报消费者启动器，启动一个线程，专门监听消息队列，当队列中有可消费的警报时，进行发送
 *
 * @author Succy
 * Created on 18-3-7.
 */
@Slf4j
public class AlarmMqConsumerStarter extends Thread {

    public AlarmMqConsumerStarter() {
        super("alarm-consumer-thread");
    }

    @Override
    public void run() {
        log.info("alarm mq consumer startup...");

        while (true) {
            try {
                TemplateModel model = AlarmMessageQueue.me().pull();
                if (model != null) {
                    List<String> senderKeys = StrUtil.split(SysConfHelper.getSysConf().getSender(), StrUtil.C_COMMA);
                    log.info("senderKeys => {}", senderKeys);
                    for (String key : senderKeys) {
                        Sender sender = SenderFactory.getSender(key);
                        sender.send(model);
                    }
                }
            } catch (Exception e) {
                // 该出捕获异常是因为：当邮件发送失败时会抛出异常，如果不捕获，这里将跳出循环
                // 队列中其他等待发送的消息就不会继续发送了。
                // 处理方式：1、假设是邮箱发送失败，则使用微信发送一条警报给接收人
                //          2、假设是微信发送失败，则使用邮箱发送一条警报给接收人
                // 由于出现此种情况场景应该不多，故同时发生异常的情况概率应该非常小
                TemplateModel model = new TemplateModel();
                model.setAlarmName("系统故障");
                model.setModelName("报警系统");
                model.setProdLineCode("sys");
                model.setDateTime(DateTime.now().toString());
                model.setLevel("严重");

                StringBuilder sb = StrUtil.builder();
                sb.append(e.toString());
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    sb.append(StrUtil.CRLF);
                    sb.append("\tat ").append(stackTraceElement);
                }

                if (e instanceof EmailRuntimeException) {
                    Sender sender = SenderFactory.getSender("wechat");
                    model.setContent("报警系统邮箱发送器故障,异常如下：\n" + sb.toString());
                    sender.send(model);
                }
                if (e instanceof WeChatRuntimeException) {
                    Sender sender = SenderFactory.getSender("email");
                    model.setContent("报警系统微信发送器故障,异常如下：\n" + sb.toString());
                    sender.send(model);
                }
                log.warn(e.getMessage(), e);
            }
        }
    }
}

