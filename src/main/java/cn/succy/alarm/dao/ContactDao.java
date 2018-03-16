package cn.succy.alarm.dao;

import cn.succy.alarm.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactDao extends JpaRepository<Contact, Integer> {
    @Query("select c.office from Contact c group by c.office")
    List<String> findOfficeList();
}
