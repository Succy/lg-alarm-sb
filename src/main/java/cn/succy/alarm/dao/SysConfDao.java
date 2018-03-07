package cn.succy.alarm.dao;

import cn.succy.alarm.entity.SysConf;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by succy on 18-3-7.
 */
public interface SysConfDao extends JpaRepository<SysConf, String>{
}
