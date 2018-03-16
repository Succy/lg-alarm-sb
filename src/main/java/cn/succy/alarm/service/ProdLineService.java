package cn.succy.alarm.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.succy.alarm.dao.ContactDao;
import cn.succy.alarm.dao.ProdLineDao;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.entity.ProdLine;
import cn.succy.alarm.util.ContactsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Service
public class ProdLineService {
    @Autowired
    private ProdLineDao prodLineDao;
    @Autowired
    private ContactDao contactDao;

    public List<ProdLine> findAll() {
        return prodLineDao.findAll();
    }

    public ProdLine findById(int id) {
        return prodLineDao.findOne(id);
    }

    public ProdLine save(ProdLine prodLine) {
        return prodLineDao.save(prodLine);
    }

    public void update(ProdLine prodLine) {
        prodLineDao.save(prodLine);
        ContactsHelper.synchronize();
    }

    public void delById(Integer id) {
        prodLineDao.delete(id);
        ContactsHelper.synchronize();
    }

    public void config(String[] ids, Integer prodlineId) {
        ProdLine prodLine = prodLineDao.findOne(prodlineId);
        List<Contact> contactList = null;
        if (ArrayUtil.isNotEmpty(ids)) {
            contactList = contactDao.findAll(CollUtil.newArrayList(Convert.toIntArray(ids)));
        }
        prodLine.setContactList(contactList);
        prodLineDao.save(prodLine);
        ContactsHelper.synchronize();
    }
}
