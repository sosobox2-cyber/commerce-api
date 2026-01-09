package com.cware.netshopping.paintp.order.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaIntpTargetVO;
import com.cware.netshopping.domain.model.PaIntpCancellist;
import com.cware.netshopping.domain.model.PaIntpCancellistKey;
import com.cware.netshopping.domain.model.PaIntpOrderlist;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderProductVO;

@Repository("paintp.order.paIntpOrderDAO")
public class PaIntpOrderDAO extends AbstractPaDAO{
	
	/**
	 * Primary Key 에 의한 주문내역(TPAINTPORDERLIST) 건수 조회
	 * @param ordNo 	인터파크 주문번호
	 * @param ordSeq 	주문순번
	 * @param paOrderGb 주문구분, optional
	 * @return 건수
	 */
	public int countOrderList(String ordNo, String ordSeq, String clmreqSeq, String paOrderGb) throws Exception{
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo", ordNo);
		paramMap.put("ordSeq",  ordSeq);
		paramMap.put("clmreqSeq",  clmreqSeq);
		paramMap.put("paOrderGb", paOrderGb);
		
		return (Integer) selectByPk("paintp.order.countOrderList", paramMap.get());
	}
	
	/**
	 * 인타파크 주문취소요청 내역(TPAINTPORDERLIST) 저장
	 * @param orderlist
	 * @return
	 */
	public int insertCancelReqPaIntpOrderlist(PaIntpOrderlist orderlist) throws Exception{
		return insert("paintp.order.insertCancelReqPaIntpOrderlist", orderlist);
	}
	
	/**
	 * 인터파크 주문취소요청 철회 여부 조회
	 * @param ordNo		인터파크 주문번호
	 * @param ordSeq	인터파크 주문순번
	 * @return	철회여부(1:철회, 0:미철회)
	 */
	public String getOrderCancelWithdrawYn(String ordNo, String ordSeq, String clmreqSeq) throws Exception{
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo", ordNo);
		paramMap.put("ordSeq",  ordSeq);
		paramMap.put("clmreqSeq",  clmreqSeq);
		return (String) selectByPk("paintp.order.getOrderCancelWithdrawYn", paramMap.get());
	}
	
	/**
	 * 인터파크 주문취소요청내역 철회로 변경
	 * @param ordNo		인터파크 주문번호
	 * @param ordSeq	인터파크 주문순번
	 * @return
	 */
	public int updateWithdrawYn(String ordNo, String ordSeq, String clmreqSeq) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo", ordNo);
		paramMap.put("ordSeq",  ordSeq);
		paramMap.put("clmreqSeq",  clmreqSeq);
		return update("paintp.order.updateWithdrawYn", paramMap.get());
	}
	
	/**
	 * TPAINTPORDERLIST table merge
	 * @param orderlist
	 * @return
	 */
	public int insertPaIntpOrderlist(PaIntpOrderlist orderlist) throws Exception{
		return insert("paintp.order.insertPaIntpOrderlist", orderlist);
	}
	
	/**
	 * 주문 생성 대상 목록 조회
	 * @param limitCount 최대 조회 주문 건 수
	 * @return List&lt;Map&lt;String, ?&gt;&gt;
	 *  	<ul>
	 *  		<li>PA_ORDER_NO: 주문번호</li>
	 *  		<li>TARGET_CNT: 주문 ROW 수</li>
	 *  	<ul>
	 */
	@SuppressWarnings("unchecked")
	public List<PaIntpTargetVO> selectOrderInputTargetList(int limitCount) throws Exception{
		ParamMap paramMap = new ParamMap();
		paramMap.put("limitCount", limitCount);
		return list("paintp.order.selectOrderInputTargetList", paramMap.get());
	}
	
	/**
	 * 주문 연동 상세 목록 조회
	 * @param paOrderNo 인터파크 주문번호(제휴주문번호)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception{
		return list("paintp.order.selectOrderInputTargetDtList", paramMap.get());
	}
	
	/**
	 * TPAORDERM 의 MAPPING_SEQ 에 대한 PA_ORDER_NO, PA_ORDER_SEQ를 조회한다.
	 * @param mappingSeq
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getPaOrderKeyByMappingSeq(String mappingSeq) throws Exception {
		Map<String, String> paramMap = Collections.singletonMap("mappingSeq", mappingSeq);
		return (Map<String, String>) selectByPk("paintp.order.getPaOrderKeyByMappingSeq", paramMap);
	}
	
	/**
	 * 인터파크 주문취소 처리 - 기취소
	 * @param mappingSeq		주문매핑순번
	 * @param preCancelYn		취소여부(1:취소, 0:미취소)
	 * @param preCancelReason	취소사유
	 * @return int
	 * @throws Exception
	 */
	public int updatePreCancelYn(String mappingSeq, String preCancelYn, String preCancelReason) throws Exception{
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("mappingSeq", mappingSeq);
		paramMap.put("preCancelYn", preCancelYn);
		paramMap.put("preCancelReason", preCancelReason);
		return update("paintp.order.updatePreCancelYn", Collections.unmodifiableMap(paramMap));
	}
	
	/**
	 * 판매거절 처리내역 저장
	 * @param ordNo		주문번호
	 * @param ordSeq	주문순번
	 * @param canceled	취소처리여부
	 * @param apiResultCode	api 처리결과 코드
	 * @param apiResultMessage	api 처리결과 메시지
	 * @param preCancelReason	판매거절사유
	 * @param clmReqSeq	취소요청순번, optional
	 * @param paCode 제휴사코드
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrdermPreCancel(String ordNo, Integer ordSeq, boolean canceled, String apiResultCode, String apiResultMessage, String preCancelReason, String clmReqSeq, String paCode) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo", ordNo);
		paramMap.put("ordSeq", ordSeq);
		paramMap.put("ordPrdSeq", ordSeq);
		paramMap.put("canceled", canceled);
		paramMap.put("apiResultCode", apiResultCode);
		paramMap.put("apiResultMessage", apiResultMessage);
		paramMap.put("preCancelReason", preCancelReason);
		paramMap.put("clmReqSeq", clmReqSeq);
		paramMap.put("paCode", paCode);
		return update("paintp.order.updatePaOrdermPreCancel", paramMap.get());
	}
	
	/**
	 * 인터파크 주문 연동 처리내역 수정
	 * @param ordNo		인터파크 주문번호
	 * @param ordSeq	인터파크 주문순번
	 * @param paOrderGb	클레임요청순번
	 * @param procFlag	처리구분
	 * @return
	 */
	public int updatePaIntpOrderListProcFlag(String ordNo, String ordSeq, String clmreqSeq, String paOrderGb, String procFlag) throws Exception{
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo", ordNo);
		paramMap.put("ordSeq", ordSeq);
		paramMap.put("clmreqSeq", clmreqSeq);
		paramMap.put("paOrderGb", paOrderGb);
		paramMap.put("procFlag", procFlag);
		return update("paintp.order.updatePaIntpOrderListProcFlag", paramMap.get());
	}
	
	/**
	 * 인터파크 주문 취소 거부 내역 저장(TPAORDERM)
	 * @param ordNo				인터파크 주문번호
	 * @param ordSeq			인터파크 주문순번
	 * @param paDoFlag			주문 처리구분
	 * @param apiResultCode		API 결과 코드
	 * @param apiResultMessage	API 결과 메시지
	 * @return
	 */
	public int updatePaOrdermCancelRefusal(String ordNo, String ordSeq, String paDoFlag, String apiResultCode, String apiResultMessage) throws Exception{
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo", ordNo);
		paramMap.put("ordSeq", ordSeq);
		paramMap.put("paDoFlag", paDoFlag);
		paramMap.put("apiResultCode", apiResultCode);
		paramMap.put("apiResultMessage", apiResultMessage);
		
		return update("paintp.order.updatePaOrdermCancelRefusal", paramMap.get());
	}
	
	/**
	 * 인터파크 주문 취소 요청 내역 조회
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PaIntpCancellist> selectPaIntpOrderCancelList() throws Exception{
		return list("paintp.order.selectPaIntpOrderCancelList", null);
	}
	
	/**
	 * 주문취소 대상 내역 조회
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PaIntpTargetVO> selectCancelInputTargetList() throws Exception {
		return list("paintp.order.selectCancelInputTargetList", null);
	}
	
	/**
	 * 주문취소 대상 상세 내역 조회
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception{
		return list("paintp.order.selectCancelInputTargetDtList", paramMap.get());
	}
	
	/**
	 * 주문취소요청내역 조회
	 * @param ordNo		인터파크 주문번호
	 * @param ordSeq	인터파크 주문순번
	 * @param clmreqSeq	인터파크 클레임요청순번
	 * @param paCode    제휴사코드
	 * @return
	 */
	public PaIntpCancellistKey selectPaIntpCancellistApproval(String ordNo, Integer ordSeq, Integer clmreqSeq, String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		if(paCode.equals(Constants.PA_INTP_BROAD_CODE)) {
			paramMap.put("paCode", Constants.PA_INTP_BROAD_CODE);
		} else if(paCode.equals(Constants.PA_INTP_ONLINE_CODE)) {
			paramMap.put("paCode", Constants.PA_INTP_ONLINE_CODE);
		}
		paramMap.put("ordNo", ordNo);
		paramMap.put("ordSeq", ordSeq);
		if (clmreqSeq != null) {
			paramMap.put("clmreqSeq", clmreqSeq);
		}
		return (PaIntpCancellistKey) selectByPk("paintp.order.selectPaIntpCancellistApproval", paramMap.get());
	}
	
	/**
	 * 주문상품정보 조회
	 * @param ordNo		인터파크 주문번호
	 * @param ordSeq	인터파크 주문순번
	 * @return
	 */
	public PaIntpOrderProductVO getOrderProductInfo(String ordNo, String ordSeq) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo", ordNo);
		paramMap.put("ordSeq", ordSeq);
		return (PaIntpOrderProductVO) selectByPk("paintp.order.getOrderProductInfo",paramMap.get());
	}
	
	/**
	 * 구매확정정보 저장
	 * @param ordNo				인터파크 주문번호
	 * @param ordSeq			인터파크 주문순번
	 * @param apiResultCode		API 결과코드
	 * @param apiResultMessage	API 결과메시지
	 * @param paDoFlag			처리 구분
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrdermResult(String ordNo, String ordSeq, String apiResultCode, String apiResultMessage, String paDoFlag) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo", ordNo);
		paramMap.put("ordSeq", ordSeq);
		paramMap.put("apiResultCode", apiResultCode);
		paramMap.put("apiResultMessage", apiResultMessage);
		paramMap.put("paDoFlag", paDoFlag);
		
		return update("paintp.order.updatePaOrdermResult", paramMap.get());
	}
	
	/**
	 * 인터파크 - 배송완료처리 조회
	 * @param paramMap
	 * @return Integer
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectSlipOutProcList(ParamMap paramMap) throws Exception{
		return list("paintp.order.selectSlipOutProcList", paramMap.get());
	}
	
	/**
	 * 인터파크 - 출고정보등록
	 * @param slipOut
	 * @return Integer
	 * @throws Exception
	 */
	public int updateSlipOutProc(Map<?,?> slipOut) throws Exception{
		return update("paintp.order.updateSlipOutProc", slipOut);
	}
	
	/**
	 * 인터파크 - 출고정보등록-실패처리
	 * @param slipOut
	 * @return Integer
	 * @throws Exception
	 */
	public int updateSlipOutProcFail(Map<?,?> slipOut) throws Exception{
		return update("paintp.order.updateSlipOutProcFail", slipOut);
	}

	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception{
		return update("paintp.order.updatePreCancelYn", preCancelMap.get());
	}

	public PaIntpCancellist selectOrgItemInfoByCancelInfo(String ordNo, String ordSeq, String clmreqSeq) throws Exception{
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo", ordNo);
		paramMap.put("ordSeq", ordSeq);
		paramMap.put("clmreqSeq", clmreqSeq);
		
		return (PaIntpCancellist) selectByPk("paintp.order.selectOrgItemInfoByCancelInfo", paramMap.get());
	}

	public int countDeliComList(String ordNo, String ordSeq) throws Exception{
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo", ordNo);
		paramMap.put("ordSeq", ordSeq);
		
		return (Integer) selectByPk("paintp.order.countDeliComList", paramMap.get());
	}

	/**
	 * 인터파크 프로모션 조회
	 * @param orderMap
	 * @return OrderpromoVO
	 * @throws Exception
	 */
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception{
		return  (OrderpromoVO)(selectByPk("paintp.order.selectOrderPromo", orderMap));
	}

	/**
	 * 인터파크 제휴OUT프로모션 조회
	 * @param orderMap
	 * @return OrderpromoVO
	 */
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception{
		return (OrderpromoVO)(selectByPk("paintp.order.selectOrderPaPromo", orderMap));
	}
}