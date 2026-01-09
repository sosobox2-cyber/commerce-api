package com.cware.netshopping.pagmkt.stockcheck.repository;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.PaGmktGoodsdtMappingVO;

@Service("pagmkt.stockcheck.PaGmktStockCheckDAO")
public class PaGmktStockCheckDAO extends AbstractPaDAO {

	/**
	 * G마켓 실시간 재고 체크 조회
	 * @param PaGmktGoodsdtMappingVO
	 * @return PaGmktGoodsdtMappingVO
	 * @throws Exception
	 */
	public PaGmktGoodsdtMappingVO selectStockCheck(PaGmktGoodsdtMappingVO paGmktGoodsdtMappingVO) throws Exception{
	    return (PaGmktGoodsdtMappingVO)selectByPk("pagmkt.stockcheck.selectStockCheck", paGmktGoodsdtMappingVO);
	}
    
}
