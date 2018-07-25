package com.example.demo.service;

import com.example.demo.domain.SaleOrder;

import java.util.List;

public interface SaleOrderService {

    SaleOrder insert(SaleOrder saleOrder);

    SaleOrder update(SaleOrder saleOrder);

    SaleOrder getModel(String id);

    List<SaleOrder> select(SaleOrder saleOrder);

    /**
     * 保存订单及更新库存
     * @param saleOrder
     * @return
     */
    SaleOrder saveOrder(SaleOrder saleOrder);

    /**
     * 保存订单及更新库存
     * @param saleOrder
     * @return
     */
    SaleOrder saveModelAndUpdateGood(SaleOrder saleOrder);

    /**
     * 创建订单
     * @param saleOrder
     */
    void create(SaleOrder saleOrder);

    /**
     * 处理订单
     * @param saleOrder
     */
    void createHandle(SaleOrder saleOrder);

}
