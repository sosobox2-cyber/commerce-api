package com.cware.netshopping.panaver.v3.process.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PanaversettlementVO;
import com.cware.netshopping.panaver.v3.domain.Pagination;
import com.cware.netshopping.panaver.v3.domain.PeriodType;
import com.cware.netshopping.panaver.v3.domain.ProductOrderType;
import com.cware.netshopping.panaver.v3.domain.SettleInfo;
import com.cware.netshopping.panaver.v3.domain.SettleInfoListResponse;
import com.cware.netshopping.panaver.v3.domain.SettleType;
import com.cware.netshopping.panaver.v3.process.PaNaverV3AccountProcess;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3AccountDAO;
import com.cware.netshopping.panaver.v3.service.PaNaverV3AccountService;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("panaver.v3.account.paNaverV3AccountProcess")
public class PaNaverV3AccountProcessImpl extends AbstractProcess implements PaNaverV3AccountProcess {
	
	@Autowired
	private SystemDAO systemDAO;

	@Autowired
	private PaNaverV3AccountDAO paNaverV3AccountDAO;
	
	@Autowired
	private PaNaverV3ConnectUtil paNaverV3ConnectUtil;
	
	@Resource(name = "panaver.v3.account.paNaverV3AccountService")
	private PaNaverV3AccountService paNaverV3AccountService;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Override
	public ResponseMsg getCaseSettleList(String searchDate, int pageNumber, int pageSize, String delYn, String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_05_001";
		String duplicateCheck = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<PanaversettlementVO> arrPaNaverAccountList = new ArrayList<PanaversettlementVO>();
		PanaversettlementVO paNaverSettlement = null;
		
		boolean isRequestDone = false;
		int dtChk = 0;
		
		String dateTime = systemService.getSysdatetimeToString();
		Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);

		log.info("======= 네이버 건별 정산 내역 조회 API Start =======");
		
		try {
			// API 중복실행 체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			paramMap.put("paCode", Constants.PA_NAVER_CODE);
			paramMap.put("searchDate", searchDate);
			
			// 기간 입력 (Default D-1)
			if ((searchDate.isEmpty() && searchDate.equals(""))) {
				searchDate = ComUtil.dateFormater(DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT), DateUtil.NAVER_DATE_FORMAT);
			} else {
				searchDate = ComUtil.dateFormater(searchDate, DateUtil.NAVER_DATE_FORMAT);
			}
			
			if (!delYn.equals("Y") && !delYn.equals("y")) {
				log.info("정산일자 중복데이터 체크");
				dtChk = paNaverV3AccountService.selectChkPaNaverAccount(paramMap);
			}
			
			if (dtChk > 0) {
				paramMap.put("code", "490");
				paramMap.put("message", getMessage("ssg.cannot_dup_data", new String[]{"날짜: "+searchDate}));
				return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
			}
			
			/** 1) 전문 데이터 세팅  **/	
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			queryParameters.put("searchDate", searchDate);
			queryParameters.put("periodType", PeriodType.SETTLE_CASEBYCASE_SETTLE_BASIS_DATE.toString()); // 정산 기준일
			queryParameters.put("settleType", SettleType.NORMAL_SETTLE_ORIGINAL.toString()); // 일반정산
			queryParameters.put("pageNumber", Integer.toString(pageNumber));
			queryParameters.put("pageSize", Integer.toString(pageSize));
			
			// Path Parameters
			String pathParameters = "";
			
			// VO 선언
			SettleInfoListResponse response = new SettleInfoListResponse();
			
			while (!isRequestDone) {
				log.info("네이버 건별 정산 내역 조회  API 호출");
				
				/** 2) 네이버 건별 정산 내역 조회 호출 **/
				resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, null);
				
				// Map -> VO 변환
				ObjectMapper objectMapper = new ObjectMapper();
				response = objectMapper.convertValue(resultMap, SettleInfoListResponse.class);
				
				List<SettleInfo> responseData = response.getSettleInfoInfoList();
				
				if (responseData != null) {
					paramMap.put("code", "200");

					if (responseData.size() > 0) {
						Pagination paging = response.getPagination();
						
						isRequestDone = (paging.getTotalPages() > paging.getPage()) ? false : true;

						for (int i=0; i<responseData.size(); i++) {
							SettleInfo settleInfo = responseData.get(i);

							paNaverSettlement = new PanaversettlementVO();
							paNaverSettlement.setPaCode(Constants.PA_NAVER_CODE);
							paNaverSettlement.setSettleBasicDate(DateUtil.toTimestamp(settleInfo.getSettleBasisDate(), "yyyy-MM-dd"));
							paNaverSettlement.setSettleExpectDate(DateUtil.toTimestamp(settleInfo.getSettleExpectDate(),"yyyy-MM-dd"));
							paNaverSettlement.setSettleCompleteDate(DateUtil.toTimestamp(settleInfo.getSettleCompleteDate(), "yyyy-MM-dd"));
							paNaverSettlement.setOrderId(settleInfo.getOrderId());
							paNaverSettlement.setProductOrderId(settleInfo.getProductOrderId());
							paNaverSettlement.setProductOrderType(ProductOrderType.valueOf(settleInfo.getProductOrderType()).codeName());
							paNaverSettlement.setSettleType(SettleType.valueOf(settleInfo.getSettleType()).codeName());
							paNaverSettlement.setProductId(settleInfo.getProductId());
							paNaverSettlement.setProductName(settleInfo.getProductName());
							paNaverSettlement.setPurchaserName(settleInfo.getPurchaserName());
							paNaverSettlement.setPaySettleAmt(settleInfo.getPaySettleAmount());
							paNaverSettlement.setTotalPayCommiAmt(settleInfo.getTotalPayCommissionAmount());
							paNaverSettlement.setFreeInstallmentCommiAmt(settleInfo.getFreeInstallmentCommissionAmount());
							paNaverSettlement.setSellingInterlockCommiAmt(settleInfo.getSellingInterlockCommissionAmount());
							paNaverSettlement.setBenefitSettleAmt(settleInfo.getBenefitSettleAmount());
							paNaverSettlement.setSettleExpectAmt(settleInfo.getSettleExpectAmount());
							paNaverSettlement.setInsertId("BATCH");
							paNaverSettlement.setInsertDate(sysdateTime);
							paNaverSettlement.setDelYn(delYn); // 수기처리시 사용 > 조회일 기준 정산 데이터 삭제 후 재저장

							arrPaNaverAccountList.add(paNaverSettlement);
						}
						
						// 페이징 처리
						if (!isRequestDone) {
							queryParameters.put("pageNumber", Integer.toString(++pageNumber));
						}
					} else {
						isRequestDone = true;
					}
				} else {
					paramMap.put("code", response.getErrorCode());
					paramMap.put("message", response.getMessage());
					return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
				}
			}
			paramMap.put("code", "200");
			
			/** 3) 네이버 정산매출정보 저장 및 업데이트 **/
			if (arrPaNaverAccountList.size() > 0) {
				paNaverV3AccountService.saveSettlementListTx(arrPaNaverAccountList);
				paramMap.put("message", "OK");
			} else {
				paramMap.put("message", "건별 정산 내역 (0)건 조회");
			}
		} catch (TransApiException ex) {
			paramMap.put("code", "500");
			paramMap.put("message", ex.getMessage());
			log.error(ex.getMessage(), ex);
		} catch (Exception e) {
			paNaverV3ConnectUtil.dealException(e, paramMap);
			log.error(e.getMessage(), e);
		} finally {
			try {
				paNaverV3ConnectUtil.dealSuccess(paramMap, request);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			
			if (duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
			
			log.info("======= 네이버 건별 정산 내역 조회 API End =======");
		}
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public int selectChkPaNaverAccount(ParamMap paramMap) throws Exception {
		return paNaverV3AccountDAO.selectChkPaNaverAccount(paramMap);
	}

	@Override
	public String saveSettlementList(List<PanaversettlementVO> arrPaNaverAccountList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		PanaversettlementVO paNaverSettlement = new PanaversettlementVO();
		int executedRtn = 0;
		String paNaverSalesSeq = "";
		HashMap<String, Object> hashMap = null;
		String paDoFlag = "";
		
		if (arrPaNaverAccountList.get(0).getDelYn().equals("y") || arrPaNaverAccountList.get(0).getDelYn().equals("Y")) {
			executedRtn = paNaverV3AccountDAO.deletePaNaverAccount(arrPaNaverAccountList.get(0)); // 매출내역 삭제
		}
		
		for (int i=0; arrPaNaverAccountList.size() > i; i++) {
			paNaverSettlement = arrPaNaverAccountList.get(i);
			
			hashMap = new HashMap<String, Object>();
    		hashMap.put("sequence_type", "PANAVER_SALES_NO");
    		
    		paNaverSalesSeq = (String) systemDAO.selectSequenceNo(hashMap);
    		paNaverSettlement.setPanavrSalesNo(paNaverSalesSeq);
    		
    		// 정산 매출정보 저장 (TPANAVERSETTLEMENT)
    		executedRtn = paNaverV3AccountDAO.insertPaNaverSettlement(paNaverSettlement);
    		
    		if (executedRtn < 1) {
    			throw processException("msg.cannot_save", new String[] { "TPANAVRSALES INSERT" });
    		}
    		
    		// 일반정산 && 상품주문건 > 정산 매출정보 업데이트 (TPAORDERM)
    		if (paNaverSettlement.getSettleType().equals(SettleType.NORMAL_SETTLE_ORIGINAL.codeName()) 
    				&& paNaverSettlement.getProductOrderType().equals(ProductOrderType.PROD_ORDER.codeName())) {
    			paDoFlag = paNaverV3AccountDAO.selectPaDoFlagCheck(paNaverSettlement);
    			
    			if (paDoFlag != null) {
    				paNaverSettlement.setPaDoFlag(paDoFlag);
    				paNaverSettlement.setModifyDate(arrPaNaverAccountList.get(i).getInsertDate());
    				
    				executedRtn = paNaverV3AccountDAO.updatePaorderm(paNaverSettlement);
    				
    				if (executedRtn < 1) {
    					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
    				}
    			}
    		}
		}
		return rtnMsg;
	}
}
