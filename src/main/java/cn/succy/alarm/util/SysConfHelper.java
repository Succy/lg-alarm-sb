package cn.succy.alarm.util;

import cn.succy.alarm.dao.SysConfDao;
import cn.succy.alarm.entity.SysConf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by succy on 18-3-7.
 */
@Component
@Slf4j
public class SysConfHelper {
    private volatile static SysConf sysConf = null;

    private static SysConfDao sysConfDao;
    @Autowired
    public void setSysConfDao(SysConfDao sysConfDao) {
        SysConfHelper.sysConfDao = sysConfDao;
    }

    public static SysConf getSysConf() {
        if (sysConf == null) {
            load();
        }
        return sysConf;
    }

    private static void load() {
        String id = Constants.SYS_CONF_ID;
        sysConf = sysConfDao.findOne(id);
    }

    /**
     * 当对数据库进行修改时，调用该方法进行同步
     */
    public static void synchronize() {
        load();
        log.info("sysConf => {}", sysConf);
    }
}
