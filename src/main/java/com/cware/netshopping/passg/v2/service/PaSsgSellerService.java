package com.cware.netshopping.passg.v2.service;

import java.util.Map;
import java.util.regex.Pattern;

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
import com.cware.netshopping.passg.common.repository.PaSsgCommonDAO;
import com.cware.netshopping.passg.common.service.PaSsgCommonService;
import com.cware.netshopping.passg.v2.domain.PaSsgShipCost;
import com.cware.netshopping.passg.v2.domain.ShppcstPlcy;
import com.cware.netshopping.passg.v2.domain.VenAddr;
import com.cware.netshopping.passg.v2.domain.VenAddrDelInfo;
import com.cware.netshopping.passg.v2.repository.PaSsgShipCostMapper;


@Service
public class PaSsgSellerService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	@Qualifier("passg.common.paSsgCommonService")
	private PaSsgCommonService paSsgCommonService;
	
	@Autowired
	@Qualifier("passg.common.paSsgCommonDAO")
	private PaSsgCommonDAO paSsgCommonDAO;

	@Autowired
	PaSsgSellerApiService sellerApiService;

	@Autowired
	PaSsgShipCostMapper shipCostMapper;

	@Autowired
	TransLogService transLogService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * SSG 업체배송지 등록
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
		serviceLog.setServiceNote("[API]SSG-업체배송지등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SSG.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 업체배송지 등록
		ResponseMsg result = callRegisterEntpSlip(entpCode, entpManSeq, paCode, procId,
				serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * SSG 업체배송지 수정
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
		serviceLog.setServiceNote("[API]SSG-업체배송지수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SSG.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 업체배송지 수정
		ResponseMsg result = callUpdateEntpSlip(entpCode, entpManSeq, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * SSG 배송비정책 등록
	 * 
	 * @param shppcstAplUnitCd
	 * @param shppcstPlcyDivCd
	 * @param collectYn
	 * @param shipCostBaseAmt
	 * @param shipCostAmt
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerShipCost(String shppcstAplUnitCd, String shppcstPlcyDivCd, String collectYn,
			Integer shipCostBaseAmt, Integer shipCostAmt, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(
				shppcstAplUnitCd + shppcstPlcyDivCd + ":" + shipCostBaseAmt + ":" + shipCostAmt + ":" + paCode);

		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]SSG-배송비정책등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SSG.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송비정책등록
		ResponseMsg result = callRegisterShipCost(shppcstAplUnitCd, shppcstPlcyDivCd, collectYn, shipCostBaseAmt,
				shipCostAmt, paCode, procId, serviceLog.getTransServiceNo());

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
			log.info("===== 업체배송지등록 서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaCode(paCode);

			Map<String, Object> entpUserMap = paSsgCommonService.selectSlipInsertList(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// SSG 업체배송지 전문생성
			VenAddr venAddr = createAddress(paEntpSlip, entpUserMap, transServiceNo);

			log.info("업체배송지등록 API 호출 {}-{}", entpCode, entpManSeq);
			VenAddrDelInfo venAddrDelInfo = sellerApiService
					.registerAddress(entpCode, entpManSeq, venAddr, paCode, transServiceNo);

			if (venAddrDelInfo != null) {
				
				result.setCode("200");
				result.setMessage(PaSsgApiRequest.API_SUCCESS_CODE);

				paEntpSlip.setPaAddrSeq(venAddrDelInfo.getGrpAddrId());
				paEntpSlip.setPaBundleNo(venAddrDelInfo.getJibunAddrId());
				paEntpSlip.setNewGmktShipNo(venAddrDelInfo.getDoroAddrId());
				paEntpSlip.setPaAddrGb((String)entpUserMap.get("ENTP_MAN_GB"));
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setInsertId(procId);
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

				rtnMsg = paSsgCommonService.savePaSsgEntpSlipTx(paEntpSlip);

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
			log.info("===== 업체배송지등록 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}
	
	private ResponseMsg callUpdateEntpSlip(String entpCode, String entpManSeq, String paCode, String procId,
			long transServiceNo) {
		
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 업체배송지수정서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaCode(paCode);

			Map<String, Object> entpUserMap = paSsgCommonDAO.selectEntpSlipUpdate(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 

			// SSG 업체배송지 전문생성
			VenAddr venAddr = createAddress(paEntpSlip, entpUserMap, transServiceNo);
			
			log.info("업체배송지수정 API 호출 {}-{}", entpCode, entpManSeq);
			VenAddrDelInfo venAddrDelInfo = sellerApiService
					.updateAddress(entpCode, entpManSeq, venAddr, paCode, transServiceNo);

			if (venAddrDelInfo != null) {
				result.setCode("200");
				result.setMessage(PaSsgApiRequest.API_SUCCESS_CODE);

				paEntpSlip.setPaAddrSeq(venAddrDelInfo.getGrpAddrId());
				paEntpSlip.setPaBundleNo(venAddrDelInfo.getJibunAddrId());
				paEntpSlip.setNewGmktShipNo(venAddrDelInfo.getDoroAddrId());
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

				rtnMsg = paSsgCommonService.updatePaSsgEntpSlipTx(paEntpSlip);

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
			log.info("===== 업체배송지수정 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}

	private VenAddr createAddress(PaEntpSlip paEntpSlip, Map<String, Object> entpUserMap, long transServiceNo) throws Exception {

		VenAddr venAddr = new VenAddr();

		if (StringUtils.hasText((String)entpUserMap.get("PA_ADDR_SEQ"))) {
			venAddr.setGrpAddrId((String)entpUserMap.get("PA_ADDR_SEQ"));
		}

		venAddr.setAddrlcAntnmNm(paEntpSlip.getPaCode() + "-" + entpUserMap.get("ENTP_CODE") + "-" + entpUserMap.get("ENTP_MAN_SEQ"));
		venAddr.setZipcd((String)entpUserMap.get("POST_NO"));
		venAddr.setDoroAddrBasc((String)entpUserMap.get("STD_ROAD_POST_ADDR1"));
		venAddr.setDoroAddrDtl((String)entpUserMap.get("STD_ROAD_POST_ADDR2"));
		venAddr.setJibunAddrBasc((String)entpUserMap.get("STD_POST_ADDR1"));
		venAddr.setJibunAddrDtl((String)entpUserMap.get("STD_POST_ADDR2"));
		venAddr.setBascAddrYn("N");
		
		// 연락처 (format : XXXX-XXXX-XXXX) 
		// 레거시의 유효성체크로직 잘못되어 수정
		String cnts = (String)entpUserMap.get("ENTP_MAN_HP");
	    if (cnts == null || !Pattern.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$", cnts)) {
	    	cnts = "010-1234-5678";
	    }
		venAddr.setCnts(cnts);
		
		//업체 지정택배 기타로 등록. (등록 안할경우 SSG 자체 회수처리됨)
		venAddr.setDelicoVenId("0000033028");
		
		return venAddr;
	}

	private ResponseMsg callRegisterShipCost(String shppcstAplUnitCd, String shppcstPlcyDivCd, String collectYn,
			Integer shipCostBaseAmt, Integer shipCostAmt, String paCode, String procId, long transServiceNo) {

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 배송비정책등록서비스 Start - {}-{} =====", shppcstAplUnitCd, shppcstPlcyDivCd);

			PaSsgShipCost shipCost = new PaSsgShipCost();

			shipCost.setPaCode(paCode);
			shipCost.setShppcstAplUnitCd(shppcstAplUnitCd);
			shipCost.setShppcstPlcyDivCd(shppcstPlcyDivCd);
			shipCost.setCollectYn(collectYn);
			shipCost.setShipCostBaseAmt(shipCostBaseAmt);
			shipCost.setShipCost(shipCostAmt);
			
			shipCost = shipCostMapper.getTransTarget(shipCost);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			if (shipCost.getPaShipPolicyNo() != null) {
				result.setCode("400");
				result.setMessage("이미 등록된 배송비정책입니다.");
				return result;
			}

			// SSG 배송비정책 전문생성
			ShppcstPlcy shppcstPlcy = createShippingPolicy(shipCost, transServiceNo);
			
			log.info("배송비정책등록 API 호출 {} {} {} {} {}", shppcstAplUnitCd, shppcstPlcyDivCd, shipCostBaseAmt, shipCostAmt, paCode);
			String shppcstId = sellerApiService.registerShippingPolicy(shppcstPlcy, paCode, transServiceNo);

			shipCost.setPaShipPolicyNo(shppcstId);
			
			if (shipCostMapper.updateCompleteTrans(shipCost) < 1) {
				result.setCode("500");
				result.setMessage("배송비정책 등록결과 업데이트 실패");
			} else {
				result.setCode("200");
				result.setMessage(PaSsgApiRequest.API_SUCCESS_CODE);
			}


		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 배송비정책등록서비스 End - {} {} {} {} {} =====", shppcstAplUnitCd, shppcstPlcyDivCd, shipCostBaseAmt, shipCostAmt, paCode);
		}
		return result;
	}

	private ShppcstPlcy createShippingPolicy(PaSsgShipCost shipCost, long transServiceNo) throws Exception {

		ShppcstPlcy shppcstPlcy = new ShppcstPlcy();

		shppcstPlcy.setShppcstPlcyDivCd(shipCost.getShppcstPlcyDivCd());
		
		if("10".equals(shipCost.getShppcstPlcyDivCd())) {//출고배송비이면
			shppcstPlcy.setShppcstAplUnitCd(shipCost.getShppcstAplUnitCd());
			shppcstPlcy.setShppcstExmpCritnAmt(shipCost.getShipCostBaseAmt());
		}

		if("60".equals(shipCost.getShppcstPlcyDivCd()) || "70".equals(shipCost.getShppcstPlcyDivCd())) {
			shppcstPlcy.setPrpayCodDivCd("");
		} else {
			shppcstPlcy.setShppcstAplUnitCd(shipCost.getShppcstAplUnitCd());
			shppcstPlcy.setPrpayCodDivCd("0".equals(shipCost.getCollectYn()) ? "10" : "20");
		}
		shppcstPlcy.setShppcst(shipCost.getShipCost());
		shppcstPlcy.setBascPlcyYn("N");
		shppcstPlcy.setShppcstAplYn("Y");
		
		return shppcstPlcy;
	}
	
}
