package cn.succy.alarm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.service.ContactService;
import cn.succy.alarm.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contact")
@Slf4j
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping("/all")
    public String findContactAll(Model model) {
        List<Contact> contactList = contactService.findAll();
        model.addAttribute(contactList);
        return "pages/contacts";
    }

    @GetMapping("/getAddView")
    public String getAddContactView() {
        return "pages/add_contact";
    }

    @GetMapping("/getEditView/{id}")
    public String getEditContactView(@PathVariable Integer id, Model model) {
        Contact contact = contactService.findById(id);
        model.addAttribute(contact);
        return "/pages/add_contact";
    }

    @PostMapping("/add")
    @ResponseBody
    public Result<Contact> addContact(@ModelAttribute Contact contact) {
        log.info("contact {}", contact);
        Result<Contact> result = new Result<>();
        if (contact == null) {
            result.setCode(-1);
            result.setMsg("联系人对象为空");
            return result;
        }
        Contact newContact = contactService.save(contact);
        result.setMsg("新增成功！");
        result.setCode(0);
        result.setData(newContact);
        return result;
    }

    @PutMapping("/update/{id}")
    @ResponseBody
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

    @DeleteMapping("/del/{id}")
    @ResponseBody
    public Result<Void> delete(@PathVariable Integer id) {
        Result<Void> result = new Result<>(0, "删除成功！", null);

        try {
            contactService.delById(id);
        }catch (Exception e) {
            result.setCode(-1);
            result.setMsg("删除失败，失败原因：" + e.getMessage());
            log.warn("delete contact failure", e);
        }
        return result;
    }
}
