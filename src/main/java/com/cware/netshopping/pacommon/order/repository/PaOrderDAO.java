package com.cware.netshopping.pacommon.order.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.OrderInfoMsg;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.CanceldtVO;
import com.cware.netshopping.domain.ClaimdtVO;
import com.cware.netshopping.domain.OrderStockInVO;
import com.cware.netshopping.domain.OrderdtVO;
import com.cware.netshopping.domain.OrdergoodsVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.OrdershipcostVO;
import com.cware.netshopping.domain.OrderstockVO;
import com.cware.netshopping.domain.PromocounselVO;
import com.cware.netshopping.domain.model.Couponissue;
import com.cware.netshopping.domain.model.Custcounseldt;
import com.cware.netshopping.domain.model.Custcounselm;
import com.cware.netshopping.domain.model.Customer;
import com.cware.netshopping.domain.model.Custspinfo;
import com.cware.netshopping.domain.model.Custsystem;
import com.cware.netshopping.domain.model.Custtel;
import com.cware.netshopping.domain.model.Ordercostapply;
import com.cware.netshopping.domain.model.Orderdt;
import com.cware.netshopping.domain.model.Ordergoods;
import com.cware.netshopping.domain.model.Orderm;
import com.cware.netshopping.domain.model.Orderpromo;
import com.cware.netshopping.domain.model.Orderreceipts;
import com.cware.netshopping.domain.model.Ordershipcost;
import com.cware.netshopping.domain.model.Orderstock;
import com.cware.netshopping.domain.model.PaMonitering;
import com.cware.netshopping.domain.model.PaOrderShipCost;
import com.cware.netshopping.domain.model.PaPersonalInfoNoticeSms;
import com.cware.netshopping.domain.model.PaSlipInfo;
import com.cware.netshopping.domain.model.Promocounsel;
import com.cware.netshopping.domain.model.Receiver;


@Service("pacommon.order.paorderDAO")
public class PaOrderDAO extends AbstractPaDAO{
	
	public String selectSequenceNoCondition(HashMap<?, ?> hashMap) throws Exception {
		return (String) selectByPk("pacommon.paorder.selectSequenceNoCondition", hashMap);
	}	

	public String selectSequenceNo(HashMap<?, ?> hashMap) throws Exception {
		return (String) selectByPk("pacommon.paorder.selectSequenceNo",hashMap);
	}
	
		
	
	/**
	 * 고객저장  - insert TCUSTOMER
	 * @param Customer 
	 * @return int
	 * @throws Exception
	 */
	public int insertCustomer(Customer customer) throws Exception{
		return insert("pacommon.paorder.insertTCustomer", customer);
	}
	
	/**
	 * 고객저장  - insert TCUSTSYSTEM
	 * @param Custsystem 
	 * @return int
	 * @throws Exception
	 */
	public int insertCustsystem(Custsystem custsystem) throws Exception{
		return (Integer)insert("pacommon.paorder.insertCustsystem", custsystem);
	}
	
	/**
	 * 고객저장  - insert TCUSTTEL
	 * @param Custtel
	 * @return int
	 * @throws Exception
	 */
	public int insertCusttel(Custtel custtel) throws Exception{
		return (Integer)insert("pacommon.paorder.insertCusttel", custtel);
	}
	
	/**
	 * 고객저장  - insert TCUSTSPINFO
	 * @param Custspinfo
	 * @return int
	 * @throws Exception
	 */
	public int insertCustspinfo(Custspinfo custspinfo) throws Exception{
		return (Integer)insert("pacommon.paorder.insertCustspinfo", custspinfo);
	}
	
	/**
	 * 고객저장  - insert TRECEIVER
	 * @param Receiver
	 * @return int
	 * @throws Exception
	 */
	public int insertReceiver(Receiver receiver) throws Exception{
		return (Integer)insert("pacommon.paorder.insertReceiver", receiver);
	}
	
	/**
	 * system 시간조회
	 * @param null
	 * @return String
	 * @throws Exception
	 */
	public String getSysdatetime() throws Exception{
        return (String)selectByPk("pacommon.paorder.getSysdatetime", null);
    }
	
	/**
	 * 상품매핑 코드 및 상품정보 조회
	 * @param ParamMap
	 * @return OrderInfoMsg
	 * @throws Exception
	 */
	public OrderInfoMsg selectGoodsInfo(ParamMap paramMap) throws Exception{
		return (OrderInfoMsg)selectByPk("pacommon.paorder.selectGoodsInfo", paramMap.get());
	}
	
	/**
	 * TORDERM INSERT
	 * @param Orderm
	 * @return
	 * @throws Exception
	 */
	public int insertOrderm(Orderm orderm) throws Exception{
        return  insert("pacommon.paorder.insertOrderm",orderm);
    }
	
	/**
	 * TORDERGOODS INSERT
	 * @param Ordergoods
	 * @return
	 * @throws Exception
	 */
	public int insertOrdergoods(Ordergoods ordergoods) throws Exception{
        return  insert("pacommon.paorder.insertOrdergoods",ordergoods);
    }
	
	/**
	 * TORDERDT INSERT
	 * @param Orderdt
	 * @return
	 * @throws Exception
	 */
	public int insertOrderdt(Orderdt orderdt) throws Exception{
        return  insert("pacommon.paorder.insertOrderdt",orderdt);
    }
	
	/**
	 * TORDERSHIPCOST INSERT
	 * @param Ordershipcost
	 * @return
	 * @throws Exception
	 */
	public int insertOrdershipcost(Ordershipcost ordershipcost) throws Exception{
        return  insert("pacommon.paorder.insertOrdershipcost",ordershipcost);
    }
	
	/**
	 * TORDERCOSTAPPLY INSERT
	 * @param OrdercostApply
	 * @return
	 * @throws Exception
	 */
	public int insertOrdercostapply(Ordercostapply ordercostapply) throws Exception{
        return  insert("pacommon.paorder.insertOrdercostapply",ordercostapply);
    }
	
	
	public int updateOrdercostapply(Ordercostapply ordercostapply) throws Exception{
        return  update("pacommon.paorder.updateOrdercostapply",ordercostapply);
    }
	
	/**
	 * TORDERPROMO INSERT
	 * @param Orderpromo
	 * @return
	 * @throws Exception
	 */
	public int insertOrderpromo(Orderpromo orderpromo) throws Exception{
        return  insert("pacommon.paorder.insertOrderpromo",orderpromo);
    }
	
	/**
	 * TORDERRECEIPT INSERT
	 * @param Orderreceipts
	 * @return
	 * @throws Exception
	 */
	public int insertOrderreceipts(Orderreceipts orderreceipts) throws Exception{
        return  insert("pacommon.paorder.insertOrderreceipts",orderreceipts);
    }
	
	
	public int insertPaOrderShipCost(PaOrderShipCost paordershipcost) throws Exception{
		return  insert("pacommon.paorder.insertTpaOrderShipCost",paordershipcost);
	}
	
	/**
	 * TORDERSTOCK UPDATE
	 * @param Orderstock
	 * @return
	 * @throws Exception
	 */
	public int updateOrderStockEnd(Orderstock stock) throws Exception{
        return  update("pacommon.paorder.updateOrderStockEnd",stock);
    }
	
	/**
	 * FUN_GET_ORDER_ABLE_QTY_PA 조회
	 *   > 주문, 교환 처리시 주문가능 수량 재조회.
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectOrderAbleQtyPa(ParamMap stockMap) throws Exception{
		return (Integer) selectByPk("pacommon.paorder.selectOrderAbleQtyPa", stockMap.get());
	}
	
	/**
	 * 재고 체크시 필요한 상품 정보
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public HashMap selectGoodsForStock(ParamMap paramMap) throws Exception {
		return (HashMap)selectByPk("pacommon.paorder.selectGoodsForStock", paramMap.get());
	}
	
	/**
	 * 창고코드
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<String, Object> selectGoodsCheckDelyGb(ParamMap paramMap) {
		return (HashMap)selectByPk("pacommon.paorder.selectGoodsCheckDelyGb",paramMap.get());
	}
	
	@SuppressWarnings({ "unchecked"})
	public List<Object> selectOrderTagetDt(String paOrderNo) throws Exception {
		return list("pacommon.paorder.selectOrderTagetDt",paOrderNo);
	}
	
		
	/**
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public long selectCurCounselQtyA(ParamMap paramMap) throws Exception{
		return (Long)selectByPk("pacommon.paorder.selectCurCounselQtyA", paramMap.get());
	}
	
	/**
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public long selectCurCounselQtyB(ParamMap paramMap) throws Exception{
		return (Long)selectByPk("pacommon.paorder.selectCurCounselQtyB", paramMap.get());
	}
	
	/**
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public long selectCurCounselQtyC(ParamMap paramMap) throws Exception{
		return (Long)selectByPk("pacommon.paorder.selectCurCounselQtyC", paramMap.get());
	}

	/**
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public long selectCurCounselQtyD(ParamMap paramMap) throws Exception{
		return (Long)selectByPk("pacommon.paorder.selectCurCounselQtyD", paramMap.get());
	}
	
	/**
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<String,Object> selectMaxSaleQtyA(ParamMap paramMap) throws Exception{
		return (HashMap)selectByPk("pacommon.paorder.selectMaxSaleQtyA", paramMap.get());
	}

	/**
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<String,Object> selectMaxSaleQtyB(ParamMap paramMap) throws Exception{
		return (HashMap)selectByPk("pacommon.paorder.selectMaxSaleQtyB", paramMap.get());
	}

	/**
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<String,Object> selectMaxSaleQtyC(ParamMap paramMap) throws Exception{
		return (HashMap)selectByPk("pacommon.paorder.selectMaxSaleQtyC", paramMap.get());
	}
	
	/**
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public long selectSyslast(ParamMap paramMap) throws Exception{
		return (Long)selectByPk("pacommon.paorder.selectSyslast", paramMap.get());
	}
	
	/**
	 * 배송 소요일
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public long selectDeliveryPointCount(ParamMap paramMap) {
		return (Long)selectByPk("pacommon.paorder.selectDeliveryPointCount", paramMap.get());
	}
	
	/**
	 * 실재고 체크
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<String,Object> selectRealStock(ParamMap paramMap) throws Exception {
		return (HashMap)selectByPk("pacommon.paorder.selectRealStock",paramMap.get());
	}
	
	/**
	 * 배송예정일 추가일수
	 * @return
	 * @throws Exception
	 */
	public long getOutPlanCnt(ParamMap paramMap) {
		return (Long)selectByPk("pacommon.paorder.getOutPlanCnt", paramMap.get());
	}
	
	public long getOutPlanCnt() {
		return (Long)selectByPk("pacommon.paorder.getOutPlanCnt2", null);
	}
	
	public long getDeliveryPointCount(ParamMap paramMap) throws Exception {
		return (Long) selectByPk("pacommon.paorder.getDeliveryPointCount", paramMap.get());
	}
	
	/**
	 * 배송가능일 조회
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List getDelyday(ParamMap paramMap) throws Exception {
		return list("pacommon.paorder.getDelyday2",paramMap.get());
	}
	
	/**
	 * 팝업- 배송가능일 조회
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */	
	@SuppressWarnings("rawtypes")
	public List getDelydayExcept(ParamMap paramMap) throws Exception {
		return list("pacommon.paorder.getDelydayExcept",paramMap.get());
	}
	
	/**
	 * 팝업- 공급능력 조회
	 * @param String
	 * @return
	 * @throws Exception
	 */
	public String getTargetGoodsdt(String goodsCode) throws Exception {
		return (String) selectByPk("pacommon.paorder.getTargetGoodsdt", goodsCode);
	}

	/**
	 * 팝업- 공급능력 조회
	 * @param String
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getSupplyCapa(ParamMap paramMap) throws Exception {
		return (HashMap) selectByPk("pacommon.paorder.getSupplyCapa", paramMap.get());
	}	

	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> getGoodsCheckDelyGb(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("pacommon.paorder.getGoodsCheckDelyGb", paramMap.get());
	}
	
	
	/**
	 * 배송비 FUN (FUN_GET_CUST_SHIPCOST)
	 * @param ParamMap
	 * @return HashMap
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public HashMap<String,Object> selectCalShpfeeAmt(ParamMap paramMap) throws Exception{
		return (HashMap<String,Object>)selectByPk("pacommon.paorder.selectCalShpfeeAmt", paramMap.get());
	}

	/**
	 * 사은품 프로모션 조회
	 * @param ParamMap
	 * @return HashMap
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectGiftPromo(ParamMap paramMap) throws Exception{
		return list("pacommon.paorder.selectGiftPromo", paramMap.get());
	}
	
	/**
	 * 프로모션 사은품 조회
	 * @param ParamMap
	 * @return HashMap
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public List<Object> selectGiftPromoDt(HashMap<String, Object> giftPromoDtMap) throws Exception{
		return list("pacommon.paorder.selectGiftPromoDt", giftPromoDtMap);
	}
	
	/**
	 * 한정수량 프로모션 잔여수량 조회
	 * @param PromocounselVO
	 * @return
	 * @throws Exception
	 */
	public long retrieveSumCouncelQty(PromocounselVO promocounsel) throws Exception {
		return (long) selectByPk("pacommon.paorder.retrieveSumCouncelQty", promocounsel);
	}
	
	/**
	 * 한정수량 프로모션 상담수량 저장 - TPROMOCOUNSEL INSERT
	 * @param PromocounselVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPromoCounsel(Promocounsel promoCounsel) throws Exception{
		return insert("pacommon.paorder.insertPromoCounsel", promoCounsel);
	}
	
	/**
	 * C/S처리 - TORDERGOODS UPDATE
	 * @param OrdergoodsVO
	 * @return
	 * @throws Exception
	 */
	public int updateOrdergoods(OrdergoodsVO ordergoods) throws Exception{
        return  update("pacommon.paorder.updateOrdergoods",ordergoods);
    }
	
	/**
	 * C/S처리 - TORDERDT UPDATE
	 * @param Canceldt
	 * @return
	 * @throws Exception
	 */
	public int updateOrderdtSyslast(CanceldtVO canceldtVO) throws Exception{
        return  update("pacommon.paorder.updateOrderdtSyslast",canceldtVO);
    }
	
	/**
	 * 취소상세 - TCANCELDT INSERT
	 * @param Canceldt
	 * @return
	 * @throws Exception
	 */
	public int insertCanceldt(CanceldtVO canceldtVO) throws Exception{
        return  insert("pacommon.paorder.insertCanceldt",canceldtVO);
    }
	
	/**
	 * C/S처리 - TINPLANQTY UPDATE 판매가능수량 적용기간이 아닐경우 최대판매가능수량을 차감 처리
	 * @param Canceldt
	 * @return
	 * @throws Exception
	 */
	public int updateInplanqty(CanceldtVO canceldtVO) throws Exception{
		return insert("pacommon.paorder.updateInplanqty",canceldtVO); 
	}
	
	/**
	 * C/S처리 - 사은품 반품
	 * @param ClaimdtVO
	 * @return
	 * @throws Exception
	 */
	public int insertClaimdt(ClaimdtVO claimdt) throws Exception{
		return insert("pacommon.paorder.insertClaimdt",claimdt);
	}
	
	/**
	 * C/S처리 - TDEPOSITUSE UPDATE
	 * @param OrderreceiptsVO
	 * @return
	 * @throws Exception
	 */
	public int updateReceiptsRepay(Orderreceipts orderreceipts) throws Exception{
        return  update("pacommon.paorder.updateReceiptsRepay",orderreceipts);
    }
	
	/**
	 * C/S처리 - TORDERPROMO UPDATE
	 * @param OrderpromoVO
	 * @return
	 * @throws Exception
	 */
	public int update(Orderpromo orderpromo) throws Exception{
        return  update("pacommon.paorder.updateTorderpromo",orderpromo);
    }
	
	/**
	 * 한정 프로모션 여부 - 조회
	 * @param String
	 * @return
	 * @throws Exception
	 */
	public String getLimitYn(String promoNo) throws Exception{
        return (String) selectByPk("pacommon.paorder.getLimitYn", promoNo);
    }
	
	/**
	 * 프로모션 접수내역  - 삭제
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public int deletePromoCounsel(Promocounsel promocounsel) throws Exception{
        return  delete("pacommon.paorder.deletePromoCounsel", promocounsel);
    }

	/**
	 * 특정 컬럼 max값 조회
	 * @param HashMap
	 * @return String
	 * @throws Exception
	 */	
	public String selectMaxNo(HashMap<?, ?> hashMap) throws Exception {
		return (String) selectByPk("pacommon.paorder.selectMaxNo", hashMap);
	}
	
	/**
	 * C/S처리 - TORDERSTOCK UPDATE
	 * @param OrderstockVO
	 * @return
	 * @throws Exception
	 */
	public int updateOrderStockCancel20(OrderstockVO orderstock) throws Exception{
        return  update("pacommon.paorder.updateOrderStockCancel20", orderstock);
    }
	
	/**
	 * 주문orderdt 조회
	 * @param ParamMap
	 * @return OrderdtVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OrderdtVO> selectOrderDtOld(CancelInputVO cancelInputVO) throws Exception{
		return list("pacommon.paorder.selectOrderDtOld", cancelInputVO);
	}
	/**
	 * w_seq 생성
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String selectWSeq(CancelInputVO cancelInputVO) throws Exception {
		return (String) selectByPk("pacommon.paorder.selectWSeq", cancelInputVO);
	}
	
	/**
	 * 주문 torderGoods 조회
	 * @param ParamMap
	 * @return OrdergoodsVO
	 * @throws Exception
	 */
	public OrdergoodsVO selectOrderGoods(ParamMap paramMap) throws Exception{
		return (OrdergoodsVO)selectByPk("pacommon.paorder.selectOrderGoods", paramMap.get());
	}
	
	/**
	 * 주문 ordershipcost 조회
	 * @param ParamMap
	 * @return OrdershipcostVO
	 * @throws Exception
	 */
	public OrdershipcostVO selectOrderShipCost(ParamMap paramMap) throws Exception{
		return (OrdershipcostVO)selectByPk("pacommon.paorder.selectOrderShipCost", paramMap.get());
	}
	
	/**
	 * 주문취소시 배송비 재계산을 위한 원 배송비 조회
	 * @param ParamMap
	 * @return OrdershipcostVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OrdershipcostVO> selectOrderShipCostOrg(ParamMap paramMap) throws Exception{
		return list("pacommon.paorder.selectOrderShipCostOrg", paramMap.get());
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<OrderdtVO> selectOrderdtAll(String orderNo) throws Exception{
		return (ArrayList<OrderdtVO>) list("pacommon.paorder.selectOrderdtAll", orderNo );
	}


	@SuppressWarnings("unchecked")
	public ArrayList<OrderdtVO> selectOrderdt4GSEQ(ParamMap paramMap) throws Exception{
		return (ArrayList<OrderdtVO>) list("pacommon.paorder.selectOrderdt4GSEQ", paramMap.get());
	}
	
	/**
	 * 제휴사 실결제금액(C/S금액 계산) FUNCTON (FUN_GET_RSALE_PA_AMT)
	 * @param ParamMap
	 * @return HashMap
	 * @throws Exception
	 */	
	public double selectRsalePaAmt(String orderGb, String orderNo, String orderGSeq, String orderDSeq, long cancelQty) throws Exception{
		if(orderNo.equals("") || orderGSeq.equals("") || orderDSeq.equals("") || cancelQty < 1)
			return 0;
		
		ParamMap paramMap = new ParamMap(); 
		paramMap.put("arg_order_gb", 	orderGb);
		paramMap.put("arg_order_no", 	orderNo);
		paramMap.put("arg_order_g_seq", orderGSeq);
		paramMap.put("arg_order_d_seq", orderDSeq);
		paramMap.put("arg_proc_qty", 	cancelQty);
		
		return (double) selectByPk("pacommon.paorder.selectRsalePaAmt", paramMap.get());
	}
	
	/**
	 * 주문 orderpromo 조회
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Orderpromo> selectOrderPromo(ParamMap paramMap) throws Exception {
		return list("pacommon.paorder.selectOrderPromo", paramMap.get());
	}
	
	public int selectRemainQty(ParamMap paramMap) throws Exception{
        return (int)selectByPk("pacommon.paorder.selectRemainQty", paramMap.get());
	}
		
	/**
	 * 제휴할인 프로모션 조회
	 * @param  null
	 * @return String
	 * @throws Exception
	 */
	public String selectPromoNo(String doType) throws Exception{
        return (String)selectByPk("pacommon.paorder.selectPromoNo", doType);
    }
	
	/**
	 * 일시불할인 프로모션 조회
	 * @param  null
	 * @return String
	 * @throws Exception
	 */
	public String selectLumpPromoNo(OrderdtVO orderdt) throws Exception{
        return (String)selectByPk("pacommon.paorder.selectLumpPromoNo", orderdt);
    }
		
	/**
	 * TORDERRECEIPTS 잔여금액 조회
	 * @param null
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Orderreceipts> selectOrderRepayPbAmt(Orderreceipts orderreceipts) throws Exception{
        return list("pacommon.paorder.selectOrderRepayPbAmt", orderreceipts);
    }
	
	/**
	 * 상품 판매상태 조회 - TGOODS.SALE_GB, TGOODSDT.SALE_GB
	 * @param OrderStockInVO
	 * @return String
	 * @throws Exception
	 */
	public String selectGoodsSaleGb(OrderStockInVO orderStockIn) throws Exception{
		return (String) selectByPk("pacommon.paorder.selectGoodsSaleGb", orderStockIn);
	}
	
	/**
	 * 주문생성 결과처리 - TPAORDERM UPDATE
	 * @param String
	 * @return List<Object>
	 * @throws Exception
	 */
	public int updatePaOrderm(ParamMap paramMap) throws Exception {
		return update("pacommon.paorder.updatePaOrderm", paramMap.get());
	}

	public int updatePaOrderm20Approval(ParamMap paramMap) throws Exception {
		return update("pacommon.paorder.updatePaOrderm20Approval", paramMap.get());
	}
	
	public int insertTpaMonitering(PaMonitering pamoniter)throws Exception {
		return insert("pacommon.paorder.insertTpaMonitering", pamoniter);
	}
	
	
	public String selectReciverPost(ParamMap paramMap) {
		// TODO Auto-generated method stub
		return (String)selectByPk("pacommon.paorder.selectReciverPost", paramMap.get());
	}
	
	public int checkSalePriceBWPaGoodsPriceNGoodsPrice(ParamMap paramMap) throws Exception{
		return (Integer) selectByPk("pacommon.paorder.checkSalePriceBWPaGoodsPriceNGoodsPrice", paramMap.get());
	}
	
	
	public int getTpaMonitering(PaMonitering pamoniter) throws Exception{
        return (Integer) selectByPk("pacommon.paorder.getTpaMonitering", pamoniter);
        		
    }
	
	public String getConfigVal(String item) throws Exception{
		return (String)selectByPk("pacommon.paorder.getConfigVal", item);
	}
	
	public int updatePreCancelOrder(String mappingSeq) throws Exception {
		return update("pacommon.paorder.updatePreCancelOrder", mappingSeq);
	}

	public int updateOrderdt20Approval(OrderdtVO orderdt) throws Exception {
		return update("pacommon.paorder.updateOrderdt20Approval", orderdt);

	}
	public int updateOrderReceipts20Approval(Orderreceipts orderReceipt) throws Exception {
		return update("pacommon.paorder.updateOrderReceipts20Approval", orderReceipt);
	}	
	public int updateOrderReceipts20ApprovalShipping(Orderreceipts orderReceipt) throws Exception {
		return update("pacommon.paorder.updateOrderReceipts20ApprovalShipping", orderReceipt);
	}
	public int selectOrderreceipt(Orderreceipts orderReceipt) throws Exception{
        return (Integer) selectByPk("pacommon.paorder.selectOrderreceipt", orderReceipt);
	}

	@SuppressWarnings("unchecked")
	public List<OrderdtVO> selectOrderDtList(CancelInputVO cancelInputVO) {
		return list("pacommon.paorder.selectOrderDt", cancelInputVO);
	}

	public int updatePaOrderShipCost(PaOrderShipCost paordershipcost) throws Exception{
		return update("pacommon.paorder.updatePaOrderShipCost", paordershipcost);
	}

	public int updateOrderReceiptCancel(Orderreceipts orderreceipts) throws Exception {
		return update("pacommon.paorder.updateOrderReceiptCancel", orderreceipts);
	}

	public double selectGetOrgShipCost(String orderNo) {
		 return (double) selectByPk("pacommon.paorder.selectGetOrgShipCost", orderNo);
	}

	public int updatePaOrderMFailConfrimPreOrder(HashMap<String, String> hmSheet) {
		return update("pacommon.paorder.updateOrderReceiptCancel", hmSheet);
	}

	public int updateOrderStock(Orderstock stock) throws Exception{
        return  update("pacommon.paorder.updateOrderStock",stock);
	}

	public int updateOrderStockCancel(OrderstockVO orderstock) {
        return  update("pacommon.paorder.updateOrderStockCancel", orderstock);
	}

	public int insertCouponissue(Couponissue couponissue) {
		return insert("pacommon.paorder.insertCouponissue", couponissue);
	}

	public String checkAddressStatus(Receiver receiver) throws Exception {
		 return (String) selectByPk("pacommon.paorder.checkAddressStatus", receiver);
	}

	public int updateDisable(Receiver receiver) throws Exception {
		return update("pacommon.paorder.updateDisable", receiver);	
	}

	public String getNewReceiverSeq(String custNo) throws Exception  {
		 return (String) selectByPk("pacommon.paorder.getNewReceiverSeq", custNo);
	}

	public int updateOrderDtForReceiverSeq(ParamMap paramMap) throws Exception  {
		return update("pacommon.paorder.updateOrderDtForReceiverSeq", paramMap.get());	

	}

	public int updateTelInfo(Receiver receiver) throws Exception {
		return update("pacommon.paorder.updateTelInfo", receiver);	

	}
	
	public int updateNaverPaOrderm20Approval(ParamMap resultParamMap) throws Exception {
		return update("pacommon.paorder.updateNaverPaOrderm20Approval", resultParamMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectPaPersonalInfoSms(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("pacommon.paorder.selectPaPersonalInfoSms", paramMap.get());
	}
	
	public int insertPaPersonalInfoNoticeSmsList(PaPersonalInfoNoticeSms paPersonalInfoNotice) {
		return insert("pacommon.paorder.insertPaPersonalInfoNoticeSmsList", paPersonalInfoNotice);
	}
	
	public String selectTransMsg(ParamMap paramMap) throws Exception{
		return (String)selectByPk("pacommon.paorder.selectTransMsg", paramMap.get());
	}

	public double getGmktShippingFee(String paOrderNo) {
		 return (double) selectByPk("pacommon.paorder.getGmktShippingFee", paOrderNo);
	}

	public OrderpromoVO selectPaOrderPromo(ParamMap paramMap) {
		return  (OrderpromoVO)(selectByPk("pacommon.paorder.selectPaOrderPromo", paramMap.get()));
	}

	public OrderpromoVO selectPaOrderPromo(Map<String, Object> map) {
		return (OrderpromoVO)(selectByPk("pacommon.paorder.selectPaOrderPromo", map));
	}
	
	public int updateOrderCancelYn(HashMap<String, String> map) {
		return update("pacommon.paorder.updateOrderCancelYn", map);
	}

	public int insertTpaSlipInfo(PaSlipInfo paSlipInfo) {
		return insert("pacommon.paorder.insertTpaSlipInfo", paSlipInfo);
	}

	public int updateTpaSlipInfo4Renew(PaSlipInfo paSlipInfo) {
		return update("pacommon.paorder.updateTpaSlipInfo4Renew", paSlipInfo);
	}

	public int selectSlipProcCount(String mappingSeq) throws Exception{
		return (Integer) selectByPk("pacommon.paorder.selectSlipProcCount", mappingSeq);
	}
	@SuppressWarnings("unchecked")
	public List<HashMap<String,String>> selectDelyNoAreaGb(ParamMap paramMap) throws Exception{
		return list("pacommon.paorder.selectDelyNoAreaGb", paramMap.get());
	}
	public String selectPostAreaGb(ParamMap paramMap) throws Exception {
		return (String)selectByPk("pacommon.paorder.selectPostAreaGb", paramMap.get());
	}
	public String selectOrderGoodsDtName(ParamMap goodsDtParam) throws Exception {
		return (String)selectByPk("pacommon.paorder.selectOrderGoodsDtName", goodsDtParam.get());

	}
	
	public int selectOrderGoodsDtDupleCheck(ParamMap goodsDtParam) throws Exception {
		return (int) selectByPk("pacommon.paorder.selectOrderGoodsDtDupleCheck", goodsDtParam.get());
	}

	public int updateRemark3N(HashMap<String, String> map) throws Exception {
		return update("pacommon.paorder.updateRemark3N", map);

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectPreoutOrderTargetList() throws Exception {
		return list("pacommon.paorder.selectPreoutOrderTargetList", null);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPreoutOrderTargetDtList(ParamMap paramMap) throws Exception {
		return list("pacommon.paorder.selectPreoutOrderTargetDtList", paramMap.get());

	}

	@SuppressWarnings("unchecked")
	public List<Receiver> selectReceiverList(String cust_no) throws Exception {
		return list("pacommon.paorder.selectReceiverList", cust_no);
	}

	@SuppressWarnings("unchecked")
	public List<Orderpromo> selectOrderPromoList(String order_no) throws Exception {
		return list("pacommon.paorder.selectOrderPromoList", order_no);
	}

	/**
	 * 주문에 의한 고객System 정보 수정
	 * 
	 * @param custsystem
	 * @return
	 * @throws Exception
	 */
	public int updateCustSystemForOrder(Custsystem custsystem) throws Exception {
		return update("pacommon.paorder.updateCustSystemForOrder", custsystem);
	}

	public int updatePreoutPaOrderm(ParamMap paramMap) {
		return update("pacommon.paorder.updatePreoutPaOrderm", paramMap.get());
	}
	
}