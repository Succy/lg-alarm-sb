package cn.succy.alarm.service;

import cn.succy.alarm.dao.SysConfDao;
import cn.succy.alarm.entity.SysConf;
import cn.succy.alarm.util.Constants;
import cn.succy.alarm.util.SysConfHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysConfService {
    @Autowired
    private SysConfDao sysConfDao;

    public SysConf findSysConf() {
       return sysConfDao.findOne(Constants.SYS_CONF_ID);
    }

    @Transactional
    public void updateSysConf(SysConf sysConf) {
        sysConfDao.save(sysConf);
        SysConfHelper.synchronize();
    }
}
