package com.cware.netshopping.pahalf.broadcast.process;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

public interface PaHalfBroadcastProcess {

	public List<MultiframescheVO> selectHalfBroadcastScheList(ParamMap paramMap) throws Exception;

	public void refreshBroadReplaceGoods(ParamMap paramMap) throws Exception;

	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception;

	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception;

	public String saveHalfBroadSche(Pabroadsche pabroadshce) throws Exception;

	public void updatePaBroadReplace(Pabroadreplace pabroadreplace) throws Exception;

}
