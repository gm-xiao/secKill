package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "sale_order")
public class SaleOrder implements Serializable {

    private static final long serialVersionUID = -1L;

    @Id
    @Column(name = "id",length = 32)
    private String id;

    @Column(name = "good_id",length = 32)
    private String goodId;

    @Column(name = "good_name",length = 60)
    private String goodName;

    @Column(name = "money",precision = 18, scale = 2)
    private BigDecimal money;

    @Column(name = "good_number", precision = 11)
    private Integer goodNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getGoodNumber() {
        return goodNumber;
    }

    public void setGoodNumber(Integer goodNumber) {
        this.goodNumber = goodNumber;
    }
}
