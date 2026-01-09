package com.cware.netshopping.pahalf.broadcast.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

public interface PaHalfBroadcastService {

	public void refreshBroadReplaceGoodsTx(ParamMap paramMap) throws Exception;

	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception;

	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception;

	public String saveHalfBroadScheTx(Pabroadsche pabroadshce) throws Exception;

	public void updatePaBroadReplaceTx(Pabroadreplace pabroadreplace) throws Exception;

	public List<MultiframescheVO> selectHalfBroadcastScheList(ParamMap paramMap) throws Exception;

 
}
