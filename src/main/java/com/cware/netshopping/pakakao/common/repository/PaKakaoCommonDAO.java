package com.cware.netshopping.pakakao.common.repository;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.Makecomp;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaMaker;
import com.cware.netshopping.domain.model.PaOrigin;


@Service("pakakao.common.paKakaoCommonDAO")
public class PaKakaoCommonDAO extends AbstractPaDAO {

	public int deletePaGoodsKindsMomentList(PaGoodsKinds paGoodsKinds) {
		return delete("pakakao.common.deletePaGoodsKindsMomentList", paGoodsKinds);
	}
	
	public int insertPaGoodsKindMomentsList(PaGoodsKinds paGoodsKinds) {
		return insert("pakakao.common.insertPaGoodsKindMomentsList", paGoodsKinds);
	}
	
	public int insertPaGoodsKindsList(PaGoodsKinds paGoodsKinds) {
		return insert("pakakao.common.insertPaGoodsKindsList", paGoodsKinds);
	}
	
	public int insertPaOrigin(PaOrigin paOrigin) throws Exception{
		return insert("pakakao.common.insertPaOrigin", paOrigin);
	}
	
	@SuppressWarnings("unchecked")
	public List<Brand> selectBrandList() throws Exception{
		return list("pakakao.common.selectBrandList", null);
	}
	
	public int countPaBrand(PaBrand paBrand) throws Exception {
		return (Integer) selectByPk("pakakao.common.countPaBrand", paBrand);
	}
	
	public int insertPaBrand(PaBrand paBrand) throws Exception{
		return insert("pakakao.common.insertPaBrand", paBrand);
	}
	
	@SuppressWarnings("unchecked")
	public List<Makecomp> selectMakerList() throws Exception{
		return list("pakakao.common.selectMakerList", null);
	}
	
	public int countPaMaker(PaMaker paMaker) throws Exception {
		return (Integer) selectByPk("pakakao.common.countPaMaker", paMaker);
	}
	
	public int insertPaMaker(PaMaker paMaker) throws Exception{
		return insert("pakakao.common.insertPaMaker", paMaker);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectPaKakaoEntpSlip(ParamMap paramMap) {
		return list("pakakao.common.selectPaKakaoEntpSlip", paramMap.get());
	}

	public int insertPaEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return insert("pakakao.common.insertPaEntpSlip", paEntpSlip);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectPaKakaoEntpSlipUpdate(ParamMap paramMap) {
		return list("pakakao.common.selectPaKakaoEntpSlipUpdate", paramMap.get());
	}
	
	public int updatePaEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return update("pakakao.common.updatePaEntpSlip", paEntpSlip);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectPaKakaoModifyEntpSlip(ParamMap paramMap) {
		return list("pakakao.common.selectPaKakaoModifyEntpSlip", paramMap.get());
	}

	// 업체주소 키로 조회하므로 단건 반환 (카카오연동구조개선)
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectEntpSlipInsert(PaEntpSlip paEntpSlip) throws Exception {
	    return (Map<String, Object>) selectByPk("pakakao.common.selectEntpSlipInsert", paEntpSlip);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> selectEntpSlipUpdate(PaEntpSlip paEntpSlip) throws Exception {
	    return (Map<String, Object>) selectByPk("pakakao.common.selectEntpSlipUpdate", paEntpSlip);
	}
}
