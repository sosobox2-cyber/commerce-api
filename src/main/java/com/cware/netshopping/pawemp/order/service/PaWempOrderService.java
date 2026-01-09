package com.cware.netshopping.pawemp.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaWempDeliveryVO;
import com.cware.netshopping.domain.PaWempTargetVO;
import com.cware.netshopping.domain.PawemporderlistVO;
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.domain.model.PaWempOrderItemList;
import com.cware.netshopping.domain.model.Paorderm;

public interface PaWempOrderService {

	/**
	 * 주문목록 제휴 주문 테이블에 저장
	 * @param paWempOrderList
	 * @param paWempOrderItemList
	 * @return
	 * @throws Exception
	 */
	public String savePaWempOrderTx(PawemporderlistVO paWempOrderList, List<PaWempOrderItemList> paWempOrderItemList) throws Exception;
	
	/**
	 * 주문생성 대상정보 조회
	 * @param targetCnt
	 * @return
	 * @throws Exception
	 */
	public List<PaWempTargetVO> selectOrderInputTargetList(int targetCnt) throws Exception;

	/**
	 * 주문생성을 위한 주문정보 목록 조회
	 * @param paOrderNo
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderInputTargetDtList (ParamMap paramMap) throws Exception;

	/**
	 * 판매자인입 취소 대상 건별 조회
	 * @param mappingSeq
	 * @return
	 * @throws Exception
	 */
	public PaWempTargetVO selectRefusalInfo(String mappingSeq) throws Exception;

	/**
	 * 위메프 주문취소 TPAORDERM 반영
	 * @param mappingSeq
	 * @param preCancelYn
	 * @param preCancelReason
	 * @param apiResultCode
	 * @param apiResultMessage
	 * @return
	 * @throws Exception
	 */
	public int updatePreCancelYnTx(ParamMap preCancelMap) throws Exception;
	
	/**
	 * 판매자인입 취소 API 호출 (주문취소 API)
	 * @param orderOptionNo
	 * @return
	 * @throws Exception
	 */
	public ParamMap orderRefusalProc(PaWempTargetVO targetVo) throws Exception;
	
	/**
	 * 발송처리(송장등록) 대상 리스트 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectSlipOutProcList() throws Exception;
	
	/**
	 * 주문관리 발송처리 API 전송
	 * @param itemMap
	 * @return
	 */
	public ParamMap slipOutProc(HashMap<String, Object> deliveryVo, HashMap<String, String> apiInfo) throws Exception;
	
	/**
	 * 발송처리(송장등록) 대상 조회
	 * @param paShipNo
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectSlipOutProcDtList (String paShipNo) throws Exception;
	
	/**
	 * 발송처리(송장등록) 처리결과 반영 및 출고중으로 상태변경
	 * @param paShipNo
	 * @return
	 * @throws Exception
	 */
	public int updateSlipOutProcTx(HashMap<String, Object> deliveryVo) throws Exception;
	
	/**
	 * 발송처리(송장등록) API연동 실패내용 반영
	 * @param paShipNo
	 * @param apiMsg
	 * @return
	 * @throws Exception
	 */
	public int updateSlipOutProcFailTx(HashMap<String, Object> deliveryVo, String apiMsg) throws Exception;

	/**
	 * 발송정보 DB 저장
	 * @param paOrderNo
	 * @param paShipNo
	 * @param invoiceNo
	 * @param delyComp
	 * @param deliveryStatus
	 * @return
	 * @throws Exception
	 */
	public int updateInvoiceInfoTx(HashMap<String, Object> deliveryVo, String deliveryStatus) throws Exception;
		
	/**
	 * 배송완료정보 반영
	 * @param Paorderm
	 * @return
	 * @throws Exception
	 */
	public String updateDeliveryCompleteTx(Paorderm paOrderm) throws Exception;
	
	/**
	 * 취소 확인전송 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<PaWempClaimList> selectCancelRequestList() throws Exception;
	
	/**
	 * 취소 확인전송 대상 상세정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<PaWempDeliveryVO> selectCancelRequestDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 취소 클레임승인 API전송
	 * @param claimBundleNo
	 * @return
	 * @throws Exception
	 */
	public ParamMap cancelApproveProc(long claimBundleNo, String paCode) throws Exception;
	
	/**
	 * 취소 클레임 거부 API전송
	 * @param deliveryVo
	 * @return
	 * @throws Exception
	 */
	public ParamMap cancelRejectProc(PaWempDeliveryVO deliveryVo) throws Exception;
	
	/**
	 * 취소승인 DB저장. TPAORDERM입력, isUpdate == true이면 TPAWEMPCLAIMITEMLIST '10'승인 update
	 * @param deliveryVoList
	 * @param outBefClaimGb
	 * @param isUpdate
	 * @return
	 * @throws Exception
	 */
	public ParamMap saveCancelApproveTx(List<PaWempDeliveryVO> deliveryVoList, String outBefClaimGb, boolean isUpdate) throws Exception;
	
	/**
	 * 취소거절 DB저장. TPAORDERM 출고 update, TPAWEMPCLAIMITEMLIST '20'취소거부 update
	 * @param deliveryVoList
	 * @return
	 * @throws Exception
	 */
	public ParamMap saveCancelRejectTx(PaWempDeliveryVO deliveryVo) throws Exception;
	
	/**
	 * TPAWEMPCLAIMITEMLIST '90'처리실패 update
	 * @param paClaimNo
	 * @param paOrderNo
	 * @param paShipNo
	 * @return
	 * @throws Exception
	 */
	public ParamMap saveCancelFailTx(PaWempDeliveryVO deliveryVo) throws Exception;
	
	/**
	 * 취소철회여부 수정
	 * @param paWempClaimList
	 * @return
	 * @throws Exception
	 */
	public int updateCancelWithdrawYnTx(PaWempClaimList paWempClaimList) throws Exception;
	
	/**
	 * 취소생성 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<PaWempTargetVO> selectCancelInputTargetList() throws Exception;
	
	/**
	 * 취소생성 - 상세조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * TPAWEMPCLAIMITEMLIST PROC_FLAG업데이트
	 * @param paClaimNo
	 * @param paOrderNo
	 * @param paShipNo
	 * @param paOrderGb
	 * @param procFlag
	 * @return
	 * @throws Exception
	 */
	public int updateClaimItemProcFlag(String paClaimNo, String paOrderNo, String paShipNo, String paOrderGb, String procFlag) throws Exception;
	
	/**
	 * 취소접수이후 택배사 휴일제외한 지난 시간 계산
	 * @param paClaimNo
	 * @param delyGb
	 * @return
	 * @throws Exception
	 */
	public int selectBusinessDayAccount(String paClaimNo, String delyGb) throws Exception;
	
	/**
	 * 배송완료 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectDeliveryCompleteList() throws Exception;
	
	/**
	 * 취소승인처리(BO) 주문 정보 생성
	 * @return
	 * @throws Exception
	 */
	public List<PaWempDeliveryVO> selectOrgItemInfoByCancelInfo(String paClaimNo) throws Exception;

	/**
	 * 직송 배송완료처리 - 배송완료 처리
	 * @param paShipNo, deliveryStatus
	 * @return
	 * @throws Exception
	 */
	public int updateDeliveryStatusTx(String paShipNo, String deliveryStatus) throws Exception;
	
	/**
	 * 취소승인처리 - 생성된 주문이 있는지 확인
	 * @param PawemporderlistVO
	 * @return
	 * @throws Exception
	 */
	public int selectPaWempOrderListExists(PawemporderlistVO paWempOrderList) throws Exception;
	
	/**
	 * 위메프 프로모션 조회 - 조회
	 * @param HashMap<String, String>
	 * @return OrderpromoVO
	 * @throws Exception
	 */
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception;
	
	/**
	 * 위메프 제휴OUT프로모션 조회 - 조회
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * @param HashMap<String, String>
	 * @return OrderpromoVO
	 * @throws Exception
	 */
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception;

	public List<Map<String, Object>> selectSlipUpdateProcList() throws Exception;
}
