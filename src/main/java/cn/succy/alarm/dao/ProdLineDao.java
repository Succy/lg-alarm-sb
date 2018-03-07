package cn.succy.alarm.dao;

import cn.succy.alarm.entity.ProdLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdLineDao extends JpaRepository<ProdLine, Integer> {
}
