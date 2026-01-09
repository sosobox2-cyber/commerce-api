package com.cware.netshopping.pa11st.v2.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cware.netshopping.pa11st.v2.repository.Pa11stCnShipCostMapper;

import zipit.rfnCustCommonAddrList;

@ContextConfiguration("classpath:repository-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Pa11stCnShipCostMapperTest {
	
	@Autowired
	private Pa11stCnShipCostMapper shipCostMapper;
	
	@Test
	public void testGetCnPaAddrSeq() {
		shipCostMapper.getCnPaAddrSeq("22410092", "11");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testVerifyPost() {

		rfnCustCommonAddrList rfn = new rfnCustCommonAddrList();
		rfn.log.disableLogs(true);
		try {
			rfn.setServerProp("172.17.20.91", "15555", "UTF-8");
			Map<String, Object> resultMap = rfn.getRfnAddrMap("59779", "전남 여수시 화양333면 안정상동길", "43-128, 해담인공장", "UTF-8", "N");
			
			System.out.println(resultMap);
			
			ArrayList<HashMap<String, Object>> result = (ArrayList<HashMap<String, Object>>) resultMap.get("DATA");
			System.out.println(result);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
