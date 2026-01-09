package com.cware.netshopping.pahalf.order.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaHalfOrderListVO;

@Service("pahalf.order.paHalfOrderDAO")
public class PaHalfOrderDAO extends AbstractPaDAO {

	
	
	public int selectCountOrderList(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		return (Integer) selectByPk("pahalf.order.selectCountOrderList", paHalfOrderListVO);
	}

	public int insertPaHalfOrderList(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		return insert("pahalf.order.insertPaHalfOrderList", paHalfOrderListVO);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderConfirmList(String paCode) throws Exception {
		return list("pahalf.order.selectOrderConfirmList", paCode);
	}

	public int updateTPaorderm(Map<String, Object> order) throws Exception {
		return update("pahalf.order.updateTPaorderm",order);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return list("pahalf.order.selectOrderInputTargetList", limitCount);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return list("pahalf.order.selectOrderInputTargetDtList", paramMap.get());
	}


	@SuppressWarnings("unchecked")
	public Map<String, Object> selectRefusalInfo(String mappingSeq) throws Exception{
		return (Map<String, Object>) selectByPk("pahalf.order.selectRefusalInfo", mappingSeq);
	}

	public int selectCountClaimList(PaHalfOrderListVO paHalfOrderListVO) throws Exception{
		return (Integer) selectByPk("pahalf.order.selectCountClaimList", paHalfOrderListVO);
	}

	public String selectDoflag(PaHalfOrderListVO orderVo) throws Exception {
		return (String) selectByPk("pahalf.order.selectDoflag", orderVo);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return list("pahalf.order.selectClaimTargetList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("pahalf.order.selectCancelInputTargetDtList", paramMap.get());
	}

	public int selectExistsExchange(PaHalfOrderListVO paHalfOrderListVO) {
		return (Integer) selectByPk("pahalf.order.selectExistsExchange", paHalfOrderListVO);
	}

	public int selectExistsReturn(PaHalfOrderListVO paHalfOrderListVO) throws Exception{
		return (Integer) selectByPk("pahalf.order.selectExistsReturn", paHalfOrderListVO);
	}

	public int updateExchangeOrdNo(PaHalfOrderListVO paHalfOrderListVO) throws Exception{
		return update("pahalf.order.updateExchangeOrdNo", paHalfOrderListVO);
	}

	public int insertPaHalfExchangeCancel(PaHalfOrderListVO paHalfOrderListVO) {
		return insert("pahalf.order.insertPaHalfExchangeCancel", paHalfOrderListVO);
		
	}
	
	public int checkPaHalfExchageCancel(PaHalfOrderListVO paHalfOrderListVO) throws Exception{
		return (Integer) selectByPk("pahalf.order.checkPaHalfExchageCancel", paHalfOrderListVO);
	}

	public String selectDelyType(String prdNo) throws Exception{
		return (String) selectByPk("pahalf.order.selectDelyType", prdNo);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return list("pahalf.order.selectPaMobileOrderAutoCancelList", null);
	}
	 
}
