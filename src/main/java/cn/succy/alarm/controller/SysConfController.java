package cn.succy.alarm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.succy.alarm.entity.SysConf;
import cn.succy.alarm.service.SysConfService;
import cn.succy.alarm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统配置控制器，主要配置系统的配置项
 * @author Succy
 * create on 218/03/08
 */
@Controller
@RequestMapping("sysconf")
public class SysConfController {

    @Autowired
    private SysConfService sysConfService;

    @GetMapping("/{key}")
    public String getConfView(@PathVariable("key") String key, Model model) {
        SysConf sysConf = sysConfService.findSysConf();
        model.addAttribute(sysConf);

        return String.format("pages/%s_conf", key);
    }

    @PutMapping("/update")
    @ResponseBody
    public Result<Void> updateWxConf(@ModelAttribute SysConf sysConf) {
        // 保险起见，避免前段传入的配置项是空值，先从数据库获取系统配置
        // 然后依次判断，如果参数有值则修改，没有则不修改。

        // 目前针对String类型的属性为""时同样会被注入做下兼容
        sysConf.blank2Null();
        SysConf oldConf = sysConfService.findSysConf();
        Result<Void> result = new Result<>();
        // 当且仅当参数sysConf中的字段不为null时，进行属性复制更新
        BeanUtil.copyProperties(sysConf, oldConf, CopyOptions.create().setIgnoreNullValue(true));
        sysConfService.updateSysConf(oldConf);
        result.setMsg("更新成功");
        result.setCode(0);
        return result;
    }


}
