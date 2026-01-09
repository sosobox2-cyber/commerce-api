package com.cware.netshopping.pahalf.claim.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaHalfOrderListVO;

@Service("pahalf.claim.paHalfClaimDAO")
public class PaHalfClaimDAO extends AbstractPaDAO{

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectExchangeCompleteList() throws Exception{
		return list("pahalf.claim.selectExchangeCompleteList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectReturnApprovalList() throws Exception{
        return list("pahalf.claim.selectReturnApprovalList", null);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Integer> selectAvailableClaim(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		return (Map<String, Integer>) selectByPk("pahalf.claim.selectAvailableClaim", paHalfOrderListVO);
	}

	public int selectCalimDoFlag(PaHalfOrderListVO paHalfOrderListVO) throws Exception  {
		return (int) selectByPk("pahalf.claim.selectCalimDoFlag", paHalfOrderListVO);
	}

	public String selectClaimNo(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		return (String) selectByPk("pahalf.claim.selectClaimNo", paHalfOrderListVO);
	}

	public String selectClaimNo4ClaimCancel(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		return (String) selectByPk("pahalf.claim.selectClaimNo4ClaimCancel", paHalfOrderListVO);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectExchangeGoodsdt(String prdNo) throws Exception {
		return list("pahalf.claim.selectExchangeGoodsdt", prdNo);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectOrderClaimTargetDt30(ParamMap paramMap) throws Exception  {
		return (HashMap<String, Object>) selectByPk("pahalf.claim.selectOrderClaimTargetDt30", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectOrderClaimTargetDt20(ParamMap paramMap)throws Exception  {
		return (HashMap<String, Object>) selectByPk("pahalf.claim.selectOrderClaimTargetDt20", paramMap.get());
	}

	public String compareAddress(HashMap<String, Object> orderClaimVO) throws Exception  {
		return (String) selectByPk("pahalf.claim.compareAddress", orderClaimVO);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectClaimCancelTargetDt(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("pahalf.claim.selectClaimCancelTargetDt", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return list("pahalf.claim.selectOrderChangeTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("pahalf.claim.selectChangeCancelTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> selectReturnStt(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		return (HashMap<String, Object>) selectByPk("pahalf.claim.selectReturnStt", paHalfOrderListVO);
	}


	public String selectExchangeGoodsDtCode(Map<String, String> paramMap) throws Exception  {
		return (String) selectByPk("pahalf.claim.selectExchangeGoodsDtCode", paramMap);
	}

	public int mergeExchangeOrderNm(PaHalfOrderListVO paHalfOrderListVO) throws Exception  {
		return insert("pahalf.claim.mergeExchangeOrderNm", paHalfOrderListVO);
		
	}

	public String selectClaimNo4ReturnCancel(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		return (String) selectByPk("pahalf.claim.selectClaimNo4ReturnCancel", paHalfOrderListVO);
	}

	public String checkShpFeeYn(HashMap<String, Object> orderClaimTargetDt)throws Exception {
		return (String) selectByPk("pahalf.claim.checkShpFeeYn", orderClaimTargetDt);
	}
	 
}
