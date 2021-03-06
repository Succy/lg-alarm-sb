package cn.succy.alarm;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONUtil;
import cn.succy.alarm.dao.ContactDao;
import cn.succy.alarm.dao.ProdLineDao;
import cn.succy.alarm.dao.SysConfDao;
import cn.succy.alarm.entity.Contact;
import cn.succy.alarm.entity.ProdLine;
import cn.succy.alarm.entity.SysConf;
import cn.succy.alarm.service.ContactService;
import cn.succy.alarm.util.Constants;
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

    @Autowired
    private SysConfDao sysConfDao;

    @Autowired
    private ContactService contactService;

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
        List<Contact> all = contactDao.findAll();
        lg.setContactList(all);

        prodLineDao.save(lg);
    }

    @Test
    public void testFillBean() {
        SysConf oldConf = sysConfDao.findOne(Constants.SYS_CONF_ID);
        System.out.println(oldConf);
        SysConf sysConf = new SysConf();
        sysConf.setEmailAddressor("报警系统");
        BeanUtil.copyProperties(sysConf, oldConf, CopyOptions.create().setIgnoreNullValue(true));
//        BeanUtils.copyProperties(sysConf, oldConf);
        System.out.println(oldConf);

    }

    @Test
    public void testGetOfficeList() {
        List<String> officeList = contactDao.findOfficeList();
        System.out.println(officeList);
    }

    @Test
    public void testGetMap4ZTree() {
        String contactMap4ZTree = contactService.getContactMap4ZTree(1);
        System.out.println(contactMap4ZTree);
    }
}
