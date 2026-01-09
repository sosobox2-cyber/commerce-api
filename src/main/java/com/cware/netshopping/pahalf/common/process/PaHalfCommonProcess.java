package com.cware.netshopping.pahalf.common.process;

import java.util.HashMap;
import java.util.List;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaHalfShipInfoVO;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaHalfBrand;
import com.cware.netshopping.domain.model.PaOfferCode;


public interface PaHalfCommonProcess {

	/**
	 * 하프클럽 브랜드 조회API 결과 저장
	 * @param paHalfBrandList
	 * @return
	 * @throws Exception
	 */

	public String savePaHalfBrand(List<PaHalfBrand> paHalfBrandList) throws Exception;

	/**
	 * 하프클럽  배송비 템플릿 정보 Setting
	 * @param paBrandList
	 * @return
	 * @throws Exception
	 */
	public void setPaShipCostInfo(PaHalfShipInfoVO shipInfo, ParamMap apiDataMap) throws Exception;
	

	
	/**
	 * 하프클럽 정보고시 조회 결과 저장
	 * @param paOfferCodeList
	 * @return
	 * @throws Exception 
	 */
	public String savePaHalfOfferCode(List<PaOfferCode> paOfferCodeList) throws Exception;
	
	/**
	 * 하프클럽 표준아이템(카테고리) 조회API 결과 저장
	 * @param stdItemList
	 * @return
	 * @throws Exception
	 */
	public String saveStdItemList(List<PaGoodsKinds> stdItemList) throws Exception;

	/**
	 * 배송템플릿 등록
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int insertTpaHalfShipInfo(ParamMap paramMap) throws Exception;

	/**
	 * 배송템플릿 등록 - 제휴사 배송비 정책 코드 등록
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateTpaHalfShipInfo(PaHalfShipInfoVO halfShipCostInfo) throws Exception;
	/**
	 * 배송템플릿 등록 - 배송탬플릿 데이터 조회 (DT)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public PaHalfShipInfoVO selectHalfShipCostInfo(ParamMap paramMap) throws Exception;
	
	/**
	 * 배송템플릿 수정 - 하프클럽 배송비 템플릿(주소, 회수지) 수정 리스트 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectPaHalfSlipInfoList(ParamMap paramMap) throws Exception;
	/**
	 * 배송템플릿 등록 - 제휴사 배송비 정책 코드 등록 Count
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectPaShipCostCode(ParamMap paramMap)  throws Exception;


	/**
	 * 브랜드 매핑
	 * @throws Exception
	 */
	public void insertPaHalfBrandMapping() throws Exception;

	/**
	 * 하프클럽 브랜드 필터 상품 리스트 조회
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectBrandFilterGoods() throws Exception;

	/**
     * 하프클럽 신규 브랜드 상품 tpagoodstarget insert
	 * @param brandFilterGoodsList
	 * @throws Exception
	 */
	public void savePaHalfGoodsTarget(List<HashMap<String, Object>> brandFilterGoodsList)  throws Exception;


	
}
