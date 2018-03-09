package cn.succy.alarm.controller;

import cn.succy.alarm.service.AlarmService;
import cn.succy.alarm.util.ParamModel;
import cn.succy.alarm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AlarmController {
    @Autowired
    private AlarmService alarmService;

    /**
     * 请求http://localhost:8080/时进入的页面
     * @return 返回index.html页面
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }


    @PostMapping("/alarm/send")
    @ResponseBody
    public Result<Void> send(@ModelAttribute ParamModel model) {
        return alarmService.sendAlarm(model);
    }
}
