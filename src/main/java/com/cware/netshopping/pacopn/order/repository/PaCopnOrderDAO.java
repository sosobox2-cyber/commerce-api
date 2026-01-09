package com.cware.netshopping.pacopn.order.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.OrderpromoVO;

@Service("pacopn.order.paCopnOrderDAO")
public class PaCopnOrderDAO extends AbstractPaDAO {
	
	/**
	 * 주문생성 대상 조회
	 * @param paOrderCreateCnt
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderInputTargetList(int paOrderCreateCnt) throws Exception{
		return list("pacopn.order.selectOrderInputTargetList", paOrderCreateCnt);
	}
	
	/**
	 * 주문생성 대상 상세 조회
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderInputTarget) throws Exception{
		return list("pacopn.order.selectOrderInputTargetDtList", orderInputTarget);
	}
	
	/**
	 * 주문생성 결과처리 - 상품 준비중 취소 parameter 조회
	 * @param mappingSeq
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception{
		return (HashMap<String, String>) selectByPk("pacopn.order.selectRefusalInfo", mappingSeq);
	}
	
	/**
	 * 쿠팡 프로모션 조회 - 조회
	 * @param HashMap<String, String>
	 * @return OrderpromoVO
	 * @throws Exception
	 */
   public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception{
	   return (OrderpromoVO)(selectByPk("pacopn.order.selectOrderPromo", orderMap));
   }	
   
   /**
    * 쿠팡 제휴OUT프로모션 조회 - 조회
    * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
    * @param HashMap<String, String>
    * @return OrderpromoVO
    * @throws Exception
    */
   public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception{
	   return (OrderpromoVO)(selectByPk("pacopn.order.selectOrderPaPromo", orderMap));
   }	
}
