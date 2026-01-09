package com.cware.netshopping.pagmkt.claim.repository;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaGmkClaim;
import com.cware.netshopping.domain.model.Paorderm;

@Service("pagmkt.claim.PaGmktClaimDAO")
public class PaGmktClaimDAO extends AbstractPaDAO{
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderClaimTargetList(ParamMap paramMap) throws Exception{
		return list("pagmkt.claim.selectOrderClaimTargetList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetList(ParamMap paramMap) throws Exception{
		return list("pagmkt.claim.selectClaimCancelTargetList", paramMap.get());
	}
	
	public int updateReturnPickup(Map<String, String> rtnMap) throws Exception {
	    return update("pagmkt.claim.updateReturnPickup", rtnMap); 
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectReturnConfirmList(ParamMap paramMap) throws Exception{
		return list("pagmkt.claim.selectReturnConfirmList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectReturnPickup50List(ParamMap paramMap) throws Exception {
    	return list("pagmkt.claim.selectReturnPickup50List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectReturnPickup60List(ParamMap paramMap) throws Exception {
    	return list("pagmkt.claim.selectReturnPickup60List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception{
		return list("pagmkt.claim.selectClaimCancelTargetDtList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception{
		return list("pagmkt.claim.selectOrderCalimTargetDt30List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception{
		return list("pagmkt.claim.selectOrderCalimTargetDt20List", paramMap.get());
	}
	
	public String selectGoodsdtChangeFlag(PaGmkClaim pagmkclaim) throws Exception {
		return (String)selectByPk("pagmkt.claim.selectGoodsdtChangeFlag", pagmkclaim);
	}
	
	public int updatePaOrdermResult(Paorderm paorderm) throws Exception {
		return update("pagmkt.claim.updatePaOrdermResult", paorderm);
	}
	public int checkPaGmktClaimList(PaGmkClaim paGmktClaimList) throws Exception {
		return (Integer)selectByPk("pagmkt.claim.checkPaGmktClaimList", paGmktClaimList);
	}
	public int checkPaGmktClaimCancelList(PaGmkClaim paGmktClaimList) throws Exception {
		return (Integer)selectByPk("pagmkt.claim.checkPaGmktClaimCancelList", paGmktClaimList);
	}	
	public int checkPaGmktClaimListForClaimCancel(PaGmkClaim paGmktClaimList) throws Exception {
		return (Integer)selectByPk("pagmkt.claim.checkPaGmktClaimListForClaimCancel", paGmktClaimList);
	}
	public int check40DatePaGmktClaimList(PaGmkClaim paGmktClaimList) throws Exception {
		return (Integer)selectByPk("pagmkt.claim.check40DatePaGmktClaimList", paGmktClaimList);
	}
	public int check30DatePaGmktClaimList(PaGmkClaim paGmktClaimList) throws Exception {
		return (Integer)selectByPk("pagmkt.claim.check30DatePaGmktClaimList", paGmktClaimList);
	}
	public Integer insertPaGmktClaimList(PaGmkClaim paGmktClaimList) throws Exception{
		return insert("pagmkt.claim.insertPaGmktClaimList", paGmktClaimList);
	}	
	
	public String selectPaorderMWseqForClaimCancel(PaGmkClaim paGmkClaim) throws Exception {
		return (String)selectByPk("pagmkt.claim.selectPaorderMWseqForClaimCancel", paGmkClaim);
	}
	
	public String selectPaorderRequestDateForClaimCancel(PaGmkClaim paGmkClaim) throws Exception {
		return (String)selectByPk("pagmkt.claim.selectPaorderRequestDateForClaimCancel", paGmkClaim);
	}
	
	public String selectPaGmktClaimReqQty(PaGmkClaim paGmktClaimList) throws Exception{
		return (String) selectByPk("pagmkt.claim.selectPaGmktClaimReqQty", paGmktClaimList);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectGoodsdtInfo(PaGmkClaim PaGmkClaim) throws Exception{
		return list("pagmkt.claim.selectGoodsdtInfo", PaGmkClaim);
	}

	@SuppressWarnings("unchecked")
	public String compareAddress(ParamMap paramMap) throws Exception {
		return (String) selectByPk("pagmkt.claim.compareAddress", paramMap.get());
	}
}