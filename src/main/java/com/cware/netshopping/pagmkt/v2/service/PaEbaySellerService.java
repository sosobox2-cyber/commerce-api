package com.cware.netshopping.pagmkt.v2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.pagmkt.common.repository.PaGmktCommonDAO;
import com.cware.netshopping.pagmkt.common.service.PaGmktCommonService;
import com.cware.netshopping.pagmkt.v2.domain.EbayShipCost;
import com.cware.netshopping.pagmkt.v2.domain.SellerAddress;
import com.cware.netshopping.pagmkt.v2.domain.ShippingFee;
import com.cware.netshopping.pagmkt.v2.domain.ShippingPlace;
import com.cware.netshopping.pagmkt.v2.domain.ShippingPolicy;
import com.cware.netshopping.pagmkt.v2.repository.PaGmktShipCostMapper;

@Service
public class PaEbaySellerService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("pagmkt.common.paGmktCommonService")
	private PaGmktCommonService paGmktCommonService;

	@Autowired
	@Qualifier("pagmkt.common.paGmktCommonDAO")
	private PaGmktCommonDAO paGmktCommonDAO;

	@Autowired
	PaEbaySellerApiService sellerApiService;

	@Autowired
	PaGmktShipCostMapper shipCostMapper;

	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 이베이 판매자주소록 등록
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paAddrGb
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerEntpSlip(String entpCode, String entpManSeq, String paAddrGb, String procId,
			long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + entpManSeq);
		serviceLog.setTransType(TransType.SELLER.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]이베이-판매자주소록등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.GMARKET.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 판매자주소록등록
		ResponseMsg result = callRegisterEntpSlip(entpCode, entpManSeq, paAddrGb, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 이베이 판매자주소록 수정
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paAddrGb
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg updateEntpSlip(String entpCode, String entpManSeq, String paAddrGb, String procId,
			long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + entpManSeq);
		serviceLog.setTransType(TransType.SELLER.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]이베이-판매자주소록수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.GMARKET.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 판매자주소록수정
		ResponseMsg result = callUpdateEntpSlip(entpCode, entpManSeq, paAddrGb, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 이베이 배송비정책 연동
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg transShipCost(String entpCode, String entpManSeq, String shipCostCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + entpManSeq + shipCostCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]이베이-배송비정책연동");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.GMARKET.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		//  배송비정책연동
		ResponseMsg result = callTransShipCost(entpCode, entpManSeq, shipCostCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}
	
	private ResponseMsg callRegisterEntpSlip(String entpCode, String entpManSeq, String paAddrGb, String procId,
			long transServiceNo) {
		
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 판매자주소록등록서비스 Start - {}-{} =====", entpCode, entpManSeq);

			dateTime = systemService.getSysdatetimeToString();

			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb(paAddrGb);
			paEntpSlip.setPaCode(PaCode.EBAY_TV.code());

			Map<String, String> entpUserMap = paGmktCommonDAO.selectEntpShipInsert(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 이베이 판매자주소 전문생성
			SellerAddress sellerAddress = createSellerAddress(paEntpSlip, entpUserMap, transServiceNo);

			log.info("판매자주소록등록 API 호출 {}-{}", entpCode, entpManSeq);
			String paAddrSeq = sellerApiService
					.registerSellerAddress(entpCode, entpManSeq, sellerAddress, transServiceNo);

			if (StringUtils.hasText(paAddrSeq)) {
				
				result.setCode("200");
				result.setMessage(PaEbayApiRequest.API_SUCCESS_CODE);

				paEntpSlip.setPaAddrSeq(paAddrSeq);
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setInsertId(procId);
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				

				rtnMsg = paGmktCommonService.savePaGmktEntpSlipTx(paEntpSlip);

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
			log.info("===== 판매자주소록등록서비스 End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}
	
	private ResponseMsg callUpdateEntpSlip(String entpCode, String entpManSeq, String paAddrGb, String procId,
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
			paEntpSlip.setPaAddrGb(paAddrGb);
			paEntpSlip.setPaCode(PaCode.EBAY_TV.code());

			Map<String, String> entpUserMap = paGmktCommonDAO.selectEntpShipModify(paEntpSlip);
			if (entpUserMap == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 이베이 판매자주소 전문생성
			SellerAddress sellerAddress = createSellerAddress(paEntpSlip, entpUserMap, transServiceNo);

			log.info("판매자주소록수정 API 호출 {}-{}", entpCode, entpManSeq);
			String paAddrSeq = sellerApiService
					.updateSellerAddress(entpCode, entpManSeq, sellerAddress, transServiceNo);

			if (StringUtils.hasText(paAddrSeq)) {
				
				result.setCode("200");
				result.setMessage(PaEbayApiRequest.API_SUCCESS_CODE);

				paEntpSlip.setPaAddrSeq(paAddrSeq);
				paEntpSlip.setTransTargetYn("0");
				paEntpSlip.setInsertId(procId);
				paEntpSlip.setModifyId(procId);
				paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				

				rtnMsg = paGmktCommonService.savePaGmktEntpSlipUpdateTx(paEntpSlip);

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
			log.info("===== 판매자주소록수정서비스 End - {}-{} =====", entpCode, entpManSeq);
		}
		return result;
	}

	private SellerAddress createSellerAddress(PaEntpSlip paEntpSlip, Map<String, String> entpUserMap, long transServiceNo) throws Exception {

		SellerAddress sellerAddress = new SellerAddress();

		if (StringUtils.hasText(entpUserMap.get("PA_ADDR_SEQ"))) {
			sellerAddress.setAddrNo(entpUserMap.get("PA_ADDR_SEQ"));
		}
		
		String addrPrefix = "Y".equals(systemService.getConfig("PA_REAL_SERVER_YN").getVal()) ? "REAL" : "DEV";
		
		sellerAddress.setAddrName(addrPrefix + "_" + entpUserMap.get("ENTP_CODE") + "_" + entpUserMap.get("ENTP_NAME") + "_" + entpUserMap.get("ENTP_MAN_SEQ"));
		sellerAddress.setRepresentativeName("SK스토아");
		sellerAddress.setZipCode(entpUserMap.get("POST_NO"));
		
		if ("1".equals(entpUserMap.get("ROAD_ADDR_YN"))) {
			sellerAddress.setAddr1(entpUserMap.get("ROAD_POST_ADDR"));
			sellerAddress.setAddr2(entpUserMap.get("ROAD_ADDR"));
		} else {
			sellerAddress.setAddr1(entpUserMap.get("POST_ADDR"));
			sellerAddress.setAddr2(entpUserMap.get("ADDR"));
		}

		String homeTel = entpUserMap.get("ENTP_MAN_TEL");
		sellerAddress.setHomeTel(homeTel.length() >= 13 ? homeTel.substring(0, 13) : homeTel);
		sellerAddress.setCellPhone(entpUserMap.get("ENTP_MAN_HP"));
		
		sellerAddress.setVisitAndTakeAddr(false);
		sellerAddress.setReturnAddr(false);
		
		return sellerAddress;
	}

	private ResponseMsg callTransShipCost(String entpCode, String entpManSeq, String shipCostCode, String procId, long transServiceNo) {
		
		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 배송비정책연동서비스 Start - {}-{} =====", entpCode, entpManSeq);

			EbayShipCost shipCost = shipCostMapper.getTransTarget(entpCode, entpManSeq, shipCostCode);
			if (shipCost == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			// 이베이 출하지 전문생성
			ShippingPlace shippingPlace = createShippingPlace(shipCost, transServiceNo);
			
			String placeNo; // 출하지번호
			
			// 출하지연동
			if (shippingPlace.getPlaceNo() == null) {
				log.info("출하지등록 API 호출 {}-{} {}", entpCode, entpManSeq, shipCostCode);
				placeNo = sellerApiService
						.registerShippingPlace(entpCode, entpManSeq, shipCostCode, shippingPlace, transServiceNo);
			} else {
				log.info("출하지수정 API 호출 {}-{} {}", entpCode, entpManSeq, shipCostCode);
				placeNo = sellerApiService
						.updateShippingPlace(entpCode, entpManSeq, shipCostCode, shippingPlace, transServiceNo);
				
			}
			
			if (StringUtils.hasText(placeNo)) {

				shipCost.setGmktShipNo(placeNo);
				
				// 이베이 묶음배송비정책 전문생성
				ShippingPolicy shippingPolicy = createShippingPolicy(shipCost, transServiceNo);
				
				String policyNo; // 묶음배송비정책번호

				// 묶음배송비정책연동
				if (shippingPolicy.getPolicyNo() == null) {
					log.info("묶음배송비정책등록 API 호출 {}-{} {}", entpCode, entpManSeq, shipCostCode);
					policyNo = sellerApiService
							.registerShippingPolicy(entpCode, entpManSeq, shipCostCode, shippingPolicy, transServiceNo);
				} else {
					log.info("묶음배송비정책수정 API 호출 {}-{} {}", entpCode, entpManSeq, shipCostCode);
					policyNo = sellerApiService
							.updateShippingPolicy(entpCode, entpManSeq, shipCostCode, shippingPolicy, transServiceNo);
				}

				if (StringUtils.hasText(policyNo)) {
	
					shipCost.setBundleNo(policyNo);
					shipCost.setModifyId(procId);
					
					if (shipCostMapper.updateCompleteTrans(shipCost) < 1) {
						result.setCode("500");
						result.setMessage("배송비정책 전송결과 업데이트 실패");
					} else {
						result.setCode("200");
						result.setMessage(PaEbayApiRequest.API_SUCCESS_CODE);
					}
				} else {
					result.setCode("500");
					result.setMessage("묶음배송비정책 연동 실패");
				}

			} else {
				result.setCode("500");
				result.setMessage("출하지 연동 실패");
			}

		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 배송비정책연동서비스 End - {}-{} {} =====", entpCode, entpManSeq, shipCostCode);
		}
		return result;
	}
	

	private ShippingPlace createShippingPlace(EbayShipCost shipCost, long transServiceNo) throws Exception {

		ShippingPlace shippingPlace = new ShippingPlace();

		if (shipCost.getGmktShipNo().compareTo("0") > 0) {
			shippingPlace.setPlaceNo(shipCost.getGmktShipNo());
		}
		
		String addrPrefix = "Y".equals(systemService.getConfig("PA_REAL_SERVER_YN").getVal()) ? "REAL" : "DEV";
		
		shippingPlace.setAddrNo(shipCost.getPaAddrSeq());
		shippingPlace.setPlaceName(addrPrefix + "_" + shipCost.getEntpCode() + "_" + shipCost.getEntpManSeq() + "_" + shipCost.getShipCostCode());

		double addIslandCost = 0;
		double addJejuCost = 0;
		
		if (shipCost.getIslandCost() > shipCost.getOrdCostAmt())
			addIslandCost = shipCost.getIslandCost() - shipCost.getOrdCostAmt();

		if (shipCost.getJejuCost() > shipCost.getOrdCostAmt())
			addJejuCost = shipCost.getJejuCost() - shipCost.getOrdCostAmt();
		
		shippingPlace.setSetAdditionalShippingFee(true);
		shippingPlace.setBackwoodsAdditionalShippingFee((int)addIslandCost);
		shippingPlace.setJejuAdditionalShippingFee((int)addJejuCost);
		shippingPlace.setDefaultShippingPlace(false);
		
		return shippingPlace;
	}
	
	private ShippingPolicy createShippingPolicy(EbayShipCost shipCost, long transServiceNo) throws Exception {

		ShippingPolicy shippingPolicy = new ShippingPolicy();

		if (StringUtils.hasLength(shipCost.getBundleNo())) {
			shippingPolicy.setPolicyNo(shipCost.getBundleNo());
		}
		
		switch (shipCost.getShipCostCode().substring(0, 2)) {
		case "FR": // 무료
			shippingPolicy.setFeeType(1);
			shippingPolicy.setFee(0);
			break;
		case "CN":
		case "PL": // 조건부
			shippingPolicy.setFeeType(3);
			shippingPolicy.setFee((int)shipCost.getOrdCostAmt());
			break;
		default: // 상품별
			shippingPolicy.setFeeType(2);
			shippingPolicy.setFee((int)shipCost.getOrdCostAmt());
			break;
		}

		shippingPolicy.setPrepayment(true);
		shippingPolicy.setCashOnDelivery(false);
		shippingPolicy.setPlaceNo(shipCost.getGmktShipNo());
		shippingPolicy.setDefault(false);
		
		//착불배송비
		if("2".equals(shipCost.getShipCostReceipt())) { 
			shippingPolicy.setFeeType(2); //유료배송비
			shippingPolicy.setFee((int)shipCost.getOrdCostAmt());
			shippingPolicy.setPrepayment(false); //선결제아님
			shippingPolicy.setCashOnDelivery(true); //착불
		}
		
		if (shippingPolicy.getFeeType() == 3) {
			// 조건부기준금액
			List<ShippingFee> shippingFee = new ArrayList<ShippingFee>();
			shippingFee.add(new ShippingFee((int)shipCost.getShipCostBaseAmt()));
			shippingPolicy.setShippingFee(shippingFee);
		}
		
		return shippingPolicy;
	}
}
