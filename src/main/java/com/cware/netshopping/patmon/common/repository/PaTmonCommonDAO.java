package com.cware.netshopping.patmon.common.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaTmonSettlement;
@Service("patmon.common.paTmonCommonDAO")
public class PaTmonCommonDAO extends AbstractPaDAO {

	public int deletePaGoodsKindsMomentList(PaGoodsKinds paGoodsKinds) {
		return delete("patmon.common.deletePaGoodsKindsMomentList", paGoodsKinds);
	}

	public int insertPaGoodsKindMomentsList(PaGoodsKinds paGoodsKinds) {
		return insert("patmon.common.insertPaGoodsKindMomentsList", paGoodsKinds);
	}

	public int insertPaGoodsKindsList(PaGoodsKinds paGoodsKinds) {
		return insert("patmon.common.insertPaGoodsKindsList", paGoodsKinds);
	}
	
	/**
	 * 롯데온 출고지/반품지 등록 - 대상 목록 조회
	 * @return
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		 return (HashMap<String, Object>) selectByPk("patmon.common.selectSlipInsertList", paEntpSlip);
	}
	
	/**
	 * 출고지/반품지 등록 저장
	 * @param PaEntpSlip 
	 * @return Map
	 * @throws Exception
	 */
	public int insertPaTmonEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		return insert("patmon.common.insertPaTmonEntpSlip", paEntpSlip);
	}

	/**
	 * 출고지/반품지 수정 조회
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectEntpSlipUpdateList() throws Exception {
		return (List<HashMap<String,Object>>) list("patmon.common.selectEntpSlipUpdateList", null);
	}

	// 업체주소 키로 조회하므로 단건 반환 (티몬연동구조개선)
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectEntpSlipUpdate(PaEntpSlip paEntpSlip) throws Exception {
	    return (Map<String, Object>) selectByPk("patmon.common.selectEntpSlipUpdate", paEntpSlip);
	}
	
	/**
	 * 출고지/반품지 수정 저장
	 * @return Map
	 * @throws Exception
	 */
	public int updatePaTmonEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		return update("patmon.common.updatePaTmonEntpSlip", paEntpSlip);
	}
	
	/**
	 * 배송템플릿 등록 리스트 조회
	 * @param ParamMap 
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectEntpSlipCost(ParamMap apiInfoMap) throws Exception {
		return (List<HashMap<String,Object>>) list("patmon.common.selectEntpSlipCost", apiInfoMap.get());
	}
	
	/**
	 * 배송템플릿 등록 리스트 저장
	 * @param ParamMap 
	 * @return List
	 * @throws Exception
	 */
	public int updatePaTmonShipCost(ParamMap tmonShipCostMap) throws Exception {
		return update("patmon.common.updatePaTmonShipCost", tmonShipCostMap.get());
	}

	/**
	 * 정산API  저장
	 * @param PaTmonSettlement 
	 * @return int
	 * @throws Exception
	 */
	public int insertPaTmonSettlement(PaTmonSettlement paTmonSettlementList) throws Exception {
		return insert("patmon.common.insertPaTmonSettlement", paTmonSettlementList );
	}

	public int deletePaTmonSettlement(ParamMap paramMap) throws Exception{
		return delete("patmon.common.deletePaTmonSettlement", paramMap.get());
	}

	public int selectPaTmonSettlementExists(PaTmonSettlement paTmonSettlement) {
		return (Integer) selectByPk("patmon.common.selectPaSettlementExists", paTmonSettlement);
	}


}
