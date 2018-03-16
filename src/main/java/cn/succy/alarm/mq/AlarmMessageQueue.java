package cn.succy.alarm.mq;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.succy.alarm.entity.SysConf;
import cn.succy.alarm.util.SysConfHelper;
import cn.succy.alarm.util.TemplateModel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * 警报消息队列，实现自MessageQueue接口
 * 内部通过BlockingQueue实现
 */
@Slf4j
public class AlarmMessageQueue<T> implements MessageQueue<T> {
    private static final Log logger = LogFactory.get();
    private static AlarmMessageQueue<TemplateModel> alarmMessageQueue = null;

    private BlockingQueue<T> blockingQueue;

    public AlarmMessageQueue() {
        SysConf sysConf = SysConfHelper.getSysConf();
        Integer size = sysConf.getMessageQueueSize();
        int defaultSize = 100;
        if (size != null && size != 0) {
            defaultSize = size;
        }
        log.info("Max message queue size: {}", defaultSize);
        this.blockingQueue = new LinkedBlockingQueue<>(defaultSize);
    }

    public static AlarmMessageQueue<TemplateModel> me() {
        if (null == alarmMessageQueue) {
            alarmMessageQueue = new AlarmMessageQueue<>();
        }

        return alarmMessageQueue;
    }

    @Override
    public boolean push(T msg) {
        try {
            // 当可用空间为0时，阻塞等待有空间再插入
            this.blockingQueue.put(msg);
        } catch (InterruptedException e) {
            log.error("AlarmMessageQueue push element failure");
            return false;
        }
        return true;
    }

    @Override
    public T pull() {
        try {
            // 当队列中没有可读消息时，阻塞等待
            return this.blockingQueue.take();
        } catch (InterruptedException e) {
            log.error("AlarmMessageQueue pull element failure");
        }
        return null;
    }

    @Override
    public int size() {
        return this.blockingQueue.size();
    }
}

