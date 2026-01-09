package com.cware.netshopping.pacopn.sales.process.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.domain.model.Pacopnsettlement;
import com.cware.netshopping.pacopn.sales.process.PaCopnSalesProcess;
import com.cware.netshopping.pacopn.sales.repository.PaCopnSalesDAO;

@Service("pacopn.sales.paCopnSalesProcess")
public class PaCopnSalesProcessImpl extends AbstractService implements PaCopnSalesProcess{

	@Resource(name = "pacopn.sales.paCopnSalesDAO")
	private PaCopnSalesDAO paCopnSalesDAO;

//	@Resource(name = "pacommon.sales.paSalesDAO")
//	private PaSalesDAO paSalesDAO;

	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Override
	public int selectChkPaCopnAccount(ParamMap paramMap) throws Exception {
		return paCopnSalesDAO.selectChkPaCopnAccount(paramMap);
	}
	
//	@Override
//	/* 쿠팡 정산 일 대사 - 쿠팡 제공 API데이터와 BO데이터 일 대사 프로세스 */
//	public ParamMap selectPaCopnCompareProcess(ParamMap paramMap) throws Exception {
//		ParamMap returnMap = new ParamMap();
//		return returnMap;
//	}
//
//	@Override
//	/* 제휴사 제공 데이터 기준 월 대사 프로세스 */
//	public ParamMap selectPaCopnCompareProcessMonth(ParamMap paramMap) throws Exception {
//		ParamMap returnMap = new ParamMap();
//		return returnMap;
//	}

	@Override
	public String savePaCopnSettlement(List<Pacopnsettlement> pacopnSettlementList, ParamMap paramMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		Pacopnsettlement settlementList = new Pacopnsettlement();
		String paCopnSalesSeq = "";
		HashMap<String, Object> hashMap = null;
		int existsChk = 0;
		int executedRtn = 0;
		
		if((paramMap.getString("delYn").equals("y")||paramMap.getString("delYn").equals("Y")) && "start".equals(paramMap.getString("nextToken"))){
			executedRtn = paCopnSalesDAO.deletePaCopnAccount(paramMap);
		}
		
		for(int i=0; i<pacopnSettlementList.size(); i++){
			settlementList = pacopnSettlementList.get(i);
			existsChk = paCopnSalesDAO.selectPaSettlementExists(settlementList);
			
			if(existsChk > 0){
				continue;
			}
			
			hashMap = new HashMap<String, Object>();
			hashMap.put("sequence_type", "PACOPN_SALES_NO");
			paCopnSalesSeq = (String)systemDAO.selectSequenceNo(hashMap);
			
			if(paCopnSalesSeq.equals("")){
				throw processException("msg.cannot_create", new String[] { "PACOPN_SALES_NO" });
			}
			settlementList.setPacopnSalesNo(paCopnSalesSeq);
			
			executedRtn = paCopnSalesDAO.insertPaCopnSettlement(settlementList);
			if(executedRtn < 1){
				throw processException("msg.cannot_save", new String[] { "TPACOPNSETTLEMENT INSERT" });
			}
		}
		return rtnMsg;
	}
}
