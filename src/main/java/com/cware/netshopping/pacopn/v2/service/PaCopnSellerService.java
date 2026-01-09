package com.cware.netshopping.pacopn.v2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.pacopn.common.repository.PaCopnCommonDAO;
import com.cware.netshopping.pacopn.common.service.PaCopnCommonService;
import com.cware.netshopping.pacopn.v2.domain.OutboundShippingPlace;
import com.cware.netshopping.pacopn.v2.domain.PlaceAddress;

@Service
public class PaCopnSellerService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("pacopn.common.paCopnCommonService")
	private PaCopnCommonService paCopnCommonService;

	@Autowired
	@Qualifier("pacopn.common.paCopnCommonDAO")
	private PaCopnCommonDAO paCopnCommonDAO;

	@Autowired
	PaCopnSellerApiService sellerApiService;

	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 쿠팡 출고지 등록
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
		serviceLog.setServiceNote("[API]쿠팡-출고지등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.COUPANG.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 출고지생성
		ResponseMsg result = callRegisterEntpSlip(entpCode, entpManSeq, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 쿠팡 출고지 수정
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
		serviceLog.setServiceNote("[API]쿠팡-출고지수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.COUPANG.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 출고지수정
		ResponseMsg result = callUpdateEntpSlip(entpCode, entpManSeq, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}
	
	private ResponseMsg callRegisterEntpSlip(String entpCode, String entpManSeq, String paCode, String procId,
			long transServiceNo) {
		
		ParamMap paramMap = new ParamMap();
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 출고지등록서비스 Start - {}-{} {}=====", entpCode, entpManSeq, paCode);

			dateTime = systemService.getSysdatetimeToString();

			paramMap.put("entpCode", entpCode);
			paramMap.put("entpManSeq", entpManSeq);
			paramMap.put("paCode",paCode);

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(paramMap.getString("entpCode"));
			paEntpSlip.setEntpManSeq(paramMap.getString("entpManSeq"));
			paEntpSlip.setPaAddrGb("30");//출고
			paEntpSlip.setPaCode(paCode);

			Map<String, String> entpUserMap = paCopnCommonService.selectEntpShipInsertList(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 쿠팡 출고지 전문생성
			OutboundShippingPlace shippingPlace = createShippingPlace(paEntpSlip, entpUserMap, transServiceNo);

			log.info("출고지등록 API 호출 {}-{} {}", entpCode, entpManSeq, paCode);
			String paAddrSeq = sellerApiService
					.registerShippingPlace(entpCode, entpManSeq, shippingPlace, paCode, transServiceNo);

			if (StringUtils.hasText(paAddrSeq)) {
				
				result.setCode("200");
				result.setMessage(PaCopnApiRequest.API_SUCCESS_CODE);

				paEntpSlip.setPaAddrSeq(paAddrSeq);
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setInsertId(procId);
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				

				rtnMsg = paCopnCommonService.savePaCopnEntpSlipTx(paEntpSlip);

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
			log.info("===== 출고지등록서비스 End - {}-{} {}=====", entpCode, entpManSeq, paCode);
		}
		return result;
	}

	private ResponseMsg callUpdateEntpSlip(String entpCode, String entpManSeq, String paCode, String procId,
			long transServiceNo) {
		
		ParamMap paramMap = new ParamMap();
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 출고지수정서비스 Start - {}-{} {}=====", entpCode, entpManSeq, paCode);

			dateTime = systemService.getSysdatetimeToString();

			paramMap.put("entpCode", entpCode);
			paramMap.put("entpManSeq", entpManSeq);
			paramMap.put("paCode",paCode);

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(paramMap.getString("entpCode"));
			paEntpSlip.setEntpManSeq(paramMap.getString("entpManSeq"));
			paEntpSlip.setPaAddrGb("30");//출고
			paEntpSlip.setPaCode(paCode);

			Map<String, String> entpUserMap = paCopnCommonDAO.selectEntpShipUpdate(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 쿠팡 출고지 전문생성
			OutboundShippingPlace shippingPlace = createShippingPlace(paEntpSlip, entpUserMap, transServiceNo);
			shippingPlace.setOutboundShippingPlaceCode(entpUserMap.get("PA_ADDR_SEQ"));

			log.info("출고지수정 API 호출 {}-{}", entpCode, entpManSeq);
			sellerApiService.updateShippingPlace(entpCode, entpManSeq, shippingPlace, paCode, transServiceNo);
				
			result.setCode("200");
			result.setMessage(PaCopnApiRequest.API_SUCCESS_CODE);

			paEntpSlip.setPaAddrSeq(shippingPlace.getOutboundShippingPlaceCode());
			paEntpSlip.setTransTargetYn("0");
			paEntpSlip.setModifyId(procId);
			paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

			rtnMsg = paCopnCommonService.updatePaCopnEntpSlipTx(paEntpSlip);

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
			log.info("===== 출고지수정서비스 End - {}-{} {}=====", entpCode, entpManSeq, paCode);
		}
		return result;
	}

	
	private OutboundShippingPlace createShippingPlace(PaEntpSlip paEntpSlip, Map<String, String> entpUserMap, long transServiceNo) throws Exception {

		OutboundShippingPlace shippingPlace = new OutboundShippingPlace();

		shippingPlace.setShippingPlaceName(entpUserMap.get("ENTP_CODE") + "-" + entpUserMap.get("ENTP_NAME") + "-" + entpUserMap.get("ENTP_MAN_SEQ"));
		shippingPlace.setGlobal(false);
		shippingPlace.setUsable(true);

		// 지번
		PlaceAddress jibun = new PlaceAddress();
		jibun.setAddressType("JIBUN");
		jibun.setCountryCode("KR");
		jibun.setCompanyContactNumber(entpUserMap.get("ENTP_MAN_TEL"));
		jibun.setPhoneNumber2(entpUserMap.get("ENTP_MAN_HP"));
		jibun.setReturnZipCode(entpUserMap.get("POST_NO"));
		jibun.setReturnAddress(entpUserMap.get("ADDRESS1"));
		jibun.setReturnAddressDetail(entpUserMap.get("ADDRESS2"));
		
		// 도로명
		PlaceAddress roadName = new PlaceAddress();
		BeanUtils.copyProperties(jibun, roadName);
		roadName.setAddressType("ROADNAME");

		List<PlaceAddress> placeAddresses = new ArrayList<PlaceAddress>();
		placeAddresses.add(jibun);
		placeAddresses.add(roadName);
		shippingPlace.setPlaceAddresses(placeAddresses);
		
		return shippingPlace;
	}

}
