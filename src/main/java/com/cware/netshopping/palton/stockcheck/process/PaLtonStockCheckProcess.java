package com.cware.netshopping.palton.stockcheck.process;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;

public interface PaLtonStockCheckProcess {

	/**
	 * 롯데온 실시간 재고 체크 조회
	 * @param PaLtonGoodsdtMappingVO
	 * @return PaLtonGoodsdtMappingVO
	 * @throws Exception
	 */
	public PaLtonGoodsdtMappingVO selectStockCheck(PaLtonGoodsdtMappingVO paLtonGoodsdtMappingVO) throws Exception;
    
}
