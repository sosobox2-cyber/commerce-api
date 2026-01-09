package com.cware.netshopping.paintp.common.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaIntpDelvSettlement;
import com.cware.netshopping.domain.model.PaIntpSettlement;

public interface PaIntpCommonService {
	
	/**
	 * 인터파크 기본전시 저장
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePaIntpGoodsKindsTx(List<PaGoodsKinds> paGoodsKindsList) throws Exception;
	
	/**
	 * 인터파크 - 전체 브랜드 정보 저장
	 * @param List<PaWmkpBrand>
	 * @return String
	 * @throws Exception
	 */
	public String savePaIntpBrandTx(List<PaBrand> paBrandList) throws Exception;
	
	/**
	 * 인터파크 출고지등록
	 * @param Entpuser
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * 인터파크 출고지등록
	 * @param PaEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public String savePaIntpEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * 인터파크 출고지수정 - 수정대상목록조회
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectEntpShipUpdateList(String paAddrGb) throws Exception;
	
	/**
	 * 인터파크 출고지수정
	 * @param PaEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public String savePaIntpEntpSlipUpdateTx(PaEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * 인터파크 배송비 정책 등록 - 배송비 조회
	 * @param ParamMap
	 * @return List<HashMap<?,?>>
	 * @throws Exception
	 */
	public List<HashMap<?,?>> selectEntpSlipCost(ParamMap paramMap) throws Exception;
	
	/**
	 * 인터파크 배송비 정책 등록 - 배송비 저장
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePaIntpCustShipCostTx(ParamMap paramMap) throws Exception;

	/**
	 * 인터파크 상품별 정산 - 정산 저장
	 * @param List
	 * @return String
	 * @throws Exception
	 */
	public String savePaIntpSettlementTx(List<PaIntpSettlement> paIntpSettlementList, ParamMap paramMap) throws Exception;
	
	/**
	 * 인터파크 배송비별 정산 - 정산 저장
	 * @param List
	 * @return String
	 * @throws Exception
	 */
	public String savePaIntpDelvSettlementTx(List<PaIntpDelvSettlement> paIntpSettleDelvSettlements, ParamMap paramMap) throws Exception;
	
}
