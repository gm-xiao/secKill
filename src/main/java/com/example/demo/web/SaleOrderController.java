package com.example.demo.web;

import com.example.demo.common.ResponseVo;
import com.example.demo.domain.Good;
import com.example.demo.domain.SaleOrder;
import com.example.demo.service.GoodService;
import com.example.demo.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private GoodService goodService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 保存订单 普通提交
     * @return
     */
    @RequestMapping("/saveOrder")
    @ResponseBody
    public ResponseVo saveOrder(SaleOrder saleOrder){

        Good good = goodService.getModel(saleOrder.getGoodId());

        saleOrder.setGoodName(good.getName());
        saleOrder.setMoney(good.getPrice().multiply(new BigDecimal(saleOrder.getGoodNumber())));

        saleOrderService.saveOrder(saleOrder);

        return new ResponseVo().setState(200).setMessage("嘿嘿嘿嘿");
    }


    /**
     * 保存订单 乐观锁提交
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResponseVo save(SaleOrder saleOrder){

        Good good = goodService.getModel(saleOrder.getGoodId());

        saleOrder.setGoodName(good.getName());
        saleOrder.setMoney(good.getPrice().multiply(new BigDecimal(saleOrder.getGoodNumber())));

        saleOrderService.saveModelAndUpdateGood(saleOrder);

        return new ResponseVo().setState(200).setMessage("嘿嘿嘿嘿");
    }

    /**
     * 创建订单 悲观锁提交
     * @return
     */
    @RequestMapping("/create")
    @ResponseBody
    public ResponseVo create(SaleOrder saleOrder){

        // 1.初始Redis库存数据
       Good good = init(saleOrder.getGoodId());

        // 2.构建订单
        saleOrder.setGoodName(good.getName());
        saleOrder.setMoney(good.getPrice().multiply(new BigDecimal(saleOrder.getGoodNumber())));

        // 3.创建订单
        saleOrderService.create(saleOrder);

        return new ResponseVo().setState(200).setMessage("呃呃呃呃");
    }

    /**
     * 初始Redis库存数据
     * @param gid 商品ID
     */
    private Good init(String gid){
        Good good = goodService.getModel(gid);

        ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
        Integer goodNumber = operations.get("goodNumber" + gid);
        if( null == goodNumber ){
            operations.set("goodNumber" + gid, good.getGoodNumber());
        }

        return good;
    }

    /**
     * 更新缓存数量
     * @return
     */
    @RequestMapping("/initRedis")
    @ResponseBody
    public ResponseVo initRedis(String id){
        Good good = goodService.getModel(id);
        ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
        Integer goodNumber = operations.get("goodNumber" + id);
        operations.set("goodNumber" + id, good.getGoodNumber());
        return new ResponseVo().setData(200);
    }

}
