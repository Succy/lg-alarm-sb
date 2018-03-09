package cn.succy.alarm.service;

import cn.succy.alarm.dao.ProdLineDao;
import cn.succy.alarm.entity.ProdLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Service
public class ProdLineService {
    @Autowired
    private ProdLineDao prodLineDao;

    public List<ProdLine> findAll() {
        return prodLineDao.findAll();
    }

    public ProdLine findById(int id) {
        return prodLineDao.findOne(id);
    }

    public ProdLine save(ProdLine prodLine) {
        return prodLineDao.save(prodLine);
    }

    public void delById(Integer id) {
        prodLineDao.delete(id);
    }
}
