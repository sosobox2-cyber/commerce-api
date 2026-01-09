package com.cware.netshopping.patdeal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaTdealShipCostVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaTdealShipArea;
import com.cware.netshopping.patdeal.domain.Address;
import com.cware.netshopping.patdeal.domain.AreaFeeDetail;
import com.cware.netshopping.patdeal.domain.Areafees;
import com.cware.netshopping.patdeal.domain.DeliveryFee;
import com.cware.netshopping.patdeal.domain.Template;
import com.cware.netshopping.patdeal.domain.TemplateGroups;
import com.cware.netshopping.patdeal.domain.Warehouses;
import com.cware.netshopping.patdeal.repository.PaTdealCommonMapper;
import com.cware.netshopping.patdeal.util.PaTdealApiRequest;
import com.cware.netshopping.patdeal.util.PaTdealConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaTdealSellerService {
		
	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	PaTdealCommonMapper paTdealCommonMapper;
	
	@Autowired
	private PaTdealConnectUtil paTdealConnectUtil;
	
	@Autowired
	private PaTdealResultService paTdealResultService;

	@Autowired
	TransLogService transLogService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());


	/**
	 * 티딜 입출고 주소 생성하기
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerEntpSlip(String entpCode, String entpManSeq, String paCode, String procId,
			long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + entpManSeq + paCode);
		serviceLog.setTransType(TransType.SELLER.name());
		serviceLog.setServiceName("IF_PATDEALAPI_00_007");
		serviceLog.setServiceNote("[API]티딜-입출고 주소 생성하기");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 입출고 주소 생성
		ResponseMsg result = callRegisterEntpSlip(entpCode, entpManSeq, paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);
		
		return result;

	}
	
	
	/**
	 * 티딜 입출고 주소 수정하기
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg updateEntpSlip(String entpCode, String entpManSeq, String paCode, String procId,
			long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + entpManSeq + paCode);
		serviceLog.setTransType(TransType.SELLER.name());
		serviceLog.setServiceName("IF_PATDEALAPI_00_008");
		serviceLog.setServiceNote("[API]티딜-입출고 주소 수정하기");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 입출고 주소 수정
		ResponseMsg result = callUpdateEntpSlip(entpCode, entpManSeq, paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	
	/**
	 * 티딜 배송비 템플릿 생성 (추가배송비 설정 생성 + 배송비 템플릿 그룹 생성)
	 * 
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerShipCost(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + shipManSeq + shipCostCode + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PATDEALAPI_00_014");
		serviceLog.setServiceNote("[API]티딜-배송비 템플릿 생성");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 지역별 추가배송비 설정 생성
		serviceLog.setServiceName("IF_PATDEALAPI_00_010");
		serviceLog.setServiceNote("[API]티딜-지역별 추가배송비 설정 생성");
		ResponseMsg result = callRegisterAreaFees(entpCode, shipManSeq, returnManSeq, shipCostCode,
				paCode, procId, serviceLog);
		
		// 배송비 템플릿 그룹 생성
		serviceLog.setServiceName("IF_PATDEALAPI_00_012");
		serviceLog.setServiceNote("[API]티딜-배송비 템플릿 그룹 생성");
		if ("200".equals(result.getCode()) || "404".equals(result.getCode())) {
			result = callRegisterShipCostGroup(entpCode, shipManSeq, returnManSeq, shipCostCode,
					paCode, procId, serviceLog);
		}

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	
	/**
	 * 티딜 배송비 템플릿 수정 (추가배송비 설정 수정 + 배송비 템플릿 그룹 수정)
	 * 
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg updateShipCost(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + shipManSeq + shipCostCode + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PATDEALAPI_00_015");
		serviceLog.setServiceNote("[API]티딜-배송비 템플릿 수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 지역별 추가배송비 설정 수정
		serviceLog.setServiceName("IF_PATDEALAPI_00_011");
		serviceLog.setServiceNote("[API]티딜-지역별 추가배송비 설정 수정");
		ResponseMsg result = callUpdateAreaFees(entpCode, shipManSeq, returnManSeq, shipCostCode,
				paCode, procId, serviceLog);
		
		// 배송비 템플릿 그룹 수정
		serviceLog.setServiceName("IF_PATDEALAPI_00_013");
		serviceLog.setServiceNote("[API]티딜-배송비 템플릿 그룹 수정");
		if ("200".equals(result.getCode()) || "404".equals(result.getCode())) {
			result = callUpdateShipCostGroup(entpCode, shipManSeq, returnManSeq, shipCostCode,
					paCode, procId, serviceLog);
		}

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	
	/**
	 * 티딜 지역별 추가배송비 설정 생성
	 * 
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerAreaFees(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + shipManSeq + shipCostCode + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PATDEALAPI_00_010");
		serviceLog.setServiceNote("[API]티딜-지역별 추가배송비 설정 생성");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 지역별 추가배송비 설정 생성
		ResponseMsg result = callRegisterAreaFees(entpCode, shipManSeq, returnManSeq, shipCostCode,
				paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	
	/**
	 * 티딜 지역별 추가배송비 설정 수정
	 * 
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg updateAreaFees(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + shipManSeq + shipCostCode + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PATDEALAPI_00_011");
		serviceLog.setServiceNote("[API]티딜-지역별 추가배송비 설정 수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 지역별 추가배송비 설정 수정
		ResponseMsg result = callUpdateAreaFees(entpCode, shipManSeq, returnManSeq, shipCostCode,
				paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	
	/**
	 * 티딜 배송비 템플릿 그룹 생성
	 * 
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerShipCostGroup(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + shipManSeq + shipCostCode + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PATDEALAPI_00_012");
		serviceLog.setServiceNote("[API]티딜-배송비 템플릿 그룹 생성");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송비 템플릿 그룹 생성
		ResponseMsg result = callRegisterShipCostGroup(entpCode, shipManSeq, returnManSeq, shipCostCode,
				paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	
	/**
	 * 티딜 배송비 템플릿 그룹 수정
	 * 
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg updateShipCostGroup(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + shipManSeq + shipCostCode + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PATDEALAPI_00_013");
		serviceLog.setServiceNote("[API]티딜-배송비 템플릿 그룹 수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송비 템플릿 그룹 수정
		ResponseMsg result = callUpdateShipCostGroup(entpCode, shipManSeq, returnManSeq, shipCostCode,
				paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	
	private ResponseMsg callRegisterEntpSlip(String entpCode, String entpManSeq, String paCode, String procId, PaTransService serviceLog) {
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		Warehouses warehouses = new Warehouses();
		
		ResponseMsg result = new ResponseMsg("", "");
		Map<String, Object> warehousesMap	= new HashMap<String, Object>();
		String dateTime = "";
		
		try {
			log.info("===== 입출고 주소 생성 서비스 Start - {}-{} =====", entpCode, entpManSeq);
		
			dateTime = systemService.getSysdatetimeToString();
		
			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaCode(paCode);
			
			Map<String, Object> entpUserMap = paTdealCommonMapper.getSlipInsert(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 티딜 입출고 주소 전문생성
			warehouses = createAddress(paEntpSlip, entpUserMap);
			
			// Body 세팅
			ParamMap apiDataObject = new ParamMap();
			apiDataObject.put("body", warehouses);
			
			log.info("입출고 주소 생성 API 호출 {}-{}", entpCode, entpManSeq);
			warehousesMap = paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);
			
			paEntpSlip.setPaAddrSeq(String.valueOf(warehousesMap.get("warehouseNo")));
			paEntpSlip.setPaAddrGb(String.valueOf(entpUserMap.get("ENTP_MAN_GB")));
			paEntpSlip.setTransTargetYn("0");
			paEntpSlip.setInsertId(procId);
			paEntpSlip.setModifyId(procId);
			paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			paTdealResultService.savePaEntpSlip(paEntpSlip);
			
			result.setCode("200");
			result.setMessage(PaTdealApiRequest.API_SUCCESS_CODE);
			
			
		} catch (TransApiException ex){
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
		} finally {
			log.info("===== 입출고 주소 생성 서비스 End - {}-{} =====", entpCode, entpManSeq);
		}
			
		return result;
	}
	
	
	private ResponseMsg callUpdateEntpSlip(String entpCode, String entpManSeq, String paCode, String procId, PaTransService serviceLog) {
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		Warehouses warehouses = new Warehouses();

		ResponseMsg result = new ResponseMsg("", "");
		String dateTime = "";

		try {
			log.info("===== 입출고 주소 수정 서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaCode(paCode);

			Map<String, Object> entpUserMap = paTdealCommonMapper.getSlipUpdate(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 티딜 입출고 주소 전문생성
			warehouses = createAddress(paEntpSlip, entpUserMap);
			
			// Body 세팅
			ParamMap apiDataObject = new ParamMap();
			apiDataObject.put("body", warehouses);
			
			pathParameters = (String)entpUserMap.get("PA_ADDR_SEQ");

			log.info("입출고 주소 수정 API 호출 {}-{}", entpCode, entpManSeq);
			paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);

			paEntpSlip.setPaAddrSeq((String)entpUserMap.get("PA_ADDR_SEQ"));
			paEntpSlip.setTransTargetYn("0");
			paEntpSlip.setModifyId(procId);
			paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

			paTdealResultService.updatePaEntpSlip(paEntpSlip);
			
			result.setCode("200");
			result.setMessage(PaTdealApiRequest.API_SUCCESS_CODE);
			
			
		} catch (TransApiException ex){
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
		} finally {
			log.info("===== 입출고 주소 수정 서비스 End - {}-{} =====", entpCode, entpManSeq);
		}
		
		return result;
	}
	
	
	private Warehouses createAddress(PaEntpSlip paEntpSlip, Map<String, Object> entpUserMap) throws Exception {

		Warehouses warehouses = new Warehouses();
		
		warehouses.setName(paEntpSlip.getPaCode() + "-" + entpUserMap.get("ENTP_CODE") + "-" + entpUserMap.get("ENTP_MAN_SEQ"));
		warehouses.setUsesSubstitutionText(false);
		warehouses.setDefaultReleaseWarehouse(false);
		warehouses.setDefaultReturnWarehouse(false);
		
		Address address = new Address();
		address.setAddress((String)entpUserMap.get("STD_ROAD_POST_ADDR1"));
		address.setDetailAddress((String)entpUserMap.get("STD_ROAD_POST_ADDR2"));
		address.setZipCd((String)entpUserMap.get("POST_NO"));
		address.setJibunAddress((String)entpUserMap.get("STD_POST_ADDR1"));
		address.setAddressStr((String)entpUserMap.get("STD_POST_ADDR2"));
		
		warehouses.setAddress(address);
		
		return warehouses;
	}
	
	private ResponseMsg callRegisterAreaFees(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, PaTransService serviceLog) {
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		Areafees areafees = new Areafees();
		
		ResponseMsg result = new ResponseMsg("", "");
		Map<String, Object> areafeesMap	= new HashMap<String, Object>();
		String dateTime = "";
		
		try {
			log.info("===== 지역별 추가배송비 설정 생성 서비스 Start - {}-{} =====", entpCode, shipCostCode);
		
			dateTime = systemService.getSysdatetimeToString();
		
			PaTdealShipCostVO shipCost = new PaTdealShipCostVO();
			shipCost.setPaCode(paCode);
			shipCost.setEntpCode(entpCode);
			shipCost.setShipManSeq(shipManSeq);
			shipCost.setReturnManSeq(returnManSeq);
			shipCost.setShipCostCode(shipCostCode);
			
			shipCost = paTdealCommonMapper.getAreaFeesRegisterTarget(shipCost);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}
			
			List<PaTdealShipArea> shipAreaList = paTdealCommonMapper.getAreaFees(shipCost);
			
			// 지역별 추가배송비 설정 전문생성
			areafees = createAreaFees(shipCost, shipAreaList);
			
			// Body 세팅
			ParamMap apiDataObject = new ParamMap();
			apiDataObject.put("body", areafees);
			
			log.info("지역별 추가배송비 설정 생성 API 호출 {}-{}", entpCode, shipCostCode);
			areafeesMap = paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);
			
			shipCost.setAreaFeeNo(String.valueOf(areafeesMap.get("areaFeeNo")));
			shipCost.setTransTargetYn("1");
			shipCost.setModifyId(procId);
			shipCost.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			paTdealResultService.updatePaTdealShipCost(shipCost);
			
			result.setCode("200");
			result.setMessage(PaTdealApiRequest.API_SUCCESS_CODE);
			
			
		} catch (TransApiException ex){
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
		} finally {
			log.info("===== 지역별 추가배송비 설정 생성 서비스 End - {}-{} =====", entpCode, shipCostCode);
		}
			
		return result;
	}
	
	private Areafees createAreaFees(PaTdealShipCostVO shipCost, List<PaTdealShipArea> shipAreaList) throws Exception {
		
		Areafees areafees = new Areafees();
		List<AreaFeeDetail> AreaFeeDetailList = new ArrayList<AreaFeeDetail>();
		double islandAddCost = 0;
		double jejuAddCost = 0;
		
		areafees.setName(shipCost.getPaCode() + "-" + shipCost.getEntpCode() + "-" + 
							shipCost.getShipManSeq() + shipCost.getReturnManSeq() + "-" + shipCost.getShipCostCode());
		areafees.setCountryCd("KR");

		// 추가배송비 계산
		if (shipCost.getIslandCost() > shipCost.getOrdCost()) {
			islandAddCost = shipCost.getIslandCost() - shipCost.getOrdCost();
		}
		if (shipCost.getJejuCost() > shipCost.getOrdCost()) {
			jejuAddCost = shipCost.getJejuCost() - shipCost.getOrdCost();
		}
		
		for(PaTdealShipArea shipArea : shipAreaList) {
			AreaFeeDetail areaFeeDetail = new AreaFeeDetail();
			
			if ("20".equals(shipArea.getAreaGb())) { // 도서산간 추가배송비
				areaFeeDetail.setAreaNo(shipArea.getAreaNo());
				areaFeeDetail.setExtraDeliveryAmt(islandAddCost);
			} else if ("30".equals(shipArea.getAreaGb())) { // 제주 추가배송비
				areaFeeDetail.setAreaNo(shipArea.getAreaNo());
				areaFeeDetail.setExtraDeliveryAmt(jejuAddCost);
			}
			
			if (areaFeeDetail.getAreaNo() != null) {
				AreaFeeDetailList.add(areaFeeDetail);
			}
		}
		areafees.setDetailList(AreaFeeDetailList);
		
		return areafees;
	}
	
	private ResponseMsg callRegisterShipCostGroup(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, PaTransService serviceLog) {
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		TemplateGroups templateGroups = new TemplateGroups();
		
		ResponseMsg result = new ResponseMsg("", "");
		Map<String, Object> templateGroupsMap	= new HashMap<String, Object>();
		String dateTime = "";
		
		try {
			log.info("===== 배송비 템플릿 그룹 생성 서비스 Start - {}-{} =====", entpCode, shipCostCode);
		
			dateTime = systemService.getSysdatetimeToString();
		
			PaTdealShipCostVO shipCost = new PaTdealShipCostVO();
			shipCost.setPaCode(paCode);
			shipCost.setEntpCode(entpCode);
			shipCost.setShipManSeq(shipManSeq);
			shipCost.setReturnManSeq(returnManSeq);
			shipCost.setShipCostCode(shipCostCode);
			shipCost.setRegisterYn("1");
			
			shipCost = paTdealCommonMapper.selectShipCostGroupTarget(shipCost);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 배송비 템플릿 그룹 전문생성
			templateGroups = createShipCostGroup(shipCost);
			
			// Body 세팅
			ParamMap apiDataObject = new ParamMap();
			apiDataObject.put("body", templateGroups);
			
			log.info("배송비 템플릿 그룹 생성 API 호출 {}-{}", entpCode, shipCostCode);
			templateGroupsMap = paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);

			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			templateGroups = objectMapper.convertValue(templateGroupsMap, TemplateGroups.class);
			
			shipCost.setTemplateGroupNo(templateGroups.getTemplateGroupNo());
			shipCost.setTemplateNo(templateGroups.getTemplateList().get(0).getTemplateNo());
			shipCost.setTransTargetYn("0");
			shipCost.setModifyId(procId);
			shipCost.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			paTdealResultService.updatePaTdealShipCost(shipCost);
			
			result.setCode("200");
			result.setMessage(PaTdealApiRequest.API_SUCCESS_CODE);
			
			
		} catch (TransApiException ex){
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
		} finally {
			log.info("===== 배송비 템플릿 그룹 생성 서비스 End - {}-{} =====", entpCode, shipCostCode);
		}
			
		return result;
	}
	
	private TemplateGroups createShipCostGroup(PaTdealShipCostVO shipCost) throws Exception {
		
		TemplateGroups templateGroups = new TemplateGroups();
		
		templateGroups.setName(shipCost.getPaCode() + "-" + shipCost.getEntpCode() + "-" + 
							shipCost.getShipManSeq() + shipCost.getReturnManSeq() + "-" + shipCost.getShipCostCode());
		templateGroups.setGroupDeliveryAmtType("MAXIMUM_SELECTED"); // 묶음배송 사용 x (최대부과 default 세팅)
		templateGroups.setPrepaid(true);
		
		templateGroups.setUsesAreaFee(true);
		templateGroups.setAreaFeeNo(shipCost.getAreaFeeNo()); // 추가배송비코드
		
		List<Template> templateList = new ArrayList<Template>();
		Template template = new Template();
		template.setName(shipCost.getPaCode() + "-" + shipCost.getEntpCode() + "-" + 
							shipCost.getShipManSeq() + shipCost.getReturnManSeq() + "-" + shipCost.getShipCostCode());
		template.setDeliveryType("PARCEL_DELIVERY");
		template.setReleaseWarehouseNo(shipCost.getPaShipManSeq());
		template.setReturnWarehouseNo(shipCost.getPaReturnManSeq());
		template.setDefaultYn(true);;
		
		DeliveryFee deliveryFee = new DeliveryFee();
		
		switch (shipCost.getShipCostCode().substring(0, 2)) {
		case "FR": // 무료
			deliveryFee.setDeliveryConditionType("FREE");
			deliveryFee.setDeliveryAmt(shipCost.getOrdCost());
			deliveryFee.setReturnDeliveryAmt(shipCost.getReturnCost() / 2);
			break;
		case "CN":
		case "PL": // 조건부
			deliveryFee.setDeliveryConditionType("CONDITIONAL");
			deliveryFee.setDeliveryAmt(shipCost.getOrdCost());
			deliveryFee.setReturnDeliveryAmt(shipCost.getReturnCost());
			deliveryFee.setCriteria(shipCost.getShipCostBaseAmt());
			deliveryFee.setRemoteAreaFeeConditionCheck(false);
			break;
		default: // 상품별
			deliveryFee.setDeliveryConditionType("FIXED_FEE");
			deliveryFee.setDeliveryAmt(shipCost.getOrdCost());
			deliveryFee.setReturnDeliveryAmt(shipCost.getReturnCost());
			break;
		}
		
		template.setDeliveryFee(deliveryFee);
		
		if (shipCost.getTemplateNo() != null) { // 수정 시에만 세팅
			template.setTemplateNo(shipCost.getTemplateNo());
		}
		
		templateList.add(template);
		
		if (shipCost.getTemplateNo() != null) {
			templateGroups.setModifyTemplateList(templateList);
		} else {
			templateGroups.setTemplateList(templateList);
		}
		
		return templateGroups;
	}
	
	
	private ResponseMsg callUpdateAreaFees(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, PaTransService serviceLog) {
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		Areafees areafees = new Areafees();
		
		ResponseMsg result = new ResponseMsg("", "");
		String dateTime = "";
		
		try {
			log.info("===== 지역별 추가배송비 설정 수정 서비스 Start - {}-{} =====", entpCode, shipCostCode);
		
			dateTime = systemService.getSysdatetimeToString();
		
			PaTdealShipCostVO shipCost = new PaTdealShipCostVO();
			shipCost.setPaCode(paCode);
			shipCost.setEntpCode(entpCode);
			shipCost.setShipManSeq(shipManSeq);
			shipCost.setReturnManSeq(returnManSeq);
			shipCost.setShipCostCode(shipCostCode);
			
			shipCost = paTdealCommonMapper.getAreaFeesUpdateTarget(shipCost);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}
			
			List<PaTdealShipArea> shipAreaList = paTdealCommonMapper.getAreaFees(shipCost);
			
			// 지역별 추가배송비 수정 전문생성
			areafees = createAreaFees(shipCost, shipAreaList);
			
			// Body 세팅
			ParamMap apiDataObject = new ParamMap();
			apiDataObject.put("body", areafees);
			
			pathParameters = shipCost.getAreaFeeNo();
			
			log.info("지역별 추가배송비 설정 수정 API 호출 {}-{}", entpCode, shipCostCode);
			paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);
			
			shipCost.setTransTargetYn("1");
			shipCost.setModifyId(procId);
			shipCost.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			paTdealResultService.updatePaTdealShipCost(shipCost);
			
			result.setCode("200");
			result.setMessage(PaTdealApiRequest.API_SUCCESS_CODE);
			
			
		} catch (TransApiException ex){
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
		} finally {
			log.info("===== 지역별 추가배송비 설정 수정 서비스 End - {}-{} =====", entpCode, shipCostCode);
		}
			
		return result;
	}
	
	
	private ResponseMsg callUpdateShipCostGroup(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, PaTransService serviceLog) {
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		TemplateGroups templateGroups = new TemplateGroups();
		
		ResponseMsg result = new ResponseMsg("", "");
		String dateTime = "";
		
		try {
			log.info("===== 배송비 템플릿 그룹 수정 서비스 Start - {}-{} =====", entpCode, shipCostCode);
		
			dateTime = systemService.getSysdatetimeToString();
		
			PaTdealShipCostVO shipCost = new PaTdealShipCostVO();
			shipCost.setPaCode(paCode);
			shipCost.setEntpCode(entpCode);
			shipCost.setShipManSeq(shipManSeq);
			shipCost.setReturnManSeq(returnManSeq);
			shipCost.setShipCostCode(shipCostCode);
			shipCost.setRegisterYn("0");
			
			shipCost = paTdealCommonMapper.selectShipCostGroupTarget(shipCost);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 배송비 템플릿 그룹 전문생성
			templateGroups = createShipCostGroup(shipCost);
			
			// Body 세팅
			ParamMap apiDataObject = new ParamMap();
			apiDataObject.put("body", templateGroups);
			
			pathParameters = shipCost.getTemplateGroupNo();
			
			log.info("배송비 템플릿 그룹 수정 API 호출 {}-{}", entpCode, shipCostCode);
			paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);
			
			shipCost.setTransTargetYn("0");
			shipCost.setModifyId(procId);
			shipCost.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			paTdealResultService.updatePaTdealShipCost(shipCost);
			
			result.setCode("200");
			result.setMessage(PaTdealApiRequest.API_SUCCESS_CODE);
			
			
		} catch (TransApiException ex){
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
		} finally {
			log.info("===== 배송비 템플릿 그룹 수정 서비스 End - {}-{} =====", entpCode, shipCostCode);
		}
			
		return result;
	}

}
