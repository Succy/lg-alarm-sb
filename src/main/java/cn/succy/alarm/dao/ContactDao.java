package cn.succy.alarm.dao;

import cn.succy.alarm.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactDao extends JpaRepository<Contact, Integer>{
}
