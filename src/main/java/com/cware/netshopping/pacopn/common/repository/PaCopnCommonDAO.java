package com.cware.netshopping.pacopn.common.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.model.PaCopnCertification;
import com.cware.netshopping.domain.model.PaCopnDocment;
import com.cware.netshopping.domain.model.PaCopnLmsdKeyOption;
import com.cware.netshopping.domain.model.PaCopnOption;
import com.cware.netshopping.domain.model.PaEntpSlip;

@Service("pacopn.common.paCopnCommonDAO")
public class PaCopnCommonDAO extends AbstractPaDAO{

	/**
	 * 카테코리 조회 (임시)
	 * @return List<Brand>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public  List<PaCopnLmsdKeyOption>  selectcategoryList()  throws Exception {
		return  list("pacopn.common.selectcategoryList", null);
	}

	/**
	 * 카테코리 옵션정보 등록 (임시)
	 * @return List<Brand>
	 * @throws Exception
	 */
	public int insertPaCopnOption(PaCopnOption opt)  throws Exception {
		return insert("pacopn.common.insertPaCopnOption", opt);
	}

	public int insertPaCopnDocument(PaCopnDocment doc) throws Exception {
		return insert("pacopn.common.insertPaCopnDocument", doc);
	}

	public int insertPaCopnCertification(PaCopnCertification certi) throws Exception{
		return insert("pacopn.common.insertPaCopnCertification", certi);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception{
		return (HashMap<String, String>) selectByPk("pacopn.common.selectEntpShipInsertList", paEntpSlip);
	}

	public int insertPaCopnEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return insert("pacopn.common.insertPaCopnEntpSlip", paEntpSlip);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectEntpShipUpdateList(String paAddrGb) throws Exception{
		return list("pacopn.common.selectEntpShipUpdateList", paAddrGb);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> selectEntpShipUpdate(PaEntpSlip paEntpSlip) throws Exception{
		return (Map<String, String>)selectByPk("pacopn.common.selectEntpShipUpdate", paEntpSlip);
	}
	
	public int updatePaCopnEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return update("pacopn.common.updatePaCopnEntpSlip", paEntpSlip);
	}
}
