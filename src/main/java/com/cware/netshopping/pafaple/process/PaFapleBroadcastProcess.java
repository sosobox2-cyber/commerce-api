package com.cware.netshopping.pafaple.process;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

public interface PaFapleBroadcastProcess {

	/**
	 * 대체편성 상품 설정
	 * @param ParamMap
	 * @return void
	 * @throws Exception
	 */
	public void refreshBroadReplaceGoods(ParamMap paramMap) throws Exception;
	
	/**
	 * 패션플러스 방송편성표 대상 조회
	 * @param ParamMap
	 * @return List<Pabroadsche>
	 * @throws Exception
	 */
	List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 대체편성상품 처리
	 * @param String
	 * @return List<Pabroadreplace>
	 * @throws Exception
	 */
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception;

	/**
	 * 패션플러스 대체편성상품 처리
	 * @param Pabroadsche
	 * @return String
	 * @throws Exception
	 */
	public String saveFapleBroadSche(Pabroadsche pabroadshce) throws Exception;
	
	/**
	 * 패션플러스 대체편성 TARGET UPDATE
	 * @param Pabroadreplace
	 * @return void
	 * @throws Exception
	 */
	public void updatePaBroadReplace(Pabroadreplace pabroadreplace) throws Exception;
	
	/**
	 * 패션플러스 방송편성표 RETURN
	 * @param ParamMap
	 * @return List<MultiframescheVO>
	 * @throws Exception
	 */
	public List<MultiframescheVO> selectFapleBroadcastScheList(ParamMap paramMap) throws Exception;

}
