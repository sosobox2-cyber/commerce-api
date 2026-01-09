package com.cware.netshopping.panaver.common.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.PanaverentpaddressmomentVO;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaNaverOrigin;
import com.cware.netshopping.domain.model.Panaverentpaddressmoment;
import com.cware.netshopping.domain.model.Panavergoodskinds;
import com.cware.netshopping.domain.model.Panaverkindscerti;
import com.cware.netshopping.domain.model.Panaverkindscertikinds;
import com.cware.netshopping.domain.model.Panaverkindsexcept;

@Service("panaver.common.paNaverCommonDAO")
public class PaNaverCommonDAO extends AbstractPaDAO {

	/**
	 * 제휴 네이버 - 원산지 삭제
	 * @param PaEntpSlip
	 * @return int
	 * @throws Exception
	 */
	public int deletePaNaverOriginList() throws Exception{
		return delete("panaver.common.deletePaNaverOriginList", null);
	}
	
	/**
	 * 제휴 네이버 - 원산지 저장
	 * @param PaEntpSlip
	 * @return int
	 * @throws Exception
	 */
	public int insertPaNaverOriginList(PaNaverOrigin paNaverOrigin) throws Exception{
		return insert("panaver.common.insertPaNaverOriginList", paNaverOrigin);
	}
	
	/**
	 * 제휴 네이버 - 원산지매핑저장
	 * @param PaEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public int insertPaNaverOriginMapping(PaNaverOrigin paNaverOrigin) throws Exception{
		return insert("panaver.common.insertPaNaverOriginMapping", paNaverOrigin);
	}
	
	//19.09.18 카테고리
	
	/**
	 * 제휴 네이버 - 전체카테고리삭제
	 * @param PaGoodsKinds
	 * @return Integer
	 * @throws Exception
	 */
	public Integer deletePaNaverGoodsKindsList() throws Exception {
		return update("panaver.common.deletePaNaverGoodsKindsList", null);
	}
	
	/**
	 * 제휴 네이버 - 전체카테고리저장
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPaNaverGoodsKindsList(Panavergoodskinds paNaverGoodsKinds) throws Exception{
		return update("panaver.common.insertPaNaverGoodsKindsList", paNaverGoodsKinds);
	}
	
	/**
	 * 제휴 네이버 - 전체카테고리변경저장
	 * @param PaGoodsKinds
	 * @return Integer
	 * @throws Exception
	 */
	public Integer updatePaGoodsKindsList(PaGoodsKinds paGoodsKinds) throws Exception {
		return update("panaver.common.updatePaGoodsKindsList", paGoodsKinds);
	}
	
	/**
	 * 제휴 네이버 - 사용하지 않는 카테고리 use_yn = 0 처리.
	 * @param PaGoodsKinds
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGoodsKindsUnUseListUseYn(PaGoodsKinds paGoodsKinds) throws Exception{
		return update("panaver.common.updatePaGoodsKindsUnUseListUseYn", paGoodsKinds);
	}

	/**
	 * 변경 or 저장
	 * @param paGoodsKinds
	 * @return Integer
	 * @throws Exception
	 */
	public int mergePaGoodsKindsList(PaGoodsKinds paGoodsKinds) throws Exception{
		return update("panaver.common.mergePaGoodsKindsList", paGoodsKinds);
	}
	
	/**
	 * 제휴 네이버 - 카테고리 예외정보 삭제
	 * @param PaEntpSlip
	 * @return int
	 * @throws Exception
	 */
	public int deletePaNaverCategoryInfo() throws Exception{
		return delete("panaver.common.deletePaNaverCategoryInfo", null);
	}
	
	/**
	 * 최하위 카테고리 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Panavergoodskinds> selectPaNaverCategoryInfo() throws Exception{
		return (List<Panavergoodskinds>) list("panaver.common.selectPaNaverCategoryInfo", null);
	}
	
	/**
	 * 제휴 네이버 - 카테고리 상세 정보 저장
	 * @param Panaverkindsexcept
	 * @return String
	 * @throws Exception
	 */
	
	public int checkPaNaverCategoryInfo(Panaverkindsexcept paNaverKindsExcept) throws Exception{
		return (Integer) selectByPk("panaver.common.checkPaNaverCategoryInfo", paNaverKindsExcept);
	}
	
	public int insertPaNaverCategoryInfoList(Panaverkindsexcept paNaverKindsExcept) throws Exception{
		return update("panaver.common.insertPaNaverCategoryInfo", paNaverKindsExcept);
	}

	public int updatePaNaverCategoryInfoYN(Panavergoodskinds paNaverGoodsKinds) throws Exception{
		return update("panaver.common.updatePaNaverCategoryInfoYN", paNaverGoodsKinds);
	}

	/**
	 * 제휴 네이버 - 네이버 상품분류 인증정보 저장
	 * @param paNaverKindsCerti
	 * @return 
	 * @throws Exception
	 */
	public int checkPaNaverKindsCerti(Panaverkindscerti paNaverKindsCerti) throws Exception{
		return (Integer) selectByPk("panaver.common.checkPaNaverKindsCerti", paNaverKindsCerti);
	}
	
	public int insertPaNaverKindsCertiList(Panaverkindscerti paNaverKindsCerti) throws Exception{
		return insert("panaver.common.insertPaNaverKindsCerti", paNaverKindsCerti);
	}

	/**
	 * 제휴 네이버 - 네이버 상품분류 인증정보 종류
	 * @param paNaverKindsCertiKinds
	 * @return 
	 * @throws Exception
	 */
	public int checkPaNaverKindsCertiKindsList(Panaverkindscertikinds paNaverKindsCertiKinds) throws Exception{
		return (Integer) selectByPk("panaver.common.checkPaNaverKindsCertiKindsList", paNaverKindsCertiKinds);
	}
	
	public int insertPaNaverKindsCertiKindsList(Panaverkindscertikinds paNaverKindsCertiKinds) throws Exception{
		return insert("panaver.common.insertPaNaverKindsCertiKinds", paNaverKindsCertiKinds);
	}

	public int insertPaNaverEntpAddressMomentList(Panaverentpaddressmoment paNaverEntpAddressMoment) throws Exception{
		return insert("panaver.common.insertPaNaverEntpAddressMomentList", paNaverEntpAddressMoment);
	}

	@SuppressWarnings("unchecked")
	public List<PanaverentpaddressmomentVO> selectInsertOrUpdate() throws Exception{
		return (List<PanaverentpaddressmomentVO>) list("panaver.common.selectInsertOrUpdate", null);
	}

	public int insertPaNaverEntpAddressList(PanaverentpaddressmomentVO panaverentpaddressmomentVO) throws Exception{
		return insert("panaver.common.insertPaNaverEntpAddressList", panaverentpaddressmomentVO);
	}
	
	public int updatePaNaverEntpAddressList(PanaverentpaddressmomentVO panaverentpaddressmomentVO) throws Exception{
		return update("panaver.common.updatePaNaverEntpAddressList", panaverentpaddressmomentVO);
	}

	public int insertTpaEntpSlip() throws Exception{
		return insert("panaver.common.insertTpaEntpSlip", null);
	}

	public int deleteTpaEntpSlip() throws Exception{
		return delete("panaver.common.deleteTpaEntpSlip", null);
	}
	
	public int updateTpaEntpSlipChange(PanaverentpaddressmomentVO panaverentpaddressmomentVO) throws Exception{
		return update("panaver.common.updateTpaEntpSlipChange", panaverentpaddressmomentVO);
	}
}
