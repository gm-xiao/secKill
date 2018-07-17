package com.example.demo.config.queue;

import com.alibaba.fastjson.JSON;
import com.example.demo.common.QueueTemplate;
import com.example.demo.domain.SaleOrder;
import com.example.demo.service.SaleOrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {
    private static final Logger LOGGER = Logger.getLogger(Receiver.class);

    private CountDownLatch latch;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    public Receiver(CountDownLatch latch) {
        this.latch = latch;
    }

    /**
     * 消息处理
     * @param message
     */
    public void objectMessage(String message) {
        try {
            QueueTemplate queueTemplate = JSON.parseObject(message,QueueTemplate.class);
            if ("saveOrder".equals(queueTemplate.getMethod())){
                SaleOrder order = JSON.parseObject(JSON.toJSONString(queueTemplate.getData()),SaleOrder.class);
                saleOrderService.createHandle(order);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        latch.countDown();
    }
}