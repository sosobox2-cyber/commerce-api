package com.cware.netshopping.palton.claim.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaLtonCancelListVO;
import com.cware.netshopping.domain.PaLtonClaimListVO;
import com.cware.netshopping.domain.model.PaLtonNotReceiveList;

public interface PaLtonClaimService {

	List<Map<String, Object>> selectPaLtonOrderCancelList() throws Exception;
	List<Map<String, Object>> selectPaLtonCancelApprovalList(ParamMap paramMap) throws Exception;
	int saveCancelConfirmTx(Map<String, Object> cancelMap) throws Exception;
	List<Map<String, Object>> selectPaLtonCancelRefusalList(ParamMap apiDataMap) throws Exception;
	int saveLtonCancelList(PaLtonCancelListVO vo) throws Exception;
	HashMap<String, Object> selectPaLtonOrderCancel(ParamMap paramMap) throws Exception;
	int updateProcFlag(Map<String, Object> c) throws Exception;
	int saveLtonCompleteCancelListTx(PaLtonCancelListVO vo) throws Exception;
	int saveLtonWithdrawCancelList(PaLtonCancelListVO vo) throws Exception;
	HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;
	void savePaLtonClaimListTx(PaLtonClaimListVO vo) throws Exception;
	List<Map<String, Object>> selectPaLtonReturnExchangeApprovalDtList(ParamMap apiDataMap) throws Exception;
	List<Map<String, Object>> selectPaLtonReturnExchangeApprovalList(ParamMap paramMap) throws Exception;
	List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;
	List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception;
	List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception;
	String compareAddress(ParamMap paramMap) throws Exception;
	List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;
	List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;
	List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;
	List<Map<String, Object>> selectReturnSlipProcList() throws Exception;
	List<Map<String, Object>> selectExchangeSlipProcList() throws Exception;
	List<Map<String, Object>> selectExchangeSendList() throws Exception;
	List<Map<String, Object>> selectExchangeCompleteList() throws Exception;
	List<Map<String, Object>> selectPaLtonExchangeRefuseList() throws Exception;
	int updateTPaOrderM4ChangeRefualReuslt(ParamMap paramMap) throws Exception;
	List<Map<String, Object>> selectPaLtonExchangeRefuseDtList(ParamMap apiDataMap) throws Exception;
	int saveLtonNotReceiveListTx(PaLtonNotReceiveList notReceiveVO) throws Exception;
	List<Map<String, Object>> selectWithDrawNotReceiveList() throws Exception;
	int updateNotReceiveList4WithDraw(ParamMap apiDatMap) throws Exception;
	void preOrderChangeCancelTx(HashMap<String, Object> hmSheet) throws Exception;
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception;
	int selectConfirmCancelQty(ParamMap paramMap) throws Exception;
	List<Map<String, Object>> selectPaLtonCancelApprovalListBO(ParamMap paramMap) throws Exception;
	List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception;

}
