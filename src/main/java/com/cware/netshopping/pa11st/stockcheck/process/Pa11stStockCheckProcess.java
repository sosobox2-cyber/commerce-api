package com.cware.netshopping.pa11st.stockcheck.process;

import java.util.HashMap;
import java.util.List;

import com.cware.netshopping.domain.StockCheckVO;

public interface Pa11stStockCheckProcess {

    /**
     * 11번가 편성내용 조회
     * @param paramMap
     * @return List<MultiframescheVO>
     * @throws Exception
     */
    public List<StockCheckVO> selectStockCheck(List<HashMap<String, String>> paramMap) throws Exception;

    
    /**
     * 11번가 채널코드 검증
     * @param paramMap
     * @return List<MultiframescheVO>
     * @throws Exception
     */
    public String selectCheckOpenApiCode() throws Exception;
}
