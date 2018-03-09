package cn.succy.alarm;

import cn.succy.alarm.dao.ProdLineDao;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.dao.ContactDao;
import cn.succy.alarm.entity.ProdLine;
import cn.succy.alarm.util.ContactsHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlarmApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(AlarmApplicationTests.class);

    @Autowired
    private ContactDao contactDao;
    @Autowired
    private ProdLineDao prodLineDao;

    @Test
    public void contextLoads() {
        Contact contact = new Contact();
        contact.setEmail("79556325@qq.com");
        contact.setName("蒋桂冬");
        contact.setWxId("jgd");

        Contact save = contactDao.save(contact);
        logger.info("{}", save);

        List<Contact> all = contactDao.findAll();
        logger.info("{}", all);
    }

    @Test
    public void del() {
        contactDao.delete(2);
    }

    @Test
    public void find() {
        List<Contact> all = contactDao.findAll();
        ProdLine prodLine = new ProdLine();
        prodLine.setCode("lg");
        prodLine.setDescription("炼钢产线质量报表系统");
        prodLine.setName("炼钢系统");
        prodLine.setContactList(all);

        ProdLine save = prodLineDao.save(prodLine);

        logger.info("{}", save);
    }

    @Test
    public void testContactHelper() {
        Map<String, List<Contact>> receiverMap = ContactsHelper.getReceiverMap();
        System.out.println(receiverMap);
    }
    @Test
    public void testFindByCode() {
        ProdLine lg = prodLineDao.findByCode("lg");
        System.out.println(lg);
    }

}
