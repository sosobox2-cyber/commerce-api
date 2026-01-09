package com.cware.netshopping.pawemp.common.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.Makecomp;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaWempBrand;
import com.cware.netshopping.domain.model.PaWempEntpSlip;
import com.cware.netshopping.domain.model.PaWempMaker;

public interface PaWempCommonService {
	
	/**
	 * 위메프 전체 카테고리 저장
	 * @param paGoodsKindsList
	 * @return
	 * @throws Exception
	 */
	public String savePaWempGoodsKindsTx(List<PaGoodsKinds> paGoodsKindsList) throws Exception;
	
	/**
	 * 위메프 정보고시 저장
	 * @param paOfferCodeList
	 * @return
	 * @throws Exception
	 */
	public String savePaWempOfferCodeTx(List<PaOfferCode> paOfferCodeList) throws Exception;

	/**
	 * 위메프 브랜드 조회
	 * @param 
	 * @return PaBrand
	 * @throws Exception
	 */
	public List<Brand> selectBrandList() throws Exception;

	/**
	 * 위메프 브랜드 저장
	 * @param paWempBrandList
	 * @return
	 * @throws Exception
	 */
	public String savePaWempBrandTx(List<PaWempBrand> paWempBrandList) throws Exception;

	/**
	 * 위메프 제조사 조회
	 * @param 
	 * @return Makecomp
	 * @throws Exception
	 */
	public List<Makecomp> selectMakerList() throws Exception;

	/**
	 * 위메프 제조사 저장
	 * @param PaWempMakerList
	 * @return
	 * @throws Exception
	 */
	public String savePaWempMakerTx(List<PaWempMaker> paWempMakerList) throws Exception;
	
	/**
	 * 출고지 상세정보 조회
	 * 
	 * @param PaEntpSlip
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String, Object> selectEntpShipInsertList(PaWempEntpSlip paEntpSlip) throws Exception;

	/**
	 * 출고지 전송결과 저장
	 * 
	 * @param PaWempEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public String savePaWempEntpSlipTx(PaWempEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * 출고지 수정대상 리스트 조회
	 * 
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<Object> selectEntpShipUpdateList(ParamMap paramMap) throws Exception;

	/**
	 * 출고지 수정대상 전송결과 저장
	 * 
	 * @param PaWempEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public String updatePaWempEntpSlipTx(PaWempEntpSlip paEntpSlip) throws Exception;
}
