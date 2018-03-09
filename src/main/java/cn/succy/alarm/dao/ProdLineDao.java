package cn.succy.alarm.dao;

import cn.succy.alarm.entity.ProdLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdLineDao extends JpaRepository<ProdLine, Integer> {
    ProdLine findByCode(String code);
}
