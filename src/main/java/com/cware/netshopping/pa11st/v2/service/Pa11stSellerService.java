package com.cware.netshopping.pa11st.v2.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.cware.netshopping.common.util.PostUtil;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.pa11st.common.repository.Pa11stCommonDAO;
import com.cware.netshopping.pa11st.common.service.Pa11stCommonService;
import com.cware.netshopping.pa11st.v2.domain.AddrBasiDlvCst;
import com.cware.netshopping.pa11st.v2.domain.InOutAddress;
import com.cware.netshopping.pa11st.v2.domain.Pa11stCnShipCost;
import com.cware.netshopping.pa11st.v2.repository.Pa11stCnShipCostMapper;

import zipit.rfnCustCommonAddrList;

@Service
public class Pa11stSellerService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	@Qualifier("pa11st.common.pa11stCommonService")
	private Pa11stCommonService pa11stCommonService;
	
	@Autowired
	@Qualifier("pa11st.common.pa11stCommonDAO")
	private Pa11stCommonDAO pa11stCommonDAO;

	@Autowired
	@Qualifier("com.cware.netshopping.common.util.PostUtil")
	private PostUtil postUtil;
	
	@Autowired
	Pa11stSellerApiService sellerApiService;

	@Autowired
	Pa11stCnShipCostMapper shipCostMapper;

	@Autowired
	TransLogService transLogService;

	// 주소정제서버
	@Value("${partner.post.server.ip}")
	String POST_SERVER_IP;
	@Value("${partner.post.server.port}")
	String POST_SERVER_PORT;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 11번가 출고/회수지 등록
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paAddrGb
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerEntpSlip(String entpCode, String entpManSeq, String paAddrGb, String paCode, 
			String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + entpManSeq + paCode);
		serviceLog.setTransType(TransType.SELLER.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]11번가-출고/회수지등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SK11ST.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 출고/회수지 등록
		ResponseMsg result = callRegisterEntpSlip(entpCode, entpManSeq, paAddrGb, paCode, procId,
				serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 11번가 출고/회수지 수정
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paAddrGb
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg updateEntpSlip(String entpCode, String entpManSeq, String paAddrGb, String paCode, String procId,
			long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + entpManSeq + paCode);
		serviceLog.setTransType(TransType.SELLER.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]11번가-출고/회수지수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SK11ST.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 출고/회수지 수정
		ResponseMsg result = callUpdateEntpSlip(entpCode, entpManSeq, paAddrGb, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 11번가 출고지조건부배송비 연동
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg transShipCost(String entpCode, String entpManSeq, String shipCostCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + entpManSeq + shipCostCode + paCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]11번가-출고지조건부배송비");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SK11ST.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 출고지조건부배송비정책연동
		ResponseMsg result = callTransShipCost(entpCode, entpManSeq, shipCostCode, paCode, procId,
				serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}
	
	private ResponseMsg callRegisterEntpSlip(String entpCode, String entpManSeq, String paAddrGb, String paCode,
			String procId, long transServiceNo) {
		
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 출고/회수지등록 서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb(paAddrGb);
			paEntpSlip.setPaCode(paCode);

			Map<String, String> entpUserMap = pa11stCommonService.selectEntpShipInsertList(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 주소정제
			Map<String, Object> refinedPost = verifyPost(entpUserMap);
			
			// 건물번호가 없는 경우 주소정제 실패
			if (!StringUtils.hasText((String)refinedPost.get("NNMB"))) {
				result.setCode("400");
				result.setMessage("주소정제에 실패하였습니다.");
				return result;
			}
			
			// 11번가 출고/회수지 전문생성
			InOutAddress inOutAddress = createInOutAddress(paEntpSlip, entpUserMap, refinedPost, transServiceNo);

			log.info(" 출고/회수지등록 API 호출 {}-{}", entpCode, entpManSeq);
			String paAddrSeq = "30".equals(paAddrGb)
					? sellerApiService.registerOutAddress(entpCode, entpManSeq, inOutAddress, paCode, transServiceNo)
					: sellerApiService
							.registerReturnAddress(entpCode, entpManSeq, inOutAddress, paCode, transServiceNo);

			if (StringUtils.hasText(paAddrSeq)) {
				
				result.setCode("200");
				result.setMessage(Pa11stApiRequest.API_SUCCESS_CODE);

				paEntpSlip.setPaAddrSeq(paAddrSeq);
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setInsertId(procId);
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				

				rtnMsg = pa11stCommonService.savePa11stEntpSlipTx(paEntpSlip);

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
			log.info("===== 출고/회수지등록 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}

	// 주소 정제		
	@SuppressWarnings("unchecked")
	private Map<String, Object> verifyPost(Map<String, String> entpUserMap) throws Exception {
		String postNo = entpUserMap.get("POST_NO");
		String roadAddrYn = null;
		String searchAddr = null;
		String searchAddrDetail = null;

		if (entpUserMap.get("ROAD_ADDR_YN").equals("1")) {
			roadAddrYn = "N";
			searchAddr = entpUserMap.get("ROAD_POST_ADDR");
			searchAddrDetail = entpUserMap.get("ROAD_ADDR");
		} else {
			roadAddrYn = "J";
			searchAddr = entpUserMap.get("POST_ADDR");
			searchAddrDetail = entpUserMap.get("ADDR");
		}

		rfnCustCommonAddrList rfn = new rfnCustCommonAddrList();
		rfn.log.disableLogs(true);
		rfn.setServerProp(POST_SERVER_IP, POST_SERVER_PORT, "UTF-8");
		Map<String, Object> resultMap = rfn.getRfnAddrMap(postNo, searchAddr, searchAddrDetail, "UTF-8", roadAddrYn);
		ArrayList<HashMap<String, Object>> result = (ArrayList<HashMap<String, Object>>) resultMap.get("DATA");
		
		return (Map<String, Object>) result.get(0);
	}
	
	private ResponseMsg callUpdateEntpSlip(String entpCode, String entpManSeq, String paAddrGb, String paCode,
			String procId, long transServiceNo) {
		
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 출고/회수지수정서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb(paAddrGb);
			paEntpSlip.setPaCode(paCode);
			paEntpSlip.setTransTargetYn("1");

			Map<String, String> entpUserMap = pa11stCommonDAO.selectEntpShipUpdate(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 

			// 주소정제
			Map<String, Object> refinedPost = verifyPost(entpUserMap);

			// 건물번호가 없는 경우 주소정제 실패
			if (!StringUtils.hasText((String)refinedPost.get("NNMB"))) {
				result.setCode("400");
				result.setMessage("주소정제에 실패하였습니다.");
				return result;
			}
			
			// 11번가 출고/회수지 전문생성
			InOutAddress inOutAddress = createInOutAddress(paEntpSlip, entpUserMap, refinedPost, transServiceNo);

			log.info("출고/회수지수정 API 호출 {}-{}", entpCode, entpManSeq);
			String paAddrSeq = "30".equals(paAddrGb)
					? sellerApiService.updateOutAddress(entpCode, entpManSeq, inOutAddress, paCode, transServiceNo)
					: sellerApiService
							.updateReturnAddress(entpCode, entpManSeq, inOutAddress, paCode, transServiceNo);

			if (StringUtils.hasText(paAddrSeq)) {
				
				result.setCode("200");
				result.setMessage(Pa11stApiRequest.API_SUCCESS_CODE);

				paEntpSlip.setPaAddrSeq(paAddrSeq);
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setInsertId(procId);
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				

				rtnMsg = pa11stCommonService.savePa11stEntpSlipUpdateTx(paEntpSlip);

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
			log.info("===== 출고/회수지수정 API End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}

	private InOutAddress createInOutAddress(PaEntpSlip paEntpSlip, Map<String, String> entpUserMap,
			Map<String, Object> refinedPost, long transServiceNo) throws Exception {

		InOutAddress inOutAddress = new InOutAddress();

		if (StringUtils.hasText(entpUserMap.get("PA_ADDR_SEQ"))) {
			inOutAddress.setAddrSeq(entpUserMap.get("PA_ADDR_SEQ"));
		}

		inOutAddress.setAddrNm(entpUserMap.get("ENTP_CODE") + "_" + entpUserMap.get("ENTP_MAN_SEQ") + "_" + paEntpSlip.getPaCode());
		if (entpUserMap.get("SHIP_COST_CODE") != null) {
			inOutAddress.setAddrNm(inOutAddress.getAddrNm() + "_" + entpUserMap.get("SHIP_COST_CODE"));
		}
		inOutAddress.setRcvrNm(entpUserMap.get("ENTP_MAN_NAME"));
		inOutAddress.setGnrlTlphnNo(entpUserMap.get("ENTP_MAN_TEL"));
		inOutAddress.setPrtblTlphnNo(entpUserMap.get("ENTP_MAN_HP"));
		inOutAddress.setBuildMngNO((String)refinedPost.get("NNMB"));
		// 레거시에서는 건물주소까지 모두 포함(NADR1S)하고 있음. 상세주소만 보내게 수정
		inOutAddress.setDtlsAddr((String)refinedPost.get("NADR2S"));
		
		return inOutAddress;
	}

	private ResponseMsg callTransShipCost(String entpCode, String entpManSeq, String shipCostCode, String paCode,
			String procId, long transServiceNo) {

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 출고지배송비연동서비스 Start - {}-{} =====", entpCode, entpManSeq);

			Pa11stCnShipCost shipCost = shipCostMapper.getTransTarget(entpCode, entpManSeq, shipCostCode, paCode);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 

			// 출고지 
			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb("30");
			paEntpSlip.setPaCode(paCode);

			Map<String, String> entpUserMap = pa11stCommonDAO.selectEntpShipUpdate(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 주소정제
			Map<String, Object> refinedPost = verifyPost(entpUserMap);

			// 건물번호가 없는 경우 주소정제 실패
			if (!StringUtils.hasText((String)refinedPost.get("NNMB"))) {
				result.setCode("400");
				result.setMessage("주소정제에 실패하였습니다.");
				return result;
			}
			
			entpUserMap.put("PA_ADDR_SEQ", shipCost.getPaAddrSeq());
			entpUserMap.put("SHIP_COST_CODE", shipCost.getShipCostCode());

			// 11번가 출고지 전문생성
			InOutAddress inOutAddress = createInOutAddress(paEntpSlip, entpUserMap, refinedPost, transServiceNo);

			shipCost.setModifyId(procId);
			
			if (shipCost.getPaAddrSeq() == null ) {

				log.info(" 출고지등록 API 호출 {}-{}", entpCode, entpManSeq);
				String paAddrSeq = sellerApiService.registerOutAddress(entpCode, entpManSeq, inOutAddress, paCode, transServiceNo);

				if (!StringUtils.hasText(paAddrSeq)) {
					result.setCode("500");
					result.setMessage("출고지배송비정책 연동 실패");
					return result;
				}
				
				shipCost.setPaAddrSeq(paAddrSeq);
				shipCost.setPaAddrNm(inOutAddress.getAddrNm());
				shipCostMapper.updateOutAddressTrans(shipCost);
			} else {
				log.info(" 출고지수정 API 호출 {}-{}", entpCode, entpManSeq);
				sellerApiService.updateOutAddress(entpCode, entpManSeq, inOutAddress, paCode, transServiceNo);
			}
			
			// 11번가 출고지조건부배송비 전문생성
			InOutAddress outShipCost = createShippingPolicy(shipCost, transServiceNo);
			
			log.info("출고지배송비정책적용 API 호출 {}-{} {} {}", entpCode, entpManSeq, shipCostCode, paCode);
			sellerApiService
					.updateShippingPolicy(entpCode, entpManSeq, shipCostCode, outShipCost, paCode, transServiceNo);

			if (shipCostMapper.updateCompleteTrans(shipCost) < 1) {
				result.setCode("500");
				result.setMessage("배송비정책 전송결과 업데이트 실패");
			} else {
				result.setCode("200");
				result.setMessage(Pa11stApiRequest.API_SUCCESS_CODE);
			}


		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 출고지배송비연동서비스 End - {}-{} {} {} =====", entpCode, entpManSeq, shipCostCode, paCode);
		}
		return result;
	}
	

	private InOutAddress createShippingPolicy(Pa11stCnShipCost shipCost, long transServiceNo) throws Exception {

		InOutAddress intOutAddress = new InOutAddress();

		intOutAddress.setAddrSeq(shipCost.getPaAddrSeq());
		intOutAddress.setAddrNm(shipCost.getEntpCode() + "_" + shipCost.getEntpManSeq() + "_" + shipCost.getShipCostCode());
		
		// 출고지 배송비 
		List<AddrBasiDlvCst> shipCostList = new ArrayList<AddrBasiDlvCst>();
		
		AddrBasiDlvCst addrBasiDlvCst = new AddrBasiDlvCst();
		addrBasiDlvCst.setDlvCst(String.valueOf((long)shipCost.getOrdCostAmt()));
		addrBasiDlvCst.setOrdBgnAmt("0");
		addrBasiDlvCst.setOrdEndAmt(String.valueOf((long)shipCost.getShipCostBaseAmt()));
		addrBasiDlvCst.setMbAddrLocation("01");
		shipCostList.add(addrBasiDlvCst);
		addrBasiDlvCst = new AddrBasiDlvCst();
		addrBasiDlvCst.setDlvCst("0");
		addrBasiDlvCst.setOrdBgnAmt(String.valueOf((long)shipCost.getShipCostBaseAmt()));
		addrBasiDlvCst.setOrdEndAmt("999999999999");
		addrBasiDlvCst.setMbAddrLocation("01");
		shipCostList.add(addrBasiDlvCst);
		intOutAddress.setAddrBasiDlvCst(shipCostList);
		
		return intOutAddress;
	}
	
}
