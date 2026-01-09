package com.cware.netshopping.pawemp.broadcast.process;

import java.util.List;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

public interface PaWempBroadcastProcess {

	public void refreshBroadReplaceGoods(ParamMap paramMap) throws Exception;
	List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception;
	List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception;
	public String saveWempBroadSche(Pabroadsche pabroadshce) throws Exception;
	public void updatePaBroadReplace(Pabroadreplace pabroadreplace) throws Exception;
}
