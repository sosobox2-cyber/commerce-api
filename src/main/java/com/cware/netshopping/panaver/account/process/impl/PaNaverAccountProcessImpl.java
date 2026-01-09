package com.cware.netshopping.panaver.account.process.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.domain.PanaversettlementVO;
import com.cware.netshopping.panaver.account.process.PaNaverAccountProcess;
import com.cware.netshopping.panaver.account.repository.PaNaverAccountDAO;

@Service("panaver.account.PaNaverAccountProcess")
public class PaNaverAccountProcessImpl extends AbstractService implements PaNaverAccountProcess{

	@Resource(name = "panaver.account.PaNaverAccountDAO")
	private PaNaverAccountDAO paNaverAccountDAO;
	
	@Resource(name = "common.system.systemDAO")
	private SystemDAO systemDAO;
	@Override
	public int selectChkPaNaverAccount(ParamMap paramMap) throws Exception {
		return paNaverAccountDAO.selectChkPaNaverAccount(paramMap);
	}
	
	/**
	 * 정산매출정보 조회 IF_PANAVRAPI_04_001 : 저장.
	 * @param List<PaNavrSalesVO>
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String saveSettelmentList(List<PanaversettlementVO> arrPaNaverAccountList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		PanaversettlementVO paNaverSettlement = new PanaversettlementVO();
		int executedRtn = 0;
		String paNaverSalesSeq = "";
		HashMap<String, Object> hashMap = null;
		String paDoFlag = "";
		
		if(arrPaNaverAccountList.get(0).getDelYn().equals("y")||arrPaNaverAccountList.get(0).getDelYn().equals("Y")){
			executedRtn = paNaverAccountDAO.deletePaNaverAccount(arrPaNaverAccountList.get(0));
		}
		
		for(int i=0; arrPaNaverAccountList.size() > i; i++){
			paNaverSettlement = arrPaNaverAccountList.get(i);
			
			hashMap = new HashMap<String, Object>();
    		hashMap.put("sequence_type", "PANAVER_SALES_NO");
    		
    		paNaverSalesSeq = (String)systemDAO.selectSequenceNo(hashMap);
    		paNaverSettlement.setPanavrSalesNo(paNaverSalesSeq);
    		
    		executedRtn = paNaverAccountDAO.insertPaNaverSettlement(paNaverSettlement);
    		if(executedRtn < 1){
    			throw processException("msg.cannot_save", new String[] { "TPANAVRSALES INSERT" });
    		}
    		
    		if(paNaverSettlement.getSettleType().equals("정산")&&paNaverSettlement.getProductOrderType().equals("상품주문")){
    			paDoFlag = paNaverAccountDAO.selectPaDoFlagCheck(paNaverSettlement);
    			if(paDoFlag!=null){
    				paNaverSettlement.setPaDoFlag(paDoFlag);
    				paNaverSettlement.setModifyDate(arrPaNaverAccountList.get(i).getInsertDate());
    				executedRtn = paNaverAccountDAO.updatePaorderm(paNaverSettlement);
    				if(executedRtn < 1){
    					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
    				}
    			}
    		}
		}
		
		return rtnMsg;
	}

}
