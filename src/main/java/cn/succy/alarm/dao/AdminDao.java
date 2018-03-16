package cn.succy.alarm.dao;

import cn.succy.alarm.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao extends JpaRepository<Admin, String> {
    Admin findByNameAndPwd(String name, String pwd);
}
