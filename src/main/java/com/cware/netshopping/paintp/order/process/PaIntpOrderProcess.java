package com.cware.netshopping.paintp.order.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaIntpTargetVO;
import com.cware.netshopping.domain.model.PaIntpCancellist;
import com.cware.netshopping.domain.model.PaIntpCancellistKey;
import com.cware.netshopping.domain.paintp.xml.PaIntpCancelReqVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderVO;

public interface PaIntpOrderProcess {

	/**
	 * 주문 연동 내역 생성
	 * @param PaIntpOrderVO 주문 연동 내역 목록
	 * @param paCode	       제휴사코드
	 * @return String
	 * @throws Exception
	 */
	public String saveOrderConfirmList(PaIntpOrderVO orderList, String paCode) throws Exception;
	
	/**
	 * 주문 연동 목록 조회
	 * @param limitCount 최대 주문 내역 생성 건 수
	 * @return
	 * @throws Exception
	 */
	public List<PaIntpTargetVO> selectOrderInputTargetList(int limitCount) throws Exception;
	
	/**
	 * 주문 연동 상세 목록 조회
	 * @param paOrderNo 인터파크 주문 번호(제휴주문번호)
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 판매거부 주문 정보 조회
	 * @param mappingSeq
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> selectRefusalInfo(String mappingSeq) throws Exception;
	
	/**
	 * 판매거절 처리
	 * @param ordNo		인터파크 주문번호
	 * @param ordSeq	주문순번
	 * @param canceled	취소전송여부
	 * @param apiResultCode	api 처리결과 코드
	 * @param apiResultMessage	api 처리결과 메시지
	 * @param preCancelReason	판매거절사유
	 * @param clmReqSeq	취소요청순번, optional
	 * @param paCode 제휴사코드
	 * @return String
	 * @throws Exception
	 */
	public String saveOrderRejectProcTx(String ordNo, Integer ordSeq, boolean canceled, String apiResultCode, String apiResultMessage, String preCancelReason, String clmReqSeq, String paCode) throws Exception;

	/**
	 * 인터파크 주문취소요청/철회 내역 저장
	 * @param cancelReqVO
	 * @throws Exception
	 */
	public void saveCancelRequestOrWithdrawList(PaIntpCancelReqVO cancelReqVO) throws Exception;
	
	/**
	 * 인터파크 주문취소요청 접수내역 조회
	 * @return
	 * @throws Exception
	 */
	public List<PaIntpCancellist> selectPaIntpOrderCancelList() throws Exception;
	
	/**
	 * 인터파크 주문취소 승인내역 저장
	 * @param cancel	주문취소내역
	 * @param claimGb	반품접수대상(0: 미대상, 1: 반품접수대상, 2: 출고전반품처리대상)
	 * @return String
	 * @throws Exception
	 */
	public String saveCancelConfirmProc(PaIntpCancellist cancel, String claimGb) throws Exception;

	/**
	 * 인터파크 주문취소 거부내역 저장
	 * @param ordNo				인터파크 주문번호
	 * @param ordSeq			인터파크 주문순번
	 * @param apiResultCode		API 결과 코드
	 * @param apiResultMessage	API 결과 메시지
	 * @return String
	 * @throws Exception
	 */
	public String updatePaOrdermCancelRefusal(String ordNo, String ordSeq, String clmreqSeq, String apiResultCode, String apiResultMessage) throws Exception;
	
	/**
	 * 인터파크 주문취소 처리 - 기취소
	 * @param mappingSeq		주문매핑순번
	 * @param preCancelYn		취소여부(1:취소, 0:미취소)
	 * @param preCancelReason	취소사유
	 * @return 저리 건 수
	 * @throws Exception
	 */
	public int updatePreCancelYn(String mappingSeq, String preCancelYn, String preCancelReason) throws Exception;
	
	/**
	 * 주문취소 대상 내역 조회
	 * @return
	 * @throws Exception
	 */
	public List<PaIntpTargetVO> selectCancelInputTargetList() throws Exception;
	
	/**
	 * 주문취소 대상 상세 내역 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 주문취소요청 내역 조회
	 * @param ordNo		인터파크 주문번호
	 * @param ordSeq	인터파크 주문순번
	 * @param clmreqSeq	인터파크 클레임요청순번, optional
	 * @param paCode	제휴사코드
	 * @return PaIntpCancellistKey
	 * @throws Exception
	 */
	public PaIntpCancellistKey selectPaIntpCancellistApproval(String ordNo, Integer ordSeq, Integer clmreqSeq, String paCode) throws Exception;
	
	/**
	 * 배송완료리스트 저장
	 * @param complete
	 * @throws Exception
	 */
	public void saveDeliveryComplete(PaIntpOrderVO complete) throws Exception;
	
	/**
     * 인터파크 - 배송정보등록 조회
     * @param paramMap
     * @return 
     * @throws Exception
     */
    public List<Map<String,String>> selectSlipOutProcList(ParamMap paramMap) throws Exception;
    
    /**
     * 인터파크 - 배송출고정보등록
     * @param slipOut
     * @return int
     * @throws Exception
     */
    public int updateSlipOutProc(Map<?,?> slipOut) throws Exception;
    
    /**
	 * 인터파크 - 배송출고정보등록-실패처리
	 * @param slipOutFail
	 * @return int
	 * @throws Exception
	 */
	public int updateSlipOutProcFail(Map<?,?> slipOutFail) throws Exception;

	/**
	 * 인터파크 - 주문생성 이전 취소건 PRE_CANCEL_YN UPDATE
	 * @param preCancelMap
	 * @return int
	 * @throws Exception
	 */
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception;

	/**
	 * 취소승인처리(BO) 주문 정보 생성
	 * @param ordNo
	 * @param ordSeq
	 * @param clmreqSeq
	 * @return PaIntpCancellist
	 * @throws Exception
	 */
	public PaIntpCancellist selectOrgItemInfoByCancelInfo(String ordNo, String ordSeq, String clmreqSeq) throws Exception;

	/**
	 * 인터파크 프로모션 조회
	 * @param orderMap
	 * @return OrderpromoVO
	 * @throws Exception
	 */
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception;

	/**
	 * 인터파크 제휴OUT프로모션 조회
	 * @param orderMap
	 * @return OrderpromoVO
	 * @throws Exception
	 */
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception;
	
}