package com.cware.netshopping.palton.stockcheck.repository;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;

@Service("palton.stockcheck.PaLtonStockCheckDAO")
public class PaLtonStockCheckDAO extends AbstractPaDAO {

	/**
	 * 롯데온 실시간 재고 체크 조회
	 * @param PaLtonGoodsdtMappingVO
	 * @return PaLtonGoodsdtMappingVO
	 * @throws Exception
	 */
	public PaLtonGoodsdtMappingVO selectStockCheck(PaLtonGoodsdtMappingVO paLtonGoodsdtMappingVO) throws Exception{
	    return (PaLtonGoodsdtMappingVO)selectByPk("palton.stockcheck.selectStockCheck", paLtonGoodsdtMappingVO);
	}
    
}
