package com.cware.netshopping.pawemp.exchange.process.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.domain.model.PaWempClaimItemList;
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pawemp.claim.repository.PaWempClaimDAO;
import com.cware.netshopping.pawemp.common.enums.WempCode;
import com.cware.netshopping.pawemp.common.model.ReturnData;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.cware.netshopping.pawemp.exchange.model.SetClaimCompleteRequest;
import com.cware.netshopping.pawemp.exchange.model.SetClaimDeliveryRequest;
import com.cware.netshopping.pawemp.exchange.process.PaWempExchangeProcess;
import com.cware.netshopping.pawemp.exchange.repository.PaWempExchangeDAO;

@Service("pawemp.exchange.paWempExchangeProcess")
public class PaWempExchangeProcessImpl extends AbstractService implements PaWempExchangeProcess {

	@Resource(name = "pawemp.common.paWempApiService")
	public PaWempApiService paWempApiService;
	
	@Resource(name = "pawemp.claim.paWempClaimDAO")
	private PaWempClaimDAO paWempClaimDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "pawemp.exchange.paWempExchangeDAO")
	private PaWempExchangeDAO paWempExchangeDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Override
	@SuppressWarnings("unchecked")
	public String saveExchangeList(PaWempClaimList paWempClaim) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		List<Object> goodsInfoList = null;
		Paorderm paorderm = null;
		HashMap<String, Object> goodsInfoMap = null;
		
		int existsCnt = 0;
		int executedRtn = 0;
		
		existsCnt = paWempClaimDAO.selectPaWempClaimListExists(paWempClaim);
		if(existsCnt > 0) {
			return Constants.SAVE_FAIL;
		}
		
		executedRtn = paWempClaimDAO.insertPaWempClaimList(paWempClaim);
		if(executedRtn != 1){
			throw processException("msg.cannot_save", new String[] { "TPAWEMPCLAIMLIST INSERT" });
		}
		for(PaWempClaimItemList claimItemList : paWempClaim.getClaimItemList()){
			executedRtn = paWempClaimDAO.insertPaWempClaimItemList(claimItemList);
			if (executedRtn != 1){
				throw processException("msg.cannot_save", new String[] { "TPAWEMPCLAIMITEMLIST INSERT" });
			}
			
			goodsInfoList = paWempExchangeDAO.selectExchangeGoodsdt(claimItemList.getProductNo());
			goodsInfoMap = new HashMap<>();
			goodsInfoMap = (HashMap<String, Object>) goodsInfoList.get(0);
			for(int j = 0; j < 2; j++){
				//= 3. TPAORDERM INSERT
				paorderm = new Paorderm();
				
				if(goodsInfoList.size() == 1 ){ //= 연동된 단품이 1개일 경우.
					paorderm.setChangeGoodsdtCode(goodsInfoMap.get("GOODSDT_CODE").toString());
					paorderm.setChangeFlag("01");
				} else { //= 연동된 단품이 N개일 경우.
					paorderm.setChangeGoodsdtCode("");
					paorderm.setChangeFlag("00");
				}
				paorderm.setChangeGoodsCode(goodsInfoMap.get("GOODS_CODE").toString());
				
				paorderm.setPaCode(paWempClaim.getPaCode());
				if(j == 0){
					paorderm.setPaOrderGb(paWempClaim.getPaOrderGb()); //40: 교환배송
				}else {
					paorderm.setPaOrderGb(WempCode.ORDER_GB.EXCHANGE_COLLECTION.getCode()); //45: 교환회수
				}
				paorderm.setPaOrderNo(claimItemList.getPaOrderNo());
				paorderm.setPaOrderSeq(claimItemList.getPaOrderSeq());
				paorderm.setPaClaimNo(claimItemList.getPaClaimNo());
				paorderm.setPaShipNo(claimItemList.getPaShipNo());
				if(claimItemList.getOptionQty() < 1) {
					paorderm.setPaProcQty(Long.toString(claimItemList.getProductQty()));
				} else {
					paorderm.setPaProcQty(Long.toString(claimItemList.getOptionQty()));
				}
				paorderm.setPaDoFlag(WempCode.DO_FLAG.APPROVED.getCode());
				paorderm.setOutBefClaimGb("0");
				paorderm.setModifyId(Constants.PA_WEMP_PROC_ID);
				paorderm.setInsertDate(new Timestamp(sdf.parse(systemDAO.getSysdatetime()).getTime()));
				paorderm.setModifyDate(new Timestamp(sdf.parse(systemDAO.getSysdatetime()).getTime()));
				paorderm.setPaGroupCode(Constants.PA_WEMP_GROUP_CODE);
				
				executedRtn = paCommonDAO.insertPaOrderM(paorderm);
				if (executedRtn < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			}
		}
			
		return Constants.SAVE_SUCCESS;
	}

	@Override
	public List<Paorderm> selectOrderChangeTargetList() throws Exception {
		return paWempExchangeDAO.selectOrderChangeTargetList();
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paWempExchangeDAO.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("changeFlag", changeFlag);
		paramMap.put("mappingSeq", mappingSeq);
		return paWempExchangeDAO.updatePaOrdermChangeFlag(paramMap);
	}
	
	@Override
	public Paorderm selectPaOrdermInfo(String mappingSeq) throws Exception {
		return paWempExchangeDAO.selectPaOrdermInfo(mappingSeq);
	}
	
	@Override
	public String saveExchangeCancelList(PaWempClaimList paWempClaim) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Paorderm paorderm = null;
		
		int existsCnt = 0;
		int executedRtn = 0;
		
		existsCnt = paWempClaimDAO.selectPaWempClaimListExists(paWempClaim);
		if(existsCnt > 0) {
			return Constants.SAVE_FAIL;
		}
		
		executedRtn = paWempClaimDAO.insertPaWempClaimList(paWempClaim);
		if(executedRtn != 1){
			throw processException("msg.cannot_save", new String[] { "TPAWEMPCLAIMLIST INSERT" });
		}
		for(PaWempClaimItemList claimItemList : paWempClaim.getClaimItemList()){
			executedRtn = paWempClaimDAO.insertPaWempClaimItemList(claimItemList);
			if (executedRtn != 1){
				throw processException("msg.cannot_save", new String[] { "TPAWEMPCLAIMITEMLIST INSERT" });
			}
			
			for(int j = 0; j < 2; j++){
				//= TPAORDERM INSERT
				paorderm = new Paorderm();
				
				paorderm.setPaCode(paWempClaim.getPaCode());
				if(j == 0){
					paorderm.setPaOrderGb(paWempClaim.getPaOrderGb()); //41: 교환배송취소
				}else {
					paorderm.setPaOrderGb(WempCode.ORDER_GB.EXCHANGE_COLLECTION_CANCELLATION.getCode()); //46: 교환회수취소
				}
				paorderm.setPaOrderNo(claimItemList.getPaOrderNo());
				paorderm.setPaOrderSeq(claimItemList.getPaOrderSeq());
				paorderm.setPaClaimNo(claimItemList.getPaClaimNo());
				paorderm.setPaShipNo(claimItemList.getPaShipNo());
				if(claimItemList.getOptionQty() < 1) {
					paorderm.setPaProcQty(Long.toString(claimItemList.getProductQty()));
				} else {
					paorderm.setPaProcQty(Long.toString(claimItemList.getOptionQty()));
				}
				paorderm.setPaDoFlag(WempCode.DO_FLAG.APPROVED.getCode());
				paorderm.setOutBefClaimGb("0");
				paorderm.setModifyId(Constants.PA_WEMP_PROC_ID);
				paorderm.setInsertDate(new Timestamp(sdf.parse(systemDAO.getSysdatetime()).getTime()));
				paorderm.setModifyDate(new Timestamp(sdf.parse(systemDAO.getSysdatetime()).getTime()));
				paorderm.setPaGroupCode(Constants.PA_WEMP_GROUP_CODE);
				
				executedRtn = paCommonDAO.insertPaOrderM(paorderm);
				if (executedRtn < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			}
		}
		return Constants.SAVE_SUCCESS;
	}
	
	@Override
	public List<Paorderm> selectChangeCancelTargetList() throws Exception {
		return paWempExchangeDAO.selectChangeCancelTargetList();
	}
	
	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paWempExchangeDAO.selectChangeCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<Object> selectExchangePickupList() throws Exception {
		return paWempExchangeDAO.selectExchangePickupList();
	}

	@Override
	public int updatePickupProc(String mappingSeq) throws Exception {
		Paorderm paorderm = new Paorderm();
		paorderm.setMappingSeq(mappingSeq);
		return paWempExchangeDAO.updatePickupProc(paorderm);
	}
	
	@Override
	public int updateProcFail(String paClaimNo, String apiResultMessage, String paOrderGb, String mappingSeq) throws Exception {
		Paorderm paorderm = new Paorderm();
		paorderm.setPaClaimNo(paClaimNo);
		paorderm.setApiResultMessage(apiResultMessage);
		paorderm.setPaOrderGb(paOrderGb);
		paorderm.setMappingSeq(mappingSeq);
		return paWempExchangeDAO.updateProcFail(paorderm);
	}

	@Override
	public List<Object> selectPickupCompleteList() throws Exception {
		return paWempExchangeDAO.selectPickupCompleteList();
	}
	
	@Override
	public int updatePickupCompleteProc(HashMap<String, Object> receive) throws Exception {
		return paWempExchangeDAO.updatePickupCompleteProc(receive);
	}
	
	@Override
	public List<Object> selectExchangeSlipOutTargetList() throws Exception {
		return paWempExchangeDAO.selectExchangeSlipOutTargetList();
	}
	
	@Override
	public ParamMap slipOutProc(HashMap<String, Object> exchangeDeliveryVo, HashMap<String, String> apiInfo) throws Exception {
		ParamMap resultMap = new ParamMap();
		String paName = exchangeDeliveryVo.get("PA_CODE").toString().equals(Constants.PA_WEMP_BROAD_CODE) ? Constants.PA_BROAD : Constants.PA_ONLINE;
		
		try {
			log.info("01.교환배송등록 API 전송");
			SetClaimDeliveryRequest deliveryReq = new SetClaimDeliveryRequest();
			deliveryReq.setClaimBundleNo(Long.parseLong(exchangeDeliveryVo.get("PA_CLAIM_NO").toString()));
			deliveryReq.setClaimType("EXCHANGE");
			
			if(StringUtils.equals("DIRECT", exchangeDeliveryVo.get("PA_DELY_GB").toString())) {
				deliveryReq.setShipMethod("DIRECT");
				deliveryReq.setScheduleShipDate(exchangeDeliveryVo.get("SCHEDULE_SHIP_DATE").toString());
				if(!StringUtils.equals("1", exchangeDeliveryVo.get("DELY_HOPE_YN").toString())) {
					log.info("slipOutProc No TSLIPM.DELY_HOPE_YN setting");
				}
			} else {
				deliveryReq.setShipMethod("PARCEL");
				deliveryReq.setParcelCompanyCode(exchangeDeliveryVo.get("PA_DELY_GB").toString());
				deliveryReq.setInvoiceNo(exchangeDeliveryVo.get("SLIP_NO").toString());
			}
			
			ReturnData returnData = (ReturnData)paWempApiService.callWApiObejct(apiInfo, "POST", deliveryReq, ReturnData.class, paName);
			if(returnData.getReturnKey() == 1) { //성공
				log.info("발송처리 API 전송성공 결과 returnKey:1, returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","1");
				resultMap.put("result_text","OK");
			} else if(StringUtils.contains(returnData.getReturnMsg(), "신청/보류")){ //클레임 신청/보류 단계만 가능합니다.
				resultMap.put("result_code","1");
				resultMap.put("result_text","Already Delivery");
			} else {
				log.info("발송처리 API 전송실패 결과 returnKey:"+returnData.getReturnKey()+", returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","0");
				resultMap.put("result_text", returnData.getReturnMsg());
			}
		}catch(Exception e){
			resultMap.put("result_code", "-9999");
			resultMap.put("result_text", e.getMessage());
		}
		
		return resultMap;
	}
	
	@Override
	public int updateDeliveryProc(HashMap<String, Object> exchageDeliveryVo) throws Exception {
		return paWempExchangeDAO.updateDeliveryProc(exchageDeliveryVo);
	}

	@Override
	public ParamMap setClaimCompleteProc(HashMap<String, Object> exchangeCompleteVo, HashMap<String, String> apiInfo) throws Exception {
		ParamMap resultMap = new ParamMap();
		String paName = exchangeCompleteVo.get("PA_CODE").toString().equals(Constants.PA_WEMP_BROAD_CODE) ? Constants.PA_BROAD : Constants.PA_ONLINE;
		
		try {
			log.info("01.교환클레임 완료 API 전송");
			SetClaimCompleteRequest completeReq = new SetClaimCompleteRequest();
			completeReq.setClaimBundleNo(Long.parseLong(exchangeCompleteVo.get("PA_CLAIM_NO").toString()));
			completeReq.setClaimType("EXCHANGE");
		
			ReturnData returnData = (ReturnData)paWempApiService.callWApiObejct(apiInfo, "POST", completeReq, ReturnData.class, paName);
			if(returnData.getReturnKey() == 1) { //성공
				log.info("교환클레임 완료 API 전송성공 결과 returnKey:1, returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","1");
				resultMap.put("result_text","OK");
			} else {
				log.info("교환클레임 완료 API 전송실패 결과 returnKey:"+returnData.getReturnKey()+", returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","0");
				resultMap.put("result_text", returnData.getReturnMsg());
			}
		}catch(Exception e){
			resultMap.put("result_code", "-9999");
			resultMap.put("result_text", e.getMessage());
		}
		
		return resultMap;
	}
	
	@Override
	public int updateExchangeEnd(HashMap<String, Object> exchangeCompleteVo) throws Exception {
		if(paWempExchangeDAO.updatePickupEnd(exchangeCompleteVo) > 0) {
			return paWempExchangeDAO.updateExchangeCompleteProc(exchangeCompleteVo);
		} else {
			return 0;
		}
	}
	
	@Override
	public List<HashMap<String, Object>> selectExchangeCompleteList() throws Exception{
		return paWempExchangeDAO.selectExchangeCompleteList();
	}
}
