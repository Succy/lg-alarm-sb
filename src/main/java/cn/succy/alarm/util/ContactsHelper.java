package cn.succy.alarm.util;

import cn.succy.alarm.dao.ProdLineDao;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.entity.ProdLine;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
     *以下几种情况需要调用该方法：
     * 1、当联系人的信息更新后，特别是联系的方式更新，需要重新加载
     * 2、当产线配置联系人发生变动时需要重新加载
     * 3、当产线信息更新时，也需要重新加载
     * 4、联系人或者产线信息做删除时需要重新加载
     * 同步操作应放在service层。
     */
    public static void synchronize() {
        if (log.isDebugEnabled()) {
            log.debug("Before synchronize: {}", receiverMap);
        }

        log.info("contact synchronize......");
        load();

        if (log.isDebugEnabled()) {
            log.debug("After synchronize: {}", receiverMap);
        }
    }
}
