package com.cware.netshopping.pakakao.v2.service;

import java.math.BigDecimal;
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
import com.cware.netshopping.pakakao.common.repository.PaKakaoCommonDAO;
import com.cware.netshopping.pakakao.common.service.PaKakaoCommonService;
import com.cware.netshopping.pakakao.v2.domain.SellerAddress;

@Service
public class PaKakaoSellerService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	@Qualifier("pakakao.common.paKakaoCommonService")
	private PaKakaoCommonService paKakaoCommonService;
	
	@Autowired
	@Qualifier("pakakao.common.paKakaoCommonDAO")
	private PaKakaoCommonDAO paKakaoCommonDAO;

	@Autowired
	PaKakaoSellerApiService sellerApiService;

	@Autowired
	TransLogService transLogService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());


	/**
	 * 카카오 판매자주소록 등록
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
		serviceLog.setServiceNote("[API]카카오-판매자주소록등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.KAKAO.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 판매자주소록 등록
		ResponseMsg result = callRegisterEntpSlip(entpCode, entpManSeq, paCode, procId,
				serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 카카오 판매자주소록 수정
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
		serviceLog.setServiceNote("[API]카카오-판매자주소록수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.KAKAO.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 판매자주소록 수정
		ResponseMsg result = callUpdateEntpSlip(entpCode, entpManSeq, paCode, procId, serviceLog.getTransServiceNo());

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
			log.info("===== 판매자주소록등록 서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaCode(paCode);

			Map<String, Object> entpUserMap = paKakaoCommonDAO.selectEntpSlipInsert(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 카카오 판매자주소록 전문생성
			SellerAddress sellerAddress = createAddress(paEntpSlip, entpUserMap, transServiceNo);

			log.info("판매자주소록등록 API 호출 {}-{}", entpCode, entpManSeq);
			String paAddrSeq = sellerApiService
					.registerAddress(entpCode, entpManSeq, sellerAddress, paCode, transServiceNo);

			if (StringUtils.hasText(paAddrSeq)) {
				
				result.setCode("200");
				result.setMessage(PaKakaoApiRequest.API_SUCCESS_CODE);

				paEntpSlip.setPaAddrSeq(paAddrSeq);
				paEntpSlip.setPaAddrGb((String)entpUserMap.get("ENTP_MAN_GB"));
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setInsertId(procId);
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

				rtnMsg = paKakaoCommonService.savePaEntpSlipTx(paEntpSlip);

				if (!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
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
			log.info("===== 판매자주소록등록 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}
	
	private ResponseMsg callUpdateEntpSlip(String entpCode, String entpManSeq, String paCode, String procId,
			long transServiceNo) {
		
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 판매자주소록수정서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaCode(paCode);

			Map<String, Object> entpUserMap = paKakaoCommonDAO.selectEntpSlipUpdate(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 

			// 카카오 판매자주소록 전문생성
			SellerAddress sellerAddress = createAddress(paEntpSlip, entpUserMap, transServiceNo);
			
			log.info("판매자주소록수정 API 호출 {}-{}", entpCode, entpManSeq);
			String paAddrSeq = sellerApiService
					.updateAddress(entpCode, entpManSeq, sellerAddress, paCode, transServiceNo);

			if (StringUtils.hasText(paAddrSeq)) {
				
				result.setCode("200");
				result.setMessage(PaKakaoApiRequest.API_SUCCESS_CODE);
				paEntpSlip.setEntpManSeq(entpManSeq);
				paEntpSlip.setPaAddrSeq(paAddrSeq);
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

				rtnMsg = paKakaoCommonService.updatePaEntpSlipTx(paEntpSlip);

				if (!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
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
			log.info("===== 판매자주소록수정 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}

	private SellerAddress createAddress(PaEntpSlip paEntpSlip, Map<String, Object> entpUserMap, long transServiceNo)
			throws Exception {

		SellerAddress sellerAddress = new SellerAddress();

		if (StringUtils.hasText((String) entpUserMap.get("PA_ADDR_SEQ"))) {
			sellerAddress.setId((String) entpUserMap.get("PA_ADDR_SEQ"));
		}

		sellerAddress.setName(paEntpSlip.getPaCode() + entpUserMap.get("ENTP_CODE") + entpUserMap.get("ENTP_MAN_SEQ"));
		sellerAddress.setPostNo((String) entpUserMap.get("POST_NO"));
		sellerAddress.setContact((String) entpUserMap.get("ENTP_MAN_DDD") + (String) entpUserMap.get("ENTP_MAN_TEL1")
				+ (String) entpUserMap.get("ENTP_MAN_TEL2"));
		sellerAddress.setAddressPost((String) entpUserMap.get("ADDRESS1"));
		sellerAddress.setAddressDetail((String) entpUserMap.get("ADDRESS2"));
		sellerAddress.setReturnFeeAmount(new BigDecimal(0));
		sellerAddress.setExchangeFeeAmount(new BigDecimal(0));
		sellerAddress.setBasicReturnAddress(false);
		sellerAddress.setBasicSenderAddress(false);

		return sellerAddress;
	}

}
