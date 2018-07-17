package com.example.demo;

import com.example.demo.domain.Good;
import com.example.demo.service.GoodService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private GoodService goodService;

	@Test
	public void goodUpdateTest() {

		Good good = goodService.getModel("0");

		good.setGoodNumber(200);

		goodService.update(good);
	}

}
