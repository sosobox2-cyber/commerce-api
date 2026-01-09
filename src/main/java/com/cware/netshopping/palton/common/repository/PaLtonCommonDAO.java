package com.cware.netshopping.palton.common.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaLtonAttrList;
import com.cware.netshopping.domain.model.PaLtonDispCtgr;
import com.cware.netshopping.domain.model.PaLtonDispList;
import com.cware.netshopping.domain.model.PaLtonPdItmsList;
import com.cware.netshopping.domain.model.PaLtonRetrieveCode;
import com.cware.netshopping.domain.model.PaLtonSettlement;
import com.cware.netshopping.domain.model.PaLtonStdCtgr;

@Service("palton.common.paLtonCommonDAO")
public class PaLtonCommonDAO extends AbstractPaDAO{

	public int insertPaDisplayCategory(PaLtonDispCtgr displayData) throws Exception{
		return insert("palton.common.insertPaDisplayCategory", displayData);
	}

	/**
	 * insert TPALTONSTANDARDDISPLIST
	 * @param PaLtonDispList
	 * @return
	 * @throws Exception
	 */
	public int insertDisInfoList(PaLtonDispList paLtonDispList) throws Exception{
		return insert("palton.common.insertDisInfoList", paLtonDispList);
	}

	/**
	 * insert TPALTONSTANDARDATTRLIST
	 * @param PaLtonAttrList
	 * @return
	 * @throws Exception
	 */
	public int insertAttrInfoList(PaLtonAttrList paLtonAttrList) throws Exception{
		return insert("palton.common.insertAttrInfoList", paLtonAttrList);
	}

	/**
	 * insert TPALTONSTANDARDPDITEMSLIST
	 * @param PaLtonPdItmsList
	 * @return
	 * @throws Exception
	 */
	public int insertPdItmsInfoList(PaLtonPdItmsList paLtonPdItmsList) throws Exception{
		return insert("palton.common.insertPdItmsInfoList", paLtonPdItmsList);
	}

	/**
	 * insert TPALTONSTANDARDCATEGORY
	 * @param PaLtonStdCtgr
	 * @return
	 * @throws Exception
	 */
	public int insertStdCtgrInfoList(PaLtonStdCtgr stdCtgrData) throws Exception{
		return insert("palton.common.insertStdCtgrInfoList", stdCtgrData);
	}

	/**
	 * insert TPABRAND
	 * @param paBrand
	 * @return
	 * @throws Exception
	 */
	public int insertPaLtonBrand(PaBrand paBrand) throws Exception{
		return insert("palton.common.insertPaLtonBrand", paBrand);
	}

	/**
	 * 롯데온 출고지/반품지 등록 - 대상 목록 조회
	 * @return
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		 return (HashMap<String, Object>) selectByPk("palton.common.selectSlipInsertList", paEntpSlip);
	}

	/**
	 * 롯데온 출고지/반품지 등록 - 저장
	 * @return
	 * @throws Exception
	 */	
	public int insertPaLtonEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return insert("palton.common.insertPaLtonEntpSlip", paEntpSlip);
	}

	/**
	 * 롯데온 출고지/반품지 수정 - 검색
	 * @return
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectEntpSlipUpdateList() throws Exception{
		return (List<HashMap<String,Object>>) list("palton.common.selectEntpSlipUpdateList", null);
	}

	// 업체주소 키로 조회하므로 단건 반환 (롯데온연동구조개선)
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectEntpSlipUpdate(PaEntpSlip paEntpSlip) throws Exception {
	    return (Map<String, Object>) selectByPk("palton.common.selectEntpSlipUpdate", paEntpSlip);
	}
	
	/**
	 * 롯데온 출고지/반품지 수정 - 저장
	 * @returnList
	 * @throws Exception
	 */
	public int updatePaLtonEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return update("palton.common.updatePaLtonEntpSlip", paEntpSlip);
	}

	/**
	 * 롯데온 배송비 정책 등록 - 검색
	 * @returnList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectEntpSlipCost(ParamMap paramMap) throws Exception{
	    return (List<HashMap<String,Object>>) list("palton.common.selectEntpSlipCost", paramMap.get());
	}
	
	/**
	 * 롯데온 추가 배송비 정책 등록 - 검색
	 * @returnList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectAddShipCost(ParamMap paramMap) throws Exception{
	    return (List<HashMap<String,Object>>) list("palton.common.selectAddShipCost", paramMap.get());
	}

	/**
	 * 롯데온 배송비정책 등록 - 저장
	 * @return 
	 * @throws Exception
	 */
	public int updatePaLtonCustShipCost(ParamMap custShipCostMap) throws Exception{
		return update("palton.common.updatePaLtonCustShipCost", custShipCostMap.get());
	}
	
	/**
	 * 롯데온 추가 배송비정책  등록 - 저장
	 * @return 
	 * @throws Exception
	 */
	public int updatePaLtonAddShipCost(ParamMap addShipCostMap) throws Exception{
		return update("palton.common.updatePaLtonAddShipCost", addShipCostMap.get());
	}

	/**
	 * 롯데온 원산지 등록 - 저장
	 * @return 
	 * @throws Exception
	 */
	public int insertPaLtonOrigin(PaLtonRetrieveCode paLtonRetrieveCode) throws Exception {
		return insert("palton.common.insertPaLtonOrigin", paLtonRetrieveCode);
	}

	public int deletePaLtonSettlement(ParamMap apiDataMap) throws Exception {
		return delete("palton.common.deletePaLtonSettlement", apiDataMap.get());
	}
	
	/**
	 * TPALTONSETTLEMENT INSERT
	 * @param settlementData
	 * @return
	 */
	public int insertPaLtonSettlement(PaLtonSettlement settlementData) throws Exception {
		return insert("palton.common.insertPaLtonSettlement", settlementData);
	}


}
