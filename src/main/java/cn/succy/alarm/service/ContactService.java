package cn.succy.alarm.service;

import cn.succy.alarm.dao.ContactDao;
import cn.succy.alarm.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactDao contactDao;

    public List<Contact> findAll() {
        return  contactDao.findAll();
    }
}
