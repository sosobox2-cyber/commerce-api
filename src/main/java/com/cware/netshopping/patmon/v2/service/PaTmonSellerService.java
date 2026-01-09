package com.cware.netshopping.patmon.v2.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.cware.netshopping.patmon.common.repository.PaTmonCommonDAO;
import com.cware.netshopping.patmon.common.service.PaTmonCommonService;
import com.cware.netshopping.patmon.v2.domain.Address;
import com.cware.netshopping.patmon.v2.domain.DeliveryTemplate;
import com.cware.netshopping.patmon.v2.domain.PaTmonShipCost;
import com.cware.netshopping.patmon.v2.domain.PartnerAddress;
import com.cware.netshopping.patmon.v2.repository.PaTmonShipCostMapper;

@Service
public class PaTmonSellerService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	@Qualifier("patmon.common.paTmonCommonService")
	private PaTmonCommonService paTmonCommonService;
	
	@Autowired
	@Qualifier("patmon.common.paTmonCommonDAO")
	private PaTmonCommonDAO paTmonCommonDAO;

	@Autowired
	PaTmonSellerApiService sellerApiService;

	@Autowired
	PaTmonShipCostMapper shipCostMapper;
	
	@Autowired
	TransLogService transLogService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());


	/**
	 * 티몬 배송지 등록
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
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]티몬-배송지등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TMON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송지 등록
		ResponseMsg result = callRegisterEntpSlip(entpCode, entpManSeq, paCode, procId,
				serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 티몬 배송지 수정
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
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]티몬-배송지수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TMON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송지 수정
		ResponseMsg result = callUpdateEntpSlip(entpCode, entpManSeq, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 티몬 배송템플릿 등록
	 * 
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param productType
	 * @param shipCostCode
	 * @param applyDate
	 * @param noShipIsland
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerShippingPolicy(String entpCode, String shipManSeq, String returnManSeq,
			String productType, String shipCostCode, String applyDate,
			String noShipIsland, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + shipManSeq + shipCostCode + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]티몬-배송템플릿등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TMON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송템플릿등록
		ResponseMsg result = callRegisterShippingPolicy(entpCode, shipManSeq, returnManSeq, productType, shipCostCode,
				applyDate, noShipIsland, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	private ResponseMsg callRegisterEntpSlip(String entpCode, String entpManSeq, String paCode, String procId,
			long transServiceNo) {
		
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 배송지등록 서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaCode(paCode);

			Map<String, Object> entpUserMap = paTmonCommonService.selectSlipInsertList(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 티몬 배송지 전문생성
			PartnerAddress partenerAddress = createAddress(paEntpSlip, entpUserMap);

			log.info("배송지등록 API 호출 {}-{}", entpCode, entpManSeq);
			String paAddrSeq = sellerApiService
					.registerAddress(entpCode, entpManSeq, partenerAddress, paCode, transServiceNo);

			if (StringUtils.hasText(paAddrSeq)) {
				
				result.setCode("200");
				result.setMessage(PaTmonApiRequest.API_SUCCESS_CODE);

				paEntpSlip.setPaAddrSeq(paAddrSeq);
				paEntpSlip.setPaAddrGb((String)entpUserMap.get("ENTP_MAN_GB"));
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setInsertId(procId);
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

				rtnMsg = paTmonCommonService.savePaTmonEntpSlipTx(paEntpSlip);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + entpCode + entpManSeq);
				}

			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 배송지등록 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}
	
	private ResponseMsg callUpdateEntpSlip(String entpCode, String entpManSeq, String paCode, String procId,
			long transServiceNo) {
		
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 배송지수정서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaCode(paCode);

			Map<String, Object> entpUserMap = paTmonCommonDAO.selectEntpSlipUpdate(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 

			// 티몬 배송지 전문생성
			PartnerAddress partnerAddress = createAddress(paEntpSlip, entpUserMap);
			
			log.info("배송지수정 API 호출 {}-{}", entpCode, entpManSeq);
			String returnMsg = sellerApiService
					.updateAddress(entpCode, entpManSeq, partnerAddress, paCode, transServiceNo);
				
			result.setCode("200");
			result.setMessage(returnMsg);

			paEntpSlip.setPaAddrSeq((String)entpUserMap.get("PA_ADDR_SEQ"));
			paEntpSlip.setTransTargetYn("0");
			paEntpSlip.setInsertId(procId);
			paEntpSlip.setModifyId(procId);
			paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			rtnMsg = paTmonCommonService.updatePaTmonEntpSlipTx(paEntpSlip);

			if (!rtnMsg.equals("000000")) {
				result.setCode("500");
				result.setMessage(rtnMsg + " : " + entpCode + entpManSeq);
			}
			
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 배송지수정 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}

	private PartnerAddress createAddress(PaEntpSlip paEntpSlip, Map<String, Object> entpUserMap) throws Exception {

		PartnerAddress partnerAddress = new PartnerAddress();

		if (StringUtils.hasText((String)entpUserMap.get("PA_ADDR_SEQ"))) {
			partnerAddress.setNo((String)entpUserMap.get("PA_ADDR_SEQ"));
		}

		partnerAddress.setType("20".equals(entpUserMap.get("ENTP_MAN_GB")) ? "R" : "D");
		partnerAddress.setAddressName(paEntpSlip.getPaCode() + "-" + entpUserMap.get("ENTP_CODE") + "-" + entpUserMap.get("ENTP_MAN_SEQ") );
		
		Address address = new Address();
		address.setZipCode((String)entpUserMap.get("POST_NO"));
		address.setAddress((String)entpUserMap.get("ADDRESS1"));
		address.setAddressDetail((String)entpUserMap.get("ADDRESS2"));
		partnerAddress.setAddress(address);
		
		partnerAddress.setManagerName((String)entpUserMap.get("ENTP_MAN_NAME"));
		partnerAddress.setManagerPhone((String)entpUserMap.get("ENTP_MAN_TEL"));
		
		return partnerAddress;
	}

	private ResponseMsg callRegisterShippingPolicy(String entpCode, String shipManSeq, String returnManSeq,
			String productType, String shipCostCode, String applyDate,
			String noShipIsland, String paCode, String procId,
			long transServiceNo) {

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 배송템플릿등록서비스 Start - {}-{} =====", entpCode, shipCostCode);

			PaTmonShipCost shipCost = new PaTmonShipCost();

			shipCost.setPaCode(paCode);
			shipCost.setEntpCode(entpCode);
			shipCost.setShipManSeq(shipManSeq);
			shipCost.setReturnManSeq(returnManSeq);
			shipCost.setProductType(productType);
			shipCost.setShipCostCode(shipCostCode);
			shipCost.setApplyDate(new Timestamp(new SimpleDateFormat("yyyyMMddHHmmss").parse(applyDate).getTime()));
			shipCost.setNoShipIsland(noShipIsland);
			
			shipCost = shipCostMapper.getTransTarget(shipCost);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			if (shipCost.getPaShipPolicyNo() != null) {
				result.setCode("400");
				result.setMessage("이미 등록된 배송템플릿입니다.");
				return result;
			}

			// 티몬 배송템플릿 전문생성
			DeliveryTemplate deliveryTemplate = createShippingPolicy(shipCost);
			
			log.info("배송템플릿등록 API 호출 {} {} {}", entpCode, shipCostCode, paCode);
			String deliveryTempateNo = sellerApiService
					.registerShippingPolicy(entpCode, shipManSeq, shipCostCode, deliveryTemplate, paCode, transServiceNo);

			shipCost.setPaShipPolicyNo(deliveryTempateNo);
			shipCost.setModifyId(procId);
			
			if (shipCostMapper.updateCompleteTrans(shipCost) < 1) {
				result.setCode("500");
				result.setMessage("배송템플릿 등록결과 업데이트 실패");
			} else {
				result.setCode("200");
				result.setMessage(PaTmonApiRequest.API_SUCCESS_CODE);
			}

		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 배송템플릿등록서비스 End - {} {} {} =====", entpCode, shipCostCode, paCode);
		}
		return result;
	}


	private DeliveryTemplate createShippingPolicy(PaTmonShipCost shipCost) throws Exception {

		DeliveryTemplate deliveryTemplate = new DeliveryTemplate();
		
		// 티몬 가이드에는 없는 전문
		// template.put("userDirectlyReturnDeliverySelectable", "false");
		
		deliveryTemplate.setDeliveryTemplateName(shipCost.getPaCode() + shipCost.getEntpCode()
				+ shipCost.getProductType() + shipCost.getShipManSeq() + shipCost.getReturnManSeq()
				+ shipCost.getShipCostCode() + new SimpleDateFormat("yyyyMMddHHmmss").format(shipCost.getApplyDate())
				+ shipCost.getNoShipIsland());

		deliveryTemplate.setPartnerDeliveryAddressNo(shipCost.getPaShipManSeq());
		deliveryTemplate.setPartnerReturnAddressNo(shipCost.getPaReturnManSeq());
	
		deliveryTemplate.setBundledDeliveryAble(false);

		deliveryTemplate.setProductType(shipCost.getProductType());
		
		if("DP07".equals(shipCost.getProductType())) {
			//일반상품
			deliveryTemplate.setDeliveryType("ND");
		} else {
			//주문제작, 설치배송상품
			deliveryTemplate.setDeliveryType("ED");
		}

		switch (shipCost.getShipCostCode().substring(0, 2)) {
		case "FR": // 무료
			deliveryTemplate.setDeliveryFeePolicy("FREE");
			// 무료배송인 경우 반품비/2를 편도반품비로 적용
			deliveryTemplate.setDeliveryFee((int)shipCost.getReturnCost()/2);
			break;
		case "CN":
		case "PL": // 조건부
			deliveryTemplate.setDeliveryFeePolicy("CONDITION");
			deliveryTemplate.setDeliveryFee((int)shipCost.getOrdCost());
			deliveryTemplate.setDeliveryFeeFreePrice(shipCost.getShipCostBaseAmt());
			break;
		default: // 상품별
			deliveryTemplate.setDeliveryFeePolicy("PER");
			deliveryTemplate.setDeliveryFee((int)shipCost.getOrdCost());
			break;
		}
		
		if("1".equals(shipCost.getNoShipIsland())) {	// 도서산간 배송비 결제여부 
			deliveryTemplate.setLongDistanceDeliveryAvailable(false);
		}else {
			deliveryTemplate.setLongDistanceDeliveryAvailable(true);
			deliveryTemplate.setLongDistanceDeliveryPrepay(true);

			double addIslandCost = 0;
			double addJejuCost = 0;

			if (shipCost.getIslandCost() > shipCost.getOrdCost())
				addIslandCost = shipCost.getIslandCost() - shipCost.getOrdCost();

			if (shipCost.getJejuCost() > shipCost.getOrdCost())
				addJejuCost = shipCost.getJejuCost() - shipCost.getOrdCost();
			
			deliveryTemplate.setLongDistanceDeliveryFeeJeju((int)addJejuCost);
			deliveryTemplate.setLongDistanceDeliveryFeeExcludingJeju((int)addIslandCost);
		}
		
		return deliveryTemplate;
	}
		
}
