package com.cware.netshopping.pawemp.common.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.Makecomp;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaWempBrand;
import com.cware.netshopping.domain.model.PaWempEntpSlip;
import com.cware.netshopping.domain.model.PaWempMaker;

@Service("pawemp.common.paWempCommonDAO")
public class PaWempCommonDAO extends AbstractPaDAO {
	
	/**
	 * 위메프 상품고시 임시저장 테이블 데이터삭제
	 * @param
	 * @return String
	 * @throws Exception
	 */
	public int deletePaWempOfferCodeMoment() throws Exception{
		return delete("pawemp.common.deletePaWempOfferCodeMoment", null);
	}
	
	/**
	 * 위메프 상품고시 임시저장
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPaWempOfferCodeMoment(PaOfferCode paWempOfferCode) throws Exception{
		return insert("pawemp.common.insertPaWempOfferCodeMoment", paWempOfferCode);
	}
	
	/**
	 * 위메프 상품고시 저장
	 * @param PaOfferCode
	 * @return String
	 * @throws Exception
	 */
	public int insertPaWempOfferCodeList(PaOfferCode paWempOfferCode) throws Exception{
		return insert("pawemp.common.insertPaWempOfferCodeList", paWempOfferCode);
	}
	
	/**
	 * 위메프 상품고시 저장
	 * @param PaOfferCode
	 * @return String
	 * @throws Exception
	 */
	public int updatePaWempOfferCodeList(PaOfferCode paWempOfferCode) throws Exception{
		return update("pawemp.common.updatePaWempOfferCodeList", paWempOfferCode);
	}
	
	/**
	 * 위메프 상품고시 저장
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int updatePaWempOfferCodeListUseYn(PaOfferCode paWempOfferCode) throws Exception{
		return update("pawemp.common.updatePaWempOfferCodeListUseYn", paWempOfferCode);
	}
	
	/**
	 * 위메프 상품고시 - 유형코드 조회
	 * @returnList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaOfferCode> selectPaOfferTypeList(PaOfferCode offerType) throws Exception {
	    return list("pawemp.common.selectPaOfferTypeList", offerType);
	}
	
	/**
	 * 위메프 상품고시 - 유형코드 tcode[O506] 삭제
	 * @param 
	 * @return String
	 * @throws Exception
	 */
	public int updatePaOfferType(PaOfferCode offerType) throws Exception{
		return update("pawemp.common.updatePaOfferType", offerType);
	}
	
	/**
	 * 위메프 상품고시 - 유형코드 tcode[O506] 저장
	 * @param PaOfferCode
	 * @return String
	 * @throws Exception
	 */
	public int insertPaOfferType(PaOfferCode offerType) throws Exception{
		return insert("pawemp.common.insertPaOfferType", offerType);
	}

	/**
	 * 위메프 브랜드 조회
	 * @param 
	 * @return PaBrand
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Brand> selectBrandList() throws Exception{
		return list("pawemp.common.selectBrandList", null);
	}
	
	public int insertPaWempBrandList(PaWempBrand paWempBrand) {
		return update("pawemp.common.insertPaWempBrandList", paWempBrand);
	}


	/**
	 * 위메프 제조사 조회
	 * @param 
	 * @return Makecomp
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Makecomp> selectMakerList() throws Exception{
		return list("pawemp.common.selectMakerList", null);
	}

	public int insertPaWempMakerList(PaWempMaker paWempMaker) {
		return update("pawemp.common.insertPaWempMakerList", paWempMaker);
	}
	
	/**
	 * 위메프 출고지등록 - 대상 목록 조회
	 * @param PaWempEntpSlip
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectEntpShipInsertList(PaWempEntpSlip paEntpSlip) throws Exception {
		return (HashMap<String, Object>) selectByPk("pawemp.common.selectEntpShipInsertList", paEntpSlip);
	}

	/**
	 * 위메프 출고지등록
	 * @param paEntpSlip
	 * @return
	 * @throws Exception
	 */
	public int insertPaWempEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		return insert("pawemp.common.insertPaWempEntpSlip", paEntpSlip);
	}
	
	/**
	 * 위메프 출고지수정 - 대상 목록 조회
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectEntpShipUpdateList(ParamMap paramMap) throws Exception {
		return list("pawemp.common.selectEntpShipUpdateList", paramMap.get());
	}

	// 배송정책 키로 조회하므로 단건 반환 (위메프연동구조개선)
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectEntpShipUpdate(PaWempEntpSlip paEntpSlip) throws Exception {
	    return (Map<String, Object>) selectByPk("pawemp.common.selectEntpShipUpdate", paEntpSlip);
	}
	
	/**
	 * 위메프 출고지수정 
	 * @param paEntpSlip
	 * @return
	 * @throws Exception
	 */
	public int updatePaWempEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		return update("pawemp.common.updatePaWempEntpSlip", paEntpSlip);
	}
	
	/**
	 * 배송비 수정
	 * @param PaWempEntpSlip
	 * @return
	 * @throws Exception
	 */
	public int updatePaWempCustShipCost(PaWempEntpSlip paEntpSlip) throws Exception {
		return update("pawemp.common.updatePaWempCustShipCost", paEntpSlip);
	}
	
	/**
	 * 제휴사 택배사명으로 택배사코드 조회
	 * @param paDelyGbName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectDelyGb(String paDelyGbName) throws Exception {
		return (HashMap<String, String>) selectByPk("pawemp.common.selectDelyGb", paDelyGbName);
	}
}
