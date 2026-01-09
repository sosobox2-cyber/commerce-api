package com.cware.netshopping.pagmkt.v2.repository;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cware.netshopping.common.util.DateUtil;

@ContextConfiguration("classpath:repository-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PaGmktGoodsMapperTest {
	
	@Autowired
	private PaGmktGoodsMapper gmktGoodsMapper;
	
	@Test
	public void test() {
		gmktGoodsMapper.getGoodsList("22408830", "21");
	}

	@Test
	public void testTransTarget() {		
		gmktGoodsMapper.selectGoodsTransTaget("22408830", "21", false);
	}

	@Test
	public void testGoodsOffer() {		
		gmktGoodsMapper.selectGoodsOffer("22408830");
	}

	@Test
	public void testGoodsDescribe() {		
		gmktGoodsMapper.selectGoodsDescribe("22408830", "21");
	}

	@Test
	public void testDispatchPolicyExceptEntp() {		
		gmktGoodsMapper.selectDispatchPolicyExceptEntp("100933", "21", "03");
		System.out.println(DateUtil.toDateString("yyyy-MM-dd", new Date()));
	}

	@Test
	public void testDispatchPolicyExceptMgroup() {		
		gmktGoodsMapper.selectDispatchPolicyExceptMgroup("2501", "21", "03");
	}

	@Test
	public void testGoodsOption() {		
		gmktGoodsMapper.selectGoodsOption("22410082", "21");
	}

}
