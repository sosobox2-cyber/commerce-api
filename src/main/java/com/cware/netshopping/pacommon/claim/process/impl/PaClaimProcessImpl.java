package com.cware.netshopping.pacommon.claim.process.impl;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
/**
* Order ProcessImpl
* 
* company        author   date               Description
* commerceware   shnam    2018.04.16        ecam4j 소스 제거
*/
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.common.util.HttpClientUtils;
import com.cware.netshopping.core.custcenter.domain.OrderShipCostVO;
import com.cware.netshopping.core.custcenter.process.OrderBizProcess;
import com.cware.netshopping.core.exchange.domain.SmsSendVO;
import com.cware.netshopping.core.exchange.process.UmsBizProcess;
import com.cware.netshopping.domain.ClaimdtVO;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.OrderdtVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.OrderreceiptsVO;
import com.cware.netshopping.domain.OrdershipcostVO;
import com.cware.netshopping.domain.OrderstockVO;
import com.cware.netshopping.domain.PaClaimdtVO;
import com.cware.netshopping.domain.ReceiverVO;
import com.cware.netshopping.domain.StockVO;
import com.cware.netshopping.domain.model.Canceldt;
import com.cware.netshopping.domain.model.Claimdt;
import com.cware.netshopping.domain.model.Custcounseldt;
import com.cware.netshopping.domain.model.Custcounselm;
import com.cware.netshopping.domain.model.Ordercostapply;
import com.cware.netshopping.domain.model.Ordergoods;
import com.cware.netshopping.domain.model.Orderreceipts;
import com.cware.netshopping.domain.model.Ordershipcost;
import com.cware.netshopping.domain.model.PaOrderShipCost;
import com.cware.netshopping.domain.model.Planshipcost;
import com.cware.netshopping.domain.model.Promocounsel;
import com.cware.netshopping.domain.model.ReqoutApiHistory;
import com.cware.netshopping.domain.model.CjOneDayToken;
import com.cware.netshopping.domain.model.CjSlipError;
import com.cware.netshopping.pacommon.claim.process.PaClaimProcess;
import com.cware.netshopping.pacommon.claim.repository.PaClaimDAO;
import com.cware.netshopping.pacommon.order.repository.PaOrderDAO;



@Service("pacommon.claim.paclaimProcess")
public class PaClaimProcessImpl extends AbstractService implements PaClaimProcess{

	@Resource(name = "pacommon.claim.paclaimDAO")
    private PaClaimDAO paclaimDAO;
	
	@Resource(name = "pacommon.order.paorderDAO")
    private PaOrderDAO paorderDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name="common.system.systemProcess")
	private SystemProcess systemProcess;
	
	@Resource(name = "core.custcenter.orderBizProcess")
	private OrderBizProcess orderBizProcess;
	
	//Core - SMS
	@Resource(name="core.exchange.umsBizProcess")
	private UmsBizProcess umsBizProcess;	
		
	/**
	 * 제휴사 반품접수
	 * @param OrderClaimVO
	 * @return HashMap<String, Object>
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@Override
	public HashMap<String, Object> saveOrderClaim(OrderClaimVO orderClaimVO) throws Exception {
		ParamMap paramMap = new ParamMap();
		OrderdtVO[] orderdt = null;
		PaClaimdtVO[] claimdt = null;
		Canceldt[] 	canceldt = null;
		OrderpromoVO[] orderpromo = null;
		ArrayList<Orderreceipts> orderreceiptsList = new ArrayList<Orderreceipts>();
		ReceiverVO receiver = new ReceiverVO();
		Ordergoods ordergoods = new Ordergoods();
		Planshipcost planshipcost = new Planshipcost();
		OrderClaimVO[] claimProcList = new OrderClaimVO[1];
		String recieverAddr = "";
		int executedRtn = 0;
		String rtnMsg = "000000";
		String rtnValue = "";
		HashMap resultMap = null;
		HashMap<String, Object> orderMap = null;
		Collection cClaimdt = new ArrayList();
		Collection cCanceldt = new ArrayList();
		Collection cOrderdt = null;
		Collection cOrderpromo = null;
		OrdershipcostVO[] ordershipcost = null;
		Ordercostapply [] ordercostapply= null;
		boolean is20Claim = false;
		List<OrderdtVO> orgOrderdtList4ShipCost = new ArrayList<OrderdtVO>();	 //출하지시 이후(운송장x) 취소건 배송비 계산 관련 변수	
		List<Ordercostapply> costApply4Gift = new ArrayList<Ordercostapply>();   //출하지시 이후(운송장x) 취소건 배송비 계산 관련 변수
		String userId = null;
		
		
		try{
			String proc_date = systemService.getSysdatetimeToString();
			//반품 건 조회
			cOrderdt = paclaimDAO.selectOrderDtInfo(orderClaimVO);
			orderdt = new OrderdtVO[cOrderdt.size()];
			orderdt = (OrderdtVO[])cOrderdt.toArray(new OrderdtVO[0]);
			userId = orderClaimVO.getInsertId();	
	
			//반품 DB 저장
			if(orderdt.length > 0){
				int k = 0; //= 본품 index
				
				//= 본품의 index를 찾음 : 본품과 사은품의 수량이 C/S처리 과정중에 달라질 수 있기 때문에 본품 기준으로 반품/취소 데이터를 생성하기 위함.
				for(int j = 0; orderdt.length > j; j++){
					if(orderdt[j].getOrderDSeq().equals("001")){
						k = j;
						break;
					}
				}
				
				//getProcPossQty = OrderQty - CanQty - CliamQty + ClaimCancelQty
				if((orderClaimVO.getClaimQty() <=orderdt[k].getSyslast() 
						&& orderClaimVO.getClaimQty() <= orderdt[k].getProcPossQty())){//반품접수
					
					paramMap.put("orderNo", 	orderdt[k].getOrderNo());
					paramMap.put("orderGSeq", 	orderdt[k].getOrderGSeq());
					paramMap.put("receiverSeq", orderdt[k].getReceiverSeq());
					paramMap.put("custNo", 		orderdt[k].getCustNo());
					paramMap.put("entpCode", 	orderdt[k].getEntpCode());
					paramMap.put("orderSyslast",paclaimDAO.selectOrderGoods(paramMap)); // ORDER_QTY -CANCEL_QTY - CLAIM_QTY+ CLAIM_CAN_QTY, 이름을 왜 이렇게 지었어 ㅠㅠ 
					//paramMap 20180502 PARKSEONJUN IP정보추가
					paramMap.put("localYn", 	orderClaimVO.getLocalYn());
					
					//주소체크
					String adrChk = ComUtil.NVL(orderClaimVO.getSameAddr());
					paramMap.put("sameAddr", "".equals(adrChk)? "0": adrChk);
					
					cOrderpromo = paclaimDAO.selectOrderPromo(paramMap);
					orderpromo = new OrderpromoVO[cOrderpromo.size()];
					orderpromo = (OrderpromoVO[])cOrderpromo.toArray(new OrderpromoVO[0]);
					
					//주문 배송지 조회 20180502 PARKSEONJUN sk스토아 기준으로 변경
					receiver = paclaimDAO.selectReceiverAddr(paramMap);
					
					claimProcList[0] = new OrderClaimVO();
					claimProcList[0].setClaimQty(orderClaimVO.getClaimQty());
					
					HashMap ExchangeGoodsdtCode = null;
					
					for(int i = 0; orderdt.length > i; i++){
						if( !orderdt[i].getOrderDSeq().equals("001") && Integer.parseInt(orderdt[i].getDoFlag()) < 30 ){
							//= 사은품일 경우 사은품의 배송 상태가(do_flag) 업체지시 이전일 경우 취소 data 생성
							cCanceldt.add(settingTcanceldt(orderClaimVO));
						} else {  //본품인 경우							
							cClaimdt.add(settingTclaimdt(orderClaimVO));
						}
						//교환 한 이력이 존재하면, 교환배송 데이터의 상품단품을 기준으로 회수처리 하도록 수정  by leejy 
						if(orderdt[i].getExchangeYn() > 0){							
							ExchangeGoodsdtCode = paclaimDAO.selectExchangeGoodsdtCode(orderdt[i]);							
							orderdt[i].setGoodsdtCode(ExchangeGoodsdtCode.get("GOODSDT_CODE").toString());
							orderdt[i].setGoodsdtInfo(ExchangeGoodsdtCode.get("GOODSDT_INFO").toString());							
						}		
					}
					
					claimdt = (PaClaimdtVO[]) cClaimdt.toArray(new PaClaimdtVO[0]); //본품   -> 반품 
					canceldt = (Canceldt[]) cCanceldt.toArray(new Canceldt[0]);     //사은품 -> 취소(배송전)
					is20Claim = orderClaimVO.getIsIs20Claim(); 		
					
					if(is20Claim){
						/** 이곳에 출하전 취소 처리 해줘야함**/
						
						costApply4Gift = getApply4Gift(orderdt, userId ,proc_date);
						orgOrderdtList4ShipCost = (ArrayList<OrderdtVO>) cOrderdt;
						ParamMap param4shipcost = new ParamMap();					
						param4shipcost.put("sysDateTime",DateUtil.toTimestamp(proc_date,"yyyy/MM/dd HH:mm:ss"));
						param4shipcost.put("orderNo",orderClaimVO.getOrderNo());
						param4shipcost.put("orderGSeq",orderClaimVO.getOrderGSeq());
						/* 출고전 반품(취소)은 부분 취소 막아버리기로 함.

						//param4shipcost.put("cancelQty",orderClaimVO.getClaimQty());
						int remainQty = selectRemainQty(param4shipcost);
						if(remainQty - orderClaimVO.getClaimQty() == 0 ) {   							
							for(OrderdtVO ot : orderdt){
								if(ot.getOrderDSeq().equals("001")){
									if( ot.getSyslast() == orderClaimVO.getClaimQty()){
										param4shipcost.put("cancelQty", orderClaimVO.getClaimQty()); //처음부터 전체 취소
									}else{
										
										param4shipcost.put("cancelQty", remainQty  + orderClaimVO.getClaimQty());  //나머지 몽땅 취소
									}
								}
							}
						}else{ //부분취소
							param4shipcost.put("cancelQty", orderClaimVO.getClaimQty());
						}
						*/

						
						param4shipcost.put("cancelQty", orderClaimVO.getClaimQty());
						param4shipcost.put("orgOrderdtList4ShipCost",orgOrderdtList4ShipCost);
						param4shipcost.put("userId",userId);
						param4shipcost.put("CostApply4Gift",costApply4Gift);
						
						HashMap<String, Object> obj = new HashMap<>();
						obj = settingTordershipcost4SKB(param4shipcost);
						ordershipcost  = (OrdershipcostVO[]) obj.get("OrderShipCost");
						ordercostapply = (Ordercostapply[]) obj.get("OrderCostApply");	
					}
					
								
					executedRtn = prepareSave(ordergoods, orderdt, claimdt, canceldt, orderpromo, claimProcList, proc_date);
					
					//treceiver 비교 후 insert  
					//이 함수의 receiver는 기존 Original임...
					executedRtn = modifiedTreceiver(orderdt, claimdt, receiver, paramMap, proc_date);
					//tclaimdt insert (중요 : 저장순서 1. tclaimdt insert, 2. tordergoods update > 순서 변경시 정산 금액 차액 발생. 저장순서 변경 금지)

					executedRtn = newClaimdt(claimdt, receiver, proc_date);
					//tordergoods update (중요 : 저장순서 1. tclaimdt insert, 2. tordergoods update > 순서 변경시 정산 금액 차액 발생. 저장순서 변경 금지)


					executedRtn = modifiedGoods(ordergoods);
					//torderpromo 비교
					executedRtn = modifiedOrderpromo(orderpromo, paramMap, proc_date, claimdt);
					//tcustcounselm,tcustcounseldt insert
					executedRtn = newCounsel(claimdt, paramMap, receiver, proc_date);
										
					executedRtn = newCanceldt(canceldt, claimdt, proc_date);
					
					//고객직접발송 인입 상담생성 - 하프클럽
					checkCustrDirectDelivery(orderdt, claimdt);
					
					// 반품접수시 업체이관 상담생성
					checkReturnReceiptAlert(orderdt, claimdt);
					
					if(is20Claim){
						
						ParamMap pm = new ParamMap();
						pm.put("orderNo",orderClaimVO.getOrderNo());
						pm.put("custNo",orderdt[k].getCustNo());
						pm.put("userId",orderClaimVO.getInsertId());
						pm.put("procdate",proc_date);
						pm.put("proc_date",proc_date);

						
					
						
						//insert TORDERRECEIPTS
						insertReceipts(orderreceiptsList,ordershipcost,pm);
						updateReceipts(orderreceiptsList);					
						//insert TORDERSHIPCOST
						executedRtn = insertOrdershipcost(ordershipcost);					
						
						//insert TORDERCOSTAPPLY
						//executedRtn = insertOrderCostApply(ordercostapply, claimdt);		
						//I:신규, U: 초도배송비에 대한 기존 10data UPDATE
						for(Ordercostapply vo : ordercostapply){
							if("I".equals(vo.getCwareAction())) {
								vo.setOrderWSeq(claimdt[0].getNewOrderWeq()); //신규의 경우 출고전 반품 데이터의 ORDER_W_SEQ를 가져오지 못함
							}
						}			
						executedRtn =  insertOrderCostApply(ordercostapply);						
						
						executedRtn = insertPaOrderShipCost4Cancel(ordershipcost, pm, claimdt, orderreceiptsList);
						
						//출고전반품 처리 (사은품까지 처리)
						for (int i = 0; i < claimdt.length; i++) {
							HashMap<String, Object> outclaimbef = new HashMap();
							outclaimbef.put("ORDER_NO", claimdt[i].getOrderNo());
							outclaimbef.put("ORDER_G_SEQ", claimdt[i].getOrderGSeq());
							outclaimbef.put("ORDER_D_SEQ", claimdt[i].getOrderDSeq());
							outclaimbef.put("ORDER_W_SEQ", claimdt[i].getOrderWSeq());
							outclaimbef.put("MODIFY_ID", orderClaimVO.getInsertId());
							outclaimbef.put("CUST_NO", orderdt[k].getCustNo());
							outclaimbef.put("PROC_DATE", proc_date);
							outclaimbef.put("CS_LMS_CODE", claimdt[i].getCsLmsCode());
							
							executedRtn = saveDeliveryReqOutCancel(outclaimbef, receiver);
						}
						
					}
					
					rtnValue = getMessage("pa.success_order_claim");
					resultMap = new HashMap<>();
					resultMap.put("MAPPING_SEQ", 	orderClaimVO.getMappingSeq());
					resultMap.put("ORDER_NO", 		orderClaimVO.getOrderNo());
					resultMap.put("ORDER_G_SEQ", 	orderClaimVO.getOrderGSeq());
					resultMap.put("ORDER_D_SEQ", 	claimdt[0].getOrderDSeq());
					resultMap.put("ORDER_W_SEQ", 	claimdt[0].getOrderWSeq());
					resultMap.put("RESULT_CODE", 	"000000"); 
					resultMap.put("RESULT_MESSAGE", rtnValue);
				}else{
					rtnValue = getMessage("pa.claim_qty_too_much");
					resultMap = new HashMap<>();
					resultMap.put("MAPPING_SEQ", 	orderClaimVO.getMappingSeq());
					resultMap.put("RESULT_CODE", 	"100001"); 
					resultMap.put("RESULT_MESSAGE", rtnValue);
				}
			}
		} catch (Exception e) {
			rtnValue = getMessage("pa.fail_order_claim", new String[]{e.toString()});
			throw processMessageException(rtnValue);
		} finally {
			try {
				if(resultMap != null){
					ParamMap resultParamMap = null;
					resultParamMap = new ParamMap();
					
					resultParamMap.setParamMap(resultMap);	
					resultParamMap.replaceCamel();
					
					if(resultParamMap.getString("resultCode").equals("000000")){
						resultParamMap.put("createYn", "1");
					} else {
						resultParamMap.put("createYn", "0");
					}
					
					//↓ Test를 위해 잠시 주석처리
					executedRtn = paorderDAO.updatePaOrderm(resultParamMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					}
				}
			} catch (Exception e2) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		}
	
		return resultMap;
	}
	
	private int selectRemainQty(ParamMap param) throws Exception{
		int executedRtn = 0;
		executedRtn = paorderDAO.selectRemainQty(param);
		return executedRtn;
	}
	
	
		
	private int insertPaOrderShipCost4Cancel(Ordershipcost[] ordershipcostArr, ParamMap param, PaClaimdtVO[] claimdt , ArrayList<Orderreceipts> orderreceiptsList) throws Exception{
		int shpFeeYn  =  0;  //0무상, 1유상 , 2 협의
		int executedRtn = 0;
		long paSheFeeAmt = 0;
		
		for(Ordershipcost os : ordershipcostArr)
		{
			paSheFeeAmt += os.getShpfeeCost();		// 11번가에서 지침을 주지않아서 일단 이렇게 코딩함;;;
		}
		
		
		if(paSheFeeAmt > 0 ){
			shpFeeYn = 1;
		}else{
			return 1;
		}
		
		param.put("paSheFeeAmt", paSheFeeAmt);
		param.put("shpFeeYn", shpFeeYn);
		
		
		try {
			
			for(Orderreceipts or : orderreceiptsList){

				
				if(or.getSettleGb().equals("65")){ 
					
					param.put("orderGB",  "20");	

				}else if ( or.getSettleGb().equals("15")){
					
					param.put("orderGB",  "20");	
					
				}else continue;
				
				
				param.put("ReceiptNo",or.getReceiptNo());
				//출하전 취소로 인한 발생건이라고 remark 해줍시다..
				executedRtn = insertPaOrderShipCost(ordershipcostArr,param,claimdt);

	
			}
			
		
		} catch (Exception e) {
			log.error("TPAORDERSHIPCOST INSERT" + e.toString());
			//throw processException("msg.cannot_save", new String[] { "TPAORDERSHIPCOST INSERT" });
		}
		return executedRtn;
	}
	
	
	/**
	 * 제휴사 반품취소
	 * @param OrderClaimVO
	 * @return HashMap<String, Object>
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@Override
	public HashMap<String, Object> saveClaimCancel(OrderClaimVO orderClaimVO) throws Exception {
		ParamMap paramMap = new ParamMap();
		Ordergoods ordergoods = new Ordergoods();
		PaClaimdtVO[] claimdt =  null;
		PaClaimdtVO[] claimdtVO = null;
		PaClaimdtVO claimdtCancelVO = new PaClaimdtVO();
		Planshipcost planshipcost = new Planshipcost();
		int executedRtn = 0;
		String rtnMsg = "000000";
		String rtnValue = "";
		HashMap resultMap = null;
		Collection cClaimdt = new ArrayList();
		Collection cClaimrdt = null;
		String userId = "999999";
		
		

		try {
			String proc_date = systemService.getSysdatetimeToString();
			//반품취소할 원건 조회
			cClaimrdt = paclaimDAO.selectClaimdt(orderClaimVO);
			claimdtVO = new PaClaimdtVO[cClaimrdt.size()];
			
			claimdtVO = (PaClaimdtVO[]) cClaimrdt.toArray(new PaClaimdtVO[0]);
			
			for(int i = 0; i < claimdtVO.length; i++){
				if(claimdtVO[i].getGoodsGb().equals("10") && claimdtVO[i].getDoFlag().equals("60")){//= 회수확정 이후 반품취소 불가.
					rtnValue = getMessage("pa.after_return_ok_claim_cancel");
					throw processMessageException(rtnValue);
				}
				
				userId = orderClaimVO.getInsertId(); 
				claimdtCancelVO =(PaClaimdtVO) claimdtVO[i].clone();
				
				claimdtVO[i].setCwareAction("U");
				claimdtVO[i].setClaimGb("30");
				claimdtVO[i].setSyscancel(orderClaimVO.getClaimQty());
				claimdtVO[i].setSyslast(0);
				claimdtVO[i].setSyslastDc(0);
				claimdtVO[i].setSyslastDcGoods(0);
				claimdtVO[i].setSyslastDcMemb(0);
				claimdtVO[i].setMsg("");
				claimdtVO[i].setLastProcId(userId);
				claimdtVO[i].setSyslastAmt(0);
				claimdtVO[i].setSyslastNet(0);
				claimdtVO[i].setClaimDate(DateUtil.toTimestamp(proc_date));
				claimdtVO[i].setInsertId(userId);
				claimdtVO[i].setLastProcId(userId);
				claimdtVO[i].setLastProcDate(DateUtil.toTimestamp(proc_date));
				cClaimdt.add(claimdtVO[i]);
				
				claimdtCancelVO.setCwareAction("I");
				claimdtCancelVO.setClaimGb("31");
				claimdtCancelVO.setClaimQty(orderClaimVO.getClaimQty());
				claimdtCancelVO.setSyscancel(0);
				claimdtCancelVO.setClaimCode("999");
				claimdtCancelVO.setSyslast(orderClaimVO.getClaimQty());
				claimdtCancelVO.setClaimType(orderClaimVO.getClaimType());
				claimdtCancelVO.setClaimDate(DateUtil.toTimestamp(proc_date));
				claimdtCancelVO.setInsertId(userId);
				claimdtCancelVO.setLastProcId(userId);
				claimdtCancelVO.setLastProcDate(DateUtil.toTimestamp(proc_date));
				claimdtCancelVO.setCsLgroup("65");
				claimdtCancelVO.setCsMgroup("01");
				claimdtCancelVO.setCsSgroup("01");
				claimdtCancelVO.setCsLmsCode("650101");
				
				cClaimdt.add(claimdtCancelVO);
			}
			
			claimdt = (PaClaimdtVO[]) cClaimdt.toArray(new PaClaimdtVO[0]);
			ordergoods.setClaimCanQty(orderClaimVO.getClaimQty());
			ordergoods.setOrderNo(orderClaimVO.getOrderNo());
			ordergoods.setOrderGSeq(orderClaimVO.getOrderGSeq());
			
			executedRtn = modifiedGoods(ordergoods);
			//tclaimdt insert
			executedRtn = newClaimdt(claimdt, null, proc_date);
			//modifiedClaimdt
			executedRtn = modifiedClaimdt(claimdt);
			
			
			//tplanshipcost  //SK-B는 안씀
			//executedRtn = savePlanShipCost(planshipcost, claimdt ,paramMap ,proc_date);
			
			rtnValue = getMessage("pa.success_claim_cancel");
			resultMap = new HashMap<>();
			for(int i = 0; claimdt.length > i; i++){
				if(claimdt[i].getCwareAction().equals("I") && claimdt[i].getGoodsGb().equals("10")){
					resultMap.put("MAPPING_SEQ", 	orderClaimVO.getMappingSeq());
					resultMap.put("ORDER_NO", 		orderClaimVO.getOrderNo());
					resultMap.put("ORDER_G_SEQ", 	orderClaimVO.getOrderGSeq());
					resultMap.put("ORDER_D_SEQ", 	claimdt[i].getOrderDSeq());
					resultMap.put("ORDER_W_SEQ", 	claimdt[i].getOrderWSeq());
					resultMap.put("RESULT_CODE", 	"000000"); 
					resultMap.put("RESULT_MESSAGE", rtnValue);
					break;
				}
			}
			
		} catch (Exception e) {
			rtnValue = getMessage("pa.fail_claim_cancel", new String[]{e.toString()});
			throw processMessageException(rtnValue);
		} finally {
			try {
				if(resultMap != null){
					ParamMap resultParamMap = null;
					resultParamMap = new ParamMap();
					
					resultParamMap.setParamMap(resultMap);
					resultParamMap.replaceCamel();
					
					if(resultParamMap.getString("resultCode").equals("000000")){
						resultParamMap.put("createYn", "1");
					} else {
						resultParamMap.put("createYn", "0");
					}
					
					executedRtn = paorderDAO.updatePaOrderm(resultParamMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					}
				}
			} catch (Exception e2) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		}
	
		return resultMap;
	}
	
	/**
	 * 제휴사 교환접수
	 * @param OrderClaimVO[]
	 * @return HashMap<String, Object>
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public HashMap<String, Object>[] saveOrderChange(OrderClaimVO[] orderClaimVO) throws Exception {
		ParamMap paramMap = new ParamMap();
		OrderdtVO[] orderdt = new OrderdtVO[1];
		ReceiverVO receiver = new ReceiverVO();
		Ordergoods ordergoods = new Ordergoods();
		int paramLength= orderClaimVO.length;
		PaClaimdtVO[] claimdt 				= new PaClaimdtVO[paramLength];
		OrderpromoVO[] orderpromo 			= new OrderpromoVO[paramLength];
		HashMap<String, Object>[] resultMap = new HashMap[paramLength];
		Ordershipcost  [] ordershipcost = null;
		Ordercostapply [] ordercostapply= null;
		int executedRtn = 0;
		String rtnValue = "";
		String userId = "999999";  //추후에 Param으로 반드시 수정
		String receiverPost = null; 
		
		try {
			String proc_date = systemService.getSysdatetimeToString();
			HashMap<String, Object> goodsInfo = new HashMap<>();
			orderdt[0] = paclaimDAO.selectOrderDtInfoEx(orderClaimVO[0]);
			
			String adrChk = "";
			for(int i = 0; i < orderClaimVO.length; i++){
				
				userId = orderClaimVO[i].getInsertId();
				claimdt[i] 		 = new PaClaimdtVO();
				if(orderClaimVO[i].getClaimGb().equals("40")){//교환출고
					//교환출고대상상품 검사
					goodsInfo = paclaimDAO.selectChangeGoodsInfo(orderClaimVO[i]);
					if(Integer.parseInt(goodsInfo.get("ABLE_QTY").toString()) < orderClaimVO[i].getClaimQty() 
							|| goodsInfo.get("SALE_GB").toString().equals("19") || (goodsInfo.get("SALE_GB").toString().equals("11") 
									&& orderClaimVO[i].getAdminProcYn().equals("0"))){ 
						//= 교환할 상품의 재고가 부족하거나 판매상태가 정상(00)이 아닌 경우.
						//= 단, 상담원 관리자가 처리 하였으면서 일시중단(11)인 경우에는 교환 접수 가능.
						rtnValue = getMessage("pa.fail_order_change", new String[]{getMessage("pa.out_of_stock")});
						for(int k = 0; paramLength > k; k++){
							resultMap[k] = new HashMap<>();
							resultMap[k].put("MAPPING_SEQ", 	orderClaimVO[i].getMappingSeq());
							resultMap[k].put("RESULT_CODE", 	"100001"); 
							resultMap[k].put("RESULT_MESSAGE", 	rtnValue);
						}
						return resultMap;
						
					}else{//교환출고면서, 교환할수 있는 재고가 존재함..
						claimdt[i].setClaimGb("40");
						claimdt[i].setGoodsCode(goodsInfo.get("GOODS_CODE").toString());
						claimdt[i].setGoodsdtCode(goodsInfo.get("GOODSDT_CODE").toString());
						claimdt[i].setGoodsdtInfo(goodsInfo.get("GOODSDT_INFO").toString());
						claimdt[i].setReceiverName(orderClaimVO[i].getReturnName());
						claimdt[i].setReceiverTel(orderClaimVO[i].getReturnTel());
						claimdt[i].setReceiverHp(orderClaimVO[i].getReturnHp());
						claimdt[i].setReceiverAddr(orderClaimVO[i].getReturnAddr());
						claimdt[i].setCustDelyYn(orderClaimVO[i].getCustDelyYn());
						//receiverPost = claimdt[i].getReceiverPost();
						
						adrChk = orderClaimVO[i].getSameAddr();
						paramMap.put("sameAddrDel", "".equals(adrChk)? "0": adrChk); //교환배송지
					}
					
				}else{
					claimdt[i].setClaimGb("45");//교환회수
					claimdt[i].setReceiverName(orderClaimVO[i].getReturnName());
					claimdt[i].setReceiverTel(orderClaimVO[i].getReturnTel());
					claimdt[i].setReceiverHp(orderClaimVO[i].getReturnHp());
					claimdt[i].setReceiverAddr(orderClaimVO[i].getReturnAddr());
					
					adrChk = orderClaimVO[i].getSameAddr();
					paramMap.put("sameAddrRet", "".equals(adrChk)? "0": adrChk); //교환수거지
				
					claimdt[i].setCustDelyYn(orderClaimVO[i].getCustDelyYn());
					setCustDelyYn( claimdt[i]  , orderClaimVO[i]);
					
				}
				
				
				claimdt[i].setClaimQty(orderClaimVO[i].getClaimQty());
				claimdt[i].setSyslast(orderClaimVO[i].getClaimQty());
				claimdt[i].setSyscancel(0);
				claimdt[i].setMappingSeq(orderClaimVO[i].getMappingSeq());
				
				claimdt[i].setRcvrTypeAdd(orderClaimVO[i].getRcvrTypeAdd());
				claimdt[i].setRcvrMailNo(orderClaimVO[i].getRcvrMailNo());
				claimdt[i].setRcvrMailNoSeq(orderClaimVO[i].getRcvrMailNoSeq());
				claimdt[i].setRcvrBaseAddr(orderClaimVO[i].getRcvrBaseAddr());
				claimdt[i].setRcvrDtlsAddr(orderClaimVO[i].getRcvrDtlsAddr());
				
				
				//백호선.. Claim Code 고도화 필요
				claimdt[i].setClaimCode("999");
				claimdt[i].setCsLgroup(orderClaimVO[i].getClaimCode().substring(0,2));
				claimdt[i].setCsMgroup(orderClaimVO[i].getClaimCode().substring(2,4));
				claimdt[i].setCsSgroup(orderClaimVO[i].getClaimCode().substring(4,6));
				claimdt[i].setCsLmsCode(orderClaimVO[i].getClaimCode());
				claimdt[i].setShpfeeYn(orderClaimVO[i].getShpfeeYn());
				claimdt[i].setClaimDesc(orderClaimVO[i].getClaimDesc());
				
				claimdt[i].setLastProcId(userId);
				claimdt[i].setInsertId(userId);
				
				claimdt[i].setMsg(orderClaimVO[i].getMsg());
			
			} //End of making 교환 회수 , 교환 배송
			
			
			/*
			for(int k=0;k<claimdt.length;k++){
				claimdt[k].setClaimDesc(orderClaimVO[k].getClaimDesc());
				claimdt[k].setClaimCode(orderClaimVO[k].getClaimCode());
				claimdt[k].setClaimType(orderClaimVO[k].getClaimType());
				claimdt[k].setShipcostChargeYn(orderClaimVO[k].getShipcostChargeYn());
			}*/
			
			
			
			paramMap.put("orderNo", 	orderdt[0].getOrderNo());
			paramMap.put("orderGSeq", 	orderdt[0].getOrderGSeq());
			paramMap.put("receiverSeq", orderdt[0].getReceiverSeq());
			paramMap.put("custNo", 		orderdt[0].getCustNo());
			paramMap.put("custName", 	orderdt[0].getCustName());
			paramMap.put("entpCode", 	orderdt[0].getEntpCode());
			paramMap.put("userId",      userId);
			paramMap.put("proc_date",   proc_date);
			paramMap.put("localYn",     orderClaimVO[0].getLocalYn());
			paramMap.put("orderGB", "40");
			
			//주문 배송지 조회
			receiver = paclaimDAO.selectReceiverAddr(paramMap); //cust_no , receive seq
			
			executedRtn = prepareSave(ordergoods, orderdt, claimdt, null, orderpromo, orderClaimVO, proc_date);
			
			//treceiver 비교 후 insert
			executedRtn = modifiedTreceiver(orderdt, claimdt, receiver, paramMap, proc_date);//11번가에서 다중 배송지를 막아놓았음..
					
			//tordergoods update
			executedRtn = modifiedGoods(ordergoods);
			
			
			//정산배송비 계산
			HashMap<String, Object> obj = new HashMap<>();
			//paramMap.put("orderWseq",   getOrderWseq4ShippingCost(claimdt) );
			//paramMap.put("ReceiverPost", receiver.getReceiverPost());
			
			
			paramMap.put("paSheFeeAmt", orderClaimVO[0].getShpFeeAmt());
			paramMap.put("shpFeeYn", orderClaimVO[0].getShpfeeYn());
			
			
			obj = newOrdershipcost(claimdt, paramMap, proc_date);
			ordershipcost  = (OrdershipcostVO[]) obj.get("OrderShipCost");
			ordercostapply = (Ordercostapply[]) obj.get("OrderCostApply");
				
			
			//tclaimdt insert  //출하예정일 부분 재 정의가 필요합니다.
			executedRtn = newClaimdt(claimdt, receiver, proc_date);
			
			//torderpromo 비교    안씀..
			//executedRtn = modifiedOrderpromo(orderpromo, paramMap, proc_date, claimdt);
			//executedRtn = newOrdershipcost4BackUp(ordershipcost,claimdt,paramMap,proc_date, null);
			
			
		    //Insert OrderCostApply, Insert OrderShipCost , Insert OrderReceipt	//tordershipcost insert, torderreceipts(정산배송비) 
			executedRtn = insertOrderreceipts(ordershipcost,paramMap , null);
			
			
			executedRtn = insertPaOrderShipCost(ordershipcost,paramMap,claimdt);
			
			//insert TORDERSHIPCOST
			executedRtn = insertOrdershipcost(ordershipcost);
			
			//insert TORDERCOSTAPPLY
			executedRtn = insertOrderCostApply(ordercostapply, claimdt);
			

			
			//tcustcounselm,tcustcounseldt insert  - Need to check
			executedRtn = newCounsel(claimdt, paramMap, receiver, proc_date);
			
			//TORDERSTOCK UPDATE                   - Need to check
			executedRtn = updateStockClaim(claimdt);
			
			//고객직접발송 인입 상담생성 - 하프클럽
			checkCustrDirectDelivery(orderdt, claimdt);
			
			rtnValue = getMessage("pa.success_order_change");
			for(int l = 0; paramLength > l; l++){
				resultMap[l] = new HashMap<String, Object>();
				for(int n = 0; paramLength > n; n++){
					if(orderClaimVO[l].getClaimGb().equals(claimdt[n].getClaimGb())){
						resultMap[l].put("MAPPING_SEQ", 	orderClaimVO[l].getMappingSeq());
						resultMap[l].put("ORDER_NO", 		claimdt[n].getOrderNo());
						resultMap[l].put("ORDER_G_SEQ", 	claimdt[n].getOrderGSeq());
						resultMap[l].put("ORDER_D_SEQ", 	claimdt[n].getOrderDSeq());
						resultMap[l].put("ORDER_W_SEQ", 	claimdt[n].getOrderWSeq());
						resultMap[l].put("RESULT_CODE", 	"000000"); 
						resultMap[l].put("RESULT_MESSAGE", 	rtnValue);
						break;
					}
				}
			}
			
			return resultMap;
		} catch (Exception e) {
			rtnValue = getMessage("pa.fail_order_change", new String[]{e.toString()});
			throw processMessageException(rtnValue);
		} finally {
			try {
				ParamMap resultParamMap = null;
				resultParamMap = new ParamMap();
				
				for(int l = 0; resultMap.length > l; l++){
					if(resultMap[l] == null) break;
					
					resultParamMap.setParamMap(resultMap[l]);
					resultParamMap.replaceCamel();
					
					if(resultParamMap.getString("resultCode").equals("000000")){
						resultParamMap.put("createYn", "1");
					} else {
						resultParamMap.put("createYn", "0");
					}
					
					executedRtn = paorderDAO.updatePaOrderm(resultParamMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					}
				}
			} catch (Exception e2) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		}
	}
	
	/**
	 * 제휴사 교환취소
	 * @param OrderClaimVO[]
	 * @return HashMap<String, Object>
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public HashMap<String, Object>[] saveChangeCancel(OrderClaimVO[] orderClaimVO) throws Exception {
		// 교환건 취소
		ParamMap paramMap = new ParamMap();
		PaClaimdtVO[] claimdt = null;
		PaClaimdtVO changeVO = new PaClaimdtVO();
		PaClaimdtVO changeCancelVO = new PaClaimdtVO();
		Ordershipcost[] ordershipcost = null;
		Ordercostapply[] ordercostapply = null;
		OrderdtVO[] orderdt = new OrderdtVO[1];
		ArrayList<Orderreceipts> orderreceiptsList = new ArrayList<>();
		int executedRtn = 0;
		String proc_date = systemService.getSysdatetimeToString();
		Collection cClaimdt = new ArrayList();
		int paramLength= orderClaimVO.length;
		HashMap<String, Object>[] resultMap = new HashMap[paramLength];
		String rtnValue = "";
		String userId   = "999999";
		
		try {
			

			orderdt[0] = paclaimDAO.selectOrderDtInfoEx(orderClaimVO[0]);
			//userId = orderClaimVO[0].getInsertId();
			
			for(int i = 0; i< paramLength; i++){
				changeVO = new PaClaimdtVO();
				changeCancelVO = new PaClaimdtVO();
				
				
				if(orderClaimVO[i].getClaimGb().equals("41")){
					//교환출고취소할 원건 데이터 조회
					changeVO = paclaimDAO.selectChangeCancelOrg(orderClaimVO[i]);
					if(Integer.parseInt(changeVO.getDoFlag()) >= 30){//= 출하지시 이후 교환취소 불가.
						rtnValue = getMessage("pa.after_slip_order_change_cancel");
						throw processMessageException(rtnValue);
					}
					
					changeCancelVO = (PaClaimdtVO)changeVO.clone();
					changeVO.setCwareAction("U");
					changeVO.setClaimGb("40");
					changeVO.setSyscancel(orderClaimVO[i].getClaimQty());
					changeVO.setSyslast(0);
					changeVO.setSyslastDc(0);
					changeVO.setSyslastDcGoods(0);
					changeVO.setSyslastDcMemb(0);
					changeVO.setLastProcId(userId);
					changeVO.setSyslastAmt(0);
					changeVO.setSyslastNet(0);
					changeVO.setClaimDate(DateUtil.toTimestamp(proc_date));
					changeVO.setInsertId(userId);
					changeVO.setLastProcId(userId);
					changeVO.setLastProcDate(DateUtil.toTimestamp(proc_date));
					cClaimdt.add(changeVO);
					//교환출고취소
					changeCancelVO.setCwareAction("I");
					changeCancelVO.setClaimGb("41");
					changeCancelVO.setSyslast(orderClaimVO[i].getClaimQty());
					changeCancelVO.setSyscancel(0);
					changeCancelVO.setClaimQty(orderClaimVO[i].getClaimQty());
					changeCancelVO.setClaimCode("999");
					changeCancelVO.setClaimType(orderClaimVO[i].getClaimType());
					changeCancelVO.setShipcostChargeYn(orderClaimVO[i].getShipcostChargeYn());
					changeCancelVO.setClaimDate(DateUtil.toTimestamp(proc_date));
					changeCancelVO.setInsertId(userId);
					changeCancelVO.setLastProcId(userId);
					changeCancelVO.setLastProcDate(DateUtil.toTimestamp(proc_date));
					changeCancelVO.setMappingSeq(orderClaimVO[i].getMappingSeq());
					changeCancelVO.setShpfeeYn(changeVO.getShpfeeYn());  // 배송비 부과여부, 판매자 사유인지 구매자 사유인지..
					changeCancelVO.setCsLgroup("66");
					changeCancelVO.setCsMgroup("01");
					changeCancelVO.setCsSgroup("01");
					changeCancelVO.setCsLmsCode("660101");
					
					cClaimdt.add(changeCancelVO);
					
					paramMap.put("entpCode", changeVO.getEntpCode());
				}else{
					//교환회수취소할 원건 데이터 조회
					changeVO = paclaimDAO.selectChangeCancelOrg(orderClaimVO[i]);
					if(Integer.parseInt(changeVO.getDoFlag()) >= 60){//= 회수확정 이후 교환취소 불가.
						rtnValue = getMessage("pa.after_return_order_change_cancel");
						throw processMessageException(rtnValue);
					}
					changeCancelVO = (PaClaimdtVO)changeVO.clone();
					changeVO.setCwareAction("U");
					changeVO.setClaimGb("45");
					changeVO.setSyscancel(orderClaimVO[i].getClaimQty());
					changeVO.setSyslast(0);
					changeVO.setSyslastDc(0);
					changeVO.setSyslastDcGoods(0);
					changeVO.setSyslastDcMemb(0);
					changeVO.setLastProcId(userId);
					changeVO.setSyslastAmt(0);
					changeVO.setSyslastNet(0);
					changeVO.setClaimDate(DateUtil.toTimestamp(proc_date));
					changeVO.setInsertId(userId);
					changeVO.setLastProcId(userId);
					changeVO.setLastProcDate(DateUtil.toTimestamp(proc_date));
					cClaimdt.add(changeVO);
					
					//교환회수취소
					changeCancelVO.setCwareAction("I");
					changeCancelVO.setClaimGb("46");
					changeCancelVO.setSyslast(orderClaimVO[i].getClaimQty());
					changeCancelVO.setSyscancel(0);
					changeCancelVO.setClaimQty(orderClaimVO[i].getClaimQty());
					changeCancelVO.setClaimCode("999");
					changeCancelVO.setClaimType(orderClaimVO[i].getClaimType());
					changeCancelVO.setShipcostChargeYn(orderClaimVO[i].getShipcostChargeYn());
					changeCancelVO.setClaimDate(DateUtil.toTimestamp(proc_date));
					changeCancelVO.setInsertId(userId);
					changeCancelVO.setLastProcId(userId);
					changeCancelVO.setLastProcDate(DateUtil.toTimestamp(proc_date));
					changeCancelVO.setMappingSeq(orderClaimVO[i].getMappingSeq());
					changeCancelVO.setShpfeeYn(changeVO.getShpfeeYn()); // 배송비 부과여부, 판매자 사유인지 구매자 사유인지..
					changeCancelVO.setCsLgroup("66");
					changeCancelVO.setCsMgroup("01");
					changeCancelVO.setCsSgroup("01");
					changeCancelVO.setCsLmsCode("660101");
					
					
					cClaimdt.add(changeCancelVO);
				}
			}
			
			claimdt = (PaClaimdtVO[]) cClaimdt.toArray(new PaClaimdtVO[0]);
			
			
			
			paramMap.put("orderNo", 	orderdt[0].getOrderNo());			
			paramMap.put("custNo", 		orderdt[0].getCustNo());
			paramMap.put("custName", 	orderdt[0].getCustName());
			paramMap.put("orderGB", "41");
			paramMap.put("userId", userId);
			paramMap.put("proc_date", proc_date);
			
			paramMap.put("paSheFeeAmt", orderClaimVO[0].getShpFeeAmt());  
			paramMap.put("shpFeeYn", claimdt[0].getShpfeeYn());
			
			//paramMap.put("orderWseq", getOrderWseq4ShippingCost(claimdt) );

			if(claimdt.length == 2){
				paramMap.put("ReceiverPost1", claimdt[0].getReceiverPost() );  //교환 철회 의 경우 주소정제 데이터가 필요없다.
				paramMap.put("ReceiverPost2", claimdt[1].getReceiverPost()); 
			}
			
			HashMap<String, Object> obj = new HashMap<>();
			obj = newOrdershipcost(claimdt, paramMap, proc_date);
			ordershipcost  = (OrdershipcostVO[]) obj.get("OrderShipCost");
			ordercostapply = (Ordercostapply[]) obj.get("OrderCostApply");
			
			
			refineOrdershipcost4ShpfeeYn(ordershipcost , claimdt[0].getShpfeeYn());

			
			//insert tclaimdt
			executedRtn = newClaimdt(claimdt, null, proc_date);
			//update tclaimdt
			executedRtn = modifiedClaimdt(claimdt);
			//insert tordershipcost, torderreceipts(정산배송비)
			
			
			//insert TORDERRECEIPTS
			executedRtn = insertOrderreceipts(ordershipcost,paramMap,orderreceiptsList);
			
			//insert TPAORDERSHIPCOST
			executedRtn = insertPaOrderShipCost(ordershipcost, paramMap,claimdt);
			
			//update TORDERRECEIPTS
			executedRtn = updateReceipts(orderreceiptsList);
			//insert TORDERSHIPCOST
			executedRtn = insertOrdershipcost(ordershipcost);
			//insert TORDERCOSTAPPLY
			executedRtn = insertOrderCostApply(ordercostapply, claimdt);
	
			//update torderreceipts
			executedRtn = modifiedReceipts(orderreceiptsList);
				
			//update torderstock
			executedRtn = updateStockClaim(claimdt);
			
			rtnValue = getMessage("pa.success_change_cancel");
			for(int l = 0; paramLength > l; l++){
				resultMap[l] = new HashMap<String, Object>();
				for(int n = 0; claimdt.length > n; n++){
					if(orderClaimVO[l].getClaimGb().equals(claimdt[n].getClaimGb())){
						resultMap[l].put("MAPPING_SEQ", 	orderClaimVO[l].getMappingSeq());
						resultMap[l].put("ORDER_NO", 		claimdt[n].getOrderNo());
						resultMap[l].put("ORDER_G_SEQ", 	claimdt[n].getOrderGSeq());
						resultMap[l].put("ORDER_D_SEQ", 	claimdt[n].getOrderDSeq());
						resultMap[l].put("ORDER_W_SEQ", 	claimdt[n].getOrderWSeq());
						resultMap[l].put("RESULT_CODE", 	"000000"); 
						resultMap[l].put("RESULT_MESSAGE", 	rtnValue);
						break;
					}
				}
			}
			
			return resultMap;
		} catch (Exception e) {
			rtnValue = getMessage("pa.fail_change_cancel", new String[]{e.toString()});
			throw processMessageException(rtnValue);
		} finally {
			try {
				ParamMap resultParamMap = null;
				resultParamMap = new ParamMap();
				
				for(int l = 0; resultMap.length > l; l++){
					if(resultMap[l] == null) break;
					
					resultParamMap.setParamMap(resultMap[l]);
					resultParamMap.replaceCamel();
					
					if(resultParamMap.getString("resultCode").equals("000000")){
						resultParamMap.put("createYn", "1");
					} else {
						resultParamMap.put("createYn", "0");
					}
					
					executedRtn = paorderDAO.updatePaOrderm(resultParamMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					}
				}
			} catch (Exception e2) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		}
	}

	private PaClaimdtVO settingTclaimdt(OrderClaimVO orderClaimVO) throws Exception {
		PaClaimdtVO claimdt = new PaClaimdtVO();
		claimdt.setBlank();
		
		claimdt.setClaimQty			(orderClaimVO.getClaimQty());
		claimdt.setSyscancel		(0);
		claimdt.setSyslast			(orderClaimVO.getClaimQty());
		claimdt.setClaimGb			("30");
		claimdt.setClaimType		(orderClaimVO.getClaimType());
		claimdt.setClaimDesc		(orderClaimVO.getClaimDesc());
		claimdt.setReceiverName		(orderClaimVO.getReturnName());
		claimdt.setReceiverTel		(orderClaimVO.getReturnTel());
		claimdt.setReceiverHp		(orderClaimVO.getReturnHp());
		//claimdt.setReceiverAddr		(orderClaimVO.getReturnAddr());//신세계기준 삭제필요 PARKSEONJUN
		claimdt.setRcvrMailNo		(orderClaimVO.getRcvrMailNo());   //우편번호
		claimdt.setRcvrMailNoSeq    (orderClaimVO.getRcvrMailNoSeq());
		claimdt.setRcvrBaseAddr		(orderClaimVO.getRcvrBaseAddr()); //기본주소
		claimdt.setRcvrDtlsAddr		(orderClaimVO.getRcvrDtlsAddr()); //상세주소
		claimdt.setRcvrTypeAdd		(orderClaimVO.getRcvrTypeAdd());  //도로명여부
		
		claimdt.setClaimCode        ("999");
		claimdt.setCsLgroup			(orderClaimVO.getCsLgroup());
		claimdt.setCsMgroup			(orderClaimVO.getCsMgroup());
		claimdt.setCsSgroup			(orderClaimVO.getCsSgroup());
		claimdt.setCsLmsCode		(orderClaimVO.getCsLmsCode());
		
		claimdt.setStandardType     ("0");
		claimdt.setShpfeeYn			(orderClaimVO.getShpfeeYn());
		claimdt.setInsertId         (orderClaimVO.getInsertId());
		claimdt.setLastProcId       (orderClaimVO.getInsertId());
		
		claimdt.setMsg				(orderClaimVO.getMsg());
		claimdt.setCustDelyYn		(orderClaimVO.getCustDelyYn());
		
		setCustDelyYn( claimdt  , orderClaimVO);
		
		return claimdt;
	}
	
	private Canceldt settingTcanceldt(OrderClaimVO orderClaimVO) throws Exception {
		Canceldt canceldt = new Canceldt();
		canceldt.setBlank();
		canceldt.setCancelQty(orderClaimVO.getClaimQty());
		canceldt.setCancelGb("20");
		canceldt.setCancelCode("999");
		
		canceldt.setCsLgroup(orderClaimVO.getCsLgroup());
		canceldt.setCsMgroup(orderClaimVO.getCsMgroup());
		canceldt.setCsSgroup(orderClaimVO.getCsSgroup());
		canceldt.setCsLmsCode(orderClaimVO.getCsLmsCode());
		
		canceldt.setLastProcId(orderClaimVO.getInsertId());
		canceldt.setInsertId(orderClaimVO.getInsertId());
		
		return canceldt;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int prepareSave(Ordergoods ordergoods, OrderdtVO[] orderdt,
			PaClaimdtVO[] claimdt, Canceldt[] canceldt, OrderpromoVO[] orderpromo, OrderClaimVO[] claimProcList, String proc_date) throws Exception {
		
		String userId = "999999";
		int executedRtn = 0;
		OrderdtVO[] cancelDataSet = null;
		OrderdtVO[] claimDataSet = null;
		
		ordergoods.setOrderNo(orderdt[0].getOrderNo());
		ordergoods.setOrderGSeq(orderdt[0].getOrderGSeq());
		ordergoods.setCustNo(orderdt[0].getCustNo());
		
		if(claimdt[0].getClaimGb().equals("30")){ //= 반품일 경우
			ordergoods.setClaimQty(claimProcList[0].getClaimQty());
			ordergoods.setCancelQty(0);
			
			Collection cClaimDataSet = new ArrayList();
			Collection cCancelDataSet = new ArrayList();
			
			for(int j = 0; j < orderdt.length; j++){
				if( !orderdt[j].getOrderDSeq().equals("001") && Integer.parseInt(orderdt[j].getDoFlag()) < 30 ){
					cCancelDataSet.add(orderdt[j]);
				} else {
					cClaimDataSet.add(orderdt[j]);
				}
			}
			
			if(cCancelDataSet.size() > 0){
				cancelDataSet = (OrderdtVO[]) cCancelDataSet.toArray(new OrderdtVO[0]);
			}
			if(cClaimDataSet.size() > 0){
				claimDataSet = (OrderdtVO[]) cClaimDataSet.toArray(new OrderdtVO[0]);
			}
		} else { //교환인경우
			ordergoods.setExchCnt(1);
		}
		
		for(int i = 0; i < claimdt.length; i++){
			
			String doFlag = claimdt[i].getDoFlag() == null || "".equals(claimdt[i].getDoFlag()) ? "10" :  claimdt[i].getDoFlag();
			
			if(claimdt[i].getClaimGb().equals("30")){ //= 반품
				claimdt[i].setOrderNo(claimDataSet[i].getOrderNo());
				claimdt[i].setOrderGSeq(claimDataSet[i].getOrderGSeq());
				claimdt[i].setOrderDSeq(claimDataSet[i].getOrderDSeq());
				claimdt[i].setCustNo(claimDataSet[i].getCustNo());
				claimdt[i].setClaimDate(DateUtil.toTimestamp(proc_date));
				claimdt[i].setLastProcDate(DateUtil.toTimestamp(proc_date));
				claimdt[i].setClaimAmt(claimDataSet[i].getOrderAmt());
				claimdt[i].setDcAmt(claimDataSet[i].getDcAmt());
				claimdt[i].setDcAmtGoods(claimDataSet[i].getDcAmtGoods());
				claimdt[i].setDcAmtMemb(claimDataSet[i].getDcAmtMemb());
				claimdt[i].setDcAmtDiv(claimDataSet[i].getDcAmtDiv());
				claimdt[i].setDcAmtCard(claimDataSet[i].getDcAmtCard());
				claimdt[i].setRsaleAmt(claimDataSet[i].getRsaleAmt());
				claimdt[i].setSaveamt(claimDataSet[i].getSaveamt());
				claimdt[i].setSaveamtGb(claimDataSet[i].getSaveamtGb());
				claimdt[i].setDoFlag(doFlag);
				claimdt[i].setLastProcDate(DateUtil.toTimestamp(proc_date));
				claimdt[i].setOrderQty(claimDataSet[i].getOrderQty());
				claimdt[i].setWhCode(claimDataSet[i].getWhCode());
				claimdt[i].setCwareAction("I");
				claimdt[i].setGoodsCode(claimDataSet[i].getGoodsCode());
				claimdt[i].setGoodsdtCode(claimDataSet[i].getGoodsdtCode());
				claimdt[i].setGoodsdtInfo(claimDataSet[i].getGoodsdtInfo());
				claimdt[i].setReceiverPost(claimDataSet[i].getReceiverPost());
				
				userId = claimdt[i].getLastProcId();
				
			} else { //= 교환
				claimdt[i].setOrderNo(orderdt[0].getOrderNo());
				claimdt[i].setOrderGSeq(orderdt[0].getOrderGSeq());
				claimdt[i].setOrderDSeq(orderdt[0].getOrderDSeq());
				claimdt[i].setCustNo(orderdt[0].getCustNo());
				claimdt[i].setClaimDate(DateUtil.toTimestamp(proc_date));
				claimdt[i].setLastProcDate(DateUtil.toTimestamp(proc_date));
				claimdt[i].setClaimAmt(orderdt[0].getOrderAmt());
				claimdt[i].setDcAmt(orderdt[0].getDcAmt());
				claimdt[i].setDcAmtGoods(orderdt[0].getDcAmtGoods());
				claimdt[i].setDcAmtMemb(orderdt[0].getDcAmtMemb());
				claimdt[i].setDcAmtDiv(orderdt[0].getDcAmtDiv());
				claimdt[i].setDcAmtCard(orderdt[0].getDcAmtCard());
				claimdt[i].setRsaleAmt(orderdt[0].getRsaleAmt());
				claimdt[i].setSaveamt(orderdt[0].getSaveamt());
				claimdt[i].setSaveamtGb(orderdt[0].getSaveamtGb());
				claimdt[i].setDoFlag(doFlag);
				claimdt[i].setLastProcDate(DateUtil.toTimestamp(proc_date));
				claimdt[i].setOrderQty(orderdt[0].getOrderQty());
				claimdt[i].setWhCode(orderdt[0].getWhCode());
				claimdt[i].setCwareAction("I");
				
				if(claimdt[i].getClaimGb().equals("45")){//교환회수일 경우.
					claimdt[i].setGoodsCode(orderdt[0].getGoodsCode());
					claimdt[i].setGoodsdtCode(orderdt[0].getGoodsdtCode());
					claimdt[i].setGoodsdtInfo(orderdt[0].getGoodsdtInfo());
					
					if (0 < orderdt[0].getExchangeYn()) { // 2024-07-25 교환 -> 재교환시 이전 교환배송건의 단품으로 교환회수건 데이터 생성
						HashMap<String, Object> exchangeGoodsdtCode = null;
						
						exchangeGoodsdtCode = paclaimDAO.selectExchangeGoodsdtCode(orderdt[0]);
						claimdt[i].setGoodsdtCode(exchangeGoodsdtCode.get("GOODSDT_CODE").toString());
						claimdt[i].setGoodsdtInfo(exchangeGoodsdtCode.get("GOODSDT_INFO").toString());
					}
				}
				userId = claimdt[i].getLastProcId();
			}
		}
		
		
		if(canceldt != null){
			for(int k=0;k<canceldt.length;k++){
				canceldt[k].setOrderNo(cancelDataSet[k].getOrderNo());
				canceldt[k].setOrderGSeq(cancelDataSet[k].getOrderGSeq());
				canceldt[k].setOrderDSeq(cancelDataSet[k].getOrderDSeq());
				canceldt[k].setCustNo(cancelDataSet[k].getCustNo());
				canceldt[k].setCancelDate(DateUtil.toTimestamp(proc_date));
				canceldt[k].setLastProcDate(DateUtil.toTimestamp(proc_date));
				canceldt[k].setCancelAmt(cancelDataSet[k].getOrderAmt());
				canceldt[k].setDcAmt(cancelDataSet[k].getDcAmt());
				canceldt[k].setDcAmtGoods(cancelDataSet[k].getDcAmtGoods());
				canceldt[k].setDcAmtMemb(cancelDataSet[k].getDcAmtMemb());
				canceldt[k].setDcAmtDiv(cancelDataSet[k].getDcAmtDiv());
				canceldt[k].setDcAmtCard(cancelDataSet[k].getDcAmtCard());
				canceldt[k].setRsaleAmt(cancelDataSet[k].getRsaleAmt());
				canceldt[k].setSaveamt(cancelDataSet[k].getSaveamt());
				canceldt[k].setSaveamtGb(cancelDataSet[k].getSaveamtGb());
				canceldt[k].setDoFlag("10");
				canceldt[k].setLastProcDate(DateUtil.toTimestamp(proc_date));
				canceldt[k].setWhCode(cancelDataSet[k].getWhCode());
				canceldt[k].setCwareAction("I");
				canceldt[k].setGoodsCode(cancelDataSet[k].getGoodsCode());
				canceldt[k].setGoodsdtCode(cancelDataSet[k].getGoodsdtCode());
				canceldt[k].setGoodsdtInfo(cancelDataSet[k].getGoodsdtInfo());
				canceldt[k].setReceiverSeq(cancelDataSet[k].getReceiverSeq());			
				canceldt[k].setGoodsGb(cancelDataSet[k].getGoodsGb());
				canceldt[k].setMediaGb(cancelDataSet[k].getMediaGb());
				canceldt[k].setMediaCode(cancelDataSet[k].getMediaCode());
				canceldt[k].setBuyPrice(cancelDataSet[k].getBuyPrice());
				canceldt[k].setVatRate(cancelDataSet[k].getVatRate());
				canceldt[k].setMdCode(cancelDataSet[k].getMdCode());
				canceldt[k].setSaleYn(cancelDataSet[k].getSaleYn());
				canceldt[k].setDelyType(cancelDataSet[k].getDelyType());
				canceldt[k].setDelyGb(cancelDataSet[k].getDelyGb());
				canceldt[k].setDelyHopeDate(cancelDataSet[k].getDelyHopeDate());
				canceldt[k].setDelyHopeYn(cancelDataSet[k].getDelyHopeYn());
				canceldt[k].setDelyHopeTime(cancelDataSet[k].getDelyHopeTime());
				canceldt[k].setPreoutGb(cancelDataSet[k].getPreoutGb());
				canceldt[k].setSingleDueGb(cancelDataSet[k].getSingleDueGb());
				canceldt[k].setRemark3N(0);
				canceldt[k].setRemark4N(0);
				
				userId = canceldt[k].getLastProcId();
			}
		}
		
		for(int l=0;l<orderpromo.length;l++){
			if(orderpromo[l] == null){
				orderpromo[l] = new OrderpromoVO();
			}
			orderpromo[l].setCancelYn("1");
			orderpromo[l].setCancelId(userId);
			orderpromo[l].setCancelDate(DateUtil.toTimestamp(proc_date));
			orderpromo[l].setCustNo(orderdt[0].getCustNo());
			orderpromo[l].setInsertDate(DateUtil.toTimestamp(proc_date));
			orderpromo[l].setInsertId(userId);
		}
		
		return executedRtn;
	}
	
	
	
	@SuppressWarnings("unused")
	private int justGetTreceiver(ReceiverVO receiver, PaClaimdtVO[] claimdt){
	
		// 0단계 check logic
		// 1단계 claimdtVO의 Receiverseq와 Post를 채워줌.  --> NewClaimdt 함수에서 배송 예정일에 사용함
		// 2단계 Tel, HP를 채워줌                    
		String orgReceiverSeq = receiver.getReceiverSeq();
		String orgReceiverPost = receiver.getReceiverPost();	
	
		
		
		if( "".equals(receiver.getReceiverPost())){
			receiver.setReceiverPost("999999");
			orgReceiverPost = "999999";
		}
	
		
		
		for(PaClaimdtVO claimdtVO : claimdt){
			claimdtVO.setReceiverSeq(orgReceiverSeq);
			claimdtVO.setReceiverPost(orgReceiverPost);
		}
			
		
		
		String hp = ComUtil.objToStr(claimdt[0].getReceiverHp());
		receiver.setReceiverHp(hp);
		if(hp.length() == 10){
			receiver.setReceiverHp1(hp.substring(0, 3));
			receiver.setReceiverHp2(hp.substring(3, 6));
			receiver.setReceiverHp3(hp.substring(6, 10));
		}else if(hp.length() == 11){
			receiver.setReceiverHp1(hp.substring(0, 3));
			receiver.setReceiverHp2(hp.substring(3, 7));
			receiver.setReceiverHp3(hp.substring(7, 11));
		}else if(hp.length() == 12){
			receiver.setReceiverHp1(hp.substring(0, 4));
			receiver.setReceiverHp2(hp.substring(4, 8));
			receiver.setReceiverHp3(hp.substring(8, 12));
		}else{
			receiver.setReceiverHp1(hp.substring(0, 4));
			receiver.setReceiverHp2(hp.substring(4, 8));
			receiver.setReceiverHp3(hp.substring(8));
		}
		
		String tel = ComUtil.objToStr(claimdt[0].getReceiverTel());
		
		receiver.setTel(tel);
		
		if(tel.length() == 7){	//= DDD 없음
			receiver.setReceiverDdd("");
			receiver.setReceiverTel1(tel.substring(0, 3));
			receiver.setReceiverTel2(tel.substring(3, 7));
		}else if(tel.length() == 8){ //= DDD 없음
			receiver.setReceiverDdd("");
			receiver.setReceiverTel1(tel.substring(0, 4));
			receiver.setReceiverTel2(tel.substring(4, 8));
		}else if(tel.length() == 9){
			receiver.setReceiverDdd(tel.substring(0, 2));
			receiver.setReceiverTel1(tel.substring(2, 5));
			receiver.setReceiverTel2(tel.substring(5, 9));
		}else if(tel.length() == 10){
			if(tel.substring(0, 2).equals("02")){
				receiver.setReceiverDdd(tel.substring(0, 2));
				receiver.setReceiverTel1(tel.substring(2, 6));
				receiver.setReceiverTel2(tel.substring(6, 10));
			}else{
				receiver.setReceiverDdd(tel.substring(0, 3));
				receiver.setReceiverTel1(tel.substring(3, 6));
				receiver.setReceiverTel2(tel.substring(6, 10));
			}
		}else if(tel.length() == 11){
			receiver.setReceiverDdd(tel.substring(0, 3));
			receiver.setReceiverTel1(tel.substring(3, 7));
			receiver.setReceiverTel2(tel.substring(7, 11));
		}else if(tel.length() == 12){
			receiver.setReceiverDdd(tel.substring(0, 4));
			receiver.setReceiverTel1(tel.substring(4, 8));
			receiver.setReceiverTel2(tel.substring(8, 12));
		}
		
		return 0;			
	}
	
	
	private int modifiedTreceiver(OrderdtVO[] orderdt, PaClaimdtVO[] claimdt, ReceiverVO receiver, ParamMap paramMap, String proc_date) throws Exception{
		ParamMap paramAddress = new ParamMap();
		////HashMap hashMap = new HashMap();
		int executedRtn = 0;
		ReceiverVO returnReceiver = new ReceiverVO();
		Boolean modifyYn = false;//주소 변경여부
		String orgReceiverSeq = receiver.getReceiverSeq();
		String orgReceiverPost = receiver.getReceiverPost();
		long receiverSeq = 0;
		long modifyCnt = 0; 
		String userId = "999999";
		
		String claimGB = "";
		
		for(int j=0;j<claimdt.length;j++){
			if(claimdt[j].getOrderDSeq().equals("001") && !claimdt[j].getCustDelyYn().equals("1")){//본품이면서 판매처 직업 수거일 경우에만 신규주소 생성.
				
				//반품, 교환 주소 미입력의 경우, 그냥 기존 Order의 주소를 사용한다.  , 입력후 정제에러는  미지정프로그램으로 넘김 
				if(claimdt[j].getRcvrMailNo() == null || "".equals(claimdt[j].getRcvrMailNo())){
					claimdt[j].setReceiverSeq(orgReceiverSeq);
					claimdt[j].setReceiverPost(orgReceiverPost);		
					continue;
				}				

				
				claimGB = claimdt[j].getClaimGb();
				
				//주문, 클레임 주소 같을 경우 order주소 사용
				if("30".equals(claimGB) && "1".equals(paramMap.getString("sameAddr"))
					|| "40".equals(claimGB) && "1".equals(paramMap.getString("sameAddrDel"))
					|| "45".equals(claimGB) && "1".equals(paramMap.getString("sameAddrRet")) ) {
					claimdt[j].setReceiverSeq(orgReceiverSeq);
					claimdt[j].setReceiverPost(orgReceiverPost);		
					continue;
				} 
				
				//SSG의 경우 교환배송지와 교환회수지 동일. 추가로 다른 제휴사도 교환/회수 배송지가 똑같으면 
				if("45".equals(claimGB) && "PASSG".equals(claimdt[j].getLastProcId())) {
					orgReceiverSeq = receiver.getReceiverSeq();
					orgReceiverPost = receiver.getReceiverPost();
	
					claimdt[j].setReceiverSeq(orgReceiverSeq);
					claimdt[j].setReceiverPost(orgReceiverPost);
					continue;
				}
				
				//paramAdress = (ParamMap) 
				ParamMap resultParamMap = null;
				resultParamMap 			= new ParamMap();
				
				resultParamMap.put("post_no", 			claimdt[j].getRcvrMailNo());
				
				if( "".equals(claimdt[j].getLastProcId()) || claimdt[j].getLastProcId() != null ){
					userId = claimdt[j].getLastProcId();	
				}
				
				if(claimdt[j].getRcvrTypeAdd().equals("02")) {//11번가 해당 항목 01 : 지번, 02 : 도로명
					resultParamMap.put("road_addr_yn", 	"1");
				} else {
					resultParamMap.put("road_addr_yn", 	"0");
				}
				
				resultParamMap.put("search_addr", 		claimdt[j].getRcvrBaseAddr());
				resultParamMap.put("search_addr2", 		claimdt[j].getRcvrDtlsAddr());
				resultParamMap.put("localYn", 			paramMap.get("localYn"));
				paramAddress = systemProcess.selectStdAddress(resultParamMap);//sk스토아 주소정제
				//신세계원소스 paramAddress = (ParamMap)retrieveStdAddress(claimdt[j].getReceiverAddr()); //주소정제 솔류션 사용
								
				
				
				//주소정제 시작
				if(paramAddress.getString("CL_SUCCESS_YN").equals("1")){
					
					if( paramAddress.getString("FLG").equals("1") && !paramAddress.getString("STD_POST_NO").equals("")){
							
						if(paramAddress.getString("ADDR_GBN").equals("1")){
							
							
							
							//= 정제된 주소가 지번주소 일 경우.
							returnReceiver.setReceiverPost(paramAddress.getString("STD_POST_NO"));       //우편번호
							returnReceiver.setReceiverPostSeq(paramAddress.getString("STD_POST_SEQ"));	  //우편번호seq
							
							returnReceiver.setReceiverPostAddr(claimdt[j].getRcvrBaseAddr());                                  //입력주소1 -지번
							returnReceiver.setReceiverAddr(claimdt[j].getRcvrDtlsAddr());                            //입력주소2 - 지번
							
							returnReceiver.setRoadAddrYn("0"); 
							returnReceiver.setRoadAddrNo("");  //ROAD_ADDR_NO								
							returnReceiver.setRoadPostAddr("");   //입력주소1  - 도로명
							returnReceiver.setRoadAddr("");    //입력주소 2  - 도로명 
							
							
							//도로명
							returnReceiver.setReceiverRoadPost(paramAddress.getString("STD_ROAD_POST_NO"));
							returnReceiver.setReceiverRoadPostSeq(paramAddress.getString("STD_ROAD_POST_SEQ"));
							returnReceiver.setReceiverRoadPostAddr(paramAddress.getString("STD_ROAD_POST_ADDR1"));
							returnReceiver.setReceiverRoadPostAddr2(paramAddress.getString("STD_ROAD_POST_ADDR2"));					
							returnReceiver.setStdRoadPostLaty(paramAddress.getString("STD_ROAD_POST_LATY"));
							returnReceiver.setStdRoadPostLngx(paramAddress.getString("STD_ROAD_POST_LNGX"));
							
							//지번
							returnReceiver.setReceiverStdPost(paramAddress.getString("STD_POST_NO"));
							returnReceiver.setReceiverStdPostSeq(paramAddress.getString("STD_POST_SEQ"));
							returnReceiver.setReceiverStdPostAddr(paramAddress.getString("STD_POST_ADDR1"));
							returnReceiver.setReceiverStdPostAddr2(paramAddress.getString("STD_POST_ADDR2"));
							returnReceiver.setStdPostLngx(paramAddress.getString("STD_POST_LNGX"));
							returnReceiver.setStdPostLaty(paramAddress.getString("STD_POST_LATY"));
							
							returnReceiver.setFullAddr(paramAddress.getString("STD_POST_ADDR1") + paramAddress.getString("STD_POST_ADDR2"));							
							returnReceiver.setSelectAddr("3");
							returnReceiver.setRefineResultCode(paramAddress.getString("REFINE_RESULT_CODE"));
							
						
						} else if(paramAddress.getString("ADDR_GBN").equals("2")){
							//= 정제된 주소가 도로명주소 일 경우.
							returnReceiver.setReceiverPost(paramAddress.getString("STD_ROAD_POST_NO"));
							returnReceiver.setReceiverPostSeq(paramAddress.getString("STD_ROAD_POST_SEQ"));	
							
							returnReceiver.setReceiverAddr("");                                  //입력주소1 -지번
							returnReceiver.setReceiverPostAddr("");                              //입력주소2 - 지번
							
							returnReceiver.setRoadAddrYn("1");
							returnReceiver.setRoadAddrNo(paramAddress.getString("ROAD_ADDR_NO"));  //ROAD_ADDR_NO								
							returnReceiver.setRoadPostAddr(claimdt[j].getRcvrBaseAddr());   //입력주소1  - 도로명
							returnReceiver.setRoadAddr(claimdt[j].getRcvrDtlsAddr());    //입력주소 2  - 도로명 
							
							
							//도로명
							returnReceiver.setReceiverRoadPost(paramAddress.getString("STD_ROAD_POST_NO"));
							returnReceiver.setReceiverRoadPostSeq(paramAddress.getString("STD_ROAD_POST_SEQ"));
							returnReceiver.setReceiverRoadPostAddr(paramAddress.getString("STD_ROAD_POST_ADDR1"));
							returnReceiver.setReceiverRoadPostAddr2(paramAddress.getString("STD_ROAD_POST_ADDR2"));					
							returnReceiver.setStdRoadPostLaty(paramAddress.getString("STD_ROAD_POST_LATY"));
							returnReceiver.setStdRoadPostLngx(paramAddress.getString("STD_ROAD_POST_LNGX"));
							
							//지번
							returnReceiver.setReceiverStdPost(paramAddress.getString("STD_POST_NO"));
							returnReceiver.setReceiverStdPostSeq(paramAddress.getString("STD_POST_SEQ"));
							returnReceiver.setReceiverStdPostAddr(paramAddress.getString("STD_POST_ADDR1"));
							returnReceiver.setReceiverStdPostAddr2(paramAddress.getString("STD_POST_ADDR2"));
							returnReceiver.setStdPostLngx(paramAddress.getString("STD_POST_LNGX"));
							returnReceiver.setStdPostLaty(paramAddress.getString("STD_POST_LATY"));
							
							returnReceiver.setFullAddr(paramAddress.getString("STD_ROAD_POST_ADDR1") + paramAddress.getString("STD_ROAD_POST_ADDR2"));							
							returnReceiver.setSelectAddr("2");
							returnReceiver.setRefineResultCode(paramAddress.getString("REFINE_RESULT_CODE"));		
							
						}else{
							returnReceiver.setReceiverPost("999999");       //우편번호
							returnReceiver.setReceiverPostSeq("001");	  //우편번호seq
							
							returnReceiver.setReceiverAddr("주소정제에 실패했습니다. 제휴미지정배송지 처리를 해주세요");
							returnReceiver.setReceiverPostAddr("실패주소 : " + claimdt[j].getRcvrBaseAddr() +" " + claimdt[j].getRcvrDtlsAddr());                            //입력주소2 - 지번
							
							returnReceiver.setRoadAddrYn("0"); 
							returnReceiver.setRoadAddrNo("");  //ROAD_ADDR_NO								
							returnReceiver.setRoadPostAddr("");   //입력주소1  - 도로명
							returnReceiver.setRoadAddr("");    //입력주소 2  - 도로명 
							
							
							//도로명
							returnReceiver.setReceiverRoadPost("");
							returnReceiver.setReceiverRoadPostSeq("");
							returnReceiver.setReceiverRoadPostAddr("");
							returnReceiver.setReceiverRoadPostAddr2("");					
							returnReceiver.setStdRoadPostLaty("");
							returnReceiver.setStdRoadPostLngx("");
							
							//지번
							returnReceiver.setReceiverStdPost("");
							returnReceiver.setReceiverStdPostSeq("");
							returnReceiver.setReceiverStdPostAddr("");
							returnReceiver.setReceiverStdPostAddr2("");
							returnReceiver.setStdPostLngx("");
							returnReceiver.setStdPostLaty("");
							
							returnReceiver.setFullAddr(claimdt[j].getRcvrBaseAddr() + claimdt[j].getRcvrDtlsAddr());							
							returnReceiver.setSelectAddr("1");
							returnReceiver.setRefineResultCode(paramAddress.getString("REFINE_RESULT_CODE"));
							
						}
						
					}else{ //수지넷 접속 성공 주소정제실패 Case
						
					
							
							returnReceiver.setReceiverPost("999999");       //우편번호
							returnReceiver.setReceiverPostSeq("001");	  //우편번호seq
							
							returnReceiver.setReceiverAddr("주소정제에 실패했습니다. 제휴미지정배송지 처리를 해주세요");
							returnReceiver.setReceiverPostAddr("실패주소 : " + claimdt[j].getRcvrBaseAddr() +" " + claimdt[j].getRcvrDtlsAddr());                            //입력주소2 - 지번
							
							returnReceiver.setRoadAddrYn("0"); 
							returnReceiver.setRoadAddrNo("");  //ROAD_ADDR_NO								
							returnReceiver.setRoadPostAddr("");   //입력주소1  - 도로명
							returnReceiver.setRoadAddr("");    //입력주소 2  - 도로명 
							
							
							//도로명
							returnReceiver.setReceiverRoadPost("");
							returnReceiver.setReceiverRoadPostSeq("");
							returnReceiver.setReceiverRoadPostAddr("");
							returnReceiver.setReceiverRoadPostAddr2("");					
							returnReceiver.setStdRoadPostLaty("");
							returnReceiver.setStdRoadPostLngx("");
							
							//지번
							returnReceiver.setReceiverStdPost("");
							returnReceiver.setReceiverStdPostSeq("");
							returnReceiver.setReceiverStdPostAddr("");
							returnReceiver.setReceiverStdPostAddr2("");
							returnReceiver.setStdPostLngx("");
							returnReceiver.setStdPostLaty("");
							
							returnReceiver.setFullAddr(claimdt[j].getRcvrBaseAddr() + claimdt[j].getRcvrDtlsAddr());							
							returnReceiver.setSelectAddr("1");
							returnReceiver.setRefineResultCode(paramAddress.getString("REFINE_RESULT_CODE"));
							
						
						
						
					}//end of CASE : 수지넷 접속 성공  ,주소정제실패
					
					
				} else { //수지넷 접속 실패
					//= 정제유형 오류일 경우.
					returnReceiver.setReceiverPost("999999");
					returnReceiver.setReceiverPostSeq("001");									
					returnReceiver.setReceiverAddr("주소정제에 실패했습니다. 제휴미지정배송지 처리를 해주세요");
					returnReceiver.setReceiverPostAddr("실패주소 : " + claimdt[j].getRcvrBaseAddr() +" " + claimdt[j].getRcvrDtlsAddr());                            //입력주소2 - 지번
					returnReceiver.setRoadAddrYn("0");
					returnReceiver.setRoadAddrNo("");  							
					returnReceiver.setRoadPostAddr("");  
					returnReceiver.setRoadAddr("");    
					
					
					//도로명
					returnReceiver.setReceiverRoadPost("");
					returnReceiver.setReceiverRoadPostSeq("");
					returnReceiver.setReceiverRoadPostAddr("");
					returnReceiver.setReceiverRoadPostAddr2("");					
					returnReceiver.setStdRoadPostLaty("");
					returnReceiver.setStdRoadPostLngx("");
					
					//지번
					returnReceiver.setReceiverStdPost("");
					returnReceiver.setReceiverStdPostSeq("");
					returnReceiver.setReceiverStdPostAddr("");
					returnReceiver.setReceiverStdPostAddr2("");
					returnReceiver.setStdPostLngx("");
					returnReceiver.setStdPostLaty("");
					
					returnReceiver.setFullAddr("");							
					returnReceiver.setSelectAddr("1");
					returnReceiver.setRefineResultCode(paramAddress.getString("REFINE_RESULT_CODE"));
					
				}//END OF 주소정제
				

			
				switch(claimGB){
				
					case "30":
					
						if(userId.equals("PA11")){
							modifyYn = false; //반품은 구지 receiver를 새로 따지 않는다 => 이유는 11번가에서 반품주소를 새로 입력할수 없다.
						}else{
							modifyYn = checkSameAddressYn(receiver , returnReceiver);			
						}
						
						break;
						
					case "40": case "45":  // 교환같은 경우  기존 주소지와 변경사항이 있거나, 혹은 주소정제 실패하면 새로운 receiver를 딴다
						                   //(고객이 교환할때 주문 시점의 주소가 바뀌었는데, 정제가 안됬다고 기존 주소지로 배송해버리면 택배가 미아상태가 되버린다. 차라리 9999999로 만들어서 미지정 배송처리를 하는게 옳다),  전화번호 변경은.... 나중에 생각하자
						modifyYn = checkSameAddressYn(receiver , returnReceiver);			
						break;
				}
				
				
				
								
				if(modifyYn){
					//treceiver 저장
					switch(claimGB){
					case "30": receiverSeq = Long.parseLong(systemService.getMaxNo("TRECEIVER", "RECEIVER_SEQ", " CUST_NO = '"+paramMap.get("custNo")+"'", 10)); break; //11번가에서 반품 배송지를 수정하지 않으니, 현재는 이걸 타면 절대 안된다.
					case "40": receiverSeq = Long.parseLong(systemService.getMaxNo("TRECEIVER", "RECEIVER_SEQ", " CUST_NO = '"+paramMap.get("custNo")+"'", 10)); modifyCnt ++; break;
					case "45": receiverSeq += modifyCnt; break; 				 	
					}
					
					
					receiver.setReceiverSeq	(ComUtil.lpad(Long.toString(receiverSeq), 10, "0"));
					receiver.setReceiverGb	("10");
					receiver.setUseYn       ("1");
					receiver.setDefaultYn   ("0");
					receiver.setCustNo		(orderdt[0].getCustNo());

			        String receiverName = claimdt[j].getReceiverName();
			        receiverName = ComUtil.removeEmoji(receiverName);
					receiver.setReceiver	(receiverName);
					receiver.setReceiver1	(receiverName);
					
					String hp = ComUtil.objToStr(claimdt[j].getReceiverHp());
					receiver.setReceiverHp(hp);
					if(hp.length() == 10){
						receiver.setReceiverHp1(hp.substring(0, 3));
						receiver.setReceiverHp2(hp.substring(3, 6));
						receiver.setReceiverHp3(hp.substring(6, 10));
					}else if(hp.length() == 11){
						receiver.setReceiverHp1(hp.substring(0, 3));
						receiver.setReceiverHp2(hp.substring(3, 7));
						receiver.setReceiverHp3(hp.substring(7, 11));
					}else if(hp.length() == 12){
						receiver.setReceiverHp1(hp.substring(0, 4));
						receiver.setReceiverHp2(hp.substring(4, 8));
						receiver.setReceiverHp3(hp.substring(8, 12));
					}else if(hp.length() == 0 ){
						receiver.setReceiverHp1("");
						receiver.setReceiverHp2("");
						receiver.setReceiverHp3("");
					}
					else{
						receiver.setReceiverHp1(hp.substring(0, 4));
						receiver.setReceiverHp2(hp.substring(4, 8));
						receiver.setReceiverHp3(hp.substring(8));
					}
					
					
					
					
					
					String tel = ComUtil.objToStr(claimdt[j].getReceiverTel());
					receiver.setTel(tel);
					
					if(tel.length() == 7){	//= DDD 없음
						receiver.setReceiverDdd("");
						receiver.setReceiverTel1(tel.substring(0, 3));
						receiver.setReceiverTel2(tel.substring(3, 7));
					}else if(tel.length() == 8){ //= DDD 없음
						receiver.setReceiverDdd("");
						receiver.setReceiverTel1(tel.substring(0, 4));
						receiver.setReceiverTel2(tel.substring(4, 8));
					}else if(tel.length() == 9){
						receiver.setReceiverDdd(tel.substring(0, 2));
						receiver.setReceiverTel1(tel.substring(2, 5));
						receiver.setReceiverTel2(tel.substring(5, 9));
					}else if(tel.length() == 10){
						if(tel.substring(0, 2).equals("02")){
							receiver.setReceiverDdd(tel.substring(0, 2));
							receiver.setReceiverTel1(tel.substring(2, 6));
							receiver.setReceiverTel2(tel.substring(6, 10));
						}else{
							receiver.setReceiverDdd(tel.substring(0, 3));
							receiver.setReceiverTel1(tel.substring(3, 6));
							receiver.setReceiverTel2(tel.substring(6, 10));
						}
					}else if(tel.length() == 11){
						receiver.setReceiverDdd(tel.substring(0, 3));
						receiver.setReceiverTel1(tel.substring(3, 7));
						receiver.setReceiverTel2(tel.substring(7, 11));
					}else if(tel.length() == 12){
						receiver.setReceiverDdd(tel.substring(0, 4));
						receiver.setReceiverTel1(tel.substring(4, 8));
						receiver.setReceiverTel2(tel.substring(8, 12));
					}else{
						receiver.setReceiverDdd("");
						receiver.setReceiverTel1("");
						receiver.setReceiverTel2("");
					}
				
					
					
					
	
					if((paramAddress.getString("CL_SUCCESS_YN").equals("1"))){
						
						if((paramAddress.getString("FLG").equals("1") && !(paramAddress.getString("STD_POST_NO").equals("")))){		
							if(paramAddress.getString("ADDR_GBN").equals("1")){

								//= 정제된 주소가 지번주소 일 경우.
								receiver.setReceiverPost(returnReceiver.getReceiverPost());       //우편번호
								receiver.setReceiverPostSeq(returnReceiver.getReceiverPostSeq());	  //우편번호seq
								
								receiver.setReceiverAddr(returnReceiver.getReceiverAddr());                                  //입력주소1 -지번
								receiver.setReceiverPostAddr(returnReceiver.getReceiverPostAddr());                            //입력주소2 - 지번
								
								receiver.setRoadAddrYn("0"); 
								receiver.setRoadAddrNo("");  //ROAD_ADDR_NO								
								receiver.setRoadPostAddr("");   //입력주소1  - 도로명
								receiver.setRoadAddr("");    //입력주소 2  - 도로명 
								
								
								//도로명
								receiver.setReceiverRoadPost(returnReceiver.getReceiverRoadPost());
								receiver.setReceiverRoadPostSeq(returnReceiver.getReceiverRoadPostSeq());
								receiver.setReceiverRoadPostAddr(returnReceiver.getReceiverRoadPostAddr());
								receiver.setReceiverRoadPostAddr2(returnReceiver.getReceiverRoadPostAddr2());					
								receiver.setStdRoadPostLaty(returnReceiver.getStdRoadPostLaty());
								receiver.setStdRoadPostLngx(returnReceiver.getStdRoadPostLngx());
								
								//지번
								receiver.setReceiverStdPost(returnReceiver.getReceiverStdPost());
								receiver.setReceiverStdPostSeq(returnReceiver.getReceiverStdPostSeq());
								receiver.setReceiverStdPostAddr(returnReceiver.getReceiverStdPostAddr());
								receiver.setReceiverStdPostAddr2(returnReceiver.getReceiverStdPostAddr2());
								receiver.setStdPostLngx(returnReceiver.getStdPostLngx());
								receiver.setStdPostLaty(returnReceiver.getStdPostLaty());
								
								receiver.setFullAddr((returnReceiver.getFullAddr()));							
								receiver.setSelectAddr("3");
								receiver.setRefineResultCode(returnReceiver.getRefineResultCode());
								
							
							} else if(paramAddress.getString("ADDR_GBN").equals("2")){
								//= 정제된 주소가 도로명주소 일 경우.
								receiver.setReceiverPost(returnReceiver.getReceiverPost());
								receiver.setReceiverPostSeq(returnReceiver.getReceiverPostSeq());	
								
								receiver.setReceiverAddr("");                                  //입력주소1 -지번
								receiver.setReceiverPostAddr("");                              //입력주소2 - 지번
								
								receiver.setRoadAddrYn("1");
								receiver.setRoadAddrNo(returnReceiver.getRoadAddrNo());  //ROAD_ADDR_NO								
								receiver.setRoadPostAddr(returnReceiver.getRoadPostAddr());   //입력주소1  - 도로명
								receiver.setRoadAddr(returnReceiver.getRoadAddr());    //입력주소 2  - 도로명 
								
								
								//도로명
								receiver.setReceiverRoadPost(returnReceiver.getReceiverRoadPost());
								receiver.setReceiverRoadPostSeq(returnReceiver.getReceiverRoadPostSeq());
								receiver.setReceiverRoadPostAddr(returnReceiver.getReceiverRoadPostAddr());
								receiver.setReceiverRoadPostAddr2(returnReceiver.getReceiverRoadPostAddr2());					
								receiver.setStdRoadPostLaty(returnReceiver.getStdRoadPostLaty());
								receiver.setStdRoadPostLngx(returnReceiver.getStdRoadPostLngx());
								
								//지번
								receiver.setReceiverStdPost(returnReceiver.getReceiverStdPost());
								receiver.setReceiverStdPostSeq(returnReceiver.getReceiverStdPostSeq());
								receiver.setReceiverStdPostAddr(returnReceiver.getReceiverStdPostAddr());
								receiver.setReceiverStdPostAddr2(returnReceiver.getReceiverStdPostAddr2());
								receiver.setStdPostLngx(returnReceiver.getStdPostLngx());
								receiver.setStdPostLaty(returnReceiver.getStdPostLaty());
								
								receiver.setFullAddr((returnReceiver.getFullAddr()));							
								receiver.setSelectAddr("2");
								receiver.setRefineResultCode(returnReceiver.getRefineResultCode());
								
								
								
							}else{
								
								
								receiver.setReceiverPost("999999");       //우편번호
								receiver.setReceiverPostSeq("001");	  //우편번호seq
								
								receiver.setReceiverAddr(returnReceiver.getReceiverAddr());                                 
								receiver.setReceiverPostAddr(returnReceiver.getReceiverPostAddr());    
								
								receiver.setRoadAddrYn("0"); 
								receiver.setRoadAddrNo("");  //ROAD_ADDR_NO								
								receiver.setRoadPostAddr("");   //입력주소1  - 도로명
								receiver.setRoadAddr("");    //입력주소 2  - 도로명 
								
								
								//도로명
								receiver.setReceiverRoadPost("");
								receiver.setReceiverRoadPostSeq("");
								receiver.setReceiverRoadPostAddr("");
								receiver.setReceiverRoadPostAddr2("");					
								receiver.setStdRoadPostLaty("");
								receiver.setStdRoadPostLngx("");
								
								//지번
								receiver.setReceiverStdPost("");
								receiver.setReceiverStdPostSeq("");
								receiver.setReceiverStdPostAddr("");
								receiver.setReceiverStdPostAddr2("");
								receiver.setStdPostLngx("");
								receiver.setStdPostLaty("");
								
								receiver.setFullAddr("");							
								receiver.setSelectAddr("1");
								receiver.setRefineResultCode(returnReceiver.getRefineResultCode());
								
							}
							
						}else{ //수지넷 접속 성공 주소정제실패 Case
							
							
								receiver.setReceiverPost("999999");       //우편번호
								receiver.setReceiverPostSeq("001");	  //우편번호seq
								
								receiver.setReceiverAddr(returnReceiver.getReceiverAddr());                                 
								receiver.setReceiverPostAddr(returnReceiver.getReceiverPostAddr());    
								
								receiver.setRoadAddrYn("0"); 
								receiver.setRoadAddrNo("");  //ROAD_ADDR_NO								
								receiver.setRoadPostAddr("");   //입력주소1  - 도로명
								receiver.setRoadAddr("");    //입력주소 2  - 도로명 
								
								
								//도로명
								receiver.setReceiverRoadPost("");
								receiver.setReceiverRoadPostSeq("");
								receiver.setReceiverRoadPostAddr("");
								receiver.setReceiverRoadPostAddr2("");					
								receiver.setStdRoadPostLaty("");
								receiver.setStdRoadPostLngx("");
								
								//지번
								receiver.setReceiverStdPost("");
								receiver.setReceiverStdPostSeq("");
								receiver.setReceiverStdPostAddr("");
								receiver.setReceiverStdPostAddr2("");
								receiver.setStdPostLngx("");
								receiver.setStdPostLaty("");
								
								receiver.setFullAddr("");							
								receiver.setSelectAddr("1");
								receiver.setRefineResultCode(returnReceiver.getRefineResultCode());
								
							
							
							
						}//end of CASE : 수지넷 접속 성공  ,주소정제실패
						
						
					} else {
						//= 정제유형 오류일 경우.
						receiver.setReceiverPost("999999");
						receiver.setReceiverPostSeq("001");									
						receiver.setReceiverAddr(returnReceiver.getReceiverAddr());                                 
						receiver.setReceiverPostAddr(returnReceiver.getReceiverPostAddr());    
						        								
						receiver.setRoadAddrYn("0");
						receiver.setRoadAddrNo("");  							
						receiver.setRoadPostAddr("");  
						receiver.setRoadAddr("");    
						
						
						//도로명
						receiver.setReceiverRoadPost("");
						receiver.setReceiverRoadPostSeq("");
						receiver.setReceiverRoadPostAddr("");
						receiver.setReceiverRoadPostAddr2("");					
						receiver.setStdRoadPostLaty("");
						receiver.setStdRoadPostLngx("");
						
						//지번
						receiver.setReceiverStdPost("");
						receiver.setReceiverStdPostSeq("");
						receiver.setReceiverStdPostAddr("");
						receiver.setReceiverStdPostAddr2("");
						receiver.setStdPostLngx("");
						receiver.setStdPostLaty("");
						
						receiver.setFullAddr("");							
						receiver.setSelectAddr("1");
						receiver.setRefineResultCode(returnReceiver.getRefineResultCode());
						
					}//END OF 주소정제
			
					
					receiver.setInsertId(userId);
					receiver.setInsertDate(DateUtil.toTimestamp(proc_date));
					receiver.setModifyId(userId);
					receiver.setModifyDate(DateUtil.toTimestamp(proc_date));
					claimdt[j].setReceiverSeq(receiver.getReceiverSeq());
					claimdt[j].setReceiverPost(receiver.getReceiverPost());
					
					executedRtn = paclaimDAO.insertReceiver(receiver);
					if (executedRtn < 1){
						throw processException("msg.cannot_save", new String[] { "TRECEIVER INSERT" });
					}
				}else{
					claimdt[j].setReceiverSeq(receiver.getReceiverSeq());
					claimdt[j].setReceiverPost(receiver.getReceiverPost());
				}
			}else{
				claimdt[j].setReceiverSeq(orgReceiverSeq);
				claimdt[j].setReceiverPost(orgReceiverPost);
			}
		}
		
		return executedRtn;
	}
	
	
	
	private boolean checkSameAddressYn(ReceiverVO receiver, ReceiverVO returnReceiver){

		boolean modifyYn = false; 

		if(receiver.getSelectAddr().equals("3")){
			if(receiver.getReceiverPost().equals(ComUtil.NVL(returnReceiver.getReceiverPost(), returnReceiver.getReceiverRoadPost()))) {
				if(receiver.getFullAddr().equals(returnReceiver.getFullAddr())){
					modifyYn = false;
				}else{
					modifyYn = true;
				}
			}else{
				modifyYn = true;
			}
		} else if(receiver.getSelectAddr().equals("2")){
			if(receiver.getReceiverPost().equals(returnReceiver.getReceiverRoadPost())){
				if(receiver.getFullAddr().equals(returnReceiver.getFullAddr())){
					modifyYn = false;
				}else{
					modifyYn = true;
				}
			}else{
				modifyYn = true;
			}
		}else {
			modifyYn = true;
		}
		
		if("999999".equals(returnReceiver.getReceiverPost())){
			modifyYn = true;
		}
		
		return modifyYn;
	}
	
		
	private int modifiedGoods(Ordergoods ordergoods) throws Exception {
		
		int executedRtn = 0;
		Ordergoods regOrdergoods = null;
		regOrdergoods = ordergoods;
		executedRtn = paclaimDAO.updateOrdergoods(regOrdergoods);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TORDERGOODS UPDATE" });
		}
		return executedRtn;
	}
	

	@SuppressWarnings({ "unused", "rawtypes" })
	private int newClaimdt(PaClaimdtVO[] claimdt, ReceiverVO receiver, String proc_date) throws Exception{
		int executedRtn = 0;
		PaClaimdtVO regClaimdt = null;
		String modiString = "";
		long claim_qty = 0;
		long order_qty = 0;

		double claim_amt = 0;
		double dc_amt = 0;
		double dc_amt_goods = 0;
		double dc_amt_memb = 0;
		double dc_amt_div = 0;
		double dc_amt_card = 0;
		double rsale_amt = 0;

		double org_claim_amt = 0;
		double org_dc_amt = 0;
		double org_dc_amt_goods = 0;
		double org_dc_amt_memb = 0;
		double org_dc_amt_div = 0;
		double org_dc_amt_card = 0;
		double org_rsale_amt = 0;

		double sum_org_claim_amt = 0;
		double sum_org_dc_amt = 0;
		double sum_org_dc_amt_goods = 0;
		double sum_org_dc_amt_memb = 0;
		double sum_org_dc_amt_div = 0;
		double sum_org_dc_amt_card = 0;
		double sum_org_rsale_amt = 0;
		double rsale_pa_amt = 0;

		long order_w_seq1 = 0;
		long order_w_seq45 = 0;
		long order_w_seq_cancel = 0;
		long exch_pair1 = 0;
		long order_w_seq_org = 0;
		
		for (int i=0;i<claimdt.length;i++){
			
			
			regClaimdt = claimdt[i];
			if (!regClaimdt.getCwareAction().equals("I"))
				continue;
			claim_qty = regClaimdt.getClaimQty();
			order_qty = regClaimdt.getOrderQty();
			
			//반품, 교환배송 ,교환회수
			if((regClaimdt.getClaimGb().equals("30")||regClaimdt.getClaimGb().equals("40")
					||regClaimdt.getClaimGb().equals("45")) && claim_qty > 0){
				
				
				
				exch_pair1 = 0;
				
				claim_amt = regClaimdt.getClaimAmt();
				dc_amt = regClaimdt.getDcAmt();
				dc_amt_goods = regClaimdt.getDcAmtGoods();
				dc_amt_memb = regClaimdt.getDcAmtMemb();
				dc_amt_div = regClaimdt.getDcAmtDiv();
				dc_amt_card = regClaimdt.getDcAmtCard();
				rsale_amt = regClaimdt.getRsaleAmt();
				
				
				org_claim_amt = ComUtil.modAmtRemoveWon((claim_amt / order_qty)*claim_qty);
				org_dc_amt_goods = 0;
				org_dc_amt_memb = 0;
				org_dc_amt_div = 0;
				org_dc_amt_card = 0;
				
				
				if (dc_amt_goods != 0)
					org_dc_amt_goods = ComUtil.modAmtRemoveWon((dc_amt_goods/order_qty)* claim_qty); // 절사작업
				if (dc_amt_memb != 0)
					org_dc_amt_memb = ComUtil.modAmtRemoveWon((dc_amt_memb/ order_qty)* claim_qty); // 절사작업 
				if (dc_amt_div != 0)
					org_dc_amt_div = ComUtil.modAmtRemoveWon((dc_amt_div/ order_qty)* claim_qty); // 절사작업 
				if (dc_amt_card != 0)
					org_dc_amt_card = ComUtil.modAmtRemoveWon((dc_amt_card/ order_qty)* claim_qty); // 절사작업 
				
				
				org_dc_amt = ComUtil.modAmt(org_dc_amt_goods + org_dc_amt_memb+ org_dc_amt_div + org_dc_amt_card);
				org_rsale_amt = ComUtil.modAmt(org_claim_amt - org_dc_amt);
								
				
				//반품 또는 교환배송
				if (regClaimdt.getClaimGb().equals("30") || regClaimdt.getClaimGb().equals("40")) {
									
								
					//W-Seq 따오는 부분...					
					modiString = " ORDER_NO    = '" + regClaimdt.getOrderNo()+"'"
							+ "   AND ORDER_G_SEQ = '"+ regClaimdt.getOrderGSeq()+"'"
							+ "   AND ORDER_D_SEQ = '"+ regClaimdt.getOrderDSeq()+"'";
					
					
					order_w_seq_cancel = Long.parseLong(systemService.getMaxNo("TCANCELDT", "ORDER_W_SEQ", modiString, 3));
					order_w_seq1 = Long.parseLong(systemService.getMaxNo("TCLAIMDT", "ORDER_W_SEQ", modiString, 3));
					order_w_seq45 = order_w_seq1;
					
					if (order_w_seq_cancel == 1) {
						// = 취소수량이 없는 경우
						if (order_w_seq1 == 1) {
							// = 반품/교환을 처음하는 경우
							order_w_seq1++;
							order_w_seq45++;
						}
					} else {
						// = 취소수량이 있는 경우
						if (order_w_seq1 == 1) {
							// = 반품/교환을 처음하는 경우
							order_w_seq1 = order_w_seq1 + (order_w_seq_cancel- 1); 
							order_w_seq45 = order_w_seq45 + (order_w_seq_cancel- 1);
						}
					}
				}else{
					order_w_seq1 = order_w_seq45 + 1;  //regClaimdt.getClaimGb().equals("45")) && claim_qty > 0
				}
						
	
				//교환 pair 처리
				exch_pair1 = order_w_seq1;
				if (regClaimdt.getClaimGb().equals("30")) {  //반품
					exch_pair1 = 0;
				}
				if (regClaimdt.getClaimGb().equals("40")) {  //교환배송
					exch_pair1 = order_w_seq1;
				} 
				else if (regClaimdt.getClaimGb().equals("45")) { //교환 회수
					exch_pair1 = order_w_seq1 - 1;
				}
			
				if(order_w_seq1 == 0 || order_w_seq1 == 1) {
					throw processException("msg.cannot_save", new String[] {"newClaimdt(OrderWseq) Error" } );
				}
		
				// = insert TCLAIMDT
				if(regClaimdt.getOrderDSeq().equals("001")){ //본품
					
					//= 제휴사 실결제금액 계산
					rsale_pa_amt = paclaimDAO.selectRsalePaAmt(regClaimdt.getClaimGb(), regClaimdt.getOrderNo(), regClaimdt.getOrderGSeq(), regClaimdt.getOrderDSeq(), claim_qty);
					if(rsale_pa_amt < 0){
						//= 제휴사 실결제금액 계산 실패.
						throw processException("pa.failed_to_calculate_rsale_pa_amt");
					}
					
					//백호선 검증필요
					regClaimdt.setOrderWSeq(ComUtil.lpad(Long.toString(order_w_seq1), 3, "0"));
					order_w_seq_org = order_w_seq1; 
					
				} else {
					rsale_pa_amt = 0;
					regClaimdt.setOrderWSeq(ComUtil.lpad(Long.toString(order_w_seq_org), 3, "0"));
				}
				
				
				
				regClaimdt.setExchPair(ComUtil.lpad(Long.toString(exch_pair1), 3, "0"));
				regClaimdt.setClaimQty(claim_qty);
				regClaimdt.setSyscancel(0);
				regClaimdt.setClaimAmt(org_claim_amt);
				regClaimdt.setDcAmt(org_dc_amt);
				regClaimdt.setDcAmtGoods(org_dc_amt_goods);
				regClaimdt.setDcAmtMemb(org_dc_amt_memb);
				regClaimdt.setDcAmtDiv(org_dc_amt_div);
				regClaimdt.setDcAmtCard(org_dc_amt_card);
				regClaimdt.setRsaleAmt(org_rsale_amt);
				regClaimdt.setSyslastDc(org_dc_amt);
				regClaimdt.setSyslastDcGoods(org_dc_amt_goods);
				regClaimdt.setSyslastDcMemb(org_dc_amt_memb);
				regClaimdt.setSyslastDcDiv(org_dc_amt_div);
				regClaimdt.setSyslastDcCard(org_dc_amt_card);
				regClaimdt.setSyslastAmt(org_rsale_amt);
				regClaimdt.setHappyCardYn("0"); //축전카드 구분
				regClaimdt.setRepayGb("0");     //환불구분[J080]   -  0 미처리, 1 환불완료, 2 선환불완료, 9 환불오류
				regClaimdt.setSaveamtGb("90");  //적립금구분[C025]
				regClaimdt.setRsalePaAmt(rsale_pa_amt);
				
				
				ParamMap uniqueCheckMap = new ParamMap();
				uniqueCheckMap.put("orderNo", regClaimdt.getOrderNo());
				uniqueCheckMap.put("orderGSeq",regClaimdt.getOrderGSeq());
				uniqueCheckMap.put("orderDSeq",regClaimdt.getOrderDSeq());
				uniqueCheckMap.put("orderWSeq",regClaimdt.getOrderWSeq());
				
				
				if (paclaimDAO.checkUniqueClaimdt(uniqueCheckMap) > 0) {
					throw processException(
							"msg.cannot_save",
							new String[] { "TCLAIMDT DUPLICATED ORDER_W_SEQ" });
				}
				
				
					
				
				
				/**Start Calculate 배송예정일...**/
				
				Timestamp firstPlanDate = null;
				Timestamp delyHopeDate  = null;
				Timestamp outPlanDate = null;
				Timestamp dely_date_1 = null;
				HashMap<String, Object> planDateInfoMap = new HashMap<>();
				planDateInfoMap = paclaimDAO.selectPlanDateInfo(regClaimdt);
				
			 
				
				if(regClaimdt.getClaimGb().equals("40")){
				
					
				    	   
					 	String temp_goodsdt_code = "";
						String dateTime = proc_date;
						ParamMap paramMap = null;
						String stock_chk_place = planDateInfoMap.get("STOCK_CHK_PLACE").toString();
						String buyMed = planDateInfoMap.get("BUY_MED").toString();
						String whCode = planDateInfoMap.get("WH_CODE").toString();
						String delyGb = regClaimdt.getDelyGb();		
						String delyType = regClaimdt.getDelyType();
						String stockMode = "1";
						long outPlanCnt = 0;
						long delyAbleDay = 0;
						String postNo = "";
						String roadAddrNo = "";
									
						if( "20".equals(delyType)){ //업체배송
							if(!"13".equals(buyMed)){  // 직사입 타창고
								whCode = ConfigUtil.getString("ENTP_WH_CODE");  //이거 값 체크해보자.					
							}					
						}
						
						stockMode = systemService.getVal("STOCK_CHECK_MODE").substring(0,1);
						
						
						ParamMap delyGbMap = null;
					    delyGbMap = new ParamMap();
					    delyGbMap.put("delyGb", delyGb);
					    delyGbMap.put("whCode", whCode);
					    delyGbMap.put("goodsCode", regClaimdt.getGoodsCode());
					    delyGbMap.put("custNo", regClaimdt.getCustNo());
					    delyGbMap.put("receiverSeq", regClaimdt.getReceiverSeqOrg());
					  
					    delyGbMap.put("RESULT_GOODS_CHECK_DELY_GB", paorderDAO.getGoodsCheckDelyGb(delyGbMap));
					    
					    whCode = ((HashMap)((List)delyGbMap.get("RESULT_GOODS_CHECK_DELY_GB")).get(0)).get("WH_CODE").toString();
					    delyGb = ((HashMap)((List)delyGbMap.get("RESULT_GOODS_CHECK_DELY_GB")).get(0)).get("DELY_GB").toString();

					    
					    //=배달지에 따른 배송소요일 SELECT. :: 최종 : DELY_GB 는 비교하지 않고 POST_NO 만 이용해서 배송소요일 계산한다.
				        paramMap = new ParamMap();
				        //paramMap.put("postNo", orderStockIn.getPostNo());
				        
				        
				        if(receiver.getReceiverPost() != null) postNo = receiver.getReceiverPost();
				        if(receiver.getRoadAddrNo()   != null) roadAddrNo = receiver.getRoadAddrNo();
				        
				        
				        paramMap.put("postNo", postNo);
				        paramMap.put("roadAddrNo", roadAddrNo);
				        
				        delyAbleDay = paorderDAO.getDeliveryPointCount(paramMap); 
				        
				        
				        
						
						if("10".equals(delyGb)  || buyMed.equals("13") || 
								( ("20").equals(delyType) && "1".equals(stockMode) ) ){
							
							outPlanDate = DateUtil.toTimestamp(dateTime, "yy/MM/dd");
							outPlanCnt = paorderDAO.getOutPlanCnt();  //주문 접수 일이 (토요일이면 12시 / 평일 이면 14시) 이전이면 당일 작업, 이후이면 +1일 을 하여 산정한다.
							
							
							
							
							paramMap = new ParamMap();
			                paramMap.put("delyGb", delyGb);
			                paramMap.put("workYn", "1");
			                paramMap.put("dataNum", 4);
			                paramMap.put("outPlanDate", outPlanDate);
			                paramMap.put("outPlanCnt", outPlanCnt);
			                executedRtn = getDelyday( paramMap );
			                
			                if( executedRtn == -1){
			                	throw processException("msg.cannot_save", new String[] {"OUT_PLAN_DATE CHECK"});
			                }
			                
			                
			                dely_date_1 = DateUtil.toTime(paramMap.getString("DELY_DATE_1"));
			                outPlanDate = dely_date_1;
			                /**
			                 * 배송예정일 :  출하예정일 + 배송소요일
			                 * 기존에는 수도권과 지방으로 나누어 지방은 +1일 이 되었으나 현재는 지방여부 상관없이 산정한다.
			                 */
			                outPlanCnt = delyAbleDay ;
			                
			                paramMap = new ParamMap();
			                paramMap.put("delyGb", delyGb);
			                paramMap.put("workYn", "1");
			                paramMap.put("dataNum", 4);
			                paramMap.put("outPlanDate", outPlanDate);
			                paramMap.put("outPlanCnt", outPlanCnt);
			                executedRtn = getDelyday( paramMap );
			               
			                if( executedRtn == -1){
			                	throw processException("msg.cannot_save", new String[] {"OUT_PLAN_DATE CHECK"});
			                }
			                
			                dely_date_1 = DateUtil.toTime(paramMap.getString("DELY_DATE_1"));
			                firstPlanDate = dely_date_1;
			                delyHopeDate  = dely_date_1;
			                
			            	regClaimdt.setFirstPlanDate(firstPlanDate);
							regClaimdt.setOutPlanDate(outPlanDate);
							regClaimdt.setDelyHopeDate(delyHopeDate);
			                
						}
							
						
						temp_goodsdt_code = regClaimdt.getGoodsdtCode();
				        if( stock_chk_place.equals("1") && !regClaimdt.getGoodsdtCode().equals("000") ){
				            temp_goodsdt_code = "000";
				        } else if( stock_chk_place.equals("3") && regClaimdt.getGoodsdtCode().equals("000") ) {
				            temp_goodsdt_code = paorderDAO.getTargetGoodsdt( regClaimdt.getGoodsCode() );
				        }
				        
				        paramMap = new ParamMap();
				        paramMap.put("goodsCode", regClaimdt.getGoodsCode());
				        paramMap.put("goodsdtCode", temp_goodsdt_code);
				        paramMap.put("RESULT_CHECK", paorderDAO.getSupplyCapa(paramMap));
						
				        if(paramMap.get("RESULT_CHECK") == null){
		                	throw processException("msg.cannot_save", new String[] {"OUT_PLAN_DATE CHECK"});
		                }
				        
				        
				        
				        outPlanDate = DateUtil.toTimestamp(dateTime, "yy/MM/dd");
				        outPlanCnt = 1; //leadTime = 1;
						
						

				        paramMap = new ParamMap();
				        paramMap.put("delyGb",delyGb);
				        paramMap.put("workYn","1");
				        paramMap.put("dataNum",4);
				        paramMap.put("outPlanDate",outPlanDate);
				        paramMap.put("outPlanCnt",outPlanCnt);
				        executedRtn=getDelyday(paramMap);
				        
				        
				        if( executedRtn == -1){
		                	throw processException("msg.cannot_save", new String[] {"OUT_PLAN_DATE CHECK"});
		                }
				        
				        dely_date_1 = DateUtil.toTime(paramMap.getString("DELY_DATE_1"));
				        outPlanDate   = dely_date_1;
				        
				        
				        /**
				         * 배송예정일 : 출하예정일 + 배송소요일
				         * 기존에는 수도권과 지방으로 나누어 지방은 +1일 이 되었으나 현재는 지방여부 상관없이 산정한다.
				         */
				        outPlanCnt = delyAbleDay;
				        paramMap=new ParamMap();
				        paramMap.put("delyGb",delyGb);
				        paramMap.put("workYn","1");
				        paramMap.put("dataNum",4);
				        paramMap.put("outPlanDate",outPlanDate);
				        paramMap.put("outPlanCnt",outPlanCnt);
				        executedRtn=getDelyday(paramMap);

		                if( executedRtn == -1){
		                	throw processException("msg.cannot_save", new String[] {"DELY_HOPE_DATE CHECK"});
		                }
		                
		                dely_date_1 = DateUtil.toTime(paramMap.getString("DELY_DATE_1"));
		                firstPlanDate = dely_date_1;
		                delyHopeDate  = dely_date_1;
				        		             
		            	regClaimdt.setFirstPlanDate(firstPlanDate);
						regClaimdt.setOutPlanDate(outPlanDate);
						regClaimdt.setDelyHopeDate(delyHopeDate); 
				    
				 
				 
					
					
				}else { //= 주문의 배송예정일 사용.
					
					
					firstPlanDate = DateUtil.toTime(planDateInfoMap.get("FIRST_PLAN_DATE").toString());
					outPlanDate = DateUtil.toTime(planDateInfoMap.get("OUT_PLAN_DATE").toString());
					delyHopeDate = DateUtil.toTime(planDateInfoMap.get("DELY_HOPE_DATE").toString());
					regClaimdt.setFirstPlanDate(firstPlanDate);
					regClaimdt.setOutPlanDate(outPlanDate);
					regClaimdt.setDelyHopeDate(delyHopeDate);				
				
				} //end of 배송예정일...
				
				
				//CostApply 계산용 - Transaction 문제 때문에 어쩔수 없이......
				if(regClaimdt.getClaimGb().equals("30")||regClaimdt.getClaimGb().equals("40")){
					claimdt[0].setNewOrderWeq(regClaimdt.getOrderWSeq());
				}
			
				
				if(regClaimdt.getCustDelyYn().equals("1")){ //= 고객배송처리 건일 경우 TORDERPROC 10, 51, 52 데이터 생성
					//= insert torderproc
					
					
					if("45".equals(regClaimdt.getClaimGb()) || "30".equals(regClaimdt.getClaimGb()) ) {
						regClaimdt.setDoFlagOrg("10");
						executedRtn = paclaimDAO.insertOrderproc(regClaimdt);
						if (executedRtn != 1) {
							throw processException("msg.cannot_save",
									new String[] { "TORDERPROC insert" });
						}
						
//						regClaimdt.setDoFlagOrg("50");
//						executedRtn = paclaimDAO.insertOrderproc(regClaimdt);
//						if (executedRtn != 1) {
//							throw processException("msg.cannot_save",
//									new String[] { "TORDERPROC insert" });
//						}
					}

//					if("12".equals(regClaimdt.getCsMgroup())) { //하프클럽 고객직접발송
					regClaimdt.setDelyGb("75");
					regClaimdt.setSlipNo("고객직접발송");
//					} //2023-12-14 제휴주문의 고객직접발송 회수건을 식별하기 위해서 반품테이블(TCLAIMDT)의 배송구분(DELY_GB)값은 '75' 로 셋팅(DR-2310-0394)

					//= insert tclaimdt
					executedRtn = paclaimDAO.insertClaimdt(regClaimdt);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save",
								new String[] { "TCLAIMDT insert" });
					}
					
						
				} else {
					//= insert tclaimdt
					executedRtn = paclaimDAO.insertClaimdt(regClaimdt);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save",
								new String[] { "TCLAIMDT insert" });
					}
				}//백호선 2018/05/05 여기까지가 반품, 교환배송 regClaimdt 만들어주는 로직
			
				
				

				
			//반품취소 31, 교환 발송 취소 41, 교환 회수취송 46	
			}else if(regClaimdt.getClaimGb().equals("31") ||regClaimdt.getClaimGb().equals("41") || regClaimdt.getClaimGb().equals("46")){
					
				
		
				if(regClaimdt.getClaimGb().equals("31") ||regClaimdt.getClaimGb().equals("41")){
					
					modiString = " ORDER_NO    = '" + regClaimdt.getOrderNo() + "'"
							+ "   AND ORDER_G_SEQ = '"
							+ regClaimdt.getOrderGSeq() + "'"
							+ "   AND ORDER_D_SEQ = '"
							+ regClaimdt.getOrderDSeq()+ "'";
					order_w_seq_cancel = Long.parseLong(systemService.getMaxNo(
							"TCANCELDT", "ORDER_W_SEQ", modiString, 3));
					order_w_seq1 = Long.parseLong(systemService.getMaxNo("TCLAIMDT", "ORDER_W_SEQ", modiString, 3));
					
					if (order_w_seq_cancel == 1) {
						// = 취소수량이 없는 경우
						if (order_w_seq1 == 1) {
							// = 반품/교환을 처음하는 경우
							order_w_seq1++;
						}
					} else {
						// = 취소수량이 있는 경우
						if (order_w_seq1 == 1) {
							// = 반품/교환을 처음하는 경우
							order_w_seq1 = order_w_seq1 + order_w_seq_cancel - 1;
						}
					}
					
				}else{
				
				    order_w_seq1++;  				
				}
					
								
				
				regClaimdt.setOrderWSeq(ComUtil.lpad(Long.toString(order_w_seq1), 3, "0"));
				regClaimdt.setSyslastDc(regClaimdt.getDcAmt());
				regClaimdt.setSyslastDcGoods(regClaimdt.getDcAmtGoods());
				regClaimdt.setSyslastDcMemb(regClaimdt.getDcAmtMemb());
				regClaimdt.setSyslastDcDiv(regClaimdt.getDcAmtDiv());
				regClaimdt.setSyslastDcCard(regClaimdt.getDcAmtCard());
				regClaimdt.setSyslastAmt(regClaimdt.getRsaleAmt());
				regClaimdt.setHappyCardYn("0");
				regClaimdt.setRepayGb("0");
				
				if (regClaimdt.getClaimGb().equals("31")) {
					regClaimdt.setSaveamtGb("90"); // 반품취소일 경우 바로 확정한다.
				}
				
				ParamMap uniqueCheckMap = new ParamMap();
				uniqueCheckMap.put("orderNo", regClaimdt.getOrderNo());
				uniqueCheckMap.put("orderGSeq", regClaimdt.getOrderGSeq());
				uniqueCheckMap.put("orderDSeq", regClaimdt.getOrderDSeq());
				uniqueCheckMap.put("orderWSeq", regClaimdt.getOrderWSeq());
				
				if (paclaimDAO.checkUniqueClaimdt(uniqueCheckMap) > 0) {
					throw processException("msg.cannot_save",
							new String[] {"TCLAIMDT CHECK"});
				}
				
				if (regClaimdt.getClaimGb().equals("31") ||regClaimdt.getClaimGb().equals("41")){
					claimdt[0].setNewOrderWeq(regClaimdt.getOrderWSeq()); //CostApply계산용
				}
				
				
				executedRtn = paclaimDAO.insertClaimdt(regClaimdt);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save",
							new String[] { "TCLAIMDT insert" });
				}
				
			}
			
			if (regClaimdt.getClaimGb().equals("31")) {
				
				if (regClaimdt.getOrderDSeq().equals("001")) {
					
					executedRtn = paclaimDAO.updateOrdergoodsClaimCanQty(regClaimdt);
					if (executedRtn != 1) {
						throw processException(
								"msg.cannot_save",
								new String[] { "TORDERGOODS updateTordergoodsClaimCanQty" });
					}
				}
			}
			
		}
		
		return executedRtn;
	}
	
	
	
	
	private int modifiedClaimdt(PaClaimdtVO[] claimdt) throws Exception {
		int executedRtn = 0;
		PaClaimdtVO regClaimdt = null;
		for(int i=0;i<claimdt.length;i++){
			regClaimdt = claimdt[i];
			if (!regClaimdt.getCwareAction().equals("U"))
				continue;
			executedRtn = paclaimDAO.updateClaimdt(regClaimdt);
			if (executedRtn == 0) {
				throw processException(
						"msg.cannot_save",
						new String[] { "TCLAIMDT update (TCLAIMDT-already changed)" });
			}
			if (executedRtn != 1) {
				throw processException("msg.cannot_save",
						new String[] { "TCLAIMDT update" });
			}
			
		}
		return executedRtn;
	}
	
	private int modifiedOrderpromo(OrderpromoVO[] orderpromo, ParamMap paramMap,
		String proc_date, PaClaimdtVO[] claimdt) throws Exception {
		int executedRtn = 0;
		Promocounsel promoCounsel = null;
		OrderpromoVO regOrderpromo = null;
		
		for(int i=0;i<orderpromo.length;i++){
			regOrderpromo = orderpromo[i];
			if(claimdt[0].getClaimGb().equals("30")){
				if(regOrderpromo.getLimitYn().equals("1")){ //= 한정 프로모션일 경우
					promoCounsel = new Promocounsel();
					
					promoCounsel.setPromoNo(regOrderpromo.getPromoNo());
					promoCounsel.setOrderNo(regOrderpromo.getOrderNo());
					promoCounsel.setOrderGSeq(regOrderpromo.getOrderGSeq());
					promoCounsel.setGoodsSelectNo(getSequenceNo("GOODS_SELECT_NO"));
					promoCounsel.setCounselQty(claimdt[0].getClaimQty() * -1);
					promoCounsel.setInsertId(regOrderpromo.getCancelId());
					promoCounsel.setInsertDate(regOrderpromo.getCancelDate());
					
					executedRtn = paclaimDAO.insertPromoCounsel(promoCounsel);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save",
								new String[] { "TPROMOCOUNSEL insert" });
					}
				}
				
				if(paramMap.getInt("orderSyslast") == claimdt[0].getClaimQty()){//전체반품
					regOrderpromo.setRemark("Order Claim");//---------------------값 확인 필요
					executedRtn = paclaimDAO.updateOrderpromo(regOrderpromo);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save",
								new String[] { "TORDERPROMO update" });
					}
				}
			}
		}
		return executedRtn;
	}
	
	private int newCounsel(PaClaimdtVO[] claimdt, ParamMap paramMap, ReceiverVO receiver, String proc_date) throws Exception {
		int executedRtn = 0;
		
		
		PaClaimdtVO regClaimdt = null;
		Custcounselm custcounselm = new Custcounselm();
		Custcounseldt custcounseldt = new Custcounseldt();
		String counsel_seq = "";
		String counselProcNote = "";
		String ref_id = "";
		String userId = "999999";
		
		
		for(int i=0; i<claimdt.length;i++){
			userId =  claimdt[i].getLastProcId();
			regClaimdt = claimdt[i];
			if(!regClaimdt.getOrderDSeq().equals("001")) continue; //= 사은품은 상담내역을 생성하지 않음.
			
			if(regClaimdt.getClaimGb().equals("30")||regClaimdt.getClaimGb().equals("40")){
				
				counsel_seq = systemProcess.getSequenceNo("COUNSEL_SEQ");
				if (counsel_seq.equals("")) {
					throw processException("msg.cannot_create",
							new String[] { "COUNSEL_SEQ" });
				}
				paramMap.put("dely_type", "10");
				paramMap.put("out_lgroup_code", "87");
				paramMap.put("out_mgroup_code", "10");
				ref_id = ComUtil.NVL(paclaimDAO.selectCustRefId(paramMap), "");
				
				custcounselm.setCounselSeq(counsel_seq);
				custcounselm.setDoFlag("40");
				custcounselm.setCustNo(regClaimdt.getCustNo());// 고객번호 확인
				custcounselm.setRefNo1(regClaimdt.getOrderNo());
				custcounselm.setRefNo2(regClaimdt.getOrderGSeq());
				custcounselm.setRefNo3(regClaimdt.getOrderDSeq());
				custcounselm.setRefNo4(regClaimdt.getOrderWSeq());
				custcounselm.setGoodsCode(regClaimdt.getGoodsCode());
				custcounselm.setGoodsdtCode(regClaimdt.getGoodsdtCode());
				//반품접수시 out_lgroup_code : 87, out_mgroup_code : 10 고정
				custcounselm.setOutLgroupCode("87");
				custcounselm.setOutMgroupCode("10");
				custcounselm.setOutSgroupCode("");
				
				custcounselm.setClaimNo(counsel_seq);
				custcounselm.setTel(ComUtil.NVL(receiver.getReceiverHp(),receiver.getTel()));
				custcounselm.setDdd(ComUtil.NVL(receiver.getReceiverHp1(),receiver.getReceiverDdd()));
				custcounselm.setTel1(ComUtil.NVL(receiver.getReceiverHp2(),receiver.getReceiverTel1()));
				custcounselm.setTel2(ComUtil.NVL(receiver.getReceiverHp3(),receiver.getReceiverTel2()));
				custcounselm.setTel3("");
				custcounselm.setWildYn("0");
				custcounselm.setQuickEndYn("0");
				custcounselm.setQuickYn("0");
				custcounselm.setHcReqDate(null);
				custcounselm.setRemark("");
				custcounselm.setRefId1(ref_id);
				custcounselm.setCsSendYn("0");
				custcounselm.setCounselMedia("61");
				
				custcounselm.setCsLgroup(claimdt[i].getCsLgroup());
				custcounselm.setCsMgroup(claimdt[i].getCsMgroup());
				custcounselm.setCsSgroup(claimdt[i].getCsSgroup());
				custcounselm.setCsLmsCode(claimdt[i].getCsLmsCode());
				
				custcounselm.setInsertId(userId);
				custcounselm.setInsertDate(DateUtil.toTimestamp(proc_date));
				custcounselm.setProcId(userId);
				custcounselm.setProcDate(DateUtil.toTimestamp(proc_date));
				
				
								
				executedRtn = paclaimDAO.insertCounselCustcounselm(custcounselm);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save",
							new String[] { "Custcounselm insert" });
				}
				
				custcounseldt.setCounselSeq(counsel_seq);
				custcounseldt.setCounselDtSeq("100");
				custcounseldt.setDoFlag("10");
				custcounseldt.setTitle("");
				custcounseldt.setDisplayYn("");
				
				
				/* 추후에 처리 요망
				
				if(claimdt[i].getClaimType().equals("01")||claimdt[i].getClaimType().equals("03")){//일반반품 || 일반교환
					counselProcNote = paclaimDAO.selectCustCounselDtProcNote(claimdt[i].getGoodsCode());
					counselProcNote = counselProcNote+"상세사유 : "+regClaimdt.getClaimDesc();
				}else if(claimdt[i].getClaimType().equals("02")){//사유반품
					counselProcNote = "제휴주문 반품처리";
				}else if(claimdt[i].getClaimType().equals("04")){//사유교환
					counselProcNote = "제휴주문 교환처리";
				}*/
				
				
				counselProcNote = "상세사유 : "+regClaimdt.getClaimDesc();
				
				// = ["] -> [']로 전환
				custcounseldt.setProcNote(ComUtil.text2db(counselProcNote));
				custcounseldt.setProcDate(DateUtil.toTimestamp(proc_date));
				custcounseldt.setProcId(userId);
				
								
				
				executedRtn = paclaimDAO.insertCounselCustcounseldt(custcounseldt);
				
				if (executedRtn != 1) {
					throw processException("msg.cannot_save",
							new String[] { "TCUSTCOUNSELDT insert" });
				} else {
					//상담 완료 데이터 생성
					custcounseldt.setCounselDtSeq("101");
					custcounseldt.setDoFlag("40");
					custcounseldt.setEndDate(DateUtil.toTimestamp(proc_date));
					custcounseldt.setDisplayYn("1");
									
					executedRtn = paclaimDAO.insertCounselCustcounseldt80(custcounseldt);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save",
								new String[] { "TCUSTCOUNSELDT insert" });
					}
				}
				
			}
			
		}
		
		return executedRtn;
	} 
	
	

	@SuppressWarnings("unused")
	private int newCanceldt(Canceldt[] canceldt, PaClaimdtVO[] claimdt, String proc_date) throws Exception{
		int executedRtn = 0;
		PaClaimdtVO regClaimdt = null;
		Canceldt regCanceldt = null;
		Canceldt cancelDataSet = null;
		
		OrderstockVO orderstock = null;
		String userId = "999999";
		String modiString = "";
		long claim_qty = 0;
		long order_qty = 0;
		
		for (int i=0;i<canceldt.length;i++){
			regCanceldt = canceldt[i];
			userId = canceldt[i].getLastProcId();
			
			executedRtn = paclaimDAO.updateOrderdtSyslast(regCanceldt);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TORDERDT UPDATE" });
			}
			
			regCanceldt.setOrderWSeq(claimdt[0].getOrderWSeq());
			regCanceldt.setSalePrice(0);
			regCanceldt.setDcRateGoods(0);
			regCanceldt.setDcRate(0);
			regCanceldt.setRsaleNet(0);
			regCanceldt.setRsaleVat(0);
			regCanceldt.setDcAmt(0);
			regCanceldt.setDcAmtGoods(0);
			regCanceldt.setDcAmtMemb(0);
			regCanceldt.setDcAmtDiv(0);
			regCanceldt.setDcAmtCard(0);
			regCanceldt.setRsaleAmt(0);
			regCanceldt.setSaveamtGb("90");
			regCanceldt.setModifyDate(regCanceldt.getCancelDate());
			regCanceldt.setRsalePaAmt(0);
			
			executedRtn = paclaimDAO.insertCanceldt(regCanceldt);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save",
						new String[] { "TCANCELDT insert" });
			}
			
			orderstock = new OrderstockVO();
			
			orderstock.setOutPlanQty(regCanceldt.getCancelQty());
			orderstock.setTotSaleQty(regCanceldt.getCancelQty());
			orderstock.setGoodsCode(regCanceldt.getGoodsCode());
			orderstock.setGoodsdtCode(regCanceldt.getGoodsdtCode());
			orderstock.setWhCode(regCanceldt.getWhCode());
			orderstock.setPreoutGb(regCanceldt.getPreoutGb());
			orderstock.setModifyDate(DateUtil.toTimestamp(proc_date));
			orderstock.setModifyId(userId);//유저아이디 넣어야함
			
			executedRtn = paclaimDAO.updateOrderStockCancel20(orderstock);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save",
						new String[] { "TORDERSTOCK insert" });
			}
			
			/*// = 최대판매가능수량 차감처리
			executedRtn = paclaimDAO.updateInplanqty(regCanceldt);
			*/
		}
		
		return executedRtn;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap<String, Object> newOrdershipcost(PaClaimdtVO claimdt[], ParamMap paramMap, String proc_date) throws Exception {
		

		
	
		
		Collection cResultOrdershipcost = new ArrayList();
		Collection cResultOrdercostapply = new ArrayList();
		//Long orderWseq = paramMap.getLong("orderWseq");
		//PaClaimdtVO orgClmdt = null;
		
		OrdershipcostVO[] returnCustShipCost = null;
		Ordercostapply[]  returnOrderCostApply = null;
		
		OrdershipcostVO ordershipcost = null;
		OrderShipCostVO orderShipCost = null;
		
		HashMap<String, Object> retrunMap  = new HashMap<>();
			
				
		String arg_goods_str    = "";
		String arg_gseq_str      = "";
		String arg_dseq_str     = "";
		String arg_wseq_str     = "";		
		String arg_qty_str      = "";
		String arg_amt_str      = "";
		String arg_cust_no		= "";
		String arg_receiver_seq = "";
		String arg_rcv_str      = "";
		String result           = null;
		String arg_calc_qty = "";
		
		String[] shipcostArr;
		String[] shipcostmArr;
		String[] shipcostapplyArr;
		String orderGB = paramMap.getString("orderGB");
		String shpFeeYn = null;
		boolean bGo = false;
		
		
		//회수지 또는 배송지가 미지정 처리일때는 배송비를 생성하지 않는다 
		//( 교환은 제휴 미지정에서 만들어주고,  교환취소는 교환이 만들어지지 않았으니  만들지 말아야함 ＊교환취소된 교환건이 제휴 미지정프로그램에 나오면 그건 절대 안된다 ) 
		for(PaClaimdtVO ct : claimdt){
			
			if("999999".equals(ct.getReceiverPost())){
				
				returnCustShipCost = (OrdershipcostVO[]) cResultOrdershipcost.toArray(new OrdershipcostVO[0]);
				returnOrderCostApply = (Ordercostapply[]) cResultOrdercostapply.toArray(new Ordercostapply[0]);
				
				
				retrunMap.put("OrderShipCost", returnCustShipCost);
				retrunMap.put("OrderCostApply", returnOrderCostApply);
					
				return retrunMap;
			}		
		}
		
	
		
		
		for (PaClaimdtVO orgClmdt : claimdt) {
	
			shpFeeYn = orgClmdt.getShpfeeYn();				
			if(!orgClmdt.getCwareAction().equals("I")) continue;
			
			arg_calc_qty = Long.toString(orgClmdt.getClaimQty());
			arg_cust_no = orgClmdt.getCustNo();
			
			//arg_receiver_seq = orgClmdt.getReceiverSeq();
		
			
			//if(!orgClmdt.getClaimGb().equals("45")){
				//arg_receiver_seq = orgClmdt.getReceiverSeq();
			//}
			arg_receiver_seq += " " + orgClmdt.getReceiverSeq();
			
			
			arg_goods_str += " " + orgClmdt.getGoodsCode();
			
			arg_gseq_str  += " " + orgClmdt.getOrderGSeq();
			arg_dseq_str  += " "  + orgClmdt.getOrderDSeq();
			arg_wseq_str  += " "  + orgClmdt.getOrderWSeq();
			 
			arg_rcv_str   += " " + "1";
			arg_amt_str   += " " + Double.toString((orgClmdt.getRsaleAmt() / orgClmdt.getOrderQty()) * Double.parseDouble(arg_calc_qty));
			//arg_amt_str   = " " + Double.toString(orgOrderdt.getRsaleAmt());
			arg_qty_str   += " " + arg_calc_qty;	
			
			bGo = true;
			
		}
		
		arg_receiver_seq = arg_receiver_seq.trim();
		arg_goods_str = arg_goods_str.trim();
		arg_qty_str = arg_qty_str.trim();
		arg_amt_str = arg_amt_str.trim();
		arg_rcv_str = arg_rcv_str.trim();
		arg_gseq_str = arg_gseq_str.trim();
		arg_dseq_str= arg_dseq_str.trim();
		arg_wseq_str= arg_wseq_str.trim(); 
			
		
		orderShipCost = new OrderShipCostVO();	
		orderShipCost.setCustNo(arg_cust_no);
		orderShipCost.setOrderFlag("40");
		
		orderShipCost.setDate(claimdt[0].getClaimDate());
		orderShipCost.setShipcostReceiptStr(arg_rcv_str);
		orderShipCost.setGoodsStr(arg_goods_str);
		orderShipCost.setAmtStr(arg_amt_str);
		orderShipCost.setQtyStr(arg_qty_str);
		
		orderShipCost.setOrderNo(paramMap.getString("orderNo"));
		orderShipCost.setReceiverSeq(arg_receiver_seq);
		
		
		orderShipCost.setCancelQtyStr(arg_qty_str);
		orderShipCost.setReturnQtyStr(arg_qty_str);
		orderShipCost.setExchangeQtyStr(arg_qty_str);
		orderShipCost.setOrderGSeqStr(arg_gseq_str);
		orderShipCost.setOrderDSeqStr(arg_dseq_str);
		orderShipCost.setOrderWSeqStr(arg_wseq_str);
			
			
		if( bGo == true){
			result = ComUtil.NVL(orderBizProcess.orderShipCost(orderShipCost));											
		}
		
	

		if(result != null ){
			if(result.length() >0 ){
				
			
				shipcostArr = result.split("::");
				shipcostmArr = shipcostArr[0].split(" ");	
				shipcostapplyArr = 	shipcostArr[1].split(" ");
				
				for (int j = 0; j < shipcostmArr.length - 7; j++) {
					if (j % 8 == 0) {
						
					   
						ordershipcost = new OrdershipcostVO();
						ordershipcost.setBlank();
						ordershipcost.setCwareAction("I");
						ordershipcost.setOrderNo(shipcostmArr[j]);
						ordershipcost.setSeq(shipcostmArr[j+1]);
						ordershipcost.setReceiverSeq(shipcostmArr[j+2]);
						//ordershipcost.setType(shipcostmArr[j+3]);
						
						ordershipcost.setType(orderGB);
						ordershipcost.setEntpCode(shipcostmArr[j+4]);
						
						if ("1".equals(shpFeeYn)){
							ordershipcost.setShpfeeCost(Double.parseDouble(shipcostmArr[j+5]));
							ordershipcost.setManualCancelAmt(0);		
						}else{
							ordershipcost.setShpfeeCost(0);
							ordershipcost.setManualCancelAmt(Double.parseDouble(shipcostmArr[j+5]));
							ordershipcost.setManualCancelId(paramMap.getString("userId"));
							ordershipcost.setManualCancelDate(DateUtil.toTimestamp(proc_date));
						}
												
						ordershipcost.setShipCostNo(shipcostmArr[j+6]);
						ordershipcost.setShipCostReceipt("1"); //1 - 선불, 2 - 착불
						ordershipcost.setShpfeeCode("20");
						ordershipcost.setInsertId(paramMap.getString("userId"));
						ordershipcost.setInsertDate(DateUtil.toTimestamp(proc_date));
						ordershipcost.setOrderDSeq("");
						ordershipcost.setOrderGSeq("");
						ordershipcost.setOrderWSeq("");
						ordershipcost.setDelyType("");

						cResultOrdershipcost.add(ordershipcost);
										
					}
				}
				
				for(int j = 0; j < shipcostapplyArr.length - 6; j++) {
					if( j % 7 == 0 ) {
						Ordercostapply ordercostapply = new Ordercostapply();
						ordercostapply.setBlank();
						ordercostapply.setOrderNo(shipcostapplyArr[j]);
						ordercostapply.setOrderGSeq(shipcostapplyArr[j+1]);
						ordercostapply.setOrderDSeq(shipcostapplyArr[j+2]);
				
						ordercostapply.setOrderWSeq(shipcostapplyArr[j+3]);
						//ordercostapply.setOrderWSeq(ComUtil.lpad(Long.toString(orderWseq) , 3, "0"));
						ordercostapply.setApplyCostSeq(shipcostapplyArr[j+4]);
						ordercostapply.setShipCostNo(Integer.parseInt(shipcostapplyArr[j+5]));
						ordercostapply.setCwareAction(shipcostapplyArr[j+6]);
						ordercostapply.setInsertId(paramMap.getString("userId"));
						ordercostapply.setInsertDate(DateUtil.toTimestamp(proc_date));
						ordercostapply.setModifyId(paramMap.getString("userId"));
						ordercostapply.setModifyDate(DateUtil.toTimestamp(proc_date));
						cResultOrdercostapply.add(ordercostapply);
					}
				}
				
			}
				
		}// END of result != null check
		
			
		//= 취소된 수량으로 재계산한 배송비 정보.
		returnCustShipCost = (OrdershipcostVO[]) cResultOrdershipcost.toArray(new OrdershipcostVO[0]);
		returnOrderCostApply = (Ordercostapply[]) cResultOrdercostapply.toArray(new Ordercostapply[0]);
		
		
		retrunMap.put("OrderShipCost", returnCustShipCost);
		retrunMap.put("OrderCostApply", returnOrderCostApply);
			
		return retrunMap;
	
	}

	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private int updateStockClaim(PaClaimdtVO[] claimdt) throws Exception{
		int executedRtn = 0;
		PaClaimdtVO regClaimdt = null;
		StockVO stock = null;
		ParamMap stockMap = null;
		int rowCnt = 0;
		long s_type = 0;
		List list = new ArrayList(rowCnt);
		int orderAbleQty = 0; 
		
		rowCnt = claimdt.length;
		for (int i = 0; i < rowCnt; i++) {
			regClaimdt = claimdt[i];
			if(regClaimdt.getCwareAction().equals("I")){
				stock = new StockVO();
				stock.setSType(4);
				stock.setGoodsCode(regClaimdt.getGoodsCode());
				stock.setGoodsdtCode(regClaimdt.getGoodsdtCode());
				stock.setClaimGb(regClaimdt.getClaimGb());
				stock.setClaimQty(regClaimdt.getClaimQty());
				stock.setWhCode(regClaimdt.getWhCode());
				list.add(stock);
			}
		} // end for
		// = update TSTOCK
		rowCnt = list.size();
		for (int i = 0; i < rowCnt; i++) {
			stock = (StockVO) list.get(i);
			s_type = stock.getSType();
			if (s_type == 4) { // 불만상세 INSERT
				// 교환배송일때, 상담접수재고 증가
				if (stock.getClaimGb().equals("40")) {
					executedRtn = paclaimDAO.updateOrderStockExchange(stock);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save",
								new String[] { "TORDERSTOCK update exchange" });
					}
					
					//= torderstock update 후 잔여 재고 check.
					stockMap = new ParamMap();
					stockMap.put("goodsCode", 	stock.getGoodsCode());
					stockMap.put("goodsdtCode", stock.getGoodsdtCode());
					stockMap.put("whCode", 		stock.getWhCode());
					
					orderAbleQty = paorderDAO.selectOrderAbleQtyPa(stockMap);
					//= FUN_GET_ORDER_ABLE_QTY 조회 결과 (-)수량일 경우 exception. 
					if(orderAbleQty < 0){
						//= [상품코드/단품코드] 의 재고가 없습니다.
						throw processMessageException(
								getMessage("errors.nodata.stock", new String[] { stockMap.getString("goodsCode") + "/" + stockMap.getString("goodsdtCode") }));
					}
					
				// 불만구분이 '41'(교환배송취소)이면 출하예정수량에서 뺀다.
				} else if (stock.getClaimGb().equals("41")) {
					executedRtn = paclaimDAO.updateOrderStockExchangeCancel(stock);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save",
								new String[] { "TORDERSTOCK update exchange cancel" });
						
					}
				}
			}
		}
		return executedRtn;
	}
	
	// 시퀀스 조회
	public String getSequenceNo(String type) throws Exception {
		String sequenceNo = "";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("sequence_type", type);
		if (hashMap.get("sequence_type").equals("CUST_NO")
				|| hashMap.get("sequence_type").equals("ORDER_NO")
				|| hashMap.get("sequence_type").equals("GOODS_SELECT_NO")
				|| hashMap.get("sequence_type").equals("RECEIPT_NO")) {
			hashMap.put("condition_flag", (String) paclaimDAO.selectSequenceNoCondition(hashMap));
		}
		sequenceNo = (String) paclaimDAO.selectSequenceNo(hashMap);
		if (sequenceNo == null || sequenceNo.equals(""))
			throw processException("msg.cannot_retrieve", new String[] { "getSequenceNo : " + type });
		return sequenceNo;
	}
	
	/**
	 * getDelyday 배송예정일 계산
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
    @SuppressWarnings({ "rawtypes", "unused" })
    private int getDelyday( ParamMap paramMap ) throws Exception {
    	int         executedRtn    = 0;
		List  rtnList = new ArrayList();
		
		paramMap.put("RESULT_CHECK",  paorderDAO.getDelyday(paramMap));
		
		   //= tdelyday 의 데이터가 없을 경우 주문은 정상적으로 저장 처리 하기 위해서 휴일 관리를 무시하고 sysdate 기준으로 데이터를 조회한다.        
		if( paramMap.get("RESULT_CHECK") == null || ((List)paramMap.get("RESULT_CHECK")).size() < 4 ){
			log.error("## tdelyday have no valid data. that check is skipped. However, the data must be checked.");
			paramMap.put("RESULT_CHECK",  paorderDAO.getDelydayExcept(paramMap));
		}        
		   
		if(paramMap.get("RESULT_CHECK") == null){
			paramMap.put("DELY_DATE_1", "");
			paramMap.put("DELY_DATE_2", "");
			paramMap.put("DELY_DATE_3", "");
			paramMap.put("DELY_DATE_4", "");
			executedRtn = -1;
		}else{
			if(((List)paramMap.get("RESULT_CHECK")).size() < 4){
				executedRtn = -1;
			}else{
				//=출하예정일                  : 기준일자 + 1
				//=배송예정일(서울1): 기준일자 + 2
				//=배송예정일(서울2): 기준일자 + 3
				//=배송예정일(지방) : 기준일자 + 4
				paramMap.put("DELY_DATE_1", ((HashMap)((List)paramMap.get("RESULT_CHECK")).get(0)).get("DELYDAY"));
				paramMap.put("DELY_DATE_2", ((HashMap)((List)paramMap.get("RESULT_CHECK")).get(1)).get("DELYDAY"));
				paramMap.put("DELY_DATE_3", ((HashMap)((List)paramMap.get("RESULT_CHECK")).get(2)).get("DELYDAY"));
				paramMap.put("DELY_DATE_4", ((HashMap)((List)paramMap.get("RESULT_CHECK")).get(3)).get("DELYDAY"));
				executedRtn = 1;
			}
		}
		return executedRtn;
    }
    
    /**
	 * system 시간조회
	 * @param null
	 * @return String
	 * @throws Exception
	 */
    public String getSysdatetimeToString() throws Exception {
    	return paorderDAO.getSysdatetime();
    }
    
    /**
	 * modifiedReceipts
	 * @param Orderreceipts
	 * @return int
	 * @throws Exception
	 */
	private int modifiedReceipts(ArrayList<Orderreceipts> orderreceiptsList) throws Exception {
		int executedRtn = 0;
		List<Orderreceipts> orgReceiptsList = null;
		Orderreceipts orderreceipts = null;
		Orderreceipts regOrderreceipts = null;
		
		if(orderreceiptsList != null){
			for(int i = 0; orderreceiptsList.size() > i; i++){
				orderreceipts = orderreceiptsList.get(i);
				
				//if(orderreceipts.getSettleGb().equals("61")) orderreceipts.setSettleGb("11");
				
				if(orderreceipts.getSettleGb().equals("61")) continue;       //의미없다
				else if(orderreceipts.getSettleGb().equals("11")) continue;  //의미없다
				else if(orderreceipts.getSettleGb().equals("15")) continue;  //의미없다
				else if(orderreceipts.getSettleGb().equals("65")) orderreceipts.setSettleGb("15");
				else throw processMessageException("Invalid settleGb Code");
				
				
				//= 정산배송비(14)의 경우 여러행이 있을 수 있으므로 repay_pb_amt 를 차감할 금액을 계산하여 update 함.
				orgReceiptsList = paclaimDAO.selectOrderRepayPbAmt(orderreceipts);
				
				
				double tempAmt = 0;
				
				if(orgReceiptsList.size() > 0){  
					
					if(orgReceiptsList.size() == 1){
						regOrderreceipts = new Orderreceipts();
						regOrderreceipts.setReceiptNo(orgReceiptsList.get(0).getReceiptNo());
						regOrderreceipts.setSettleGb(orderreceipts.getSettleGb());
						regOrderreceipts.setOrderNo(orderreceipts.getOrderNo());
						regOrderreceipts.setQuestAmt(orderreceipts.getQuestAmt());
						
						log.info("//---- update TORDERRECEIPTS");
						executedRtn = paorderDAO.updateReceiptsRepay(regOrderreceipts);
						if (executedRtn != 1) {
							throw processException("msg.cannot_save", new String[] { "TORDERRECEIPTS UPDATE" });
						}
						
					} else {  						
						
						tempAmt = orderreceipts.getQuestAmt();
						//procPbAmt = 0;
						boolean compYn = false;
						
						for(int k = 0; orgReceiptsList.size() > k; k++){
							if(tempAmt == orgReceiptsList.get(k).getRepayPbAmt()){ //= 동일한 잔여 금액이 있을 경우.
								regOrderreceipts = new Orderreceipts();
								regOrderreceipts.setReceiptNo(orgReceiptsList.get(k).getReceiptNo());
								regOrderreceipts.setSettleGb(orderreceipts.getSettleGb());
								regOrderreceipts.setOrderNo(orderreceipts.getOrderNo());
								regOrderreceipts.setQuestAmt(tempAmt);
								
								log.info("//---- update TORDERRECEIPTS");
								executedRtn = paorderDAO.updateReceiptsRepay(regOrderreceipts);
								if (executedRtn != 1) {
									throw processException("msg.cannot_save", new String[] { "TORDERRECEIPTS UPDATE" });
								}
								compYn = true;
								break;
							}
						}
						
						if(!compYn){
							for(int j = 0; orgReceiptsList.size() > j; j++){ //= 동일한 잔여 금액이 없을 경우.
								if(tempAmt == 0) break;
								
								regOrderreceipts = new Orderreceipts();
								regOrderreceipts.setReceiptNo(orgReceiptsList.get(j).getReceiptNo());
								regOrderreceipts.setSettleGb(orderreceipts.getSettleGb());
								regOrderreceipts.setOrderNo(orderreceipts.getOrderNo());
								
								if(tempAmt > orgReceiptsList.get(j).getRepayPbAmt()){
									tempAmt = orderreceipts.getQuestAmt() - orgReceiptsList.get(j).getRepayPbAmt();
									regOrderreceipts.setQuestAmt(orgReceiptsList.get(j).getRepayPbAmt());
								} else {
									regOrderreceipts.setQuestAmt(tempAmt);
									tempAmt = 0;
								}
								
								log.info("//---- update TORDERRECEIPTS");
								executedRtn = paorderDAO.updateReceiptsRepay(regOrderreceipts);
								if (executedRtn != 1) {
									throw processException("msg.cannot_save", new String[] { "TORDERRECEIPTS UPDATE" });
								}
							}
						}
					}
				} else {
					throw processMessageException("TORDERRECEIPTS update invalid target");
				}
				
			}
		}
			
		return executedRtn;
	}
	
	
	/**
	 * 출고전 반품 프로세스 (BO 기준)
	 * @param HashMap
	 * @return int
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int saveDeliveryReqOutCancel(HashMap<String, Object> orderMap, ReceiverVO receiver) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		String sysdate = systemService.getSysdatetimeToString();
		String slipINo = "";
		String stockModNo = "";
		int chkPrsQty = 0;
		
		Map<String, String> headers = null;
        JSONObject orderInfo = null;
        JSONArray orderList = null;
        JSONObject sndList = null;
		String responseText = null;
        String lotteDelivUrl = ConfigUtil.getString("LOTTE_DELIVERY_CANCEL_URL");
		String lotteApiKey = ConfigUtil.getString("LOTTE_DELIVERY_APIKEY");
		
		HashMap temp = paclaimDAO.selectDeliveryReqOutCancelTarget(orderMap);
		List<HashMap<String, Object>> cancelMap = new ArrayList();
		
		if(temp != null && temp.size() > 0) {
			
			cancelMap.add(temp);
			
			ParamMap resultMap = new ParamMap();
			resultMap.setParamMap(cancelMap.get(0));
			
			if(resultMap != null ){ // 출고전반품대상
				resultMap.put("MODIFY_ID", orderMap.get("MODIFY_ID"));
				resultMap.put("MODIFY_DATE", sysdate);
				executedRtn = paclaimDAO.updatedirectClaimdt(resultMap);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TCLAIMDT update" });
				}
			
				// = TORDERGOODS update
				if (resultMap.get("ORDER_D_SEQ").toString().equals("001")
						&& (!resultMap.get("GOODS_GB").toString().equals("30"))) {
					executedRtn = paclaimDAO.updateDeliveryReqOutCancelOrdergoods(resultMap);
					if (executedRtn < 1) {
						throw processException("msg.cannot_save",
								new String[] { "TORDERGOODS update" });
					}
				}
	
				slipINo = paclaimDAO.selectSlipINoSequence();
				
				//= 직사입타창고건인경우 msg_note 컬럼에 현재시간 셋팅
				//=> 위탁업체창고 제외 나머지의 경우(직매입타창고, 직매입센터입고, 위수탁센터입고)로 변경
	            if ( !resultMap.get("BUY_MED").toString().equals("22") ) {
	            	resultMap.put("MSG_NOTE", sysdate);
	            }
	            
				// = TSLIPM insert (반품)
	            resultMap.put("NEW_SLIP_I_NO", slipINo);
	            resultMap.put("RETURN_QC_CODE", "100");
				executedRtn = paclaimDAO.insertCancelSlipm(resultMap);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TSLIPM insert" });
				}
				
				// = TSLIPDT insert (반품)
				executedRtn = paclaimDAO.insertClaimSlipdt(resultMap);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TSLIPDT insert" });
				}
	
				// = TORDERSTOCK update
				executedRtn = paclaimDAO.updateOrderstock(resultMap);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TORDERSTOCK update" });
				}
				
				//=> 재고관리하는 상품의 경우 출고처리 시 정상재고 차감, 회수확정 시 대기재고 증가 (양품화 배치에서 대기->정상으로 변경됨) 
				//=> 직매입타창고 재고 수불 반영 제외
				if ( resultMap.get("BUY_MED").toString().equals("11") || resultMap.get("BUY_MED").toString().equals("21") ) {
					executedRtn = paclaimDAO.updatetStock(resultMap);
					if (executedRtn < 1) {
						throw processException("msg.cannot_save", new String[] { "TSTOCK update" });
					}
					
					executedRtn = paclaimDAO.updateRack(resultMap);
					if (executedRtn < 1) {
						throw processException("msg.cannot_save", new String[] { "TRACK update" });
					}
					
					//BO 재고등급변경 프로그램에서 조회하기 위한 insert
					stockModNo = paclaimDAO.selectStockModReqSeqNo();
					
					resultMap.put("STOCK_MOD_NO", stockModNo);
					executedRtn = paclaimDAO.insertStockModReq(resultMap);
					if (executedRtn < 1) {
						throw processException("msg.cannot_save", new String[] { "TSTOCK_MOD_REQ insert" });
					}
					
					//회수확정으로 인한 대기수량 증가
					executedRtn = paclaimDAO.updateReturnStockWaitQty(resultMap);
					if (executedRtn < 1) {
						throw processException("msg.cannot_save", new String[] { "TSTOCK update" });
					}
				}
				
				// 배송완료처리
				executedRtn = paclaimDAO.updateOrderDt(resultMap);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TORDERDT update" });
				}
				
				// = 기존 출하지시건을 배송완료 상태로 변경
				// = tslipm update
				executedRtn = paclaimDAO.updateSlipM(resultMap);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TSLIPM update" });
				}
				
				
				// 분리포장일 때 배송(SLIP) 중 한건은 출고, 한건은 출하지시이면 주문(ORDER) 상태는 출하지시 상태임
				// 이때 출하지시 건을 출고전반품처리 하면 배송(SLIP)은 출고이지만 주문(ORDER)은 출하지시상태로 남게 됨
				// 따라서 주문(ORDER)의 상태를 배송(SLIP)과 동기화 시켜 줌
				/*else{
					executedRtn = paclaimDAO.chkSlipProcMin(cancelMap);
					if(executedRtn == 1){
						executedRtn = paclaimDAO.updateOrderSlipDoFlagSync(cancelMap);
						if (executedRtn < 1) {
							throw processException("msg.cannot_save", new String[] { "TORDERDT update" });
						}
					}
				}*/
				
				//= TSLIPCREATE UPDATE
				//직매입상품의 경우만->위탁업체창고 제외 나머지
				if ( !resultMap.get("BUY_MED").toString().equals("22") ) {
					executedRtn = paclaimDAO.updateSlipcreate40(resultMap);
					if (executedRtn < 0){
						throw processException("msg.cannot_save", new String[]{"TSLIPCREATE update"});
					}
				}
				
				//직송 체크 쿼리
				/*//20220901 CJ대한통운 직송/직택배 DB to DB에서  API 연동으로 변경 
				executedRtn = paclaimDAO.selectEntpDirectDeliveryCount(resultMap);
				if (executedRtn > 0){
					executedRtn = paclaimDAO.insertEntpDirectReturnBeforeOut(resultMap);
					if (executedRtn < 1){
						throw processException("msg.cannot_save", new String[]{"TSLIPCJORDERINFO insert"});
					}	
				}
				
				// 출고전반품정보 TSLIPCJORDERINFO 테이블에 저장(반품인 경우)
				executedRtn = paclaimDAO.selectEntpDirectReturnCount(resultMap);
				if (executedRtn > 0){
					executedRtn = paclaimDAO.insertEntpDirectReturnBeforeReturn(resultMap);
					if (executedRtn < 1){
						throw processException("msg.cannot_save", new String[]{"TSLIPCJORDERINFO insert"});
					}	
				}*/
				
				//출고전반품 CJ대한통운 직송/직택배 예약취소 API - TSLIPCJORDERINFO
				List<HashMap<String, Object>> cjDeliveryList = null;
				List<HashMap<String, Object>> cjReturnList = null;
				
				//주문건 취소처리
				cjDeliveryList = paclaimDAO.selectEntpDirectCjDeliveryList(resultMap);
				requestCjOutCancel("out", cjDeliveryList, resultMap);
				//반품건 취소처리
				cjReturnList = paclaimDAO.selectEntpDirectCjReturnList(resultMap);
				requestCjOutCancel("return", cjReturnList, resultMap);
				
				//출고전반품정보 TSLIPLOTTEORDERINFO 조회 -20220127
				List<HashMap<String, Object>> lottelist = null;
				HashMap<String, Object> lotteParam = null;
				lottelist = paclaimDAO.selectEntpDirectLotteDeliveryList(resultMap);
				
				for(int a=0; a < lottelist.size(); a++ ) {
					lotteParam = (HashMap<String, Object>) lottelist.get(a);

					headers = new HashMap<String, String>();
			        headers.put("Content-Type", "application/json;charset=utf-8");
					headers.put("authorization", "IgtAK "+lotteApiKey);
					
					orderInfo = new JSONObject();
					
					orderInfo.put("jobCustCd", lotteParam.get("CUST_CD").toString());
					orderInfo.put("wkCclSct", lotteParam.get("WK_SCT").toString());
					orderInfo.put("invNo", lotteParam.get("INV_NO").toString());
					orderInfo.put("ordNo", lotteParam.get("REF_ORD_NO").toString());
					orderInfo.put("rpnCustCd", "SKSTOA");
					orderInfo.put("cclRsnCd", "99");
					orderInfo.put("cclRsn", "출고전반품 취소");
					orderInfo.put("usrKey", "");
					
					orderList = new JSONArray();
					orderList.put(orderInfo);
					
					sndList = new JSONObject();
					sndList.put("sndList", orderList);
					
					responseText = HttpClientUtils.postResponseText(lotteDelivUrl, sndList, headers, "UTF-8");
					log.info("//---- responseText : " + responseText);
					
					resultMap.put("ORD_NO", lotteParam.get("ORD_NO").toString());
					executedRtn = paclaimDAO.updateLotteOrderInfoRequestYn(resultMap);
					if(executedRtn < 0){
						throw processException("msg.cannot_save", new String[]{"TSLIPLOTTEORDERINFO update"});
					}	
					
					/* API 전송 결과 저장 */
					if(responseText != null && !"".equals(responseText)){
						JSONObject res = new JSONObject(responseText);
						JSONArray responseData = res.getJSONArray("rtnList");
					
						
						for(int b=0; b < responseData.length(); b++) {
							JSONObject rtnObj = responseData.getJSONObject(b);
							ReqoutApiHistory reqOutApiHistory = new ReqoutApiHistory();
							
							reqOutApiHistory.setDelyType	("02");
							reqOutApiHistory.setOrdNo		(lotteParam.get("ORD_NO").toString());
							reqOutApiHistory.setErrorCode	(rtnObj.getString("rtnCd"));
							reqOutApiHistory.setErrorReason	(rtnObj.getString("rtnMsg"));
							reqOutApiHistory.setInsertId	(orderMap.get("MODIFY_ID").toString());
							
							//출고전반품 연동 HISTORY
							executedRtn = paclaimDAO.insertReqoutApiHistory(reqOutApiHistory);
							if(executedRtn < 0){
								throw processException("msg.cannot_save", new String[]{"TREQOUT_APIHISTORY insert"});
							}
						}
					}
				}	 
				
				//출고전반품정보 한진택배 조회 -20240709
				List<HashMap<String, Object>> hanjinlist = null;
				hanjinlist = paclaimDAO.selectEntpDirectHanjinDeliveryList(resultMap);
				
				requestHanjinOutCancel(hanjinlist, resultMap);
				
				executedRtn = paclaimDAO.insertReqOutHistory(resultMap);
				if (executedRtn < 0){
					throw processException("msg.cannot_save", new String[]{"TREQOUTHISTORY insert"});
				}
				
				//출고전반품 상담 생성
				Custcounselm custcounselm = new Custcounselm();
				Custcounseldt custcounseldt = new Custcounseldt();
				
				String proc_id = "999999";
				String counsel_seq = "";
				String cust_no = (String)orderMap.get("CUST_NO");
				
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("sequence_type", "COUNSEL_SEQ");
				
				counsel_seq = (String)systemDAO.selectSequenceNo(hashMap);
				
				custcounselm.setCounselSeq(counsel_seq);
				custcounselm.setCustNo(cust_no);
				custcounselm.setDoFlag("25");
				custcounselm.setRefNo1(resultMap.getString("ORDER_NO"));
				custcounselm.setRefNo2("");
				custcounselm.setRefNo3("");
				custcounselm.setRefNo4("");
				custcounselm.setOutLgroupCode("99");
				custcounselm.setOutMgroupCode("99");
				custcounselm.setCsLgroup("25");
				custcounselm.setCsMgroup("01");
				custcounselm.setCsSgroup("01");
				custcounselm.setCsLmsCode("250101");
				custcounselm.setGoodsCode(resultMap.getString("GOODS_CODE"));
				custcounselm.setGoodsdtCode(resultMap.getString("GOODSDT_CODE"));
				custcounselm.setClaimNo(counsel_seq);
				custcounselm.setTel(ComUtil.NVL(receiver.getReceiverHp(),receiver.getTel()));
				custcounselm.setDdd(ComUtil.NVL(receiver.getReceiverHp1(),receiver.getReceiverDdd()));
				custcounselm.setTel1(ComUtil.NVL(receiver.getReceiverHp2(),receiver.getReceiverTel1()));
				custcounselm.setTel2(ComUtil.NVL(receiver.getReceiverHp3(),receiver.getReceiverTel2()));
				custcounselm.setTel3("");
				custcounselm.setWildYn("0");
				
				String hcReqDate = paclaimDAO.selectHcReqDate();
				//custcounselm.setHcReqDate(DateUtil.toTime(hcReqDate));
				custcounselm.setHcReqDate(null);
				
				custcounselm.setRemark("");
				custcounselm.setRefId1("");
				custcounselm.setQuickYn("0");
				custcounselm.setCsSendYn("1");
				custcounselm.setQuickTransYn("1");
				custcounselm.setQuickEndYn("0");
				custcounselm.setTransFlag("0");
				custcounselm.setSendEntpCode(resultMap.getString("ENTP_CODE"));
				custcounselm.setInsertId(proc_id);
				custcounselm.setProcId(proc_id);
				
				String date = (String)orderMap.get("PROC_DATE");
				custcounselm.setProcDate(DateUtil.toTimestamp(date));
				custcounselm.setInsertDate(DateUtil.toTimestamp(date));
				
				executedRtn = paclaimDAO.insertCounselCustcounselm(custcounselm);
				if (executedRtn != 1){
					throw processException("msg.cannot_save", new String[]{"TCUSTCOUNSELM insert"});
				}
				
				custcounseldt.setCounselSeq(counsel_seq);
				custcounseldt.setCounselDtSeq("100");
				custcounseldt.setDoFlag("10");
				custcounseldt.setTitle("");
				custcounseldt.setDisplayYn("1");
				//custcounseldt.setHcReqDate(DateUtil.toTime(hcReqDate));
				custcounseldt.setHcReqDate(null);
				custcounseldt.setProcId(proc_id);
				custcounseldt.setProcDate(DateUtil.toTimestamp(date));
				custcounseldt.setInsertDate(DateUtil.toTimestamp(date));
				
				String cs_lms_name = null;
				if(! "".equals(orderMap.get("CS_LMS_CODE")) && ! (orderMap.get("CS_LMS_CODE") == null) ) {
					cs_lms_name = paclaimDAO.selectCsLmsName((String)orderMap.get("CS_LMS_CODE"));
					custcounseldt.setProcNote(cs_lms_name);
				} else {
					cs_lms_name = paclaimDAO.selectCsLmsName("250101");
				}
	
				executedRtn = paclaimDAO.insertCounselCustcounseldt(custcounseldt);				if (executedRtn != 1){
					throw processException("msg.cannot_save", new String[]{"TCUSTCOUNSELDT insert"});
				}
				
				custcounseldt.setCounselDtSeq("101");
				custcounseldt.setDoFlag("25");
				custcounseldt.setProcNote("출고 전 반품 접수 처리 건 확인 요망, D+2일 후 SR 자동 업체 완료 처리 됨" + "\n" + cs_lms_name);
				
				executedRtn = paclaimDAO.insertCounselCustcounseldt(custcounseldt);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELDT insert" });
				}
				
				executedRtn = paclaimDAO.updateCustsystemLastCon(cust_no);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TCUSTSYSTEM update last_con" });
				}
				
				if("61".equals(resultMap.get("SOURCING_MEDIA"))) {
					
					String sysDateTimeNow = systemDAO.getSysdatetime().replace("/", "").replace(":", "").replace(" ", "");
					
					String sysdateToString = systemDAO.getSysdate();
					String todayTimefrom = sysdateToString.replace("/", "").replace(":", "").replace(" ", "") + "210000";
					String todayTimeto = sysdateToString.replace("/", "").replace(":", "").replace(" ", "") + "240000";
					
					String nextdayTimefrom = sysdateToString.replace("/", "").replace(":", "").replace(" ", "") + "000000";
					String nextdayTimeto = sysdateToString.replace("/", "").replace(":", "").replace(" ", "") + "070000";
					
					Boolean timeCompare1 = DateUtil.between(sysDateTimeNow, todayTimefrom, todayTimeto);
					Boolean timeCompare2 = DateUtil.between(sysDateTimeNow, nextdayTimefrom, nextdayTimeto);
								
					String baseDateN = DateUtil.addDay(1, "yyyyMMdd")+"070000";
					String baseDateT = sysdateToString.replace("/", "").replace(":", "").replace(" ", "") +"070000";
					
					String entp_man_hp =  null;
					String entp_code = resultMap.getString("ENTP_CODE");
					String ship_man_seq = "";
					
					entp_man_hp = paclaimDAO.selectEntpManHp(resultMap);
				
					SmsSendVO smsSendVO = new SmsSendVO();
					
					if(timeCompare1){
						smsSendVO.setBaseDate(baseDateN);
					}else if(timeCompare2){
						smsSendVO.setBaseDate(baseDateT);
					}
					smsSendVO.setSendFlag("908");
					smsSendVO.setReceiveNo(entp_man_hp);
					smsSendVO.setSendNo(systemDAO.getVal("SMS_SEND_NO"));
					smsSendVO.setOrderNo(resultMap.getString("ORDER_NO"));
					smsSendVO.setRemark(resultMap.getString("GOODS_CODE"));
					smsSendVO.setCustNo(cust_no);
					smsSendVO.setInsertId(proc_id);
					
					try {
						umsBizProcess.sendSms(smsSendVO);
					} catch (Exception e) {
						log.error(Arrays.toString(e.getStackTrace()));
						throw processException("msg.no_data_processed" + e.getMessage());
					}
				}
					
				
				
			}
		}
		return executedRtn;
	}
	
	public String requestCjOutCancel(String gubun, List<HashMap<String, Object>> cjList, ParamMap resultMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		String responseText = null;
		
		Map<String, String> cjHeaders = null;
		JSONObject tokenDataInfo = new JSONObject();
		JSONObject tokenList = new JSONObject();
		JSONObject dataObject = null;
		JSONObject regDataInfo = null;
		JSONObject mpckObject = null;
		JSONArray arrMpckList = null;
		
		String cjToken = "";
		
		String tokenUrl = ConfigUtil.getString("CJ_LOGISTICS_URL") + "/ReqOneDayToken";
		String regUrl = ConfigUtil.getString("CJ_LOGISTICS_URL") + "/CnclBrkrBook";
		String cjCustId = ConfigUtil.getString("CJ_LOGISTICS_CUST_ID");
		String cjBizRegNum = ConfigUtil.getString("CJ_LOGISTICS_BIZ_REG_NUM");
		String cjUserId = ConfigUtil.getString("CJ_LOGISTICS_USER_ID");
		
		cjHeaders = new HashMap<String, String>();
		cjHeaders.put("Content-Type", "application/json;charset=utf-8");
		cjHeaders.put("Accept", "application/json");
		
		HashMap<String, Object> cjParam = null;
		
		for(int a=0; a < cjList.size(); a++ ) {
			
			cjParam = (HashMap<String, Object>) cjList.get(a);
			
			resultMap.put("CUST_USE_NO", cjParam.get("CUST_USE_NO").toString());
			
			if(cjParam.get("DELY_PROC_GB").toString().equals("03")) {
				//직송
				tokenList.put("CUST_ID", 	 cjParam.get("CUST_ID").toString());	//고객ID
				tokenList.put("BIZ_REG_NUM", cjParam.get("S_IDNO").toString());		//사업자번호
				tokenList.put("USER_ID", 	 cjUserId);								//중개업체ID
				tokenList.put("ENTP_CODE", 	 cjParam.get("ENTP_CODE").toString());	//업체코드
			}else {
				//직택배
				tokenList.put("CUST_ID", 	 cjCustId);			//고객ID
				tokenList.put("BIZ_REG_NUM", cjBizRegNum);		//사업자번호
				tokenList.put("USER_ID", 	 cjUserId);			//중개업체ID
				tokenList.put("ENTP_CODE", 	 "100001");			//업체코드
			}
			
			tokenDataInfo.put("DATA", tokenList);
			
			CjOneDayToken onedayToken = new CjOneDayToken();
			onedayToken.setEntpCode(tokenList.get("ENTP_CODE").toString());
			onedayToken.setCjSIdno(tokenList.get("BIZ_REG_NUM").toString());
			onedayToken.setCustId(tokenList.get("CUST_ID").toString());
			cjToken = paclaimDAO.getCjOnedayTokenNo(onedayToken);

			if(cjToken == null || "".equals(cjToken)) {
				//원데이 토큰 발행
				responseText = HttpClientUtils.postResponseText(tokenUrl, tokenDataInfo, cjHeaders, "UTF-8");
				
				if(responseText != null && !"".equals(responseText)){
					CjOneDayToken regCjOneDayToken = new CjOneDayToken();
					JSONObject tokenRes = new JSONObject(responseText);
					
					regCjOneDayToken.setResultCd(tokenRes.optString("RESULT_CD", ""));							//결과코드 성공 : S, 실패 : E
					regCjOneDayToken.setResultDetail(tokenRes.optString("RESULT_DETAIL", ""));					//결과상세 실패시 에러 내용
					regCjOneDayToken.setEntpCode(tokenList.get("ENTP_CODE").toString());
					regCjOneDayToken.setCjSIdno(tokenList.get("BIZ_REG_NUM").toString());
					regCjOneDayToken.setCustId(tokenList.get("CUST_ID").toString());
					
					if ("S".equals(tokenRes.optString("RESULT_CD", ""))){
						JSONObject tokenDataRes = new JSONObject(tokenRes.optString("DATA", ""));
						
						regCjOneDayToken.setTokenNum(tokenDataRes.optString("TOKEN_NUM", ""));					//토큰번호
						regCjOneDayToken.setTokenExprtnDtm(tokenDataRes.optString("TOKEN_EXPRTN_DTM", ""));		//토큰만료시간
						regCjOneDayToken.setNotice(tokenDataRes.optString("NOTICE", ""));						//공지사항
						
						//토큰번호가 없거나, 공지사항이 달라진 경우에만 데이터 저장
						int result = 0;
						result = paclaimDAO.selectCjToken(regCjOneDayToken);
						
						if(result == 0){
							String tokenSeq = paclaimDAO.getTokenSequenceNo();
							regCjOneDayToken.setTokenSeq(tokenSeq);
							
							executedRtn = paclaimDAO.insertCjOneDayToken(regCjOneDayToken);
							if(executedRtn < 0){
								throw processException("msg.cannot_save", new String[]{"TCJONEDAYTOKEN insert"});
							}
						}

						// API 수신 토큰 값
						cjToken = tokenDataRes.optString("TOKEN_NUM", "");
						
					}else {
						String tokenSeq = paclaimDAO.getTokenSequenceNo();
						regCjOneDayToken.setTokenSeq(tokenSeq);
						
						executedRtn = paclaimDAO.insertCjOneDayToken(regCjOneDayToken);
						if(executedRtn < 0){
							throw processException("msg.cannot_save", new String[]{"TCJONEDAYTOKEN insert"});
						}
					}
				}
			}
			
			if(cjToken != null && !"".equals(cjToken)){
				dataObject = new JSONObject();
				regDataInfo = new JSONObject();
				
				cjHeaders.put("CJ-Gateway-APIKey", 	cjToken);
				
				dataObject.put("TOKEN_NUM", 		cjToken);			//토큰번호
				dataObject.put("USER_ID", 			cjUserId);											//중개업체 ID
				dataObject.put("CUST_ID", 			cjParam.get("CUST_ID").toString());					//고객 ID
				dataObject.put("RCPT_YMD", 			cjParam.get("RCPT_YMD").toString());				//접수일자 
				dataObject.put("CUST_USE_NO", 		cjParam.get("REF_ORD_NO").toString());				//고객사용번호
				dataObject.put("RCPT_DV", 			cjParam.get("RCPT_DV").toString());					//접수구분 		01: 일반, 02: 반품
				dataObject.put("WORK_DV_CD", 		cjParam.get("WORK_DV_CD").toString());				//작업구분코드 		01: 일반, 02: 교환, 03: A/S
				dataObject.put("REQ_DV_CD", 		cjParam.get("REQ_DV_CD").toString());				//요청구분코드		01: 요청, 02: 취소
				dataObject.put("MPCK_KEY", 			cjParam.get("MPCK_KEY").toString());				//합포장 키
				dataObject.put("CAL_DV_CD", 		cjParam.get("CAL_DV_CD").toString());				//정산구분코드		01: 계약 운임 
				dataObject.put("FRT_DV_CD", 		cjParam.get("FRT_DV_CD").toString());				//운임구분코드		01: 선불, 02: 착불, 03: 신용
				dataObject.put("CNTR_ITEM_CD", 		cjParam.get("CNTR_ITEM_CD").toString());			//계약품목코드 		01: 일반 품목
				dataObject.put("BOX_TYPE_CD", 		cjParam.get("BOX_TYPE_CD").toString());				//박스타입코드 
				dataObject.put("BOX_QTY", 			cjParam.get("BOX_QTY").toString());					//박스 수량
				dataObject.put("FRT", 				cjParam.get("FRT").toString());						//운임
				dataObject.put("CUST_MGMT_DLCM_CD", cjParam.get("CUST_MGMT_DLCM_CD").toString());		//고객관리거래처코드
				dataObject.put("SENDR_NM", 			cjParam.get("SENDR_NM").toString());				//보내는분 명 
				dataObject.put("SENDR_TEL_NO1", 	cjParam.get("SENDR_TEL_NO1").toString());			//보내는분 전화번호1
				dataObject.put("SENDR_TEL_NO2", 	cjParam.get("SENDR_TEL_NO2").toString());			//보내는분 전화번호2
				dataObject.put("SENDR_TEL_NO3", 	cjParam.get("SENDR_TEL_NO3").toString());			//보내는분 전화번호3
				dataObject.put("SENDR_CELL_NO1", 	cjParam.get("SENDR_CELL_NO1").toString());			//보내는분 휴대폰번호1 
				dataObject.put("SENDR_CELL_NO2", 	cjParam.get("SENDR_CELL_NO2").toString());			//보내는분 휴대폰번호2
				dataObject.put("SENDR_CELL_NO3", 	cjParam.get("SENDR_CELL_NO3").toString());			//보내는분 휴대폰번호3
				dataObject.put("SENDR_SAFE_NO1", 	cjParam.get("SENDR_SAFE_NO1").toString());			//보내는분 안심번호1
				dataObject.put("SENDR_SAFE_NO2", 	cjParam.get("SENDR_SAFE_NO2").toString());			//보내는분 안심번호2
				dataObject.put("SENDR_SAFE_NO3", 	cjParam.get("SENDR_SAFE_NO3").toString());			//보내는분 안심번호3
				dataObject.put("SENDR_ZIP_NO", 		cjParam.get("SENDR_ZIP_NO").toString());			//보내는분 우편번호
				dataObject.put("SENDR_ADDR", 		cjParam.get("SENDR_ADDR").toString());				//보내는분 주소
				dataObject.put("SENDR_DETAIL_ADDR", cjParam.get("SENDR_DETAIL_ADDR").toString());		//보내는분 상세주소 
				dataObject.put("RCVR_NM", 			cjParam.get("RCVR_NM").toString());					//받는분 명
				dataObject.put("RCVR_TEL_NO1", 		cjParam.get("RCVR_TEL_NO1").toString());			//받는분 전화번호1
				dataObject.put("RCVR_TEL_NO2", 		cjParam.get("RCVR_TEL_NO2").toString());			//받는분 전화번호2
				dataObject.put("RCVR_TEL_NO3", 		cjParam.get("RCVR_TEL_NO3").toString());			//받는분 전화번호3
				dataObject.put("RCVR_CELL_NO1", 	cjParam.get("RCVR_CELL_NO1").toString());			//받는분 휴대폰번호1 
				dataObject.put("RCVR_CELL_NO2", 	cjParam.get("RCVR_CELL_NO1").toString());			//받는분 휴대폰번호2
				dataObject.put("RCVR_CELL_NO3", 	cjParam.get("RCVR_CELL_NO3").toString());			//받는분 휴대폰번호3
				dataObject.put("RCVR_SAFE_NO1", 	cjParam.get("RCVR_SAFE_NO1").toString());			//받는분 안심번호1 
				dataObject.put("RCVR_SAFE_NO2", 	cjParam.get("RCVR_SAFE_NO2").toString());			//받는분 안심번호2
				dataObject.put("RCVR_SAFE_NO3", 	cjParam.get("RCVR_SAFE_NO3").toString());			//받는분 안심번호3
				dataObject.put("RCVR_ZIP_NO", 		cjParam.get("RCVR_ZIP_NO").toString());				//받는분 우편번호
				dataObject.put("RCVR_ADDR", 		cjParam.get("RCVR_ADDR").toString());				//받는분 주소
				dataObject.put("RCVR_DETAIL_ADDR", 	cjParam.get("RCVR_DETAIL_ADDR").toString());		//받는분 상세주소
				dataObject.put("ORDRR_NM", 			cjParam.get("ORDRR_NM").toString());				//주문자 명 
				dataObject.put("ORDRR_TEL_NO1", 	cjParam.get("ORDRR_TEL_NO1").toString());			//주문자전화번호1
				dataObject.put("ORDRR_TEL_NO2", 	cjParam.get("ORDRR_TEL_NO2").toString());			//주문자전화번호2
				dataObject.put("ORDRR_TEL_NO3", 	cjParam.get("ORDRR_TEL_NO3").toString());			//주문자전화번호3
				dataObject.put("ORDRR_CELL_NO1", 	cjParam.get("ORDRR_CELL_NO1").toString());			//주문자휴대폰번호1
				dataObject.put("ORDRR_CELL_NO2", 	cjParam.get("ORDRR_CELL_NO2").toString());			//주문자휴대폰번호2
				dataObject.put("ORDRR_CELL_NO3", 	cjParam.get("ORDRR_CELL_NO3").toString());			//주문자휴대폰번호3
				dataObject.put("ORDRR_SAFE_NO1", 	cjParam.get("ORDRR_SAFE_NO1").toString());			//주문자안심번호1 
				dataObject.put("ORDRR_SAFE_NO2", 	cjParam.get("ORDRR_SAFE_NO2").toString());			//주문자안심번호2
				dataObject.put("ORDRR_SAFE_NO3", 	cjParam.get("ORDRR_SAFE_NO3").toString());			//주문자안심번호3
				dataObject.put("ORDRR_ZIP_NO", 		cjParam.get("ORDRR_ZIP_NO").toString());			//주문자 우편번호
				dataObject.put("ORDRR_ADDR", 		cjParam.get("ORDRR_ADDR").toString());				//주문자 주소
				dataObject.put("ORDRR_DETAIL_ADDR", cjParam.get("ORDRR_DETAIL_ADDR").toString());		//주문자 상세주소
				dataObject.put("INVC_NO", 			cjParam.get("INVC_NO").toString());					//운송장 번호
				dataObject.put("ORI_INVC_NO", 		cjParam.get("ORI_INVC_NO").toString());				//원운송장번호
				dataObject.put("ORI_ORD_NO", 		cjParam.get("ORI_ORD_NO").toString());				//원주문 번호
				dataObject.put("COLCT_EXPCT_YMD", 	cjParam.get("COLCT_EXPCT_YMD").toString());			//집화 예정일자 
				dataObject.put("COLCT_EXPCT_HOUR", 	cjParam.get("COLCT_EXPCT_HOUR").toString());		//집화 예정시간
				dataObject.put("SHIP_EXPCT_YMD", 	cjParam.get("SHIP_EXPCT_YMD").toString());			//배송 예정일자
				dataObject.put("SHIP_EXPCT_HOUR", 	cjParam.get("SHIP_EXPCT_HOUR").toString());			//배송 예정시간
				dataObject.put("PRT_ST", 			cjParam.get("PRT_ST").toString());					//운송장 출력상태
				dataObject.put("ARTICLE_AMT", 		cjParam.get("ARTICLE_AMT").toString());				//물품가 액 
				dataObject.put("REMARK_1", 			cjParam.get("REMARK_1").toString());				//비고1 
				dataObject.put("REMARK_2", 			cjParam.get("REMARK_2").toString());				//비고2
				dataObject.put("REMARK_3", 			cjParam.get("REMARK_3").toString());				//비고3
				dataObject.put("COD_YN", 			cjParam.get("COD_YN").toString());					//COD여부
				dataObject.put("ETC_1", 			cjParam.get("ETC_1").toString());					//기타1 
				dataObject.put("ETC_2", 			cjParam.get("ETC_2").toString());					//기타2
				dataObject.put("ETC_3", 			cjParam.get("ETC_3").toString());					//기타3
				dataObject.put("ETC_4", 			cjParam.get("ETC_4").toString());					//기타4
				dataObject.put("ETC_5", 			cjParam.get("ETC_5").toString());					//기타5
				dataObject.put("DLV_DV", 			cjParam.get("DLV_DV").toString());					//택배구분 		‘01’: 택배
				//dataObject.put("RCPT_SERIAL", cjParam.getserial());									//접수시리얼 번호 
				
				mpckObject = new JSONObject();
				arrMpckList = new JSONArray();
				
				mpckObject.put("MPCK_SEQ", 			cjParam.get("MPCK_SEQ").toString());				//합포장 순번
				mpckObject.put("GDS_CD", 			cjParam.get("GDS_CD").toString());					//상품코드
				mpckObject.put("GDS_NM", 			cjParam.get("GDS_NM").toString());					//상품명 
				mpckObject.put("GDS_QTY", 			cjParam.get("GDS_QTY").toString());					//상품수량
				mpckObject.put("UNIT_CD", 			cjParam.get("UNIT_CD").toString());					//단품코드 
				mpckObject.put("UNIT_NM", 			cjParam.get("UNIT_NM").toString());					//단품명
				mpckObject.put("GDS_AMT", 			cjParam.get("GDS_AMT").toString());					//상품가액
				
				arrMpckList.put(mpckObject);
				
				dataObject.put("ARRAY", arrMpckList);
				
				regDataInfo.put("DATA", dataObject);
				
				responseText = HttpClientUtils.postResponseText(regUrl, regDataInfo, cjHeaders, "UTF-8");
				
				if(responseText != null && !"".equals(responseText)){
					JSONObject jsonRegDataObject = new JSONObject(responseText);
					
					if ("S".equals(jsonRegDataObject.optString("RESULT_CD", ""))){
						if("out".equals(gubun)) {
							executedRtn = paclaimDAO.insertEntpDirectReturnBeforeOut(resultMap);
							if (executedRtn < 1){
								throw processException("msg.cannot_save", new String[]{"TSLIPCJORDERINFO insert"});
							}
						}else {
							executedRtn = paclaimDAO.insertEntpDirectReturnBeforeReturn(resultMap);
							if (executedRtn < 1){
								throw processException("msg.cannot_save", new String[]{"TSLIPCJORDERINFO insert"});
							}
						}
					}else {
						CjSlipError regCjSlipError = new CjSlipError();
						
						regCjSlipError.setApiGubun		("CnclBrkrBook");
						regCjSlipError.setApiParam		(cjParam.get("CUST_USE_NO").toString());
						regCjSlipError.setErrorCode		(jsonRegDataObject.get("RESULT_CD").toString());
						regCjSlipError.setErrorReason	(jsonRegDataObject.get("RESULT_DETAIL").toString());
						regCjSlipError.setInsertId		(resultMap.get("MODIFY_ID").toString());
						
						executedRtn = paclaimDAO.insertCjSlipError(regCjSlipError);
						if(executedRtn < 0){
							throw processException("msg.cannot_save", new String[]{"TCJSLIP_ERROR insert"});
						}
					}
					
					ReqoutApiHistory reqOutApiHistory = new ReqoutApiHistory();
					
					reqOutApiHistory.setDelyType	("01");
					reqOutApiHistory.setOrdNo		(cjParam.get("CUST_USE_NO").toString());
					reqOutApiHistory.setErrorCode	(jsonRegDataObject.get("RESULT_CD").toString());
					reqOutApiHistory.setErrorReason	(jsonRegDataObject.get("RESULT_DETAIL").toString());
					reqOutApiHistory.setInsertId	(resultMap.get("MODIFY_ID").toString());
					
					//출고전반품 연동 HISTORY
					executedRtn = paclaimDAO.insertReqoutApiHistory(reqOutApiHistory);
					if(executedRtn < 0){
						throw processException("msg.cannot_save", new String[]{"TREQOUT_APIHISTORY insert"});
					}
				}
			}

			executedRtn = paclaimDAO.updateCjOrderInfoRequestYn(resultMap);
			if(executedRtn < 0){
				throw processException("msg.cannot_save", new String[]{"TSLIPCJORDERINFO  update"});
			}
		}
		
		return rtnMsg;
	}
	
	public String requestHanjinOutCancel(List<HashMap<String, Object>> hanjinList, ParamMap resultMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		String hanjinDelivUrl = ConfigUtil.getString("HANJIN_DELIVERY_URL");
		String hanjinApiKey = ConfigUtil.getString("HANJIN_API_KEY");
		String hanjinSecretKey = ConfigUtil.getString("HANJIN_SECRET_KEY");
		String regUrl = hanjinDelivUrl+"/order/cancel-order";
		String clientId = "SKSTOA";
		String method = "POST";

		Map<String, String> headers = null;
		HashMap<String, Object> hanjinParam = null;
		JSONObject dataObject = null;
		String responseText = null;
		
		//header HMAC signature 생성
		String authorization = hmacGenerate(method, clientId, hanjinSecretKey);
		
		// header setting
		headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json;charset=utf-8");
		headers.put("Accept", "application/json");
		headers.put("x-api-key", hanjinApiKey);
		headers.put("authorization", authorization);

		for(int a=0; a < hanjinList.size(); a++ ) {
			hanjinParam = (HashMap<String, Object>) hanjinList.get(a);

			dataObject = new JSONObject();
			dataObject.put("custEdiCd", 	hanjinParam.get("CUST_EDI_CD").toString()); 
			dataObject.put("custOrdNo", 	hanjinParam.get("CUST_ORD_NO").toString()); 
			
			responseText = HttpClientUtils.postResponseText(regUrl, dataObject, headers, "UTF-8");
			log.info("//---- responseText : " + responseText);
			
			if(responseText != null && !"".equals(responseText)){
				JSONObject responseData = new JSONObject(responseText);
				
				//OK 또는 ERROR-03(미접수된 주문번호(예약)) 일때 처리
				if ("OK".equals(responseData.getString("resultCode")) ||  "ERROR-03".equals(responseData.getString("resultCode"))){
					resultMap.put("CUST_ORD_NO", hanjinParam.get("CUST_ORD_NO").toString());
					executedRtn = paclaimDAO.updateHanjinOrderInfoRequestYn(resultMap);
					if(executedRtn < 0){
						throw processException("msg.cannot_save", new String[]{"TSLIPHANJINORDERINFO update"});
					}	
				}
				
				/* API 전송 결과 저장 */
				ReqoutApiHistory reqOutApiHistory = new ReqoutApiHistory();
				
				reqOutApiHistory.setDelyType	("03");
				reqOutApiHistory.setOrdNo		(hanjinParam.get("CUST_ORD_NO").toString());
				reqOutApiHistory.setErrorCode	(responseData.getString("resultCode"));
				reqOutApiHistory.setErrorReason	(responseData.getString("resultMessage"));
				reqOutApiHistory.setInsertId	(resultMap.get("MODIFY_ID").toString());
				
				executedRtn = paclaimDAO.insertReqoutApiHistory(reqOutApiHistory);
				if(executedRtn < 0){
					throw processException("msg.cannot_save", new String[]{"TREQOUT_APIHISTORY insert"});
				}
				
			}
		}
		
		
		return rtnMsg;
	}
	
	public String hmacGenerate(String method, String clientId, String secretKey) {
		String authorization = "";
		String timestamp = DateUtil.getLocalDateTime();
		
		try {
			// 메세지 조합
			String message = timestamp + method + secretKey;
			log.info("message: " + message);
			// HMAC 알고리즘
			String algorithm = "HmacSHA256";
			// secretKey 바이트 배열로 변환
			byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
			String signature = "";
			
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, algorithm);
			// MAC 객체 생성
			Mac mac = Mac.getInstance(algorithm);
			mac.init(secretKeySpec);
			// 메시지를 바이트 배열로 변환하여 HMAC 생성
			byte[] hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
			// HMAC을 HEX 문자열로 변환
			signature = bytesToHex(hmacBytes);
			authorization = String.format("client_id=%s timestamp=%s signature=%s", new Object[] { clientId, timestamp, signature });
			

		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
		}
		

		return authorization;		
		
	}
    
	private String bytesToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xFF & b);
			if (hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
	
	public Long getOrderWseq4ShippingCost(PaClaimdtVO[] claimdt) throws Exception {
		
		
		PaClaimdtVO regClaimdt = null;
		String modiString = "";
		long order_w_seq1 = 0;
		long order_w_seq45 = 0;
		long order_w_seq_cancel = 0;
			
		
	
		//long order_w_seq_org = 0;
		
		for (int i=0;i<claimdt.length;i++){

			regClaimdt = claimdt[i];
			
			
			
			if (!regClaimdt.getCwareAction().equals("I"))
				continue;
			
			//if ("0".equals(regClaimdt.getShpfeeYn()))  //0 : 무상 , 1: 유상, 2: 협의  (ex, 0은 판매자 사유 교환- 상품파괴 등등)
			//	continue;
			
				//반품 또는 교환배송
			if (regClaimdt.getClaimGb().equals("40")) {
									
								
					//W-Seq 따오는 부분...					
				modiString = " ORDER_NO    = " + regClaimdt.getOrderNo()
						+ "   AND ORDER_G_SEQ = "+ regClaimdt.getOrderGSeq()
						+ "   AND ORDER_D_SEQ = "+ regClaimdt.getOrderDSeq();
					
					
				order_w_seq_cancel = Long.parseLong(systemService.getMaxNo("TCANCELDT", "ORDER_W_SEQ", modiString, 3));
				order_w_seq1 = Long.parseLong(systemService.getMaxNo("TCLAIMDT", "ORDER_W_SEQ", modiString, 3));
				order_w_seq45 = order_w_seq1;
					
				if (order_w_seq_cancel == 1) {
						// = 취소수량이 없는 경우
					if (order_w_seq1 == 1) {
							// = 반품/교환을 처음하는 경우
						order_w_seq1++;
						order_w_seq45++;
					}
				} else {
						// = 취소수량이 있는 경우
					if (order_w_seq1 == 1) {
						// = 반품/교환을 처음하는 경우
						order_w_seq1 = order_w_seq1 + (order_w_seq_cancel- 1); 
						order_w_seq45 = order_w_seq45 + (order_w_seq_cancel- 1);
					}
				}
			}
			else if(regClaimdt.getClaimGb().equals("41")){
				modiString = " ORDER_NO    = " + regClaimdt.getOrderNo()
						+ "   AND ORDER_G_SEQ = " + regClaimdt.getOrderGSeq()
						+ "   AND ORDER_D_SEQ = " + regClaimdt.getOrderDSeq();
				order_w_seq_cancel = Long.parseLong(systemService.getMaxNo("TCANCELDT", "ORDER_W_SEQ", modiString, 3));
				order_w_seq1 = Long.parseLong(systemService.getMaxNo("TCLAIMDT", "ORDER_W_SEQ", modiString, 3));
				if (order_w_seq_cancel == 1) {
					// = 취소수량이 없는 경우
					if (order_w_seq1 == 1) {
						// = 반품/교환을 처음하는 경우
						order_w_seq1++;
					}
				} else {
					// = 취소수량이 있는 경우
					if (order_w_seq1 == 1) {
						// = 반품/교환을 처음하는 경우
						order_w_seq1 = order_w_seq1 + order_w_seq_cancel - 1;
					}
				}					
			}
					
		}
		
		
		return order_w_seq1;
	}
	
	
	public ParamMap getOrderWseqNothers4ShippingCost(PaClaimdtVO[] claimdt) throws Exception {
		
		
		PaClaimdtVO regClaimdt = null;
		String modiString = "";
		long order_w_seq1 = 0;
		long order_w_seq45 = 0;
		long order_w_seq_cancel = 0;
		long claim_qty = 0;
		
		String arg_goods_str    = "";
		String arg_gseq_str      = "";
		String arg_dseq_str     = "";
		String arg_wseq_str     = "";		
		String arg_qty_str      = "";
		String arg_amt_str      = "";
		String arg_cust_no		= "";
		String arg_receiver_seq = "";
		String arg_calc_qty		= "";
		String arg_rcv_str      = "";
		
		
		
	
		//long order_w_seq_org = 0;
		
		for (int i=0;i<claimdt.length;i++){

			regClaimdt = claimdt[i];
			
			
			
			if (!regClaimdt.getCwareAction().equals("I"))
				continue;
			
			if ("0".equals(regClaimdt.getShpfeeYn()))  //0 : 무상 , 1: 유상, 2: 협의  (ex, 0은 판매자 사유 교환- 상품파괴 등등)
				continue;
			
			
			claim_qty = regClaimdt.getClaimQty();
			
			

			if((regClaimdt.getClaimGb().equals("30")||regClaimdt.getClaimGb().equals("40") ||regClaimdt.getClaimGb().equals("45")) && claim_qty >0   ){
				
						
				//반품 또는 교환배송
				if (regClaimdt.getClaimGb().equals("30") || regClaimdt.getClaimGb().equals("40")) {
									
								
					//W-Seq 따오는 부분...					
					modiString = " ORDER_NO    = " + regClaimdt.getOrderNo()
							+ "   AND ORDER_G_SEQ = "+ regClaimdt.getOrderGSeq()
							+ "   AND ORDER_D_SEQ = "+ regClaimdt.getOrderDSeq();
					
					
					order_w_seq_cancel = Long.parseLong(systemService.getMaxNo("TCANCELDT", "ORDER_W_SEQ", modiString, 3));
					order_w_seq1 = Long.parseLong(systemService.getMaxNo("TCLAIMDT", "ORDER_W_SEQ", modiString, 3));
					order_w_seq45 = order_w_seq1;
					
					if (order_w_seq_cancel == 1) {
						// = 취소수량이 없는 경우
						if (order_w_seq1 == 1) {
							// = 반품/교환을 처음하는 경우
							order_w_seq1++;
							order_w_seq45++;
						}
					} else {
						// = 취소수량이 있는 경우
						if (order_w_seq1 == 1) {
							// = 반품/교환을 처음하는 경우
							order_w_seq1 = order_w_seq1 + (order_w_seq_cancel- 1); 
							order_w_seq45 = order_w_seq45 + (order_w_seq_cancel- 1);
						}
					}
				}else{
					order_w_seq1 = order_w_seq45 + 1;  //regClaimdt.getClaimGb().equals("45")) && claim_qty > 0
				}
				
	
				arg_calc_qty = Long.toString(regClaimdt.getClaimQty());										
				arg_cust_no = regClaimdt.getCustNo();
				arg_receiver_seq = regClaimdt.getReceiverSeq();
				arg_goods_str = " " + regClaimdt.getGoodsCode();				
				arg_gseq_str  = " " + regClaimdt.getOrderGSeq();
				arg_dseq_str = " "  + regClaimdt.getOrderDSeq();
				arg_wseq_str = " "  +  order_w_seq1;
				arg_rcv_str   = " " + "1";
				arg_amt_str   = " " + Double.toString((regClaimdt.getRsaleAmt() / regClaimdt.getOrderQty()) * Double.parseDouble(arg_calc_qty));			
				arg_qty_str   = " " + arg_calc_qty;	
					
			}
			else if(regClaimdt.getClaimGb().equals("31") ||regClaimdt.getClaimGb().equals("41") || regClaimdt.getClaimGb().equals("46")){
				modiString = " ORDER_NO    = " + regClaimdt.getOrderNo()
						+ "   AND ORDER_G_SEQ = "
						+ regClaimdt.getOrderGSeq()
						+ "   AND ORDER_D_SEQ = "
						+ regClaimdt.getOrderDSeq();
				order_w_seq_cancel = Long.parseLong(systemService.getMaxNo(
						"TCANCELDT", "ORDER_W_SEQ", modiString, 3));
				order_w_seq1 = Long.parseLong(systemService.getMaxNo("TCLAIMDT", "ORDER_W_SEQ", modiString, 3));
				if (order_w_seq_cancel == 1) {
					// = 취소수량이 없는 경우
					if (order_w_seq1 == 1) {
						// = 반품/교환을 처음하는 경우
						order_w_seq1++;
					}
				} else {
					// = 취소수량이 있는 경우
					if (order_w_seq1 == 1) {
						// = 반품/교환을 처음하는 경우
						order_w_seq1 = order_w_seq1 + order_w_seq_cancel - 1;
					}
				}
				
				if(regClaimdt.getClaimGb().equals("46")){
					order_w_seq1++;
				}
				
				
				arg_calc_qty = Long.toString(regClaimdt.getClaimQty());										
				arg_cust_no = regClaimdt.getCustNo();
				arg_receiver_seq = regClaimdt.getReceiverSeq();
				arg_goods_str = " " + regClaimdt.getGoodsCode();				
				arg_gseq_str  = " " + regClaimdt.getOrderGSeq();
				arg_dseq_str = " "  + regClaimdt.getOrderDSeq();
				arg_wseq_str = " "  +  order_w_seq1;
				arg_rcv_str   = " " + "1";
				arg_amt_str   = " " + Double.toString((regClaimdt.getRsaleAmt() / regClaimdt.getOrderQty()) * Double.parseDouble(arg_calc_qty));			
				arg_qty_str   = " " + arg_calc_qty;	
					
			}
			
			
			
		}
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("arg_calc_qty",     arg_calc_qty);
		paramMap.put("arg_cust_no" ,     arg_cust_no);
		paramMap.put("arg_receiver_seq", arg_receiver_seq);
		paramMap.put("arg_goods_str",    arg_goods_str);
		paramMap.put("arg_gseq_str",     arg_gseq_str);
		paramMap.put("arg_dseq_str",     arg_dseq_str);
		paramMap.put("arg_wseq_str",     arg_wseq_str);
		paramMap.put("arg_rcv_str",      arg_rcv_str);
		paramMap.put("arg_amt_str",      arg_amt_str);
		paramMap.put("arg_qty_str",      arg_qty_str);
		
		
	
		return paramMap;
	}
	
	
	private int insertOrdershipcost(Ordershipcost[] ordershipcost) throws Exception {
		int executedRtn = 0;
		Ordershipcost regOrdershipcost = null;
		
		for(int i = 0; ordershipcost.length > i; i++){
			log.info("//---- insert TORDERSHIPCOST");
			regOrdershipcost = ordershipcost[i];
			
			executedRtn = paorderDAO.insertOrdershipcost(regOrdershipcost);
			
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERSHIPCOST INSERT"});
			}
		}
		return executedRtn;
	}
	
	
	
	
	private int insertPaOrderShipCost(Ordershipcost[] ordershipcostArr, ParamMap param, PaClaimdtVO[] claimdt ) throws Exception{
		
		String out_receiver_seq = "";
		String return_receiver_seq = "";
		
		
		
		if(ordershipcostArr ==null ||  ordershipcostArr.length < 1 )	return 1;
		
		if(claimdt.length < 1) return 1;
		
		for( PaClaimdtVO pc : claimdt){
			if("999999".equals(pc.getReceiverPost())) return 1;
		}
		
	
			
		
		long shipfeeCost = 0;
		long shipMenualCancelAmt = 0;
 		

		String order_no  = param.getString("orderNo"); 
		String cust_no   = param.getString("custNo");
		Timestamp claim_date = claimdt[0].getClaimDate();
		String proc_id   = param.getString("userId");
		String proc_date = param.getString("proc_date");
		String receiptNo = param.getString("ReceiptNo");
		String type      = param.getString("orderGB");
		
		long seq = 0;
		long paSheFeeAmt = param.getLong("paSheFeeAmt");
		int shpFeeYn  =   param.getInt("shpFeeYn");  //0무상, 1유상 , 2 협의
		int executedRtn = 0;		
			
			
		for( PaClaimdtVO pc : claimdt){
		
			if("40".equals(pc.getClaimGb()) || "41".equals(pc.getClaimGb())){
				out_receiver_seq = pc.getReceiverSeq();
			}
			
			else if("45".equals(pc.getClaimGb()) || "46".equals(pc.getClaimGb())){
				return_receiver_seq = pc.getReceiverSeq();
			}
			
			else{  //반품
				out_receiver_seq = pc.getReceiverSeq();
				return_receiver_seq = "";
			}
		}
		
				
		
		for (Ordershipcost ordershipcost : ordershipcostArr){
			
			
			shipfeeCost += ordershipcost.getShpfeeCost();		
			shipMenualCancelAmt += ordershipcost.getManualCancelAmt();			
		}
		
		
		
		if(shipfeeCost + paSheFeeAmt + shipMenualCancelAmt > 0){
			
			
			seq  = Long.parseLong(systemService.getMaxNo("TPAORDERSHIPCOST","SEQ", "ORDER_NO = '" + order_no + "'" , 3));
			PaOrderShipCost paordershipcost = new PaOrderShipCost();

			paordershipcost.setOrderNo(order_no);
			paordershipcost.setCustNo(cust_no);
			paordershipcost.setSeq((ComUtil.lpad(Long.toString(seq), 3, "0")));
			paordershipcost.setOrderDate(claim_date);
			//paordershipcost.setOrderDate(DateUtil.toTimestamp(proc_date));
			paordershipcost.setReceiptNo(receiptNo);
			paordershipcost.setType(type);
			paordershipcost.setPaShpFeeAmt(paSheFeeAmt);
			paordershipcost.setShpFeeYn(shpFeeYn);
			
			paordershipcost.setOutReceiverSeq(out_receiver_seq);
			paordershipcost.setReturnReceiverSeq(return_receiver_seq);
			
			if(shpFeeYn == 0){  //0 무상, 1 유상 ,2 협의 - 협의는 일단 생각하지 않는걸로...
				paordershipcost.setShpFeeAmt(0);
				paordershipcost.setManualCancelAmt(shipMenualCancelAmt);
			}else{
				paordershipcost.setShpFeeAmt(shipfeeCost);
				paordershipcost.setManualCancelAmt(0);
			}

			paordershipcost.setInsertId(proc_id);
			paordershipcost.setInsertDate(DateUtil.toTimestamp(proc_date));
		
			
			executedRtn = insertPaOrderShipCost(paordershipcost);
		}else{
			executedRtn = 1;
		}
		
		
		
		return executedRtn;
	}

	
	private int insertOrderreceipts(Ordershipcost[] ordershipcostArr, ParamMap param, ArrayList<Orderreceipts> orderreceiptsList ) throws Exception{
		
		int executedRtn = 1;
		String proc_date = null;
	    String order_no  = null;
	    String cust_no   = null;
		String proc_id        = null;
		String cust_name = null;
	    long ship15feeCost = 0; //교환     배송비  
		long ship65feeCost = 0; //교환취소 배송비  
		

		order_no  = param.getString("orderNo"); 
		cust_no   = param.getString("custNo");
		proc_id   = param.getString("userId");
		cust_name = param.getString("custName");
		proc_date = param.getString("proc_date");
		
		
		for (Ordershipcost ordershipcost : ordershipcostArr){
				
			if (("40").equals(ordershipcost.getType())){
				ship15feeCost += ordershipcost.getShpfeeCost();		
			}else{
				ship65feeCost += ordershipcost.getShpfeeCost();		
			}			
		}
		
		
		if(ship15feeCost > 0 || ship65feeCost > 0){
			
			OrderreceiptsVO regOrderreceipts = new OrderreceiptsVO();
			
			regOrderreceipts.setReceiptNo(getSequenceNo("RECEIPT_NO"));
			regOrderreceipts.setOrderNo(order_no);
			regOrderreceipts.setCustNo(cust_no);
			regOrderreceipts.setCardBankCode("");
			regOrderreceipts.setBankSeq("");
			regOrderreceipts.setCardName("");
			regOrderreceipts.setCardNo("");
			regOrderreceipts.setCvv("");		
			regOrderreceipts.setDepositor(cust_name);
			regOrderreceipts.setValidDate("");
			
			
			regOrderreceipts.setReceiptPlanDate(null);
			regOrderreceipts.setOkNo("");
			regOrderreceipts.setOkDate(DateUtil.toTimestamp(proc_date));
			regOrderreceipts.setOkMed("000");
			regOrderreceipts.setOkErrorCode("0000");
			regOrderreceipts.setVanComp("");
			regOrderreceipts.setPayMonth(0);
			regOrderreceipts.setNorestYn("0");
			regOrderreceipts.setNorestRate(0);
			regOrderreceipts.setNorestAmt(0);
			regOrderreceipts.setEndYn("1");		
			regOrderreceipts.setCancelYn("0");
			regOrderreceipts.setCancelCode("000");
			regOrderreceipts.setCancelDate(null);
			regOrderreceipts.setCancelId("");
			regOrderreceipts.setSaveamtUseFlag("0");
			regOrderreceipts.setCodDelyYn("0");
			regOrderreceipts.setDivideYn("0");
			regOrderreceipts.setPartialCancelYn("0");
			regOrderreceipts.setProtxVendortxcode("");
			regOrderreceipts.setProtxStatus("");
			regOrderreceipts.setProtxStatusdetail("");
			regOrderreceipts.setProtxVpstxid("");
			regOrderreceipts.setProtxSecuritykey("");
			regOrderreceipts.setProtxTxauthno("");
			regOrderreceipts.setIssueNumber("");
			regOrderreceipts.setPayboxIdentifiant("");
			regOrderreceipts.setPayboxCodetraitement("");
			regOrderreceipts.setPayboxPays("");
			regOrderreceipts.setPayboxDateq("");
			regOrderreceipts.setPayboxReference("");
			regOrderreceipts.setCardPassword("");
			regOrderreceipts.setRemark1V("");
			regOrderreceipts.setRemark2V("");
			regOrderreceipts.setRemark3N(0);
			regOrderreceipts.setRemark4N(0);
			regOrderreceipts.setRemark5D(null);
			regOrderreceipts.setRemark6D(null);
			regOrderreceipts.setRemark("");
			regOrderreceipts.setProcId(proc_id);
			regOrderreceipts.setProcDate(DateUtil.toTimestamp(proc_date));
			regOrderreceipts.setInsertId(proc_id);
			regOrderreceipts.setInsertDate(DateUtil.toTimestamp(proc_date));
			
			
			if (ship15feeCost > 0){  //교환 배송비
				
				regOrderreceipts.setQuestAmt(ship15feeCost);
				regOrderreceipts.setSettleGb("15");
				regOrderreceipts.setDoFlag("90");
				regOrderreceipts.setReceiptAmt(ship15feeCost);
				regOrderreceipts.setReceiptDate(DateUtil.toTimestamp(proc_date));
				regOrderreceipts.setRepayPbAmt(ship15feeCost);
				
			}
			else{			          //교환 취소 배송비
				regOrderreceipts.setQuestAmt(ship65feeCost);
				regOrderreceipts.setSettleGb("65");
				regOrderreceipts.setDoFlag("90");
				regOrderreceipts.setReceiptAmt(0);
				regOrderreceipts.setReceiptDate(null);
				regOrderreceipts.setRepayPbAmt(0);
				orderreceiptsList.add(regOrderreceipts);
			}
			
			
			param.put("ReceiptNo", regOrderreceipts.getReceiptNo());
		
			executedRtn = paclaimDAO.insertOrderreceipts(regOrderreceipts);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TORDERRECEIPTS insert" });
			}	
		
		}

		return executedRtn;
	}
	
	
	private int insertOrderCostApply(Ordercostapply[] ordercostapply) throws Exception {
		int executedRtn = 0;
		Ordercostapply regOrderCostApply = null;
		
		for (int i = 0; i < ordercostapply.length ; i++) {
			regOrderCostApply = ordercostapply[i];
			
			log.info("//---- insert Tordershipcost");
			
			if ("I".equals(regOrderCostApply.getCwareAction())){
				executedRtn = paorderDAO.insertOrdercostapply(regOrderCostApply);
			}else if("U".equals(regOrderCostApply.getCwareAction())){
				executedRtn = paorderDAO.updateOrdercostapply(regOrderCostApply);
			}
			
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERCOSTAPPLY INSERT"});
			}
		}
		return executedRtn;
	}
	
	private int insertOrderCostApply(Ordercostapply[] ordercostapply, PaClaimdtVO[] claimdt) throws Exception {
			
		for(Ordercostapply vo : ordercostapply){
			vo.setOrderWSeq(claimdt[0].getNewOrderWeq());
		}
		
		return insertOrderCostApply(ordercostapply);	
	}

	
	private void test() throws Exception{
		
		if(1 == 1){
			throw processException("test");
		}
	}
	
	/**
	 * 배송비 계산
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap<String, Object> settingTordershipcost4SKB(ParamMap paramMap) throws Exception {
	
		
		List<OrderdtVO> orgOrderdtList = new ArrayList<OrderdtVO>();
		Timestamp sysDateTime =  (Timestamp) paramMap.get("sysDateTime");
		
		
		
		Collection cResultOrdershipcost = new ArrayList();
		Collection cResultOrdercostapply = new ArrayList();
	
		OrderdtVO orgOrderdt = null; 
		OrdershipcostVO[] returnCustShipCost = null;
		Ordercostapply[]  returnOrderCostApply = null;
		
		HashMap<String, Object> retrunMap  = new HashMap<>();
		List<Ordercostapply> costApply4Gift = new ArrayList<Ordercostapply>();
		
		
		String receiveSeq       = "001";
		
		String arg_goods_str    = "";
		String arg_geq_str      = "";
		String arg_dseq_str     = "";
		String arg_wseq_str     = "";		
		String arg_qty_str      = "";
		String arg_amt_str      = "";
		String arg_cust_no		= "";
		String arg_receiver_seq = "";
		String arg_calc_qty		= "";
		String arg_rcv_str      = "";
		String result           = null;
		String[] shipcostArr;
		String[] shipcostmArr;
		String[] shipcostapplyArr;
		OrderShipCostVO orderShipCost = null;
		OrdershipcostVO ordershipcost = null;
		String orderWseq = null;
		String ReciverPost = null;
		
		orgOrderdtList = (List<OrderdtVO>) paramMap.get("orgOrderdtList4ShipCost");
		costApply4Gift = (List<Ordercostapply>) paramMap.get("CostApply4Gift");
		
		for (int i = 0; i < orgOrderdtList.size(); i++) {
			
			orgOrderdt = new OrderdtVO();
			orgOrderdt = orgOrderdtList.get(i);
			arg_calc_qty = "";
			
			if(paramMap.getString("orderGSeq").equals(orgOrderdt.getOrderGSeq())){
				//arg_calc_qty = Long.toString(orgOrderdt.getSyslast() - paramMap.getLong("cancelQty"));
				arg_calc_qty = Long.toString(paramMap.getLong("cancelQty"));	
			} else {
				continue;	
			}
			
			receiveSeq   = orgOrderdt.getReceiverSeq();
			
			arg_cust_no = orgOrderdt.getCustNo();
			arg_receiver_seq = orgOrderdt.getReceiverSeq();
			arg_goods_str += " " + orgOrderdt.getGoodsCode();
			
			arg_geq_str  += " " + orgOrderdt.getOrderGSeq();
			arg_dseq_str += " " + orgOrderdt.getOrderDSeq();
			arg_wseq_str += " " + orgOrderdt.getOrderWSeq();
			
			arg_rcv_str   += " " + "1";
			arg_amt_str   += " " + Double.toString((orgOrderdt.getRsaleAmt() / orgOrderdt.getOrderQty()) * Double.parseDouble(arg_calc_qty));
			//arg_amt_str   = " " + Double.toString(orgOrderdt.getRsaleAmt());
			arg_qty_str   += " " + arg_calc_qty;		
		}
		
	
		
		arg_goods_str = arg_goods_str.trim();
		arg_qty_str = arg_qty_str.trim();
		arg_amt_str = arg_amt_str.trim();
		arg_rcv_str = arg_rcv_str.trim();
		arg_geq_str = arg_geq_str.trim();
		arg_dseq_str= arg_dseq_str.trim();
		arg_wseq_str= arg_wseq_str.trim(); 
			
		
		orderShipCost = new OrderShipCostVO();	
		orderShipCost.setCustNo(arg_cust_no);
		orderShipCost.setOrderFlag("20");
		
		orderShipCost.setDate(orgOrderdt.getOrderDate());
		orderShipCost.setShipcostReceiptStr(arg_rcv_str);
		orderShipCost.setGoodsStr(arg_goods_str);
		orderShipCost.setAmtStr(arg_amt_str);
		orderShipCost.setQtyStr(arg_qty_str);
		
		orderShipCost.setOrderNo(paramMap.getString("orderNo"));
		orderShipCost.setReceiverSeq(arg_receiver_seq);
		
		
		orderShipCost.setCancelQtyStr(arg_qty_str);
		orderShipCost.setReturnQtyStr(arg_qty_str);
		orderShipCost.setExchangeQtyStr(arg_qty_str);
		orderShipCost.setOrderGSeqStr(arg_geq_str);
		orderShipCost.setOrderDSeqStr(arg_dseq_str);
		orderShipCost.setOrderWSeqStr(arg_wseq_str);
			
			
		ParamMap receiverParam = new ParamMap();
		receiverParam.put("custno",arg_cust_no);
		receiverParam.put("recieverseq", receiveSeq);
		ReciverPost =  paorderDAO.selectReciverPost(receiverParam);
			
		
		/*
		//조건부 배송 반품 오류 체크 로직
		//int cnt = 0;
		//cnt = paorderDAO.checkCNOrdershipcost(paramMap);
		
		if(!"999999".equals(ReciverPost)){
			try{
				test();
				//result = ComUtil.NVL(orderBizProcess.orderShipCost(orderShipCost));				
					
			}catch(Exception e){
				PaMonitering pamoniter = new PaMonitering();
				
				pamoniter.setCheckNo("paOrderNO : " + paramMap.getString("paOrderNo"));
				pamoniter.setGoodsCode(paramMap.getString("goodsCode"));
				pamoniter.setPaGoodsCode(paramMap.getString("paGoodsCode"));
				pamoniter.setCheckGb("80");
				pamoniter.setCheckTxt("코어 배송비 계산 오류");
				
				paorderDAO.insertTpaMonitering(pamoniter);
				
			}
		}
		
		*/
	
		if(!"999999".equals(ReciverPost)){
			result = ComUtil.NVL(orderBizProcess.orderShipCost(orderShipCost));	
		}

		
		if(result != null ){
			if(result.length() >0 ){
				
			
				shipcostArr = result.split("::");
				shipcostmArr = shipcostArr[0].split(" ");	
				shipcostapplyArr = 	shipcostArr[1].split(" ");
				
				for (int j = 0; j < shipcostmArr.length - 7; j++) {
					if (j % 8 == 0) {
							
						ordershipcost = new OrdershipcostVO();
						ordershipcost.setBlank();
						ordershipcost.setCwareAction("I");
						ordershipcost.setOrderNo(shipcostmArr[j]);
						ordershipcost.setSeq(shipcostmArr[j+1]);
						ordershipcost.setOrderGSeq("");
						ordershipcost.setOrderDSeq("");
						ordershipcost.setOrderWSeq("");
						ordershipcost.setDelyType("");
						ordershipcost.setReceiverSeq(shipcostmArr[j+2]);
						ordershipcost.setType(shipcostmArr[j+3]);
						ordershipcost.setEntpCode(shipcostmArr[j+4]);
						ordershipcost.setShpfeeCost(Double.parseDouble(shipcostmArr[j+5]));
						ordershipcost.setManualCancelAmt(0);
						ordershipcost.setShipCostNo(shipcostmArr[j+6]);
						ordershipcost.setShipCostReceipt("1"); //1 - 선불, 2 - 착불
						ordershipcost.setShpfeeCode("20");
						ordershipcost.setInsertId(paramMap.getString("userId"));
						ordershipcost.setInsertDate(sysDateTime);

						cResultOrdershipcost.add(ordershipcost);
										
					}
				}
				
				for(int j = 0; j < shipcostapplyArr.length - 6; j++) {
					if( j % 7 == 0 ) {
						Ordercostapply ordercostapply = new Ordercostapply();
						ordercostapply.setBlank();
						ordercostapply.setOrderNo(shipcostapplyArr[j]);
						ordercostapply.setOrderGSeq(shipcostapplyArr[j+1]);
						ordercostapply.setOrderDSeq(shipcostapplyArr[j+2]);					
						ordercostapply.setOrderWSeq(shipcostapplyArr[j+3]);
						orderWseq = shipcostapplyArr[j+3];
						ordercostapply.setApplyCostSeq(shipcostapplyArr[j+4]);
						ordercostapply.setShipCostNo(Integer.parseInt(shipcostapplyArr[j+5]));
						ordercostapply.setCwareAction(shipcostapplyArr[j+6]);
						ordercostapply.setInsertId(paramMap.getString("userId"));
						ordercostapply.setInsertDate(sysDateTime);
						ordercostapply.setModifyId(paramMap.getString("userId"));
						ordercostapply.setModifyDate(sysDateTime);
						cResultOrdercostapply.add(ordercostapply);
					}
				}
				
			}
				
		}// END of result != null check
		

		//사은품 처리
		if( costApply4Gift != null){
			for( Ordercostapply costApplyVo  : costApply4Gift){
				costApplyVo.setOrderWSeq(orderWseq);
				cResultOrdercostapply.add(costApplyVo);
		  };
		}
		
		//= 취소된 수량으로 재계산한 배송비 정보.
		returnCustShipCost = (OrdershipcostVO[]) cResultOrdershipcost.toArray(new OrdershipcostVO[0]);
		returnOrderCostApply = (Ordercostapply[]) cResultOrdercostapply.toArray(new Ordercostapply[0]);
		retrunMap.put("OrderShipCost", returnCustShipCost);
		retrunMap.put("OrderCostApply", returnOrderCostApply);
		retrunMap.put("ReciverPost", ReciverPost );	
		
		return retrunMap;
	}
	
	
	
	
	
	/**
	 * newReceipts
	 * @param Orderreceipts
	 * @return int
	 * @throws Exception
	 */
	private int insertReceipts(ArrayList<Orderreceipts> orderreceiptsList, OrdershipcostVO[] ordershipcost,ParamMap paramMap) throws Exception {
		
		
		int executedRtn = 0;
		double sumQuestAmt10 = 0;
		double sumQuestAmt20 = 0;
		Timestamp sysDateTime = paramMap.getTimestamp("procdate");
		String user_id  = paramMap.getString("userId");
		String order_no = paramMap.getString("orderNo");
		String custNo = paramMap.getString("custNo");
		//doflg,setDepositor
		
		for(int i = 0; ordershipcost.length > i; i++){
			if(ordershipcost[i].getType().equals("10")){ //= 추가 배송비
				sumQuestAmt10 = sumQuestAmt10 + ordershipcost[i].getShpfeeCost();
			}else { //= 취소 배송비
				sumQuestAmt20 = sumQuestAmt20 + ordershipcost[i].getShpfeeCost();
			}
		}
		
		
		Orderreceipts regOrderreceipts = new Orderreceipts(); 
		
		regOrderreceipts.setOrderNo(order_no);
		regOrderreceipts.setCustNo(custNo);
		regOrderreceipts.setDoFlag("90");
		regOrderreceipts.setDepositor("");
		regOrderreceipts.setCardBankCode("");
		regOrderreceipts.setBankSeq("");
		regOrderreceipts.setCardName("");
		regOrderreceipts.setCardNo("");
		regOrderreceipts.setCvv("");		
		regOrderreceipts.setValidDate("");
		regOrderreceipts.setReceiptPlanDate(null);
		regOrderreceipts.setOkNo("");
		regOrderreceipts.setOkDate(sysDateTime);
		regOrderreceipts.setOkMed("000");
		regOrderreceipts.setOkErrorCode("0000");
		regOrderreceipts.setVanComp("");
		regOrderreceipts.setPayMonth(0);
		regOrderreceipts.setNorestYn("0");
		regOrderreceipts.setNorestRate(0);
		regOrderreceipts.setNorestAmt(0);
		regOrderreceipts.setReceiptAmt(0);
		regOrderreceipts.setReceiptDate(null);
		regOrderreceipts.setEndYn("1");
		regOrderreceipts.setRepayPbAmt(0);
		regOrderreceipts.setCancelYn("0");
		regOrderreceipts.setCancelCode("000");
		regOrderreceipts.setCancelDate(null);
		regOrderreceipts.setCancelId("");
		regOrderreceipts.setSaveamtUseFlag("0");
		regOrderreceipts.setCodDelyYn("0");
		regOrderreceipts.setDivideYn("0");
		regOrderreceipts.setPartialCancelYn("1");
		regOrderreceipts.setProtxVendortxcode("");
		regOrderreceipts.setProtxStatus("");
		regOrderreceipts.setProtxStatusdetail("");
		regOrderreceipts.setProtxVpstxid("");
		regOrderreceipts.setProtxSecuritykey("");
		regOrderreceipts.setProtxTxauthno("");
		regOrderreceipts.setIssueNumber("");
		regOrderreceipts.setPayboxIdentifiant("");
		regOrderreceipts.setPayboxCodetraitement("");
		regOrderreceipts.setPayboxPays("");
		regOrderreceipts.setPayboxDateq("");
		regOrderreceipts.setPayboxReference("");
		regOrderreceipts.setCardPassword("");
		regOrderreceipts.setRemark1V("");
		regOrderreceipts.setRemark2V("");
		regOrderreceipts.setRemark3N(0);
		regOrderreceipts.setRemark4N(0);
		regOrderreceipts.setRemark5D(null);
		regOrderreceipts.setRemark6D(null);
		regOrderreceipts.setRemark("");
		regOrderreceipts.setProcId(user_id);
		regOrderreceipts.setProcDate(sysDateTime);
		regOrderreceipts.setInsertId(user_id);
		regOrderreceipts.setInsertDate(sysDateTime);
			
		if(sumQuestAmt10 > 0){//= 추가 배송비
			
			
			regOrderreceipts.setReceiptNo(selectSequenceNo("RECEIPT_NO"));
			regOrderreceipts.setSettleGb("15");
			regOrderreceipts.setDoFlag("90");
			regOrderreceipts.setQuestAmt(sumQuestAmt10);
			regOrderreceipts.setRepayPbAmt(sumQuestAmt10);
			regOrderreceipts.setReceiptAmt(sumQuestAmt10);
			regOrderreceipts.setReceiptDate(regOrderreceipts.getProcDate());
			regOrderreceipts.setPartialCancelYn("0");
			
			log.info("//---- insert TORDERRECEIPTS");
			executedRtn = paorderDAO.insertOrderreceipts(regOrderreceipts);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS INSERT"});
			}
			orderreceiptsList.add(regOrderreceipts);
		}
			
		if(sumQuestAmt20 > 0){ //= 취소 배송비
		
			regOrderreceipts.setReceiptNo(selectSequenceNo("RECEIPT_NO"));
			regOrderreceipts.setSettleGb("65");
			regOrderreceipts.setDoFlag("90");
			regOrderreceipts.setQuestAmt(sumQuestAmt20);
			regOrderreceipts.setRepayPbAmt(0);
			regOrderreceipts.setReceiptAmt(0);
			regOrderreceipts.setPartialCancelYn("0");
			
			log.info("//---- insert TORDERRECEIPTS");
			executedRtn = paorderDAO.insertOrderreceipts(regOrderreceipts);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS INSERT"});
			}
			orderreceiptsList.add(regOrderreceipts);
		}
		
		return executedRtn;
	}
	
	
	private int updateReceipts(ArrayList<Orderreceipts> orderreceiptsList) throws Exception {
		int executedRtn = 0;
		List<Orderreceipts> orgReceiptsList = null;
		Orderreceipts orderreceipts = null;
		Orderreceipts regOrderreceipts = null;
		
		if(orderreceiptsList != null){
			for(int i = 0; orderreceiptsList.size() > i; i++){
				orderreceipts = orderreceiptsList.get(i);
				
				if(orderreceipts.getSettleGb().equals("65")) orderreceipts.setSettleGb("15"); 
				else if(orderreceipts.getSettleGb().equals("15")) continue;
				else throw processMessageException("Invalid settleGb Code");
				
				//= 정산배송비(14)의 경우 여러행이 있을 수 있으므로 repay_pb_amt 를 차감할 금액을 계산하여 update 함.
				orgReceiptsList = paorderDAO.selectOrderRepayPbAmt(orderreceipts);
				
				double tempAmt = 0;
				
				if(orgReceiptsList.size() > 0){
					if(orgReceiptsList.size() == 1){
						
						regOrderreceipts = new Orderreceipts();
						regOrderreceipts.setReceiptNo(orgReceiptsList.get(0).getReceiptNo());
						regOrderreceipts.setSettleGb(orderreceipts.getSettleGb());
						regOrderreceipts.setOrderNo(orderreceipts.getOrderNo());
						regOrderreceipts.setQuestAmt(orderreceipts.getQuestAmt());
						
						log.info("//---- update TORDERRECEIPTS");
						executedRtn = paorderDAO.updateReceiptsRepay(regOrderreceipts);
						if (executedRtn != 1) {
							throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS UPDATE"});
						}
						
					}else {
						tempAmt = orderreceipts.getQuestAmt();
						boolean compYn = false;
						
						for(int k = 0; orgReceiptsList.size() > k; k++){
							if(tempAmt == orgReceiptsList.get(k).getRepayPbAmt()){ //= 동일한 잔여 금액이 있을 경우.
								regOrderreceipts = new Orderreceipts();
								regOrderreceipts.setReceiptNo(orgReceiptsList.get(k).getReceiptNo());
								regOrderreceipts.setSettleGb(orderreceipts.getSettleGb());
								regOrderreceipts.setOrderNo(orderreceipts.getOrderNo());
								regOrderreceipts.setQuestAmt(tempAmt);
								
								log.info("//---- update TORDERRECEIPTS");
								executedRtn = paorderDAO.updateReceiptsRepay(regOrderreceipts);
								if (executedRtn != 1) {
									throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS UPDATE"});
								}
								compYn = true;
								break;
							}
						}
						
						if(!compYn){
							for(int j = 0; orgReceiptsList.size() > j; j++){ //= 동일한 잔여 금액이 없을 경우.
								if(tempAmt == 0) break;
								
								regOrderreceipts = new Orderreceipts();
								regOrderreceipts.setReceiptNo(orgReceiptsList.get(j).getReceiptNo());
								regOrderreceipts.setSettleGb(orderreceipts.getSettleGb());
								regOrderreceipts.setOrderNo(orderreceipts.getOrderNo());
								
								if(tempAmt > orgReceiptsList.get(j).getRepayPbAmt()){
									tempAmt = orderreceipts.getQuestAmt() - orgReceiptsList.get(j).getRepayPbAmt();
									regOrderreceipts.setQuestAmt(orgReceiptsList.get(j).getRepayPbAmt());
								} else {
									regOrderreceipts.setQuestAmt(tempAmt);
									tempAmt = 0;
								}
								
								log.info("//---- update TORDERRECEIPTS");
								executedRtn = paorderDAO.updateReceiptsRepay(regOrderreceipts);
								if (executedRtn != 1) {
									throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS UPDATE"});
								}
							}
						}
					}
				} else {
					throw processMessageException("TORDERRECEIPTS update invalid target");
					
				}
				
			}
		}
			
		return executedRtn;
	}
	
	
	
	// 시퀀스 조회
	public String selectSequenceNo(String type) throws Exception {
		String sequenceNo = "";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("sequence_type", type);
		if (hashMap.get("sequence_type").equals("CUST_NO")
				|| hashMap.get("sequence_type").equals("ORDER_NO")
				|| hashMap.get("sequence_type").equals("GOODS_SELECT_NO")
				|| hashMap.get("sequence_type").equals("RECEIPT_NO")) {
			hashMap.put("condition_flag", (String) paorderDAO.selectSequenceNoCondition(hashMap));
		}
		sequenceNo = (String) paorderDAO.selectSequenceNo(hashMap);
		if (sequenceNo == null || sequenceNo.equals(""))
			throw processException("msg.cannot_retrieve", new String[] { "getSequenceNo : " + type });
		return sequenceNo;
	}
	
	
	
	private List<Ordercostapply> getApply4Gift(OrderdtVO[] orderdt, String user_id, String proc_date) throws Exception{
		
		List<Ordercostapply> costApply4Gift = new ArrayList<Ordercostapply>();
		
		for(OrderdtVO orderdtvo : orderdt ){
			
			if(!"001".equals(orderdtvo.getOrderDSeq())){
				Ordercostapply orderCostApply = new Ordercostapply();
				orderCostApply.setBlank();
				orderCostApply.setCwareAction("I");								
				orderCostApply.setOrderNo(orderdtvo.getOrderNo());
				orderCostApply.setOrderGSeq(orderdtvo.getOrderGSeq());
				orderCostApply.setOrderDSeq(orderdtvo.getOrderDSeq());
				orderCostApply.setOrderWSeq("");
				orderCostApply.setApplyCostSeq("000");
				orderCostApply.setShipCostNo(0);
				orderCostApply.setInsertId(user_id);
				orderCostApply.setInsertDate(DateUtil.toTimestamp(proc_date,"yyyy/MM/dd HH:mm:ss"));
				orderCostApply.setModifyId(user_id);
				orderCostApply.setModifyDate(DateUtil.toTimestamp(proc_date,"yyyy/MM/dd HH:mm:ss"));
				costApply4Gift.add(orderCostApply);
			}
				
		}
							
		return costApply4Gift;
	}
	
	private int insertPaOrderShipCost(PaOrderShipCost paordershipcost) throws Exception  {
		
		if (paordershipcost == null){
			return 1;
		}
		
		log.info("//---- insert TPAORDERSHIPCOST");
		int executedRtn  = 0;
		executedRtn = paorderDAO.insertPaOrderShipCost(paordershipcost);
		
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] {"TPAORDERSHIPCOST INSERT"});
		}
		
		return executedRtn;
	}
	
    //판매자 사유로 인한 교환은 교환 배송비를 만들지 않으므로, 교환 취소시 교환 취소 배송비는 0원이 되어야한다. 	
 	private void refineOrdershipcost4ShpfeeYn(Ordershipcost[] ordershipcost, String shpFeeYn){
 
		
		if ( shpFeeYn == null || "".equals(shpFeeYn)) return;
		
		if ("1".equals(shpFeeYn)) return;	//유상인경우 pass
		
					
		for(Ordershipcost sc : ordershipcost){
			sc.setShpfeeCost(0);
		}
		 return;
	}
 	
 	private void setCustDelyYn(PaClaimdtVO claimdt, OrderClaimVO orderClaimVO) {
 		
 		if(!claimdt.getClaimGb().equals("30") && !claimdt.getClaimGb().equals("45") ) return;
  		
 		if("1".equals(claimdt.getCustDelyYn())) {
			claimdt.setDoFlag("50");  //회수지시
			//claimdt.setDelyType("20");
			claimdt.setDelyGb(orderClaimVO.getReturnDelyGb());
			claimdt.setSlipNo(orderClaimVO.getReturnSlipNo());	
 		}
 		
 		
 	}
	
	//고객 직접발송시 상담생성
	private void checkCustrDirectDelivery(OrderdtVO[] orderdt, PaClaimdtVO[] claimdt) {
		if(!"12".equals(claimdt[0].getCsMgroup())) return; //하프클럽만 적용..
		if(claimdt == null || claimdt.length < 1 ) return;		
		
		
		for(int i = 0; i < claimdt.length; i++) {
			try {
				if(!"1".equals(claimdt[i].getCustDelyYn()) || "40".equals(claimdt[i].getClaimGb())) continue; //고객직접발송이 아닌경우, 교환출고인 경우
				
				ParamMap paramMap = new ParamMap();
				paramMap.put("orderNo"		, orderdt[0].getOrderNo());
				paramMap.put("orderGSeq"	, claimdt[i].getOrderGSeq());
				paramMap.put("orderDSeq"	, claimdt[i].getOrderDSeq());
				paramMap.put("orderWSeq"	, claimdt[i].getOrderWSeq());
				paramMap.put("custNo"		, orderdt[0].getCustNo());
				paramMap.put("lGroupCode"	, "60");
				paramMap.put("mGroupCode"	, "12"); // 하프클럽만 적용
				paramMap.put("sGroupCode"	, "95"); 
				paramMap.put("goodsCode"	, orderdt[0].getGoodsCode());
				paramMap.put("goodsdtCode"	, orderdt[0].getGoodsdtCode());
				paramMap.put("entpCode"		, orderdt[0].getEntpCode());
				paramMap.put("sysdate"	 	, String.valueOf(claimdt[i].getLastProcDate()));
				paramMap.put("paGroupCode"  , claimdt[0].getCsMgroup());
				String delyName = paclaimDAO.selectDelyName(claimdt[i].getDelyGb());
				paramMap.put("note"	, "하프클럽 2차 제휴사에서 반품 방법 중 고객직접발송 인입되었습니다.\n자동으로 회수지시 상태로 변경됩니다.\n실제 택배 물건 확인 후 회수확정 처리 부탁드립니다.\n택배사 - " + delyName + " 운송장 번호 - " + claimdt[i].getSlipNo());
				insertCustCounsel(paramMap);
			} catch (Exception e) {
				log.error("고객직접발송 인입  상담 생성 에러 :: " + e.getMessage());
			}
			
		}

	}
	
	private void insertCustCounsel(ParamMap paramMap) throws Exception{
		int executedRtn 						= 0;
		Custcounselm custcounselm 				= new Custcounselm();
		
		//CounselSeq 채번
		String counsel_seq = systemProcess.getSequenceNo("COUNSEL_SEQ");
		if (counsel_seq == null || "".equals(counsel_seq))  throw processException("msg.cannot_create", new String[] { "COUNSEL_SEQ" });		
	
		custcounselm.setCounselSeq		(counsel_seq);
		custcounselm.setClaimNo			(counsel_seq);
		custcounselm.setCustNo			(paramMap.getString("custNo"));
		custcounselm.setDoFlag			("25");  //업체이관
		custcounselm.setOutLgroupCode	("81");  //배송 수거 문의  , 'C031';
		custcounselm.setOutMgroupCode	("47");	 //기타
		custcounselm.setOutSgroupCode	("");
		custcounselm.setTel3			("");
		custcounselm.setWildYn			("0");
		custcounselm.setQuickEndYn		("0");
		custcounselm.setQuickYn			("0");
		custcounselm.setHcReqDate		(null);
		custcounselm.setRemark			("");
		custcounselm.setRefNo1			(paramMap.getString("orderNo"));
		custcounselm.setRefNo2			(paramMap.getString("orderGSeq"));
		custcounselm.setRefNo3			(paramMap.getString("orderDSeq")); 
		custcounselm.setRefNo4			(paramMap.getString("orderWSeq"));
		custcounselm.setCsSendYn		("1");
		custcounselm.setCounselMedia	("61");		
		custcounselm.setCsLgroup		(paramMap.getString("lGroupCode"));
		custcounselm.setCsMgroup		(paramMap.getString("mGroupCode"));
		custcounselm.setCsSgroup		(paramMap.getString("sGroupCode"));
		custcounselm.setCsLmsCode		(paramMap.getString("lGroupCode") + paramMap.getString("mGroupCode") + paramMap.getString("sGroupCode"));
	
		custcounselm.setGoodsCode		(paramMap.getString("goodsCode"));
		custcounselm.setGoodsdtCode		(paramMap.getString("goodsdtCode"));

		custcounselm.setSendEntpCode	(paramMap.getString("entpCode"));
		
		custcounselm.setInsertId		("12".equals(paramMap.get("paGroupCode").toString()) ? "PAHALF" : "PACOPN");
		custcounselm.setProcDate		(DateUtil.toTimestamp(paramMap.getString("sysdate"), "yyyy-MM-dd HH:mm:ss"));
		custcounselm.setProcId			("12".equals(paramMap.get("paGroupCode").toString()) ? "PAHALF" : "PACOPN");

		executedRtn = paclaimDAO.insertCounselCustcounselm(custcounselm);
		
		if (executedRtn != 1) throw processException("msg.cannot_save",new String[] { "Custcounselm insert" });		
		
		String counselProcNote = paramMap.getString("note");
		
		for(int i = 0; i < 2; i++) {
			Custcounseldt custcounseldt = new Custcounseldt();
			custcounseldt.setCounselSeq		(counsel_seq);
			if(i == 0) {
				custcounseldt.setCounselDtSeq	("100"); 
				custcounseldt.setDoFlag			("10");  //접수
			}else {
				custcounseldt.setCounselDtSeq	("101"); 
				custcounseldt.setDoFlag			("25");  //업체이관
			}
			custcounseldt.setTitle			("");
			custcounseldt.setDisplayYn		("1");
			custcounseldt.setProcNote		("상세사유 : " + counselProcNote);
			custcounseldt.setProcId			("12".equals(paramMap.get("paGroupCode").toString()) ? "PAHALF" : "PACOPN");
			
			executedRtn = paclaimDAO.insertCounselCustcounseldt(custcounseldt);
			if (executedRtn != 1) throw processException("msg.cannot_save",new String[] { "TCUSTCOUNSELDT insert" });
		}
	}
	
	private void checkReturnReceiptAlert(OrderdtVO[] orderdt, PaClaimdtVO[] claimdt) {
		if (!"05".equals(claimdt[0].getCsMgroup())) return;	// 쿠팡만
		if (claimdt == null || claimdt.length < 1) return;
		
		for (int i=0; i<claimdt.length; i++) {
			try {
				ParamMap paramMap = new ParamMap();
				
				paramMap.put("orderNo"	  , orderdt[0].getOrderNo());
				paramMap.put("orderGSeq"  , claimdt[i].getOrderGSeq());
				paramMap.put("orderDSeq"  , claimdt[i].getOrderDSeq());
				paramMap.put("orderWSeq"  , claimdt[i].getOrderWSeq());
				paramMap.put("custNo"	  , orderdt[0].getCustNo());
				paramMap.put("lGroupCode" , "60");
				paramMap.put("mGroupCode" , "05");
				paramMap.put("sGroupCode" , "06");
				paramMap.put("goodsCode"  , orderdt[0].getGoodsCode());
				paramMap.put("goodsdtCode", orderdt[0].getGoodsdtCode());
				paramMap.put("entpCode"	  , orderdt[0].getEntpCode());
				paramMap.put("sysdate"	  , String.valueOf(claimdt[i].getLastProcDate()));
				paramMap.put("note"		  , "쿠팡 주문 반품 접수 처리 건 회수 접수 요망.\nD+2일 영업일 후 SR 자동 완료 처리 됨.\n회수된 물품에 이상이 있을 시 회수 이의제기 신청 또는 SK스토아 SCM실(1670-4973)\n유선 문의 요망");
				paramMap.put("paGroupCode", claimdt[0].getCsMgroup());
				
				insertCustCounsel(paramMap);
			} catch (Exception e) {
				log.error("쿠팡 반품접수 업체이관 상담 생성 에러 :: " + e.getMessage());
			}
		}
	}
}