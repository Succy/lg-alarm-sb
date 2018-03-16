package cn.succy.alarm.service;

import cn.hutool.json.JSONUtil;
import cn.succy.alarm.dao.ContactDao;
import cn.succy.alarm.dao.ProdLineDao;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.entity.ProdLine;
import cn.succy.alarm.util.ContactsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContactService {
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private ProdLineDao prodLineDao;

    public List<Contact> findAll() {
        return contactDao.findAll();
    }

    public Contact findById(Integer id) {
        return contactDao.findOne(id);
    }

    public Contact save(Contact contact) {
        return contactDao.save(contact);
    }

    public void update(Contact contact) {
        contactDao.save(contact);
        ContactsHelper.synchronize();
    }

    public void delById(Integer id) {
        contactDao.delete(id);
        ContactsHelper.synchronize();
    }

    public String getContactMap4ZTree(Integer prodlineId) {
        List<Map<String, Object>> childrenList = new ArrayList<>();
        Map<String, Integer> pIdMap = new HashMap<>();
        List<String> officeList = contactDao.findOfficeList();
        for (int i = 0; i < officeList.size(); i++) {
            String office = officeList.get(i);
            pIdMap.put(office, i + 1);
            Map<String, Object> map = new HashMap<>();
            map.put("id", i + 1);
            map.put("name", office);
            map.put("pId", 0);
            map.put("isParent", true);
            childrenList.add(map);
        }

        // 查找产线id对应的产线
        ProdLine prodLine = prodLineDao.findOne(prodlineId);
        List<Contact> prodLineContactList = prodLine.getContactList();
        List<Contact> contactList = contactDao.findAll();
        // 叶子节点的id基数
        int idBasic = 10;
        for (Contact contact : contactList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", ++idBasic);
            map.put("contactId", contact.getId());
            map.put("name", contact.getName());
            Integer pId = pIdMap.get(contact.getOffice());
            map.put("pId", pId);
            if (prodLineContactList.contains(contact)) {
                map.put("checked", true);
                for (Map<String, Object> childrenMap : childrenList) {
                    if (pId.equals(childrenMap.get("id"))) {
                        childrenMap.put("checked", true);
                    }
                }
            }
            childrenList.add(map);
        }

        return JSONUtil.toJsonStr(childrenList);
    }
}
