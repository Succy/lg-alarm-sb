package cn.succy.alarm.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.succy.alarm.dao.AdminDao;
import cn.succy.alarm.dao.ProdLineDao;
import cn.succy.alarm.entity.Admin;
import cn.succy.alarm.entity.ProdLine;
import cn.succy.alarm.mq.AlarmMessageQueue;
import cn.succy.alarm.util.ParamModel;
import cn.succy.alarm.util.TemplateModel;
import cn.succy.alarm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Succy
 * @date 2018-03-07 21:08
 **/
@Service
public class AlarmService {
    @Autowired
    private ProdLineDao prodLineDao;

    @Autowired
    private AdminDao adminDao;

    /**
     * 将ParamModel转成TemplateModel
     *
     * @param model ParamModel
     * @return 如果成功推到消息队列，返回true，反之返回false
     */
    public Result<Void> sendAlarm(ParamModel model) {
        Result<Void> result = new Result<>(-1, "警报发送失败，原因：系统内部错误，请稍后重试！", null);
        // 统一校验必选项非空
        // 检查modelCode
        String modelCode = model.getModelCode().toLowerCase();
        if (StrUtil.isBlank(modelCode)) {
            result.setCode(-2);
            result.setMsg("警报发送失败！原因：modelCode不能为空");
            return result;
        }

        // 检查level
        String level = model.getLevel();
        if (StrUtil.isBlank(level)) {
            result.setCode(-3);
            result.setMsg("警报发送失败！原因：level不能为空");
            return result;
        }

        // 检查alarmName，如果alarmName为空，赋予默认值
        String alarmName = model.getAlarmName();
        if (StrUtil.isBlank(alarmName)) {
            alarmName = "报警系统";
        }

        // 查询数据库，看是否有对应的modelName的产线代号
        ProdLine prodLine = prodLineDao.findByCode(modelCode);
        if (prodLine == null) {
            result.setCode(-4);
            result.setMsg(String.format("警报发送失败！原因：无效modelCode=%s,请填写正确的模块代号。", modelCode));
            return result;
        }

        // 检查产线是否配置有对应接收人
        if (prodLine.getContactList().size() <= 0) {
            result.setCode(-5);
            result.setMsg(String.format("警报发送失败！原因：告警模块:%s 对应接收联系人列表未配置,请联系管理人员进行配置。", modelCode));
            return result;
        }
        String modelName = prodLine.getName();
        TemplateModel templateModel = new TemplateModel();
        templateModel.setProdLineCode(modelCode);
        templateModel.setAlarmName(alarmName);
        templateModel.setModelName(modelName);
        templateModel.setContent(model.getContent());
        templateModel.setLevel(level);
        templateModel.setDateTime(DateTime.now().toString());

        boolean isSuccess = AlarmMessageQueue.me().push(templateModel);
        if (isSuccess) {
            result.setCode(0);
            result.setMsg("警报发送成功，请注意查收");
            return result;
        }

        return result;
    }

    public Admin login(String name, String pwd) {
        pwd = SecureUtil.md5(pwd);
        return adminDao.findByNameAndPwd(name, pwd);
    }

    public void updateAdmin(Admin admin) {
        adminDao.save(admin);
    }
}
