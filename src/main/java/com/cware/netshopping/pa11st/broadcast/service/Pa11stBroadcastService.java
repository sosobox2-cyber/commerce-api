package com.cware.netshopping.pa11st.broadcast.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

public interface Pa11stBroadcastService {

	/**
	 * 11번가 편성내용 조회
	 * @param paramMap
	 * @return List<MultiframescheVO>
	 * @throws Exception
	 */
	List<MultiframescheVO> selectBroadcastScheListOld(ParamMap paramMap) throws Exception;
	
	
	public void refreshBroadReplaceGoodsTx(ParamMap paramMap) throws Exception;
	List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception;
	List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception;
	public String save11stBroadScheTx(Pabroadsche pabroadshce) throws Exception;
	public void updatePaBroadReplaceTx(Pabroadreplace pabroadreplace) throws Exception;
	List<MultiframescheVO> select11stBroadcastScheList(ParamMap paramMap) throws Exception;

}
