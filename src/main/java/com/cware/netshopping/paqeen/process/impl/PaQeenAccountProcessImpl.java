package com.cware.netshopping.paqeen.process.impl;


import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.model.PaQeenSettlement;
import com.cware.netshopping.paqeen.domain.Settlement;
import com.cware.netshopping.paqeen.domain.SettlementListResponse;
import com.cware.netshopping.paqeen.process.PaQeenAccountProcess;
import com.cware.netshopping.paqeen.repository.PaQeenAccountDAO;
import com.cware.netshopping.paqeen.util.PaQeenApiRequest;
import com.cware.netshopping.paqeen.util.PaQeenConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("paqeen.account.paQeenAccountProcess")
public class PaQeenAccountProcessImpl extends AbstractProcess implements PaQeenAccountProcess {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PaQeenApiRequest paQeenApiRequest;
	
	@Autowired
	private PaQeenConnectUtil paQeenConnectUtil;
	
	@Resource(name = "paqeen.account.paQeenAccountDAO")
	PaQeenAccountDAO paQeenAccountDAO;

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getQeenAccountSettlementList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		
		String apiCode = "IF_PAQEENAPI_05_001";
		String duplicateCheck = "";
		String paCode  = "";
		int page = 1;
		ParamMap paramMap = new ParamMap();
		ParamMap retrieveDate = null;
		PaQeenSettlement paQeenSettlement = new PaQeenSettlement(); 
		try {
			
			log.info("======= 퀸잇 정산데이터 목록조회 API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paQeenContractCnt = Constants.PA_QEEN_CONTRACT_CNT;
			
			for(int i = 0; i< paQeenContractCnt; i ++) {
				boolean nextPageFlag = true;
				Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				page = 1;
				log.info("03.API Request Setting");
				retrieveDate = paQeenConnectUtil.setRetrieveDateFormat(fromDate, toDate, "5");
				fromDate = retrieveDate.getString("startAt");
				toDate = retrieveDate.getString("endAt");
				 
				while(nextPageFlag) {
					HashMap<String, Object> apiRequestObject  = new HashMap<String, Object>();
					apiRequestObject.put("page", ComUtil.objToStr(page)); // 페이지 더 돌려야함 
					apiRequestObject.put("size", "40");
					apiRequestObject.put("startAt", fromDate);
					apiRequestObject.put("endAt", toDate);
					apiRequestObject.put("includeNullBrandCode", true);
					
					log.info("04.API Call");
					responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, null);
					
					ObjectMapper objectMapper = new ObjectMapper();
					SettlementListResponse settlementListResponse = new SettlementListResponse();
					settlementListResponse = objectMapper.convertValue(responseMap, SettlementListResponse.class);
					if(((List<Map<String, Object>>)(responseMap.get("list"))).size() < 1 || settlementListResponse.getTotalPageCount() <= ComUtil.objToLong(page)) {
						nextPageFlag = false;
					}else {
						page++;
					}
					
					if(settlementListResponse.getList() != null) {
						log.debug("getSettlementList succeed");
						paramMap.put("code", "200");
						if(settlementListResponse.getList().size() > 0) {
							for(Settlement Settlement : settlementListResponse.getList()) {
								try {
									paQeenSettlement.setId(Settlement.getId());
									if(paQeenAccountDAO.selectSettlementSeq(paQeenSettlement) == 0 && Settlement.getBaseDate() != null) {
										paQeenSettlement.setTransactionType(Settlement.getTransactionType());
										paQeenSettlement.setOrderId(Settlement.getOrderId());
										paQeenSettlement.setOrderLineGroupId(Settlement.getOrderLineGroupId());
										paQeenSettlement.setOrderLineId(Settlement.getOrderLineId());
										paQeenSettlement.setSellerShare(Settlement.getShare().getSellerShare());
										paQeenSettlement.setSellerDutyDiscount(Settlement.getShare().getSellerDutyDiscount());
										paQeenSettlement.setQueenitShare(Settlement.getShare().getQueenitShare());
										paQeenSettlement.setQueenitDutyDiscount(Settlement.getShare().getQueenitDutyDiscount());
										paQeenSettlement.setAmount(Settlement.getPayment().getAmount());
										paQeenSettlement.setMethod(Settlement.getPayment().getMethod());
										paQeenSettlement.setType(Settlement.getPayment().getType());
										paQeenSettlement.setSellerCompensationAmount(Settlement.getSellerCompensation().getSellerCompensationAmount());
										paQeenSettlement.setLowestBaseDate(Settlement.getBase().getLowestBaseDate());
										paQeenSettlement.setCommissionPercentage(Settlement.getBase().getCommissionPercentage());
										paQeenSettlement.setIncludedBookId(Settlement.getIncludedBookId());
										paQeenSettlement.setBaseDate(Settlement.getBaseDate());
										paQeenSettlement.setSalesType(Settlement.getSalesType());
										paQeenSettlement.setMemo(Settlement.getMemo());
										paQeenSettlement.setProductId(Settlement.getProduct().getId());
										paQeenSettlement.setProductCode(Settlement.getProduct().getProductCode());
										paQeenSettlement.setOptionTitle(Settlement.getProduct().getOptionTitle());
										paQeenSettlement.setProductTitle(Settlement.getProduct().getProductTitle());
										paQeenSettlement.setSingleProductPrice(Settlement.getProduct().getSingleProductPrice());
										paQeenSettlement.setQuantity(Settlement.getProduct().getQuantity());
										paQeenSettlement.setCommissionTarget(Settlement.getProfit().getCommissionTarget());
										paQeenSettlement.setCommission(Settlement.getProfit().getCommission());
										paQeenSettlement.setSellerProfit(Settlement.getProfit().getSellerProfit());
										paQeenSettlement.setQueenitProfit(Settlement.getProfit().getQueenitProfit());
										paQeenSettlement.setEventAtMillis(new Date(Settlement.getEventAtMillis()));
										if(Settlement.getDeliveryCompletedAtMillis() != 0) {
											paQeenSettlement.setDeliveryCompletedAtMillis(new Date(Settlement.getDeliveryCompletedAtMillis()));
										}
										paQeenSettlement.setSellerDirectCouponDiscountAmount(Settlement.getDiscountDetail().getSellerDirectCouponDiscountAmount());
										paQeenSettlement.setSellerIssuedCouponDiscountAmount(Settlement.getDiscountDetail().getSellerIssuedCouponDiscountAmount());
										paQeenSettlement.setQueenitDirectCouponDiscountAmount(Settlement.getDiscountDetail().getQueenitDirectCouponDiscountAmount());
										paQeenSettlement.setQueenitIssuedCouponDiscountAmount(Settlement.getDiscountDetail().getQueenitIssuedCouponDiscountAmount());
										paQeenSettlement.setQueenitDiscountAmountByCoupon(Settlement.getDiscountDetail().getQueenitDiscountAmountByCoupon());
										paQeenSettlement.setQueenitPointDiscountAmount(Settlement.getDiscountDetail().getQueenitPointDiscountAmount());
										paQeenAccountDAO.insertPaQeenSettlement(paQeenSettlement);
									}
								} catch (Exception e) {
									log.error(e.getMessage()); 
									continue;
								}
							}
						}
					}else {
						nextPageFlag = false;
					}
				}
			}
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 정산데이터목록조회 API End - {} =======");
		}
		
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
}
