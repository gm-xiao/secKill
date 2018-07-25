package com.example.demo.service;

import com.example.demo.domain.Good;

import java.util.List;

public interface GoodService {

    Good insert(Good good);

    Good update(Good good);

    int updateNumber(Integer number, String id);

    Good getModel(String id);

    List<Good> select(Good good);

    /**
     * 锁定一个商品
     * @param id
     * @return
     */
    Good lock(String id);

    /**
     * 更新商品数量
     * @param id
     * @param count
     */
    void update(String id, Integer count);

}
