package cn.succy.alarm.controller;

import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}
