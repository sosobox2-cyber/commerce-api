package com.cware.netshopping.patdeal.process.impl;


import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.model.PaTdealSettlement;
import com.cware.netshopping.patdeal.domain.PaTdealSettlementResponse;
import com.cware.netshopping.patdeal.process.PaTdealAccountProcess;
import com.cware.netshopping.patdeal.repository.PaTdealAccountDAO;
import com.cware.netshopping.patdeal.util.PaTdealApiRequest;
import com.cware.netshopping.patdeal.util.PaTdealConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("patdeal.account.paTdealAccountProcess")
public class PaTdealAccountProcessImpl extends AbstractProcess implements PaTdealAccountProcess {

	@Autowired
	private PaTdealConnectUtil paTdealConnectUtil;
	
	@Autowired
	private PaTdealApiRequest paTdealApiRequest;
	
	@Autowired
	private PaTdealAccountDAO patdealAccountDAO;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getTdealAccountSettlementList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_05_002";
		String duplicateCheck = "";
		String paCode  = "";
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 파트너 정산 상세 데이터 조회 API Start - {} =======");
		
		try {
			
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for(int i = 0; i< Constants.PA_TDEAL_CONTRACT_CNT ; i ++) {
				
				paCode = (i==0 )? Constants.PA_TDEAL_BROAD_CODE : Constants.PA_TDEAL_ONLINE_CODE;
				/** 1) 전문 데이터 세팅  **/
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dateTime = systemService.getSysdatetimeToString();
				
				Calendar from = Calendar.getInstance();
				Calendar to = Calendar.getInstance();
				
				if (fromDate != null && !fromDate.equals("")) {
					from.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(fromDate));
				} else {
					from.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTime));
					from.add(Calendar.DATE, -30);
				}
				
				if (toDate != null && !toDate.equals("")
						) {
					to.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(toDate));
				} else {
					to.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTime));
				}
			
				// Query Parameters 세팅
				Map<String, String> queryParameters = new HashMap<String, String>();
				
				//필요한 파라미터 및 통신 세팅
				queryParameters.put("startYmd", dateFormat.format(from.getTime()));
				queryParameters.put("endYmd", dateFormat.format(to.getTime()));
				queryParameters.put("settlementPartnerType", "DOMESTIC");
				
				ParamMap apiDataObject = new ParamMap();
				
				PaTdealSettlementResponse paTdealSettlementResponse = new PaTdealSettlementResponse();
				
				//통신
				responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
				
				if("V0004".equals(responseMap.get("code"))) {
					continue;
				}
				
				if(((List<Map<String, Object>>)(responseMap.get("Data"))).size() < 1) {
					continue;
				}
				
				// vo변환
				ObjectMapper objectMapper = new ObjectMapper();
				paTdealSettlementResponse = objectMapper.convertValue(responseMap, PaTdealSettlementResponse.class);
				
				if(paTdealSettlementResponse.getData() != null) {
					log.debug("getTdealSettlement succeed");
					paramMap.put("code", "200");
					if(paTdealSettlementResponse.getData().size() > 0) {
						
						// paTdealSettlement 테이블에 저장 
						for(PaTdealSettlement settlement : paTdealSettlementResponse.getData()) {
							try {
								if(settlement.getCommissionRate() > 0 && (settlement.getOrderCnt() > 0 || settlement.getRefundCnt() > 0)) {
									settlement.setOptionManagementCd(settlement.getOptionManagementCd().toString().replaceAll("_", ""));
								}else {
									settlement.setOrderGb(settlement.getSettlementDeliveryTypeLabel().substring(1,3).equals("초도")?"1" : "2");
									settlement.setShippingNo(settlement.getSettlementDeliveryTypeLabel().substring(8,16));
								}
								
								if(patdealAccountDAO.selectSettlementSeq(settlement) == 0) {
									patdealAccountDAO.insertPaTdealSettlement(settlement);
								}
								
							} catch (Exception e) {
								log.error(e.getMessage()); // 에러처리 어떻게 할지 추후에 
								paramMap.put("code", "500");
								paramMap.put("message", "정산 내역 입력중 에러 발생 ");
								continue;
							}
						}
					}else {
						log.debug("settlement count : 0");
						paramMap.put("message", " 정산 내역 (0) 건 조회");
					}
				}
			}
		}catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(paramMap ,  e);
		} finally {
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "정산 내역 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			paTdealConnectUtil.closeApi(request, paramMap);
		}
		
		log.info("======= 파트너 정산 상세 데이터 조회 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	
	
}
