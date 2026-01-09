package com.cware.netshopping.patmon.v2.repository;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cware.netshopping.patmon.v2.domain.PaTmonShipCost;

@ContextConfiguration("classpath:repository-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PaTmonShipCostMapperTest {
	
	@Autowired
	private PaTmonShipCostMapper shipCostMapper;
	

	@Test
	public void testTransTarget() throws ParseException {		
		PaTmonShipCost shipCost = new PaTmonShipCost();

		shipCost.setPaCode("91");
		shipCost.setEntpCode("109029");
		shipCost.setShipManSeq("010");
		shipCost.setReturnManSeq("012");
		shipCost.setProductType("DP04");
		shipCost.setShipCostCode("CN001");
		shipCost.setApplyDate(new Timestamp(new SimpleDateFormat("yyyyMMddHHmmss").parse("20210305143322").getTime()));
		shipCost.setNoShipIsland("0");
		
		shipCost = shipCostMapper.getTransTarget(shipCost);
		System.out.println(shipCost);
	}

	

}
