package com.example.demo.dao;

import com.example.demo.domain.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleOrderDao extends JpaRepository<SaleOrder,String> {

}
