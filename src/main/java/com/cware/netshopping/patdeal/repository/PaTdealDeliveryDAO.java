package com.cware.netshopping.patdeal.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;

@Repository("patdeal.delivery.paTdealDeliveryDAO")
public class PaTdealDeliveryDAO extends AbstractPaDAO {

	public int selectCountOrderList(ParamMap param) throws Exception {
		
		return (Integer) selectByPk("patdeal.delivery.selectCountOrderList", param.get());
	}

	public int insertPaTdealOrderList(ParamMap param) throws Exception {
		// TODO Auto-generated method stub
		return insert("patdeal.delivery.insertPaTdealOrderList", param.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderConfirmList(ParamMap paramMap) throws Exception {
		return list("patdeal.delivery.selectOrderConfirmList", paramMap.get());
	}
	
	public int updatePaorderm(Map<String, Object> order) throws Exception {
		return update("patdeal.delivery.updatePaorderm",order);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return list("patdeal.delivery.selectOrderInputTargetList", limitCount);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return list("patdeal.delivery.selectOrderInputTargetDtList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipProcList(ParamMap paramMap) throws Exception {
		return list("patdeal.delivery.selectSlipProcList", paramMap.get());
	}
	
	public int selectOrderAbleQty(Map<String,Object> map) throws Exception {
		
		Object result = selectByPk("patdeal.delivery.selectOrderAbleQty", map) ; 
		if (result == null) {
	        return 0;
	    }
		return (Integer) result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDeliveryComplete(ParamMap paramMap) throws Exception {
		return list("patdeal.delivery.selectDeliveryComplete", paramMap.get());
	}
	
	
	public int selectTdealAreaGbChk(String postNo) throws Exception {
		
		Object result = selectByPk("patdeal.delivery.selectTdealAreaGbChk", postNo) ; 
		if (result == null) {
	        return 0;
	    }
		return (Integer) result;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) {
		return (HashMap<String, Object>) selectByPk("patdeal.delivery.selectRefusalInfo", mappingSeq);
	}
	
}