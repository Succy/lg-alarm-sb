package cn.succy.alarm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.service.ContactService;
import cn.succy.alarm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping("/all")
    public String findContactAll(Model model) {
        List<Contact> contactList = contactService.findAll();
        model.addAttribute(contactList);
        return "pages/contacts";
    }

    @PostMapping("/add")
    public Result<Void> addContact(@ModelAttribute Contact contact) {
        Result<Void> result = new Result<>();
        if (contact == null) {
            result.setCode(-1);
            result.setMsg("联系人对象为空");
            return result;
        }

        return result;
    }

    @PutMapping("/update/{id}")
    public Result<Void> update(@ModelAttribute Contact contact, @PathVariable Integer id) {
        Result<Void> result = new Result<>();
        Contact oldContact = contactService.findById(id);
        if (oldContact == null) {
            result.setCode(-1);
            result.setMsg(String.format("要修改的联系人(id=%s)不存在", id));
            return result;
        }

        BeanUtil.copyProperties(contact, oldContact, CopyOptions.create().setIgnoreNullValue(true));
        contactService.update(oldContact);
        result.setCode(0);
        result.setMsg("修改成功！");
        return result;
    }
}
