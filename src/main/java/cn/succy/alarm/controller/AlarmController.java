package cn.succy.alarm.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.succy.alarm.entity.Admin;
import cn.succy.alarm.service.AlarmService;
import cn.succy.alarm.util.ParamModel;
import cn.succy.alarm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(String name, String pwd, HttpSession session, Model model) {
        if (StrUtil.isBlank(name) || StrUtil.isBlank(pwd)) {
            model.addAttribute("errmsg", "用户名或密码错误");
            return "login";
        }
        Admin admin = alarmService.login(name, pwd);
        if (admin == null) {
            model.addAttribute("errmsg", "用户名或密码错误");
            return "login";
        }
        session.setAttribute("user", admin);
        return "redirect:/";
    }

    @PutMapping("/update/pwd")
    @ResponseBody
    public Result<Void> updatePwd(String oldPwd, String newPwd) {
        Result<Void> result = new Result<>();
        Admin superAdmin = alarmService.login("SuperAdmin", oldPwd);
        if (superAdmin == null) {
            result.setCode(-1);
            result.setMsg("旧密码错误");
            return result;
        }
        superAdmin.setPwd(SecureUtil.md5(newPwd));
        alarmService.updateAdmin(superAdmin);
        result.setCode(0);
        result.setMsg("修改成功！");
        return result;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/login";
    }

    @PostMapping("/alarm/send")
    @ResponseBody
    public Result<Void> send(@ModelAttribute ParamModel model) {
        return alarmService.sendAlarm(model);
    }
}
