package com.cware.netshopping.pacopn.stockcheck.repository;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;

@Service("pacopn.stockcheck.paCopnStockCheckDAO")
public class PaCopnStockCheckDAO extends AbstractPaDAO {

	/**
	 * 쿠팡 주문 가능 상품 정보 조회
	 * @param paramMap
	 * @return List<MultiframescheVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectOrderableGoods(ParamMap paramMap) throws Exception{
		return (HashMap<String, Object>) selectByPk("pacopn.stockcheck.selectOrderableGoods", paramMap.get());
	}
	
}
