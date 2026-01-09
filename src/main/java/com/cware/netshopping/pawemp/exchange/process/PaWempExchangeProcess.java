package com.cware.netshopping.pawemp.exchange.process;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.domain.model.Paorderm;

public interface PaWempExchangeProcess {

	/**
	 * 교환요청 목록 저장
	 * @param paWempClaimList
	 * @return
	 * @throws Exception
	 */
	public String saveExchangeList(PaWempClaimList paWempClaim)throws Exception;
	
	/**
	 * 교환접수 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Paorderm> selectOrderChangeTargetList() throws Exception;
	
	/**
	 * 교환접수 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 재고부족 교환보류후 CHANGE_FLAG = '06' 업데이트
	 * @param changeFlag
	 * @param mappingSeq
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception;

	/**
	 * TPAORDERM 조회 by MAPPING_SEQ
	 * @param String
	 * @return Paorderm
	 * @throws Exception
	 */
	public Paorderm selectPaOrdermInfo(String mappingSeq) throws Exception;
	
	/**
	 * 교환철회요청 DB저장
	 * @param paWempClaimList
	 * @return
	 * @throws Exception
	 */
	public String saveExchangeCancelList(PaWempClaimList paWempClaim) throws Exception;
	
	/**
	 * 교환철회대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	public List<Paorderm> selectChangeCancelTargetList() throws Exception;
	
	/**
	 * 교환철회 대상 상세 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 교환회수 수거요청 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectExchangePickupList() throws Exception;
	
	/**
	 * 수거요청 전송성공
	 * @param paClaimNo
	 * @return
	 * @throws Exception
	 */
	public int updatePickupProc(String paClaimNo) throws Exception;
	
	/**
	 * API연동 오류
	 * @param paClaimNo
	 * @param paOrderNo
	 * @param paShipNo
	 * @param apiResultMessage
	 * @param paOrderGb
	 * @return
	 * @throws Exception
	 */
	public int updateProcFail(String paClaimNo, String apiResultMessage, String paOrderGb, String mappingSeq) throws Exception;

	/**
	 * 수거요청 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectPickupCompleteList() throws Exception;

	/**
	 * 교환회수 수거완료 전송성공
	 * @param paClaimNo
	 * @param paOrderNo
	 * @param paShipNo
	 * @return
	 * @throws Exception
	 */
	public int updatePickupCompleteProc(HashMap<String, Object> receive) throws Exception;
	
	/**
	 * 교환 배송등록 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectExchangeSlipOutTargetList() throws Exception;
	
	/**
	 * 교환배송등록 API전송
	 * @param paClaimNo
	 * @param parcelCompanyCode
	 * @param scheduleShipDate
	 * @param delyHopeYn
	 * @param paDelyGbName
	 * @param invoiceNo
	 * @return
	 * @throws Exception
	 */
	public ParamMap slipOutProc(HashMap<String, Object> exchangeDeliveryVo, HashMap<String, String> apiInfo) throws Exception;

	/**
	 * 교환배송 등록성공
	 * @param paClaimNo
	 * @param paOrderNo
	 * @param paShipNo
	 * @return
	 * @throws Exception
	 */
	public int updateDeliveryProc(HashMap<String, Object> exchageDeliveryVo) throws Exception;
	
	/**
	 * 교환클레임완료 API전송
	 * @param claimBundleNo
	 * @param claimType
	 * @return
	 * @throws Exception
	 */
	public ParamMap setClaimCompleteProc(HashMap<String, Object> exchangeCompleteVo, HashMap<String, String> apiInfo) throws Exception;

	/**
	 * 교환클레임 완료 DB반영
	 * @param paClaimNo
	 * @param paOrderNo
	 * @param paShipNo
	 * @return
	 * @throws Exception
	 */
	public int updateExchangeEnd(HashMap<String, Object> exchangeCompleteVo) throws Exception;
	
	/**
	 * 교환배송완료 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectExchangeCompleteList() throws Exception;
}
