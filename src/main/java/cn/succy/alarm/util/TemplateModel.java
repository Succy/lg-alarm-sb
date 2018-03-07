package cn.succy.alarm.util;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 模板模型
 *
 * @author Succy
 * @date 2017-10-12 16:46
 **/
@Data
public class TemplateModel implements Serializable {

    private static final long serialVersionUID = -6201793338937462437L;
    /**
     * 警报名称
     */
    private String alarmName;

    /**
     * 告警模块名称
     */
    private String appName;
    /**
     * 警报级别
     */
    private String level;
    /**
     * 告警的主机
     */
    private String host;
    /**
     * 报警时间
     */
    private String dateTime;
    /**
     * 报警的内容
     */
    private String content;


    public Map<String, Object> toMap() {
        return BeanUtil.beanToMap(this);
    }
}
