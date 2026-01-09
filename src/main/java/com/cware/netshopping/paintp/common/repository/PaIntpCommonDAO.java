package com.cware.netshopping.paintp.common.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaIntpDelvSettlement;
import com.cware.netshopping.domain.model.PaIntpSettlement;

@Service("paintp.common.paIntpCommonDAO")
public class PaIntpCommonDAO extends AbstractPaDAO{	
	
	/**
	 * 인터파크 - 브랜드 정보 저장
	 * @param PaIntpBrand
	 * @return Integer
	 * @throws Exception
	 */
	public Integer insertPaIntpBrand(PaBrand paBrand) throws Exception {
		return update("paIntp.common.insertPaIntpBrand", paBrand);
	}
	
	/**
	 * 인터파크 출고지등록 - 대상 목록 조회
	 * @returnList
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception {
	    return (HashMap<String, String>) selectByPk("paIntp.common.selectEntpShipInsertList", paEntpSlip);
	}
	
	/**
	 * 인터파크 출고지등록
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPaIntpEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return insert("paIntp.common.insertPaIntpEntpSlip", paEntpSlip);
	}

	/**
	 * 인터파크 출고지수정 - 대상 목록 조회
	 * @returnList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectEntpShipUpdateList(String paAddrGb) throws Exception {
	    return list("paIntp.common.selectEntpShipUpdateList", paAddrGb);
	}

	// 업체주소 키로 조회하므로 단건 반환 (인터파크연동구조개선)
	@SuppressWarnings("unchecked")
	public Map<String, String> selectEntpShipUpdate(PaEntpSlip paEntpSlip) throws Exception {
	    return (Map<String, String>) selectByPk("paIntp.common.selectEntpShipUpdate", paEntpSlip);
	}
	
	/**
	 * 인터파크 출고지수정
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int updatePaIntpEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return update("paIntp.common.updatePaIntpEntpSlip", paEntpSlip);
	}
	
	/**
	 * 인터파크 배송비 정책 등록 - 배송비 조회
	 * @param ParamMap
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<?,?>> selectEntpSlipCost(ParamMap paramMap) throws Exception {
	    return (List<HashMap<?,?>>) list("paIntp.common.selectEntpSlipCost", paramMap.get());
	}

	/**
	 * 인터파크 사용하지 않는 카테고리 use_yn = 0 처리.
	 * @param PaGoodsKinds
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGoodsKindsUnUseListUseYn(PaGoodsKinds paGoodsKinds) throws Exception{
		return update("paIntp.common.updatePaGoodsKindsUnUseListUseYn", paGoodsKinds);
	}
	
	/**
	 * 인터파크 배송비 정책 등록 - 배송비 저장
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public int updatePaIntpCustShipCost(ParamMap paramMap) throws Exception{
		return update("paIntp.common.updatePaIntpCustShipCost", paramMap.get());
	}
	
	/**
	 * 인터파크 배송비별 정산 - 정산 결과 저장
	 * @param PaIntpSettlementVO
	 * @return int
	 * @throws Exception
	 */
	public int deletePaIntpSettlement(ParamMap paramMap) throws Exception{
		return delete("paIntp.common.deletePaIntpSettlement", paramMap.get());
	}
	
	public int selectPaIntpSettlementExists(PaIntpSettlement paIntpSettlement) throws Exception{
		return (Integer) selectByPk("paIntp.common.selectPaSettlementExists", paIntpSettlement);
	}
	
	public int insertPaIntpSettlement(PaIntpSettlement paIntpSettlement) throws Exception{
		return insert("paIntp.common.insertPaIntpSettlement", paIntpSettlement);
	}

	public int deletePaIntpDelvSettlement(ParamMap paramMap) throws Exception{
		return delete("paIntp.common.deletePaIntpDelvSettlement", paramMap.get());
	}

	public int selectPaIntpDelvSettlementExists(PaIntpDelvSettlement paIntpDelvSettlement) throws Exception{
		return (Integer) selectByPk("paIntp.common.selectPaDelvSettlementExists", paIntpDelvSettlement);
	}

	public int insertPaIntpDelvSettlement(PaIntpDelvSettlement paIntpDelvSettlement) throws Exception{
		return insert("paIntp.common.insertPaIntpDelvSettlement", paIntpDelvSettlement);
	}
}