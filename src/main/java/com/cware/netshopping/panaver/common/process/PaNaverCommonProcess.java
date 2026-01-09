package com.cware.netshopping.panaver.common.process;

import java.util.List;

import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaNaverOrigin;
import com.cware.netshopping.domain.model.Panaverentpaddressmoment;
import com.cware.netshopping.domain.model.Panavergoodskinds;
import com.cware.netshopping.domain.model.Panaverkindscerti;
import com.cware.netshopping.domain.model.Panaverkindscertikinds;
import com.cware.netshopping.domain.model.Panaverkindsexcept;

public interface PaNaverCommonProcess {

	/**
	 * 제휴 네이버 - 전체 원산지 정보 저장
	 * @param List<PaNaverOrigin>
	 * @return String
	 * @throws Exception
	 */
	public String savePaNaverOriginList(List<PaNaverOrigin> paNaverOriginList) throws Exception;
	
	/**
	 * 제휴 네이버 - 원산지매핑
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveMappingPaNaverOrigin(List<PaNaverOrigin> paNaverOriginList) throws Exception;
	
	/**
	 * 제휴 네이버 - 전체카테고리저장
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePaNaverGoodsKinds(List<Panavergoodskinds> paNaverGoodsKindsList, List<PaGoodsKinds> paGoodsKindsList) throws Exception;

	/**
	 * 제휴 네이버 - 카테고리 예외정보 저장
	 * @param List<Panaverkindsexcept>
	 * @return String
	 * @throws Exception
	 */
	public String savePaNaverCategoryInfo(List<Panaverkindsexcept> paNaverKindsExceptList, List<Panavergoodskinds> paNaverGoodsKindsList, List<Panaverkindscerti> paNaverKindsCertiList, List<Panaverkindscertikinds> paNaverKindsCertiKindsList)throws Exception;

	/**
	 * 최하위 카테고리 조회
	 * @param paNaverGoodsKindsList
	 * @return
	 * @throws Exception
	 */
	public List<Panavergoodskinds> selectPaNaverCategoryInfo() throws Exception;
	
	public String savePaNaverAddressList(List<Panaverentpaddressmoment> paNaverEntpAddressList) throws Exception;
	
}
