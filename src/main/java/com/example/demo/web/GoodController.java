package com.example.demo.web;

import com.example.demo.common.ResponseVo;
import com.example.demo.domain.Good;
import com.example.demo.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/good")
public class GoodController {

    @Autowired
    private GoodService goodService;

    /**
     * 保存商品
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResponseVo save(){

        Good good = new Good();
        good.setGoodNumber(200);
        good.setCode("0001");
        good.setName("维斯大树的冷笑");
        good.setPrice(new BigDecimal(0.01));
        goodService.insert(good);

        return new ResponseVo().setState(200).setMessage("哈哈哈哈");
    }

    /**
     * 更新商品数量
     * @param id
     * @param count
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseVo update(@RequestParam("id")String id, @RequestParam("count")Integer count){

        goodService.update(id,count);

        return new ResponseVo().setState(200).setMessage("呵呵");
    }

    /**
     * 查询商品列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseVo list(){
        return new ResponseVo().setState(200).setData(goodService.select(null));
    }


    /**
     * 更新商品测试
     * @return
     */
    @RequestMapping("/goodUpdateTest")
    @ResponseBody
    public ResponseVo goodUpdateTest() {

        Good good = goodService.getModel("0");

        int result = goodService.updateNumber(200,"0",10);

        return new ResponseVo().setState(200).setMessage("王扎");
    }


}
