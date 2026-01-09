package com.cware.netshopping.paqeen.service;

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
import com.cware.netshopping.domain.PaQeenShipCostVO;
import com.cware.netshopping.pacommon.v2.repository.PaCommonMapper;
import com.cware.netshopping.paqeen.domain.LogisticsPolicy;
import com.cware.netshopping.paqeen.domain.PolicyTarget;
import com.cware.netshopping.paqeen.domain.ReturnCostPolicy;
import com.cware.netshopping.paqeen.domain.ShipPolicyCreate;
import com.cware.netshopping.paqeen.domain.ShipPolicyUpdate;
import com.cware.netshopping.paqeen.domain.ShippingCostPolicy;
import com.cware.netshopping.paqeen.domain.ShippingPolicy;
import com.cware.netshopping.paqeen.repository.PaQeenCommonMapper;
import com.cware.netshopping.paqeen.util.PaQeenApiRequest;
import com.cware.netshopping.paqeen.util.PaQeenConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaQeenSellerService {
	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	TransLogService transLogService;
	
	@Autowired
	PaQeenConnectUtil paQeenConnectUtil;
	
	@Autowired
	PaQeenApiRequest paQeenApiRequest;
	
	@Autowired
	PaCommonMapper paCommonMapper;
	
	@Autowired
	PaQeenCommonMapper paQeenCommonMapper;
	
	@Autowired
	PaQeenResultService paQeenResultService;
	// 퀸잇 디폴트 타겟 코드
	String defaultCode = "999999";
		
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 퀸잇 배송비 정책 생성
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
		serviceLog.setServiceName("IF_PAQEENAPI_00_002");
		serviceLog.setServiceNote("[API]퀸잇-배송비 정책 등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.QEEN.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 배송비 정책 생성
		ResponseMsg result = callRegisterShipCost(entpCode, shipManSeq, returnManSeq, shipCostCode,
				paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	/**
	 * 퀸잇 배송비 정책 수정
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
		serviceLog.setServiceName("IF_PAQEENAPI_00_003");
		serviceLog.setServiceNote("[API]퀸잇-배송비 정책 변경");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.QEEN.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 배송비 정책 생성
		ResponseMsg result = callUpdateShipCost(entpCode, shipManSeq, returnManSeq, shipCostCode,
				paCode, procId, serviceLog);
		
		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);
		
		return result;
	}

	private ResponseMsg callRegisterShipCost(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, PaTransService serviceLog) {
		
		// VO 선언
		ShipPolicyCreate shipPolicyCreate = new ShipPolicyCreate();
		
		ResponseMsg result = new ResponseMsg("", "");
		Map<String, Object> responseMap	= new HashMap<String, Object>();
		String dateTime = "";
		
		try {
			ParamMap paramMap = new ParamMap();
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.QEEN.code());
			
			log.info("===== 배송비 정책 등록 서비스 Start - {}-{} =====", entpCode, shipCostCode);
		
			dateTime = systemService.getSysdatetimeToString();
		
			PaQeenShipCostVO shipCost = new PaQeenShipCostVO();
			shipCost.setPaCode(paCode);
			shipCost.setEntpCode(entpCode);
			shipCost.setShipManSeq(shipManSeq);
			shipCost.setReturnManSeq(returnManSeq);
			shipCost.setShipCostCode(shipCostCode);
			shipCost.setRegisterYn("1");
			
			shipCost = paQeenCommonMapper.selectQeenShipCost(shipCost);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 배송비 등록 전문생성
			shipPolicyCreate = createShipCost(shipCost);
			
			// Body 세팅
			// Body 세팅
			ParamMap apiRequestObject = new ParamMap();
			apiRequestObject.put("body", paQeenApiRequest.getBody(shipPolicyCreate));
			
			log.info("배송비 정책 등록 API 호출 {}-{}", entpCode, shipCostCode);
			responseMap = paQeenConnectUtil.callPaQeenAPI(paramMap, apiRequestObject, serviceLog, null);

			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			PolicyTarget policyTarget = objectMapper.convertValue(responseMap, PolicyTarget.class);
			
			if(policyTarget!=null) {
				
				shipCost.setPaShipcostId(Integer.toString(policyTarget.getId()));
				
				shipCost.setTransTargetYn("0");
				shipCost.setModifyId(procId);
				shipCost.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				
				paQeenResultService.updatePaQeenShipCost(shipCost);
				
				result.setCode("200");
				result.setMessage(PaQeenApiRequest.API_SUCCESS_CODE);
			}else {
				result.setCode("400");
				result.setMessage(PaQeenApiRequest.API_ERROR_CODE);
			}
			
			
		} catch (TransApiException ex){
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
		} finally {
			log.info("===== 배송비 정책 등록 서비스 End - {}-{} =====", entpCode, shipCostCode);
		}
			
		return result;
	}
	
	private ResponseMsg callUpdateShipCost(String entpCode, String shipManSeq, String returnManSeq,
			String shipCostCode, String paCode, String procId, PaTransService serviceLog) {
		
		// VO 선언
		ShipPolicyUpdate shipPolicyUpdate = new ShipPolicyUpdate();
		
		ResponseMsg result = new ResponseMsg("", "");
		Map<String, Object> responseMap	= new HashMap<String, Object>();
		String dateTime = "";
		
		try {
			ParamMap paramMap = new ParamMap();
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.QEEN.code());
			
			log.info("===== 배송비 정책 변경 서비스 Start - {}-{} =====", entpCode, shipCostCode);
			
			dateTime = systemService.getSysdatetimeToString();
			
			PaQeenShipCostVO shipCost = new PaQeenShipCostVO();
			shipCost.setPaCode(paCode);
			shipCost.setEntpCode(entpCode);
			shipCost.setShipManSeq(shipManSeq);
			shipCost.setReturnManSeq(returnManSeq);
			shipCost.setShipCostCode(shipCostCode);
			shipCost.setRegisterYn("0");
			
			shipCost = paQeenCommonMapper.selectQeenShipCost(shipCost);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 배송비 변경 전문생성
			shipPolicyUpdate = createShippingUpdate(shipCost);
			
			//Body 세팅
			ParamMap apiRequestObject = new ParamMap();
			apiRequestObject.put("body", paQeenApiRequest.getBody(shipPolicyUpdate));
			apiRequestObject.put("urlParameter", shipCost.getPaShipcostId());
			
			log.info("배송비 정책 변경 API 호출 {}-{}", entpCode, shipCostCode);
			responseMap = paQeenConnectUtil.callPaQeenAPI(paramMap, apiRequestObject, serviceLog, null);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			PolicyTarget policyTarget = objectMapper.convertValue(responseMap, PolicyTarget.class);
			
			
			if(policyTarget!=null) {
				shipCost.setTransTargetYn("0");
				shipCost.setModifyId(procId);
				shipCost.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				
				paQeenResultService.updatePaQeenShipCost(shipCost);
				
				result.setCode("200");
				result.setMessage(PaQeenApiRequest.API_SUCCESS_CODE);
			}else {
				result.setCode("400");
				result.setMessage(PaQeenApiRequest.API_ERROR_CODE);
			}
			
		} catch (TransApiException ex){
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
		} finally {
			log.info("===== 배송비 정책 변경 서비스 End - {}-{} =====", entpCode, shipCostCode);
		}
		
		return result;
	}
	

	private ShipPolicyCreate createShipCost(PaQeenShipCostVO shipCost) throws Exception {
		
		ShipPolicyCreate shipPolicy = new ShipPolicyCreate();
		
		shipPolicy.setName(shipCost.getPaShipcostName());
		
		shipPolicy.setAssorted(false);//이 필드의 값이 true인 경우, 동일 배송 정책(policy target)에 해당하면 합배송/반품이 된다
		
		List<String> brandCodes = new ArrayList<String>();
		shipPolicy.setBrandCodes(brandCodes);
		
		ShippingPolicy shippingPolicy = createShippingPolicy(shipCost);
		
		shipPolicy.setShippingPolicy(shippingPolicy);
		
		//ShippingPolicy 세팅 e
		
		return shipPolicy;
	}
	
	private ShipPolicyUpdate createShippingUpdate(PaQeenShipCostVO shipCost) throws Exception {
		
		ShipPolicyUpdate shipPolicyUpdate = new ShipPolicyUpdate();
		
		ShippingPolicy shippingPolicy = createShippingPolicy(shipCost);
		
		shipPolicyUpdate.setShippingPolicy(shippingPolicy);
		
		return shipPolicyUpdate;
	}
	
	private ShippingPolicy createShippingPolicy(PaQeenShipCostVO shipCost) throws Exception {
		
		ShippingPolicy shippingPolicy = new ShippingPolicy();
		
		// 배송비 세팅
		ShippingCostPolicy shippingCostPolicy = new ShippingCostPolicy(); ;
		shippingCostPolicy.setDefaultCost((long) shipCost.getOrdCost());
		shippingCostPolicy.setJejuIsland((long) shipCost.getJejuCost());
		shippingCostPolicy.setBackCountry((long) shipCost.getIslandCost());
		shippingCostPolicy.setRequiredAmountForFree((long)0);
		
		//반품비 세팅
		ReturnCostPolicy returnCostPolicy = new ReturnCostPolicy();
		returnCostPolicy.setDefaultCost((long) shipCost.getReturnCost());
		returnCostPolicy.setJejuIsland((long) shipCost.getJejuReturnCost());
		returnCostPolicy.setBackCountry((long) shipCost.getIslandReturnCost());
		
		// 출고지/회수지 세팅
		LogisticsPolicy logisticsPolicy = new LogisticsPolicy();
		logisticsPolicy.setShippingPlace(shipCost.getShipAddress());
		logisticsPolicy.setReturnPlace(shipCost.getReturnAddress());
		logisticsPolicy.setVendor("CJ대한통운");
		
		shippingPolicy.setShippingCostPolicy(shippingCostPolicy);
		shippingPolicy.setReturnCostPolicy(returnCostPolicy);
		shippingPolicy.setLogisticsPolicy(logisticsPolicy);
		
		return shippingPolicy;
	}
	
	
}
