package com.cware.netshopping.pagmkt.stockcheck.process;

import com.cware.netshopping.domain.PaGmktGoodsdtMappingVO;

public interface PaGmktStockCheckProcess {

	/**
	 * G마켓 실시간 재고 체크 조회
	 * @param PaGmktGoodsdtMappingVO
	 * @return PaGmktGoodsdtMappingVO
	 * @throws Exception
	 */
	public PaGmktGoodsdtMappingVO selectStockCheck(PaGmktGoodsdtMappingVO paGmktGoodsdtMappingVO) throws Exception;
    
}
