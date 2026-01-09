package com.cware.netshopping.pa11st.stockcheck.repository;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.StockCheckVO;

@Service("pa11st.stock.pa11stStockCheckDAO")
public class Pa11stStockCheckDAO extends AbstractPaDAO {

	/**
	 * 11번가 편성내용 조회
	 * @param paramMap
	 * @return List<MultiframescheVO>
	 * @throws Exception
	 */
	public StockCheckVO selectStockCheck(HashMap<String, String> paramMap) throws Exception{
		return (StockCheckVO)selectByPk("pa11st.stockCheck.selectStockCheck", paramMap);
	}
	
	/**
	 * 11번가 채널코드 검증
	 * @param paramMap
	 * @return List<MultiframescheVO>
	 * @throws Exception
	 */
	public String selectCheckOpenApiCode() throws Exception{
		return (String)selectByPk("pa11st.stockCheck.selectCheckOpenApiCode","");
	}

}
