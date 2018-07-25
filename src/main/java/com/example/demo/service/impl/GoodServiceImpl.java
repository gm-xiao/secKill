package com.example.demo.service.impl;

import com.example.demo.common.GUIDHelper;
import com.example.demo.dao.GoodDao;
import com.example.demo.domain.Good;
import com.example.demo.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private GoodDao goodDao;

    @Override
    public Good insert(Good good) {
        good.setId(GUIDHelper.genRandomGUID());
        return goodDao.save(good);
    }

    @Override
    public Good update(Good good) {
        return goodDao.save(good);
    }

    @Override
    public int updateNumber(Integer number, String id) {
        Good good = this.getModel(id);
        if(good.getGoodNumber() <=0){
            throw new RuntimeException("商品库存已到达底线");
        }

        Integer goodNumber = good.getGoodNumber() + number;

        Integer result = goodDao.updateGoodNumberById(goodNumber, id, good.getVersion());

        if ( result == 0){
            throw new RuntimeException("更新失败");
        }

        return result;

    }

    @Override
    public Good getModel(String id) {
        return goodDao.getOne(id);
    }

    @Override
    public List<Good> select(Good good) {
        return goodDao.findAll();
    }

    @Override
    public Good lock(String id) {
        return goodDao.findLockForUpdate(id);
    }

    @Transactional
    @Override
    public void update(String id, Integer count) {
        Good good = this.lock(id);
        if (null == good){
            throw new RuntimeException("Good id = "+ id +" not found");
        }

        good.setGoodNumber(good.getGoodNumber() + count);

        goodDao.save(good);

    }
}
