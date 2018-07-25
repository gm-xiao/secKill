package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.common.GUIDHelper;
import com.example.demo.common.QueueTemplate;
import com.example.demo.dao.SaleOrderDao;
import com.example.demo.domain.Good;
import com.example.demo.domain.SaleOrder;
import com.example.demo.service.GoodService;
import com.example.demo.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SaleOrderServiceImpl implements SaleOrderService {

    @Autowired
    private SaleOrderDao saleOrderDao;

    @Autowired
    private GoodService goodService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate template;

    @Override
    public SaleOrder insert(SaleOrder saleOrder) {
        saleOrder.setId(GUIDHelper.genRandomGUID());
        return saleOrderDao.save(saleOrder);
    }

    @Override
    public SaleOrder update(SaleOrder saleOrder) {
        return saleOrderDao.save(saleOrder);
    }

    @Override
    public SaleOrder getModel(String id) {
        return saleOrderDao.getOne(id);
    }

    @Override
    public List<SaleOrder> select(SaleOrder saleOrder) {
        return saleOrderDao.findAll();
    }

    /**
     * 普通保存订单
     * @param saleOrder
     * @return
     */
    @Transactional
    @Override
    public SaleOrder saveOrder(SaleOrder saleOrder){

        // 1.查询商品
        Good good =  goodService.getModel(saleOrder.getGoodId());
        if (null == good){
            throw new RuntimeException("Good id "+ saleOrder.getGoodId() +" not found");
        }

        // 2.校验库存
        if(good.getGoodNumber() < saleOrder.getGoodNumber() || good.getGoodNumber() <= 0){
            throw new RuntimeException("商品库存不足");
        }

        // 3.更新库存
        good.setGoodNumber(good.getGoodNumber() - saleOrder.getGoodNumber());
        goodService.update(good);

        // 4.保存订单
        saleOrder = this.insert(saleOrder);

        return saleOrder;
    }

    /**
     * 使用乐观锁保存订单
     * @param saleOrder
     * @return
     */
    @Transactional
    @Override
    public SaleOrder saveModelAndUpdateGood(SaleOrder saleOrder) {

        // 1.查询商品
        Good good =  goodService.getModel(saleOrder.getGoodId());
        if (null == good){
            throw new RuntimeException("Good id "+ saleOrder.getGoodId() +" not found");
        }

        // 2.校验库存
        if(good.getGoodNumber() < saleOrder.getGoodNumber() || good.getGoodNumber() <= 0){
            throw new RuntimeException("商品库存不足");
        }

        // 3.更新库存（乐观锁）
        Integer result = goodService.updateNumber(-saleOrder.getGoodNumber(),good.getId());

        // 4.保存订单
        if (result > 0){

            saleOrder = this.insert(saleOrder);

        }

        return saleOrder;
    }

    /**
     * 创建订单并发送队列
     * @param saleOrder
     */
    @Override
    public void create(SaleOrder saleOrder) {

        // 1.校验库存Redis
        ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
        Integer goodNumber = operations.get("goodNumber" + saleOrder.getGoodId());
        if ( saleOrder.getGoodNumber() > goodNumber || goodNumber == 0 ){
            throw new RuntimeException("库存不足");
        }

        // 2.订单发送到消息队列
        QueueTemplate queue = new QueueTemplate();
        queue.setMethod("saveOrder");
        queue.setData(saleOrder);
        template.convertAndSend("appQueue", JSON.toJSONString(queue));
    }

    /**
     * 使用悲观锁保存订单
     * @param saleOrder
     */
    @Transactional
    @Override
    public void createHandle(SaleOrder saleOrder) {

        // 1.锁定商品
        Good good =  goodService.lock(saleOrder.getGoodId());
        if (null == good){
            throw new RuntimeException("Good id "+ saleOrder.getGoodId() +" not found");
        }

        // 2.校验库存
        if(good.getGoodNumber() < saleOrder.getGoodNumber() || good.getGoodNumber() <= 0){
            throw new RuntimeException("商品库存不足");
        }

        // 3.更新Redis库存
        ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
        operations.increment("goodNumber" + saleOrder.getGoodId(), -saleOrder.getGoodNumber());


        // 4.更新商品库存
        good.setGoodNumber(good.getGoodNumber() - saleOrder.getGoodNumber());
        goodService.update(good);

        // 5.保存订单
        saleOrder = this.insert(saleOrder);

    }
}
