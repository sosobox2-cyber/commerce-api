package com.cware.netshopping.pawemp.v2.service;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaWempEntpSlip;
import com.cware.netshopping.pawemp.common.repository.PaWempCommonDAO;
import com.cware.netshopping.pawemp.common.service.PaWempCommonService;
import com.cware.netshopping.pawemp.v2.domain.ShipPolicy;

@Service
public class PaWempSellerService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	@Qualifier("pawemp.common.paWempCommonService")
	private PaWempCommonService paWempCommonService;
	
	@Autowired
	@Qualifier("pawemp.common.paWempCommonDAO")
	private PaWempCommonDAO paWempCommonDAO;

	@Autowired
	PaWempSellerApiService sellerApiService;

	@Autowired
	TransLogService transLogService;

	// 위메프 안심번호 노출여부
	@Value("${partner.wemp.api.safety.display:Y}")
	String SAFET_NO_DISPALY_YN;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 위메프 배송정책 등록
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param noShipIsland
	 * @param installYn
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerShippingPolicy(String entpCode, String entpManSeq, String shipCostCode,
			String noShipIsland, String installYn, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + entpManSeq + shipCostCode + noShipIsland + installYn + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]위메프-배송정책등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.WEMP.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송정책등록
		ResponseMsg result = callRegisterShippingPolicy(entpCode, entpManSeq, shipCostCode, noShipIsland, installYn,
				paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}

	/**
	 * 위메프 배송정책 수정
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param noShipIsland
	 * @param installYn
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg updateShippingPolicy(String entpCode, String entpManSeq, String shipCostCode, String noShipIsland,
			String installYn, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + entpManSeq + shipCostCode + noShipIsland + installYn + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]위메프-배송정책수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.WEMP.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송정책수정
		ResponseMsg result = callUpdateShippingPolicy(entpCode, entpManSeq, shipCostCode, noShipIsland, installYn,
				paCode, procId, serviceLog.getTransServiceNo());
		
		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	private ResponseMsg callRegisterShippingPolicy(String entpCode, String entpManSeq, String shipCostCode,
			String noShipIsland, String installYn, String paCode, String procId, long transServiceNo) {

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 배송정책등록서비스 Start - {}-{} =====", entpCode, shipCostCode);

			PaWempEntpSlip paEntpSlip = new PaWempEntpSlip();
			paEntpSlip.setPaCode(paCode);
		    paEntpSlip.setEntpCode(entpCode);
		    paEntpSlip.setEntpManSeq(entpManSeq);
		    paEntpSlip.setShipCostCode(shipCostCode);
		    paEntpSlip.setNoShipIsland(noShipIsland);
		    paEntpSlip.setInstallYn(installYn);
		    paEntpSlip.setPaAddrGb("20");// 회수지

			Map<String, Object> entpUserMap = paWempCommonService.selectEntpShipInsertList(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 

			// 위메프 배송정책 전문생성
			ShipPolicy shipPolicy = createShippingPolicy(paEntpSlip, entpUserMap, transServiceNo);
			
			log.info("배송정책등록 API 호출 {}-{} {} {}", entpCode, shipCostCode, paCode);
			String shipPolicyNo = sellerApiService.registerShippingPolicy(entpCode, entpManSeq, shipCostCode, noShipIsland, installYn, shipPolicy, paCode, transServiceNo);

			String dateTime = systemService.getSysdatetimeToString();
			paEntpSlip.setPaShipPolicyNo(shipPolicyNo);
			paEntpSlip.setTransTargetYn("0");
			paEntpSlip.setInsertId(procId);
			paEntpSlip.setModifyId(procId);
			paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			String rtnMsg = paWempCommonService.savePaWempEntpSlipTx(paEntpSlip);
			if(!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
				result.setCode("500");
				result.setMessage(rtnMsg);
			} else {
				result.setCode("200");
				result.setMessage(PaWempApiRequest.API_SUCCESS_CODE);
			}

		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 배송정책등록서비스 End - {}-{} {} {} =====", entpCode, shipCostCode, paCode);
		}
		return result;
	}

	private ResponseMsg callUpdateShippingPolicy(String entpCode, String entpManSeq, String shipCostCode,
			String noShipIsland, String installYn, String paCode, String procId, long transServiceNo) {

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 배송정책수정서비스 Start - {}-{} =====", entpCode, shipCostCode);

			PaWempEntpSlip paEntpSlip = new PaWempEntpSlip();
			paEntpSlip.setPaCode(paCode);
		    paEntpSlip.setEntpCode(entpCode);
		    paEntpSlip.setEntpManSeq(entpManSeq);
		    paEntpSlip.setShipCostCode(shipCostCode);
		    paEntpSlip.setNoShipIsland(noShipIsland);
		    paEntpSlip.setInstallYn(installYn);
		    paEntpSlip.setPaAddrGb("20");// 회수지

			Map<String, Object> entpUserMap = paWempCommonDAO.selectEntpShipUpdate(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 

			// 위메프 배송정책 전문생성
			ShipPolicy shipPolicy = createShippingPolicy(paEntpSlip, entpUserMap, transServiceNo);
			
			log.info("배송정책수정 API 호출 {}-{} {} {}", entpCode, shipCostCode, paCode);
			sellerApiService.updateShippingPolicy(entpCode, entpManSeq, shipCostCode, noShipIsland, installYn, shipPolicy, paCode, transServiceNo);

			String dateTime = systemService.getSysdatetimeToString();
			paEntpSlip.setTransTargetYn("0");
			paEntpSlip.setModifyId(procId);
			paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			String rtnMsg = paWempCommonService.updatePaWempEntpSlipTx(paEntpSlip);
			if(!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
				result.setCode("500");
				result.setMessage(rtnMsg);
			} else {
				result.setCode("200");
				result.setMessage(PaWempApiRequest.API_SUCCESS_CODE);
			}

		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 배송정책수정서비스 End - {}-{} {} {} =====", entpCode, shipCostCode, paCode);
		}
		return result;
	}

	private ShipPolicy createShippingPolicy(PaEntpSlip paEntpSlip, Map<String, Object> entpUserMap, long transServiceNo) throws Exception {

		ShipPolicy shipPolicy = new ShipPolicy();

		if (StringUtils.hasText((String)entpUserMap.get("PA_SHIP_POLICY_NO"))) {
			shipPolicy.setShipPolicyNo((String)entpUserMap.get("PA_SHIP_POLICY_NO"));
		}

		shipPolicy.setShipPolicyName((String) entpUserMap.get("ENTP_CODE") + "-"
				+ (String) entpUserMap.get("ENTP_MAN_SEQ") + "-" + paEntpSlip.getShipCostCode() + "-"
				+ paEntpSlip.getNoShipIsland() + "-" + paEntpSlip.getInstallYn() + "-"+ paEntpSlip.getPaCode());
		shipPolicy.setShipMethod("KP"); // 배송방법, 일반-택배배송
		
		//주문제작 여부
		if("1".equals(paEntpSlip.getInstallYn())){
			shipPolicy.setReleaseDay(10); // 출고기한
		} else {
			shipPolicy.setReleaseDay(3); // 출고기한
		}
		
		long shipFee = ((BigDecimal)entpUserMap.get("ORD_COST")).longValue();
		long returnCost = ((BigDecimal)entpUserMap.get("RETURN_COST")).longValue();

		// 도서산간 배송 여부
		if("0".equals(paEntpSlip.getNoShipIsland())){
			shipPolicy.setShipArea("WHOLE"); // 배송 가능 지역
			// 레거시 추가 배송비 계산 개선
			long jejuShipFee = ((BigDecimal)entpUserMap.get("JEJU_COST")).longValue() - shipFee;
			long islandShipFee = ((BigDecimal)entpUserMap.get("ISLAND_COST")).longValue() - shipFee;
			shipPolicy.setJejuShipFee(jejuShipFee > 0 ? jejuShipFee : 0);
			shipPolicy.setIslandMountainShipFee(islandShipFee > 0 ? islandShipFee : 0);
		} else {
			shipPolicy.setShipArea("NO_MOUNTAIN_ISLAND");
		}

		switch (paEntpSlip.getShipCostCode().substring(0, 2)) {
		case "FR": // 무료
			shipPolicy.setBundleKind("PRD");
			shipPolicy.setShipType("FREE");
			shipPolicy.setClaimShipFee(returnCost > 0 ? returnCost/2 : 0);
			break;
		case "CN":
		case "PL": // 조건부
			shipPolicy.setBundleKind("PRD");
			shipPolicy.setShipType("COND");
			shipPolicy.setShipFee(shipFee);
			shipPolicy.setClaimShipFee(returnCost);
			shipPolicy.setFreeCondition(((BigDecimal)entpUserMap.get("SHIP_COST_BASE_AMT")).longValue());
			break;
		default: // 상품별
			shipPolicy.setBundleKind("PRD");
			shipPolicy.setShipType("FIXED");
			shipPolicy.setShipFee(shipFee);
			shipPolicy.setClaimShipFee(returnCost);
			break;
		}

		// 출고지 주소(회수지 주소 사용)
		shipPolicy.setReleaseZipCode((String)entpUserMap.get("POST_NO")); 
		shipPolicy.setReleaseRoadAddress1((String)entpUserMap.get("ROAD_ADDR1"));  
		shipPolicy.setReleaseRoadAddress2((String)entpUserMap.get("ROAD_ADDR2"));  
		shipPolicy.setReleaseAddress1((String)entpUserMap.get("POST_ADDR1"));  
		shipPolicy.setReleaseAddress2((String)entpUserMap.get("POST_ADDR2"));  
		
		// 회수지 주소
		shipPolicy.setReturnZipCode(shipPolicy.getReleaseZipCode()); 
		shipPolicy.setReturnRoadAddress1(shipPolicy.getReleaseRoadAddress1());  
		shipPolicy.setReturnRoadAddress2(shipPolicy.getReleaseRoadAddress2());  
		shipPolicy.setReturnAddress1(shipPolicy.getReleaseAddress1());  
		shipPolicy.setReturnAddress2(shipPolicy.getReleaseAddress2());
		
		//안심번호 서비스 사용 노출여부(Y:노출, N:비노출), 개발 때는 비노출로 설정
		shipPolicy.setSafetyNoDisplayYn(SAFET_NO_DISPALY_YN);
		
		return shipPolicy;
	}
	
}
