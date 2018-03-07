package cn.succy.alarm.util;

import cn.succy.alarm.dao.ProdLineDao;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.entity.ProdLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Succy
 * @date 2018-03-07 21:17
 **/
@Component
public class ContactsHelper {
    private static Map<String, List<Contact>> receiverMap = new HashMap<>(16);

    private static ProdLineDao prodLineDao;

    @Autowired
    public void setProdLineDao(ProdLineDao prodLineDao) {
        ContactsHelper.prodLineDao = prodLineDao;
    }

    public static Map<String, List<Contact>> getReceiverMap() {
        if (receiverMap == null || receiverMap.size() <= 0) {
            load();
        }
        return receiverMap;
    }

    private static void load() {
        receiverMap.clear();
        List<ProdLine> prodLines = prodLineDao.findAll();
        for (ProdLine prodLine : prodLines) {
            String key = prodLine.getCode();
            List<Contact> contactList = prodLine.getContactList();
            receiverMap.put(key, contactList);
        }
    }

    /**
     * 当对数据库进行修改时，调用该方法进行同步
     */
    public static void synchronize() {
        load();
    }
}
