package com.cware.netshopping.pawemp.broadcast.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

public interface PaWempBroadcastService {

	public void refreshBroadReplaceGoodsTx(ParamMap paramMap) throws Exception;
	List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception;
	List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception;
	public String saveWempBroadScheTx(Pabroadsche pabroadshce) throws Exception;
	public void updatePaBroadReplaceTx(Pabroadreplace pabroadreplace) throws Exception;
}
