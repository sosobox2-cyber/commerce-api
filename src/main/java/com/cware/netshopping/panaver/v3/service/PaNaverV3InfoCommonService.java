package com.cware.netshopping.panaver.v3.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.panaver.message.v3.ChangedProductOrderInfoListMsg;
import com.cware.netshopping.panaver.v3.domain.ChangedProductOrderInfo;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoAll;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoMsg;

public interface PaNaverV3InfoCommonService {
	
	/**
	 * 네이버 변경 상품 주문내역 조회 
	 * 
	 * @param lastChangedType
	 * @param fromDate
	 * @param toDate
	 * @param productOrderId
	 * @param limitCount
	 * @param procId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public ChangedProductOrderInfoListMsg getChangedProductOrderInfoList(String lastChangedType, String fromDate, String toDate, String productOrderId, int limitCount, String procId, HttpServletRequest request) throws Exception;

	/**
	 * 변경 상품 주문내역 조회 결과 저장 (TPANAVERORDERCHANGE)
	 * 
	 * @param changedProductOrderInfoList
	 * @param productOrderId
	 * @return
	 * @throws Exception
	 */
	public List<ChangedProductOrderInfo> mergeChangeOrderListTx(List<ChangedProductOrderInfo> changedProductOrderInfoList, String productOrderId) throws Exception;

	/**
	 * 주문 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderInputTargetList() throws Exception;

	/**
	 * 주문 취소 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectCancelInputTargetList() throws Exception;

	/**
	 * 반품 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderClaimTargetList() throws Exception;

	/**
	 * 교환 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderChangeTargetList() throws Exception;

	/**
	 * 반품 철회 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectClaimCancelTargetList() throws Exception;

	/**
	 * 교환 철회 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectChangeCancelTargetList() throws Exception;

	/**
	 * 변경 내역 반영여부 업데이트 (TPANAVERORDERCHANGE.CHANGE_APPLIED_YN)
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int updateChangeApplied(ChangedProductOrderInfo order) throws Exception;

	/**
	 * 취소 상세내역 적재
	 * 
	 * @param cancelInfo
	 * @return
	 * @throws Exception
	 */
	public ProductOrderInfoAll insertCancelInfoTx(ProductOrderInfoAll cancelInfo) throws Exception;

	/**
	 * 주문 상세내역 조회 호출
	 * 
	 * @param  productOrderIds
	 * @param  procId
	 * @return ProductOrderInfoMsg
	 * @throws Exception
	 */
	public ProductOrderInfoMsg orderDetailInfo(String productOrderIds,String procId, HttpServletRequest request) throws Exception;
	
	/**
	 * 반품 상세내역 적재
	 * 
	 * @param returnInfo 
	 * @return
	 * @throws Exception
	 */
	public ProductOrderInfoAll insertReturnInfoTx(ProductOrderInfoAll returnInfo) throws Exception;

	/**
	 * 교환 상세내역 적재
	 * 
	 * @param exchangeInfo
	 * @return
	 * @throws Exception
	 */
	public ProductOrderInfoAll insertExchangeInfoTx(ProductOrderInfoAll exchangeInfo) throws Exception;

	/**
	 * 진행중인 클레임 건수 조회
	 * 
	 * @param productOrderId 
	 * @param claimType
	 * @param claimId
	 * @return
	 * @throws Exception
	 */
	public String getExistingClaimCount(String productOrderId, String claimType, String claimId) throws Exception;

	/**
	 * 클레임 배송비 정보 조회
	 * 
	 * @param productOrderId 
	 * @param claimStatus
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> selectClaimShipcostInfo(String productOrderId, String claimStatus) throws Exception;

	/**
	 * 변경 상품 주문 내역 미반영건 조회
	 * 
	 * @param productOrderId
	 * @param procId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public ChangedProductOrderInfoListMsg getUnappliedChangedProductOrderInfoList(String productOrderId, String procId, HttpServletRequest request) throws Exception;

	/**
	 * 변경 상품 주문 내역 미반영건 대상 제외 처리
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int updateUnappliedChangedInfo(ChangedProductOrderInfo order) throws Exception;

	/**
	 * 클레임 철회 데이터 생성 여부 조회
	 * 
	 * @param orderId
	 * @param productOrderId
	 * @param claimType
	 * @param claimId
	 * @return
	 * @throws Exception
	 */
	public int selectExistingClaimReject(String orderId, String productOrderId, String claimType, String claimId) throws Exception;

	/**
	 * 취소 철회 데이터 생성 여부 조회
	 * 
	 * @param orderId
	 * @param productOrderId
	 * @param claimId
	 * @return
	 * @throws Exception
	 */
	public int selectExistingCancelReject(String orderId, String productOrderId, String claimId) throws Exception;
}
