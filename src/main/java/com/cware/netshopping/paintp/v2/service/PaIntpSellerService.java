package com.cware.netshopping.paintp.v2.service;

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
import com.cware.netshopping.domain.model.PaCustShipCost;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.pacommon.v2.repository.PaCustShipCostMapper;
import com.cware.netshopping.paintp.common.repository.PaIntpCommonDAO;
import com.cware.netshopping.paintp.common.service.PaIntpCommonService;
import com.cware.netshopping.paintp.v2.domain.DelvCostPlc;
import com.cware.netshopping.paintp.v2.domain.DelvItem;

@Service
public class PaIntpSellerService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	@Qualifier("paintp.common.paIntpCommonService")
	private PaIntpCommonService paIntpCommonService;
	
	@Autowired
	@Qualifier("paintp.common.paIntpCommonDAO")
	private PaIntpCommonDAO paIntpCommonDAO;

	@Autowired
	PaIntpSellerApiService sellerApiService;

	@Autowired
	PaCustShipCostMapper shipCostMapper;

	@Autowired
	TransLogService transLogService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 인터파크 반품배송지 등록
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
		serviceLog.setServiceNote("[API]인터파크-반품배송지등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.INTERPARK.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 반품배송지 등록
		ResponseMsg result = callRegisterEntpSlip(entpCode, entpManSeq, paCode, procId,
				serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 인터파크 반품배송지 수정
	 * 
	 * @param entpCode
	 * @param entpManSeq
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
		serviceLog.setServiceNote("[API]인터파크-반품배송지수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.INTERPARK.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 반품배송지 수정
		ResponseMsg result = callUpdateEntpSlip(entpCode, entpManSeq, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 인터파크 배송비정책 등록
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerShipCost(String entpCode, String shipCostCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + shipCostCode + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]인터파크-배송비정책등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.INTERPARK.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송비정책등록
		ResponseMsg result = callRegisterShipCost(entpCode, shipCostCode, paCode, procId,
				serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}

	/**
	 * 인터파크 배송비정책 수정
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg updateShipCost(String entpCode, String shipCostCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + shipCostCode + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]인터파크-배송비정책수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.INTERPARK.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송비정책수정
		ResponseMsg result = callUpdateShipCost(entpCode, shipCostCode, paCode, procId, serviceLog.getTransServiceNo());

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
			log.info("===== 반품배송지등록 서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb("20");
			paEntpSlip.setPaCode(paCode);

			Map<String, String> entpUserMap = paIntpCommonService.selectEntpShipInsertList(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 인터파크 반품배송지 전문생성
			DelvItem delvItem = createReturnAddress(paEntpSlip, entpUserMap, transServiceNo);

			log.info("반품배송지등록 API 호출 {}-{}", entpCode, entpManSeq);
			String paAddrSeq = sellerApiService
					.registerReturnAddress(entpCode, entpManSeq, delvItem, paCode, transServiceNo);

			if (StringUtils.hasText(paAddrSeq)) {
				
				result.setCode("200");
				result.setMessage(PaIntpApiRequest.API_SUCCESS_CODE);

				paEntpSlip.setPaAddrSeq(paAddrSeq);
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setInsertId(procId);
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				

				rtnMsg = paIntpCommonService.savePaIntpEntpSlipTx(paEntpSlip);

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
			log.info("===== 반품배송지등록 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}
	
	private ResponseMsg callUpdateEntpSlip(String entpCode, String entpManSeq, String paCode, String procId,
			long transServiceNo) {
		
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 반품배송지수정서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb("20");
			paEntpSlip.setPaCode(paCode);

			Map<String, String> entpUserMap = paIntpCommonDAO.selectEntpShipUpdate(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 

			// 인터파크 반품배송지 전문생성
			DelvItem delvItem = createReturnAddress(paEntpSlip, entpUserMap, transServiceNo);
			
			log.info("반품배송지수정 API 호출 {}-{}", entpCode, entpManSeq);
			String paAddrSeq = sellerApiService
					.updateReturnAddress(entpCode, entpManSeq, delvItem, paCode, transServiceNo);

			if (StringUtils.hasText(paAddrSeq)) {
				
				result.setCode("200");
				result.setMessage(PaIntpApiRequest.API_SUCCESS_CODE);

				paEntpSlip.setPaAddrSeq(paAddrSeq);
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setInsertId(procId);
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				

				rtnMsg = paIntpCommonService.savePaIntpEntpSlipUpdateTx(paEntpSlip);

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
			log.info("===== 반품배송지수정 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}

	private DelvItem createReturnAddress(PaEntpSlip paEntpSlip, Map<String, String> entpUserMap, long transServiceNo) throws Exception {

		DelvItem delvItem = new DelvItem();

		if (StringUtils.hasText(entpUserMap.get("PA_ADDR_SEQ"))) {
			delvItem.setEntrDelvInfoNo(entpUserMap.get("PA_ADDR_SEQ"));
			delvItem.setUseYn("Y");
		}

		delvItem.setDelvNm(entpUserMap.get("ENTP_CODE") + "_" + entpUserMap.get("ENTP_MAN_SEQ") + "_" + paEntpSlip.getPaCode());
		delvItem.setDelvZipcd(entpUserMap.get("POST_NO"));
		delvItem.setDelvAddr1(entpUserMap.get("ADDRESS1"));
		delvItem.setDelvAddr2(entpUserMap.get("ADDRESS2"));
		delvItem.setDelvTelno(entpUserMap.get("ENTP_MAN_TEL"));
		delvItem.setDelvHp(entpUserMap.get("ENTP_MAN_HP"));
		
		return delvItem;
	}

	private ResponseMsg callRegisterShipCost(String entpCode, String shipCostCode, String paCode, String procId,
			long transServiceNo) {

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 배송비정책등록서비스 Start - {}-{} =====", entpCode, shipCostCode);

			PaCustShipCost shipCost = shipCostMapper.getTransTarget(entpCode, shipCostCode, paCode);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			if (shipCost.getGroupCode() != null) {
				result.setCode("400");
				result.setMessage("이미 등록된 배송비정책입니다.");
				return result;
			}

			// 인터파크 배송비정책 전문생성
			DelvCostPlc delvCostPlc = createShippingPolicy(shipCost, transServiceNo);
			
			log.info("배송비정책등록 API 호출 {}-{} {} {}", entpCode, shipCostCode, paCode);
			String delvCostPlcNo = sellerApiService
					.registerShippingPolicy(entpCode, shipCostCode, delvCostPlc, paCode, transServiceNo);

			shipCost.setGroupCode(delvCostPlcNo);
			shipCost.setModifyId(procId);
			
			if (shipCostMapper.updateCompleteTrans(shipCost) < 1) {
				result.setCode("500");
				result.setMessage("배송비정책 등록결과 업데이트 실패");
			} else {
				result.setCode("200");
				result.setMessage(PaIntpApiRequest.API_SUCCESS_CODE);
			}


		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 배송비정책등록서비스 End - {}-{} {} {} =====", entpCode, shipCostCode, paCode);
		}
		return result;
	}


	private ResponseMsg callUpdateShipCost(String entpCode, String shipCostCode, String paCode, String procId,
			long transServiceNo) {

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 배송비정책수정서비스 Start - {}-{} =====", entpCode, shipCostCode);

			PaCustShipCost shipCost = shipCostMapper.getTransTarget(entpCode, shipCostCode, paCode);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 

			if (shipCost.getGroupCode() == null) {
				result.setCode("400");
				result.setMessage("등록된 배송비정책이 아닙니다.");
				return result;
			}

			// 인터파크 배송비정책 전문생성
			DelvCostPlc delvCostPlc = createShippingPolicy(shipCost, transServiceNo);
			
			log.info("배송비정책수정 API 호출 {}-{} {} {}", entpCode, shipCostCode, paCode);
			String delvPlcNo = sellerApiService
					.updateShippingPolicy(entpCode, shipCostCode, delvCostPlc, paCode, transServiceNo);

			shipCost.setGroupCode(delvPlcNo);
			shipCost.setModifyId(procId);
			
			if (shipCostMapper.updateCompleteTrans(shipCost) < 1) {
				result.setCode("500");
				result.setMessage("배송비정책 수정결과 업데이트 실패");
			} else {
				result.setCode("200");
				result.setMessage(PaIntpApiRequest.API_SUCCESS_CODE);
			}


		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 배송비정책수정서비스 End - {}-{} {} {} =====", entpCode, shipCostCode, paCode);
		}
		return result;
	}

	private DelvCostPlc createShippingPolicy(PaCustShipCost shipCost, long transServiceNo) throws Exception {

		DelvCostPlc delvCostPlc = new DelvCostPlc();

		if (StringUtils.hasText(shipCost.getGroupCode())) {
			delvCostPlc.setDelvPlcNo(shipCost.getGroupCode());
		}

		//배송비 종류(00:무료, 98:판매자 조건부 무료, 99:판매자 정액)
		switch (shipCost.getShipCostCode().substring(0, 2)) {
		case "FR": // 무료
			delvCostPlc.setDistCostTp("00");
			break;
		case "CN":
		case "PL": // 조건부
			delvCostPlc.setDistCostTp("98");
			delvCostPlc.setMaxbuyAmt((long)shipCost.getShipCostBaseAmt());
			break;
		default: // 상품별
			delvCostPlc.setDistCostTp("99");
			break;
		}

		//결제방법 (01:착불, 02:선불(배송비 종류가 무료일 경우에도 선택), 03:선/착불)
		delvCostPlc.setDistCostCd("02");
		//일부지역 유료여부 (Y:예, N:아니오(배송비 종류가 무료일 때만 입력))
		if(shipCost.getIslandCost() > 0 || shipCost.getJejuCost() > 0) {
			delvCostPlc.setLocalCostYn("Y");				
		}else {
			delvCostPlc.setLocalCostYn("N");	
		}
		
		// 배송비
		delvCostPlc.setDistCost((long)shipCost.getOrdCost());
		
		return delvCostPlc;
	}
	
}
