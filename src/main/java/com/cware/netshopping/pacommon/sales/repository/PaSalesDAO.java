package com.cware.netshopping.pacommon.sales.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PasalescompareVO;

@Service("pacommon.sales.paSalesDAO")
public class PaSalesDAO extends AbstractDAO {
	/**
	 * 정산 일 대사 - [BO기준] 대사데이터 조회
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PasalescompareVO> selectPaCompareBoSales(ParamMap paramMap) throws Exception {
		return (List<PasalescompareVO>)list("pacommon.pasales.selectPaCompareBoSales", paramMap.get());
	}
	
	/**
	 * 정산 일 대사 - 대사테이블(TPASALESCOMPARE)의 Sequence 채번
	 * @return String
	 * @throws Exception
	 */
	public String selectTpasalesCompareSeq() throws Exception {
		return (String)selectByPk("pacommon.pasales.selectTpasalesCompareSeq", null);
	}
	
	/**
	 * 정산 일 대사 - 대사테이블(TPASALESCOMPARE) Insert
	 * @return int
	 * @throws Exception
	 */
	public int insertTpaSalesCompare(PasalescompareVO paCompare) throws Exception {
		return insert("pacommon.pasales.insertTpaSalesCompare", paCompare);
	}
	
	/**
	 * 정산 일 대사 - TPASALESCOMPARE에 이전 대사데이터 있는지 확인
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PasalescompareVO> selectExistTpasalecompare(PasalescompareVO paCompare) throws Exception {
		return (List<PasalescompareVO>)list("pacommon.pasales.selectExistTpasalecompare", paCompare);
	}
	
	/**
	 * 정산 일 대사 - 매출일자 다른 기존 대사데이터 삭제
	 * @return ParamMap
	 * @throws Exception
	 */
	public int deleteDiffPaCompareList(PasalescompareVO paCompare) throws Exception {
		return delete("pacommon.pasales.deleteDiffPaCompareList", paCompare);
	}
	
	/**
	 * 정산 일 대사 - 같은 매출일자에 주문출고, 반품 모두 있어 매칭이 안되는 경우, E로 추가된 Row 삭제
	 * @return ParamMap
	 * @throws Exception
	 */
	public int deleteTpasalescompareList(ParamMap paramMap) throws Exception {
		return delete("pacommon.pasales.deleteTpasalescompareList", paramMap.get());
	}
	
	/**
	 * 정산 일 대사 - 같은 매출일자 중복되는 Rows 제거
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PasalescompareVO> selectPaDupliCheck(ParamMap paramMap) throws Exception {
		return (List<PasalescompareVO>)list("pacommon.pasales.selectPaDupliCheck", paramMap.get());
	}
	
	/**
	 * 정산 일 대사 - 같은 매출일자 중복되는 Rows 제거
	 * @return ParamMap
	 * @throws Exception
	 */
	public int deleteTpasalesDuplList(PasalescompareVO paCompare) throws Exception {
		return delete("pacommon.pasales.deleteTpasalesDuplList", paCompare);
	}
	
	/**
	 * 정산 일 대사 - TPASALESCOMPARE에 이전 대사데이터 있는지 확인
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PasalescompareVO> selectPaExistTpasalecompare(PasalescompareVO paCompare) throws Exception {
		return (List<PasalescompareVO>)list("pacommon.pasales.selectPaExistTpasalecompare", paCompare);
	}
}