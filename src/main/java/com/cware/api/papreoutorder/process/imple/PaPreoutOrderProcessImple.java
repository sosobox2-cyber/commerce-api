package com.cware.api.papreoutorder.process.imple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cware.api.papreoutorder.process.PaPreoutOrderProcess;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.PreoutOrderInputVO;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;

@Service("papreoutorder.papreoutorderProcess")
public class PaPreoutOrderProcessImple extends AbstractController implements PaPreoutOrderProcess {
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paOrderService;

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Async
	public void preoutOrderInput(Map<String, String> order, HttpServletRequest request) throws Exception {
		HashMap<String, Object>[] resultMap = null;
		PreoutOrderInputVO[] preoutOrderInputVO 		= null;
		List<Map<String, Object>>  preoutOrderInputTargetDtList = null;
		ParamMap paramMap					= null;
		PreoutOrderInputVO			  vo		= new PreoutOrderInputVO();
		int	index 							= 0;

		try {
			paramMap = new ParamMap();
			paramMap.put("paOrderNo" , order.get("PA_ORDER_NO"));
			paramMap.put("orderNo"   , order.get("ORDER_NO"));

			preoutOrderInputTargetDtList = paOrderService.selectPreoutOrderTargetDtList(paramMap);
			if(preoutOrderInputTargetDtList == null || preoutOrderInputTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectPreoutOrderTargetDtList size 0" });
			
			preoutOrderInputVO = new PreoutOrderInputVO[preoutOrderInputTargetDtList.size()];
			
			ComUtil.replaceCamelList(preoutOrderInputTargetDtList);
			
			for(Map<String, Object> map : preoutOrderInputTargetDtList) {
				vo = (PreoutOrderInputVO)ComUtil.map2VO(map, PreoutOrderInputVO.class); 
				
				if(vo.getSalePrice() != vo.getOrgSalePrice()) throw processException("pa.fail_preout_order_input", new String[] { "가격 정보가 잘못되었습니다." });
				
				preoutOrderInputVO[index] = vo;
				index++;
			}
			resultMap = paOrderService.savePreoutOrderTx(preoutOrderInputVO);
			
		}catch (Exception e) {
			updatePaOrdermTxForRollback(preoutOrderInputVO, e);
			
			paramMap.put("apiCode"	, "PA_PREOUT_ORDER_INPUT");
			paramMap.put("message"	, "pa_order_no : " + order.get("PA_ORDER_NO") + " > " + PaHalfComUtill.getErrorMessage(e));
		
		}finally {
			paramMap.put("apiCode"	, "PA_PREOUT_ORDER_INPUT");
			saveApiTracking(paramMap, request);
		}
		
	}

	private void updatePaOrdermTxForRollback(PreoutOrderInputVO[] preoutOrderInputVO, Exception e) {
		if(preoutOrderInputVO == null || preoutOrderInputVO.length < 1 || preoutOrderInputVO[0] == null)  return;
		
		ParamMap paramMap 	= null;
		int excuteCnt 		= 0;
		
		for(int j = 0; preoutOrderInputVO.length > j; j++){
			paramMap = new ParamMap();
			paramMap.put("mappingSeq", preoutOrderInputVO[j].getMappingSeq());
			
			
			String errMsg = PaHalfComUtill.getErrorMessage(e);
			paramMap.put("resultCode"		, "999999");
			paramMap.put("resultMessage"	, errMsg.length() > 1950 ? errMsg.substring(0,1950) : errMsg);
			paramMap.put("createYn"			, "0");
			
			try {
				excuteCnt = paOrderService.updatePreoutPaOrdermTx(paramMap);
				if(excuteCnt != 1){
					log.error(paramMap.getString("mappingSeq") + " Update ERROR - Result Code 999999 in TPAORDERM");
				}
			} catch (Exception e1) {
				log.error(paramMap.getString("mappingSeq") + " Update ERROR - Result Code 999999 in TPAORDERM");
				continue;
			}
		}//end of for
		
	}
	
	private void saveApiTracking(ParamMap paramMap , HttpServletRequest request) {
		if(paramMap == null) return;
		if(paramMap.getString("apiCode").equals("")) return;
		
		try{
			paramMap.put("startDate"	, systemService.getSysdatetimeToString());
			paramMap.put("code"			, "500");
			paramMap.put("siteGb"		, "PAORDER");
			systemService.insertApiTrackingTx(request, paramMap);
			
		}catch(Exception ee){
			log.error("ApiTracking Insert Error : "+ee.toString());
		}
	}
}
