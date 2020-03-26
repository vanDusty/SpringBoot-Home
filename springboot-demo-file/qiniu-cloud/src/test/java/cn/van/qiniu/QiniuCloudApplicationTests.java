package cn.van.qiniu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

// @RunWith(SpringRunner.class)
// @SpringBootTest
public class QiniuCloudApplicationTests {

	@Test
	public void contextLoads() {
		String moneyAmount = "2.1,2";
		String[] moneys = moneyAmount.split(",");
		BigDecimal sum = new BigDecimal(0.0);
		for (String money : moneys) {
			BigDecimal rmb = new BigDecimal(money);
			sum = sum.add(rmb);
		}
		System.out.println(String.valueOf(sum));
	}

}
