package com.cware.netshopping.pa11st.stockcheck.service;

import java.util.HashMap;
import java.util.List;

import com.cware.netshopping.domain.StockCheckVO;

public interface Pa11stStockCheckService {

    /**
     * 11번가 실시간 재고 체크
     * @param HashMap<String, Object>
     * @return ParamMap
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
