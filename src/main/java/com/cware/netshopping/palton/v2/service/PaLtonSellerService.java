package com.cware.netshopping.palton.v2.service;

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
import com.cware.netshopping.domain.model.PaLtonAddShipCost;
import com.cware.netshopping.pacommon.v2.repository.PaCustShipCostMapper;
import com.cware.netshopping.palton.common.repository.PaLtonCommonDAO;
import com.cware.netshopping.palton.common.service.PaLtonCommonService;
import com.cware.netshopping.palton.v2.domain.DvCstSr;
import com.cware.netshopping.palton.v2.domain.DvpSr;
import com.cware.netshopping.palton.v2.repository.PaLtonAddShipCostMapper;

@Service
public class PaLtonSellerService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	@Qualifier("palton.common.paLtonCommonService")
	private PaLtonCommonService paLtonCommonService;
	
	@Autowired
	@Qualifier("palton.common.paLtonCommonDAO")
	private PaLtonCommonDAO paLtonCommonDAO;

	@Autowired
	PaLtonSellerApiService sellerApiService;

	@Autowired
	PaCustShipCostMapper shipCostMapper;
	
	@Autowired
	PaLtonAddShipCostMapper addShipCostMapper;

	@Autowired
	TransLogService transLogService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());


	/**
	 * 롯데온 출고/반품지 등록
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
		serviceLog.setServiceNote("[API]롯데온-출고/반품지등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.LOTTEON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 출고/반품지 등록
		ResponseMsg result = callRegisterEntpSlip(entpCode, entpManSeq, paCode, procId,
				serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 롯데온 출고/반품지 수정
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
		serviceLog.setServiceNote("[API]롯데온-출고/반품지수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.LOTTEON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 출고/반품지 수정
		ResponseMsg result = callUpdateEntpSlip(entpCode, entpManSeq, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 롯데온 배송비정책 등록
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param paCode
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
		serviceLog.setServiceNote("[API]롯데온-배송비정책등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.LOTTEON.code());
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
	 * 롯데온 배송비정책 수정
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param paCode
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
		serviceLog.setServiceNote("[API]롯데온-배송비정책수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.LOTTEON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송비정책수정
		ResponseMsg result = callUpdateShipCost(entpCode, shipCostCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}

	/**
	 * 롯데온 추가배송비정책 등록
	 * 
	 * @param islandCost
	 * @param jejuCost
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerAddShipCost(Double islandCost, Double jejuCost, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(islandCost + ":" + islandCost + ":" + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]롯데온-추가배송비정책등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.LOTTEON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 추가배송비정책등록
		ResponseMsg result = callRegisterAddShipCost(islandCost, jejuCost, paCode, procId,
				serviceLog.getTransServiceNo());

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
			log.info("===== 출고/반품지등록 서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaCode(paCode);

			Map<String, Object> entpUserMap = paLtonCommonService.selectSlipInsertList(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 롯데온 출고/반품지 전문생성
			DvpSr dvpSr = createAddress(paEntpSlip, entpUserMap, transServiceNo);

			log.info("출고/반품지등록 API 호출 {}-{}", entpCode, entpManSeq);
			String paAddrSeq = sellerApiService
					.registerAddress(entpCode, entpManSeq, dvpSr, paCode, transServiceNo);

			if (StringUtils.hasText(paAddrSeq)) {
				
				result.setCode("200");
				result.setMessage(PaLtonApiRequest.API_SUCCESS_MSG);

				paEntpSlip.setPaAddrSeq(paAddrSeq);
				paEntpSlip.setPaAddrGb((String)entpUserMap.get("ENTP_MAN_GB"));
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setInsertId(procId);
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

				rtnMsg = paLtonCommonService.savePaLtonEntpSlipTx(paEntpSlip);

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
			log.info("===== 출고/반품지등록 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}
	
	private ResponseMsg callUpdateEntpSlip(String entpCode, String entpManSeq, String paCode, String procId,
			long transServiceNo) {
		
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 출고/반품지수정서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaCode(paCode);

			Map<String, Object> entpUserMap = paLtonCommonDAO.selectEntpSlipUpdate(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 

			// 롯데온 출고/반품지 전문생성
			DvpSr dvpSr = createAddress(paEntpSlip, entpUserMap, transServiceNo);
			
			log.info("출고/반품지수정 API 호출 {}-{}", entpCode, entpManSeq);
			String returnMsg = sellerApiService
					.updateAddress(entpCode, entpManSeq, dvpSr, paCode, transServiceNo);
				
			result.setCode("200");
			result.setMessage(returnMsg);

			paEntpSlip.setPaAddrSeq((String)entpUserMap.get("PA_ADDR_SEQ"));
			paEntpSlip.setTransTargetYn("0");
			paEntpSlip.setInsertId(procId);
			paEntpSlip.setModifyId(procId);
			paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			rtnMsg = paLtonCommonService.updatePaLtonEntpSlipTx(paEntpSlip);

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
			log.info("===== 출고/반품지수정 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}

	private DvpSr createAddress(PaEntpSlip paEntpSlip, Map<String, Object> entpUserMap, long transServiceNo) throws Exception {

		DvpSr dvpSr = new DvpSr();

		if (StringUtils.hasText((String)entpUserMap.get("PA_ADDR_SEQ"))) {
			dvpSr.setDvpNo((String)entpUserMap.get("PA_ADDR_SEQ"));
			dvpSr.setUseYn("Y");
		}

		dvpSr.setDvpTypCd("20".equals(entpUserMap.get("ENTP_MAN_GB")) ? "01" : "02");
		dvpSr.setDvpNm(entpUserMap.get("ENTP_CODE") + "_" + entpUserMap.get("ENTP_MAN_SEQ") + "_" + paEntpSlip.getPaCode());
		dvpSr.setZipNo((String)entpUserMap.get("POST_NO"));
		dvpSr.setZipAddr((String)entpUserMap.get("POST_ADDR"));
		dvpSr.setDtlAddr((String)entpUserMap.get("ADDR"));
		dvpSr.setStnmZipNo((String)entpUserMap.get("STD_ROAD_POST"));
		dvpSr.setStnmZipAddr((String)entpUserMap.get("STD_POST_ADDR1"));
		dvpSr.setStnmDtlAddr((String)entpUserMap.get("STD_POST_ADDR2"));
		dvpSr.setRpbtrNm((String)entpUserMap.get("ENTP_MAN_NAME"));
		dvpSr.setMphnNo((String)entpUserMap.get("ENTP_MAN_HP"));
		dvpSr.setTelNo((String)entpUserMap.get("ENTP_MAN_TEL")); // 레거시는 모바일 연동하고 있음 TODO
		
		return dvpSr;
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

			// 롯데온 배송비정책 전문생성
			DvCstSr dvCstSr = createShippingPolicy(shipCost, transServiceNo);
			
			log.info("배송비정책등록 API 호출 {} {} {}", entpCode, shipCostCode, paCode);
			String dvCstPolNo = sellerApiService
					.registerShippingPolicy(entpCode, shipCostCode, dvCstSr, paCode, transServiceNo);

			shipCost.setGroupCode(dvCstPolNo);
			shipCost.setModifyId(procId);
			
			if (shipCostMapper.updateCompleteTrans(shipCost) < 1) {
				result.setCode("500");
				result.setMessage("배송비정책 등록결과 업데이트 실패");
			} else {
				result.setCode("200");
				result.setMessage(PaLtonApiRequest.API_SUCCESS_MSG);
			}


		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 배송비정책등록서비스 End - {} {} {} =====", entpCode, shipCostCode, paCode);
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

			// 롯데온 배송비정책 전문생성
			DvCstSr dvCstSr = createShippingPolicy(shipCost, transServiceNo);
			
			log.info("배송비정책수정 API 호출 {} {} {}", entpCode, shipCostCode, paCode);
			String returnMsg = sellerApiService
					.updateShippingPolicy(entpCode, shipCostCode, dvCstSr, paCode, transServiceNo);

			shipCost.setModifyId(procId);
			
			if (shipCostMapper.updateCompleteTrans(shipCost) < 1) {
				result.setCode("500");
				result.setMessage("배송비정책 수정결과 업데이트 실패");
			} else {
				result.setCode("200");
				result.setMessage(returnMsg);
			}


		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 배송비정책수정서비스 End - {} {} {} =====", entpCode, shipCostCode, paCode);
		}
		return result;
	}

	private ResponseMsg callRegisterAddShipCost(Double islandCost, Double jejuCost, String paCode, String procId,
			long transServiceNo) {

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 추가배송비정책등록서비스 Start - {}-{} =====", islandCost, jejuCost);

			PaLtonAddShipCost shipCost = addShipCostMapper.getTransTarget(islandCost, jejuCost, paCode);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 롯데온 배송비정책 전문생성
			DvCstSr dvCstSr = createAddShippingPolicy(shipCost, transServiceNo);
			
			log.info("추가배송비정책등록 API 호출 {}-{} {}", islandCost, jejuCost, paCode);
			String dvCstPolNo = sellerApiService
					.registerAddShippingPolicy(dvCstSr, paCode, transServiceNo);

			shipCost.setDvCstPolNo(dvCstPolNo);
			shipCost.setModifyId(procId);
			
			if (addShipCostMapper.updateCompleteTrans(shipCost) < 1) {
				result.setCode("500");
				result.setMessage("추가배송비정책 등록결과 업데이트 실패");
			} else {
				result.setCode("200");
				result.setMessage(PaLtonApiRequest.API_SUCCESS_MSG);
			}

		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 추가배송비정책등록서비스 End - {}-{} {} =====", islandCost, jejuCost, paCode);
		}
		return result;
	}

	private DvCstSr createShippingPolicy(PaCustShipCost shipCost, long transServiceNo) throws Exception {

		DvCstSr dvCstSr = new DvCstSr();

		if (StringUtils.hasText(shipCost.getGroupCode())) {
			dvCstSr.setDvCstPolNo(shipCost.getGroupCode());
		}

		dvCstSr.setAplyStrtDt(DateUtil.getCurrentDateAsString());
		dvCstSr.setAplyEndDt("29991220");
		dvCstSr.setCtrtTypCd("A");
		dvCstSr.setDvTypCd("DRECT");
		dvCstSr.setDvProcTypCd("LO_ENTP");
		dvCstSr.setUseYn("Y");
		
		dvCstSr.setDvCstTypCd("DV_CST");
		dvCstSr.setCndlFreeStdAmt(shipCost.getShipCostBaseAmt());
		dvCstSr.setDvCst(shipCost.getOrdCost());
		// 반품비 편도설정
		dvCstSr.setRcst(shipCost.getReturnCost());
		
		//배송비 종류(00:무료, 98:판매자 조건부 무료, 99:판매자 정액)
		switch (shipCost.getShipCostCode().substring(0, 2)) {
		case "FR": // 무료
			dvCstSr.setDvCstDvsCd("B");
			break;
		case "CN":
		case "PL": // 조건부
			dvCstSr.setDvCstDvsCd("C");
			break;
		default: // 상품별
			dvCstSr.setDvCstDvsCd("A");
			break;
		}
		
		return dvCstSr;
	}
	
	private DvCstSr createAddShippingPolicy(PaLtonAddShipCost shipCost, long transServiceNo) throws Exception {

		DvCstSr dvCstSr = new DvCstSr();

		dvCstSr.setAplyStrtDt(DateUtil.getCurrentDateAsString());
		dvCstSr.setAplyEndDt("29991220");
		dvCstSr.setCtrtTypCd("A");
		dvCstSr.setDvTypCd("DRECT");
		dvCstSr.setDvProcTypCd("LO_ENTP");
		dvCstSr.setUseYn("Y");
		
		dvCstSr.setDvCstTypCd("ADTN_DV_CST");
		dvCstSr.setInrmAdtnDvCst(shipCost.getIslandCost());
		dvCstSr.setJejuAdtnDvCst(shipCost.getJejuCost());

		if(shipCost.getIslandCost() > 0 || shipCost.getJejuCost() > 0) {
			dvCstSr.setDvCstDvsCd("A");
		}else {
			dvCstSr.setDvCstDvsCd("B");
		}	
		
		return dvCstSr;
	}
	
}
