package com.cware.netshopping.palton.claim.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaLtonCancelListVO;
import com.cware.netshopping.domain.PaLtonClaimListVO;
import com.cware.netshopping.domain.model.PaLtonNotReceiveList;

@Service("palton.delivery.paLtonClaimDAO")
public class PaLtonClaimDAO extends AbstractPaDAO{

	public int insertTPaLtonCancelList(PaLtonCancelListVO cancelVo) throws Exception {
		return insert("palton.claim.insertTPaLtonCancelList", cancelVo);
	}

	public int updatePaLtonCancelList4Withdraw(PaLtonCancelListVO cancelVo) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("odNo"		, cancelVo.getOdNo());
		paramMap.put("odSeq"	, cancelVo.getOdSeq());
		paramMap.put("procSeq"	, cancelVo.getOrglProcSeq());
		
		return update("palton.claim.updatePaLtonCancelList4Withdraw", paramMap.get());
	}

	public int selectPaLtonCancelListCount(PaLtonCancelListVO cancelVo) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("odNo"		, cancelVo.getOdNo());
		paramMap.put("odSeq"	, cancelVo.getOdSeq());
		paramMap.put("procSeq"	, cancelVo.getProcSeq());
		paramMap.put("odPrgsStepCd", cancelVo.getOdPrgsStepCd());

		return (Integer) selectByPk("palton.claim.selectPaLtonCancelListCount", paramMap.get());
	}
	
	public int selectPaLtonWithdrawCancelListCount(PaLtonCancelListVO cancelVo) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("odNo"		, cancelVo.getOdNo());
		paramMap.put("odSeq"	, cancelVo.getOdSeq());
		paramMap.put("procSeq"	, cancelVo.getOrglProcSeq());

		return (Integer) selectByPk("palton.claim.selectPaLtonCancelListCount", paramMap.get());
	}

	public int selectOutClaimGb(PaLtonCancelListVO cancelVo)  throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("odNo"		, cancelVo.getOdNo());
		paramMap.put("odSeq"	, cancelVo.getOdSeq());
		paramMap.put("paCode"	, cancelVo.getPaCode());
		
		return (int) selectByPk("palton.claim.selectOutClaimGb", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaLtonOrderCancelList() throws Exception{
		return list("palton.claim.selectPaLtonOrderCancelList",  null);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaLtonCancelApprovalList(ParamMap paramMap) throws Exception {
		return list("palton.claim.selectPaLtonCancelApprovalList",  paramMap.get());
	}

	public int updateProcFlag(Map<String, Object> cancelMap) throws Exception {
		return update("palton.claim.updateProcFlag", cancelMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaLtonCancelRefusalList(ParamMap apiDataMap) throws Exception{
		return list("palton.claim.selectPaLtonCancelRefusalList",  apiDataMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaLtonOrderCancel(ParamMap paramMap) throws Exception{
		return (HashMap<String, Object>) selectByPk("palton.claim.selectPaLtonOrderCancel", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("palton.claim.selectCancelInputTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaLtonReturnExchangeApprovalList(ParamMap paramMap) throws Exception {
		return list("palton.claim.selectPaLtonReturnExchangeApprovalList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaLtonReturnExchangeApprovalDtList(ParamMap apiDataMap) throws Exception {
		return list("palton.claim.selectPaLtonReturnExchangeApprovalDtList", apiDataMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return list("palton.claim.selectClaimTargetList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return list("palton.claim.selectOrderCalimTargetDt20List", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return list("palton.claim.selectOrderCalimTargetDt30List", paramMap.get());
	}

	public String compareAddress(ParamMap paramMap) throws Exception {
		return (String) selectByPk("palton.claim.compareAddress", paramMap.get());
	}

	public int selectGoodsdtInfoCount(ParamMap paramMap) throws Exception {
		return (int) selectByPk("palton.claim.selectGoodsdtInfoCount", paramMap.get());
	}

	public int selectPaLtonClaimListCount(PaLtonClaimListVO claimlist) throws Exception {
		return (Integer) selectByPk("palton.claim.selectPaLtonExistsClaimListCount", claimlist);
	}

	public int insertTPaLtonClaimList(PaLtonClaimListVO claimlist) throws Exception {
		return insert("palton.claim.insertTPaLtonClaimList", claimlist);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("palton.claim.selectClaimCancelTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return list("palton.claim.selectOrderChangeTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("palton.claim.selectChangeCancelTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectReturnSlipProcList() throws Exception {
		return list("palton.claim.selectReturnSlipProcList", null);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectExchangeSlipProcList() throws Exception {
		return list("palton.claim.selectExchangeSlipProcList", null );
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectExchangeSendList() throws Exception {
		return list("palton.claim.selectExchangeSendList", null);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectExchangeCompleteList()  throws Exception {
		return list("palton.claim.selectExchangeCompleteList", null );
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaLtonExchangeRefuseList() throws Exception {
		return list("palton.claim.selectPaLtonExchangeRefuseList", null );
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaLtonExchangeRefuseDtList(ParamMap apiDataMap) throws Exception{
		return list("palton.claim.selectPaLtonExchangeRefuseDtList", apiDataMap.get() );
	}
	
	public int updateTPaOrderM4ChangeRefualReuslt(ParamMap paramMap) throws Exception {
		 return update("palton.claim.updateTPaOrderM4ChangeRefualReuslt", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPaLtonClaimList4Exchange(PaLtonClaimListVO claimlist) throws Exception {
		ParamMap param = new ParamMap();
		param.put("odNo"	, claimlist.getOdNo());
		param.put("clmNo"	, claimlist.getClmNo());
		param.put("odSeq"	, claimlist.getOdSeq());
		return list("palton.claim.getPaLtonClaimList4Exchange", param.get() );
	}

	public int updateTPaLtonClaimList(PaLtonClaimListVO claimVO) throws Exception {
		return update("palton.claim.updateTPaLtonClaimList", claimVO);		
	}

	public int insertTPaltonNotReceiveList(PaLtonNotReceiveList notReceiveVO) throws Exception {
		return insert("palton.claim.insertTPaltonNotReceiveList", notReceiveVO);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> selectCustInfoForCustCounsel(ParamMap paramMap) throws Exception {
		return (Map<String, String>) selectByPk("palton.claim.selectCustInfoForCustCounsel", paramMap.get());
	}

	public int updateTPaLtonNotReceiveList(ParamMap paramMap) {
		return update("palton.claim.updateTPaLtonNotReceiveList", paramMap.get());
	}

	public int selectTPaLtonNotReceiveListExist(ParamMap paramMap) {
		return (int) selectByPk("palton.claim.selectTPaLtonNotReceiveListExist", paramMap.get());

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectWithDrawNotReceiveList() {
		return list("palton.claim.selectWithDrawNotReceiveList",  null );
	}

	public int updateNotReceiveList4WithDraw(ParamMap paramMap) {
		return update("palton.claim.updateNotReceiveList4WithDraw", paramMap.get());
	}

	public int updatePaOrderm4PreChangeCancel(Map<String, Object> preCancelMap) {
		return update("palton.claim.updatePaOrderm4PreChangeCancel", preCancelMap);
	}

	public int updatePaOrdermChangeFlag(ParamMap paramMap) throws Exception {
		return update("palton.claim.updatePaOrdermChangeFlag", paramMap.get());
	}
	
	public int selectConfirmCancelQty(ParamMap paramMap) throws Exception {
		return (int) selectByPk("palton.claim.selectConfirmCancelQty", paramMap.get());
	}
	
	public int mergePaLtonCancelList(PaLtonCancelListVO cancelVo) throws Exception {
		return insert("palton.claim.mergePaLtonCancelList", cancelVo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaLtonCancelApprovalListBO(ParamMap paramMap) throws Exception {
		return list("palton.claim.selectPaLtonCancelApprovalListBO",  paramMap.get());
	}
	
	public String selectGoodsDelyType(Map<String, String> map)  throws Exception {
		return (String) selectByPk("palton.claim.selectGoodsDelyType", map);
	}
	
	public int selectOutClaimGb(Map<String, String> map)  throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("odNo"		, map.get("odNo"));
		paramMap.put("odSeq"	, map.get("odSeq"));
		paramMap.put("paCode"	, map.get("paCode"));
		
		return (int) selectByPk("palton.claim.selectOutClaimGb", paramMap.get());
	}

	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() {
		return list("palton.claim.selectPaMobileOrderAutoCancelList",  null);
	}
}
