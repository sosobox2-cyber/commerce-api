package com.cware.netshopping.pagmkt.broadcast.service;

import java.util.List;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

public interface PaGmktBroadcastService {
	
	public void refreshBroadReplaceGoodsTx(ParamMap paramMap) throws Exception;	
	List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception;
	List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception;
	public List<Object> selectBraocastV2GoodsList(ParamMap paramMap) throws Exception;	
	public int deleteGmktBroadSche(Pabroadsche pabroadshce)throws Exception;
	public int insertGmktBroadSche(Pabroadsche pabroadshce)throws Exception;
	public int updateGmktBroadSche(Pabroadsche pabroadshce)throws Exception;	
	public void updateGmktBroadReplaceTx(Pabroadreplace pabroadreplace) throws Exception;

}
