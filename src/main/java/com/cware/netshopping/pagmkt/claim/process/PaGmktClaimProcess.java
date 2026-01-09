package com.cware.netshopping.pagmkt.claim.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaGmkClaim;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;

public interface PaGmktClaimProcess {

	public int saveClaimList(PaGmkClaim paGmktClaim) throws Exception;
	public PaGmkClaim setPaGmkClaimVo(Map<String, Object> returnMap, ParamMap paramMap) throws Exception;
	public int updateReturnPickup(Map<String, String> rtnMap) throws Exception;
	public List<Object> selectReturnConfirmList(ParamMap paramMap) throws Exception;
	public int saveReturnConfirmProc(PaGmktAbstractRest rest,	ParamMap paramMap, HashMap<String, Object> returnMap) throws Exception;
	public List<Object> selectOrderClaimTargetList(ParamMap paramMap) throws Exception;
	public List<Object> selectClaimCancelTargetList(ParamMap paramMap) throws Exception;
	public List<Map<String, String>> selectReturnPickup50List(ParamMap paramMap) throws Exception;
	public List<Map<String, String>> selectReturnPickup60List(ParamMap paramMap) throws Exception;
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception;
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception;
	public String selectGoodsdtChangeFlag(PaGmkClaim pagmkclaim) throws Exception;
	public String compareAddress(ParamMap paramMap) throws Exception;
	
}