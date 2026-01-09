package com.cware.netshopping.paintp.claim.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaIntpCancellist;
import com.cware.netshopping.domain.model.PaIntpClaimlist;
import com.cware.netshopping.domain.model.PaIntpOrderlist;
import com.cware.netshopping.domain.model.Paorderm;

@Service("paintp.claim.paIntpClaimDAO")
public class PaIntpClaimDAO extends AbstractPaDAO{

	/**
	 * 인터파크 반품신청목록 조회 - 데이터 중복 체크
	 * @param PaIntpClaimlistVO
	 * @return Integer
	 * @throws Exception
	 */
	public int selectPaIntpClaimListExists(String ordNo, String ordSeq, String clmNo, String clmSeq, String paOrderGb) throws Exception {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("ordNo"	, ordNo);
		paramMap.put("ordSeq"	,  ordSeq);
		paramMap.put("clmNo"	, clmNo);
		paramMap.put("clmSeq"	,  clmSeq);
		
		if (StringUtils.hasText(paOrderGb)) {
			paramMap.put("paOrderGb", paOrderGb);
		}
		
		return (Integer) selectByPk("paintp.claim.selectPaIntpClaimListExists", Collections.unmodifiableMap(paramMap));		
	}
	/**
	 * 인터파크 반품신청목록 조회 - 데이터 저장[TpaIntpClaimList]
	 * @param PaIntpClaimlistVO
	 * @return Integer
	 * @throws Exception
	 */
	public int insertPaIntpClaimList(PaIntpClaimlist claimlist) throws Exception{
		return insert("paintp.claim.insertPaIntpClaimList", claimlist);
	}
	/**
	 * 인터파크 클레임 대상 타겟 조회 - Target을 추출하여 AsyncContoller로 호출 
	 * @param PaIntpClaimlistVO
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception{
		return list("paintp.claim.selectClaimTargetList", paramMap.get());
	}
	public String compareAddress(ParamMap paramMap) throws Exception{
		return (String) selectByPk("paintp.claim.compareAddress", paramMap.get());
	}
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception{
		return list("paintp.claim.selectOrderCalimTargetDt30List", paramMap.get());
	}
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception{
		return list("paintp.claim.selectOrderCalimTargetDt20List", paramMap.get());
	}
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception{
		return list("paintp.claim.selectClaimCancelTargetDtList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception{
		return list("paintp.claim.selectOrderChangeTargetDtList", paramMap.get());
	}
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception{
		return list("paintp.claim.selectChangeCancelTargetDtList", paramMap.get());
	}
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectGoodsOptionCode4Exchange(ParamMap paramMap) throws Exception {
		return (HashMap<String, String>)selectByPk("paintp.claim.selectGoodsOptionCode4Exchange", paramMap.get());
	}
	public int updatePaOrdermResult(Paorderm paorderm) throws Exception{
		return update("paintp.claim.updatePaOrdermResult", paorderm);
	}

	public String selectPaIntpCliamOrdMappingSeq(ParamMap paramMap) throws Exception {
		return (String) selectByPk("paintp.claim.selectPaIntpCliamOrdMappingSeq", paramMap.get());
	}
	@SuppressWarnings("unchecked")
	public List<Object> selectReturnOkList() throws Exception{
		return list("paintp.claim.selectReturnOkList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectExchangeSlipList() throws Exception{
		return list("paintp.claim.selectExchangeSlipList", null);
	}	
	
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception{
		return update("paintp.claim.updatePreCancelYn", preCancelMap.get());
	}

	public int selectGoodsdtInfoCount(ParamMap paramMap) throws Exception{
		return (int) selectByPk("paintp.claim.selectGoodsdtInfoCount", paramMap.get());
	}
	public PaIntpCancellist selectPaIntpOrderCanceDone(PaIntpOrderlist claimlistVO) throws Exception{
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo"	, claimlistVO.getOrdNo());
		paramMap.put("ordSeq"	, claimlistVO.getOrdSeq());
		return (PaIntpCancellist) selectByPk("paintp.claim.selectPaIntpOrderCanceDone", paramMap.get());
	}
	public String selectMaxClmReqSeq(String ordNo, String ordSeq) throws Exception{
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo"	,	ordNo);
		paramMap.put("ordSeq"	,	ordSeq);
		return (String) selectByPk("paintp.claim.selectMaxClmReqSeq", paramMap.get());
	}

		
	/*
	
	@SuppressWarnings("unchecked")
	public List<Object> selectReturnHoldList() throws Exception{
		return list("paintp.claim.selectReturnHoldList", null);
	}
	
	public int updatePaOrdermHoldYn(Paorderm paorderm) throws Exception{
		return update("paintp.claim.updatePaOrdermHoldYn", paorderm);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectReturnInfo(PaIntpClaimlistVO paIntpClaimList) throws Exception{
		return (HashMap<String, Object>) selectByPk("paintp.claim.selectReturnInfo", paIntpClaimList);
	}
*/
	
}