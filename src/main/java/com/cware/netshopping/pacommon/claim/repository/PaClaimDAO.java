package com.cware.netshopping.pacommon.claim.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.ClaimdtVO;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.OrderdtVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.OrderreceiptsVO;
import com.cware.netshopping.domain.OrderstockVO;
import com.cware.netshopping.domain.PaClaimdtVO;
import com.cware.netshopping.domain.ReceiverVO;
import com.cware.netshopping.domain.StockVO;
import com.cware.netshopping.domain.model.Canceldt;
import com.cware.netshopping.domain.model.Custcounseldt;
import com.cware.netshopping.domain.model.Custcounselm;
import com.cware.netshopping.domain.model.Ordergoods;
import com.cware.netshopping.domain.model.Orderreceipts;
import com.cware.netshopping.domain.model.Ordershipcost;
import com.cware.netshopping.domain.model.Planshipcost;
import com.cware.netshopping.domain.model.Promocounsel;
import com.cware.netshopping.domain.model.ReqoutApiHistory;
import com.cware.netshopping.domain.model.CjOneDayToken;
import com.cware.netshopping.domain.model.CjSlipError;

@Service("pacommon.claim.paclaimDAO")
public class PaClaimDAO extends AbstractPaDAO{

	/**
	 * 일반 반품 대상건 조회
	 * @param OrderClaimVO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<OrderdtVO> selectOrderDtInfo(OrderClaimVO orderClaimVO) throws Exception{
		return (ArrayList<OrderdtVO>)list("pacommon.paclaim.selectOrderDtInfo", orderClaimVO);
	}
	
	/**
	 * 일반 교환 대상건 조회
	 * @param OrderClaimVO
	 * @return
	 * @throws Exception
	 */
	public OrderdtVO selectOrderDtInfoEx(OrderClaimVO orderClaimVO) throws Exception{
		return (OrderdtVO)selectByPk("pacommon.paclaim.selectOrderDtInfoEx", orderClaimVO);
	}
	
	/**
	 * 주문수량 - 취소수량 - 반품수량 + 반품취소수량
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int selectOrderGoods(ParamMap paramMap) throws Exception{
		return (Integer)selectByPk("pacommon.paclaim.selectOrderGoods", paramMap.get());
	}
	/**
	 * 배송지조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public ReceiverVO selectReceiverAddr(ParamMap paramMap) throws Exception{
		return (ReceiverVO)selectByPk("pacommon.paclaim.selectReceiverAddr",paramMap.get());
	}

	public int insertReceiver(ReceiverVO receiver) throws Exception{
		return insert("pacommon.paclaim.insertReceiver", receiver);
	}

	public int updateOrdergoods(Ordergoods regOrdergoods) throws Exception{
		return  update("pacommon.paclaim.updateOrdergoods", regOrdergoods);
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
		
		return (double) selectByPk("pacommon.paclaim.selectRsalePaAmt", paramMap.get());
	}

	public int checkUniqueClaimdt(ParamMap uniqueCheckMap) throws Exception{
		return (Integer)selectByPk("pacommon.paclaim.checkUniqueClaimdt", uniqueCheckMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPlanDateInfo(PaClaimdtVO claimdt) throws Exception{
		return (HashMap<String, Object>)selectByPk("pacommon.paclaim.selectPlanDateInfo", claimdt);
	}

	/**
	 * 조회 - 고정 창고일 경우 상품 창고코드와 배송구분 셋팅
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> getGoodsCheckDelyGb(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("pacommon.paclaim.getGoodsCheckDelyGb", paramMap.get());
	}
	
	
	
	public String selectGoodsWhCode(String goodsCode) throws Exception {
		return (String) selectByPk("pacommon.paclaim.selectGoodsWhCode", goodsCode);
	}	
	
	
	public int insertClaimdt(PaClaimdtVO claimdt) throws Exception{
		return insert("pacommon.paclaim.insertClaimdt",claimdt);
	}
	
	public int insertOrderproc(PaClaimdtVO claimdt) throws Exception{
		return insert("pacommon.paclaim.insertOrderproc",claimdt);
	}
	
	public int insertCanceldt(Canceldt canceldt) throws Exception{
		return insert("pacommon.paclaim.insertCanceldt", canceldt);
	}

	public int updateOrdergoodsClaimCanQty(PaClaimdtVO regClaimdt) throws Exception{
		return update("pacommon.paclaim.updateOrdergoodsClaimCanQty",regClaimdt);
	}

	public int updateClaimdt(PaClaimdtVO regClaimdt) throws Exception{
		return update("pacommon.paclaim.updateClaimdt",regClaimdt);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<OrderpromoVO> selectOrderPromo(ParamMap paramMap) throws Exception{
		return (ArrayList<OrderpromoVO>) list("pacommon.paclaim.selectOrderPromo",paramMap.get());
	}

	public int updateOrderpromo(OrderpromoVO regOrderpromo) throws Exception{
		return  update("pacommon.paclaim.updateOrderpromo",regOrderpromo);
	}

	public String selectCustRefId(ParamMap paramMap) throws Exception{
		return (String) selectByPk("pacommon.paclaim.selectCustRefId", paramMap.get());
		}

	public int insertCounselCustcounselm(Custcounselm custcounselm) throws Exception{
		return insert("pacommon.paclaim.insertCounselCustcounselm", custcounselm);
	}

	public int insertPlanshipcost(Planshipcost planshipcost) throws Exception{
		return  insert("pacommon.paclaim.insertPlanshipcost",planshipcost);
	}

	/**
	 * 상담내역 조회
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	public String selectCustCounselDtProcNote(String goodsCode) throws Exception{
		return (String) selectByPk("pacommon.paclaim.selectCustCounselDtProcNote", goodsCode);
	}

	/**
	 * 상담접수 데이터 저장
	 * @param custcounseldt
	 * @return
	 * @throws Exception
	 */
	public int insertCounselCustcounseldt(Custcounseldt custcounseldt) throws Exception{
		return insert("pacommon.paclaim.insertCounselCustcounseldt", custcounseldt);
	}

	/**
	 * 상담접수완료 데이터 저장
	 * @param custcounseldt
	 * @return
	 * @throws Exception
	 */
	public int insertCounselCustcounseldt80(Custcounseldt custcounseldt) throws Exception{
		return insert("pacommon.paclaim.insertCounselCustcounseldt80", custcounseldt);
	}

	public double selectCalShpfeeAmt(ParamMap paramMap) throws Exception {
		return (Double)selectByPk("pacommon.paclaim.selectCalShpfeeAmt",paramMap.get());
	}

	public int insertOrdershipcost(Ordershipcost regOrdershipcost) throws Exception{
		return insert("pacommon.paclaim.insertOrdershipcost", regOrdershipcost);
	}

	@SuppressWarnings("rawtypes")
	public HashMap selectChangeGoodsInfo(OrderClaimVO orderClaimVO) throws Exception{
		return (HashMap)selectByPk("pacommon.paclaim.selectChangeGoodsInfo", orderClaimVO);
	}
	
	public int insertOrderreceipts(OrderreceiptsVO orderreceipts) throws Exception{
		return insert("pacommon.paclaim.insertOrderreceipts", orderreceipts);
	}

	public int updateOrderStockExchange(StockVO stock) throws Exception{
		return  update("pacommon.paclaim.updateOrderStockExchange",stock);
	}

	public int updateOrderStockExchangeCancel(StockVO stock) throws Exception{
		return  update("pacommon.paclaim.updateOrderStockExchangeCancel",stock);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ClaimdtVO> selectClaimdt(OrderClaimVO orderClaimVO) throws Exception{
		return (ArrayList<ClaimdtVO>)list("pacommon.paclaim.selectClaimdt", orderClaimVO);
	}
	
	public int updatePlanshipcost(Planshipcost regPlanshipcost) throws Exception{
		return update("pacommon.paclaim.updatePlanshipcost",regPlanshipcost);
	}

	public PaClaimdtVO selectChangeCancelOrg(OrderClaimVO orderClaimVO) throws Exception{
		return (PaClaimdtVO)selectByPk("pacommon.paclaim.selectChangeCancelOrg", orderClaimVO);
	}
	
	public String selectSequenceNoCondition(HashMap<?, ?> hashMap) throws Exception {
		return (String) selectByPk("pacommon.paclaim.selectSequenceNoCondition", hashMap);
	}	

	public String selectSequenceNo(HashMap<?, ?> hashMap) throws Exception {
		return (String) selectByPk("pacommon.paclaim.selectSequenceNo",hashMap);
	}
	
	/**
	 * 한정수량 프로모션 상담수량 저장 - TPROMOCOUNSEL INSERT
	 * @param PromocounselVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPromoCounsel(Promocounsel promoCounsel) throws Exception{
		return insert("pacommon.paclaim.insertPromoCounsel", promoCounsel);
	}
	
	/**
	 * C/S처리 - TORDERDT UPDATE
	 * @param Canceldt
	 * @return
	 * @throws Exception
	 */
	public int updateOrderdtSyslast(Canceldt canceldt) throws Exception{
        return  update("pacommon.paclaim.updateOrderdtSyslast",canceldt);
    }
	
	/**
	 * C/S처리 - TORDERSTOCK UPDATE
	 * @param OrderstockVO
	 * @return
	 * @throws Exception
	 */
	public int updateOrderStockCancel20(OrderstockVO orderstock) throws Exception{
        return  update("pacommon.paclaim.updateOrderStockCancel20", orderstock);
    }
	
	/**
	 * C/S처리 - TINPLANQTY UPDATE 판매가능수량 적용기간이 아닐경우 최대판매가능수량을 차감 처리
	 * @param Canceldt
	 * @return
	 * @throws Exception
	 */
	public int updateInplanqty(Canceldt canceldt) throws Exception{
		return insert("pacommon.paclaim.updateInplanqty",canceldt); 
	}
	
	/**
	 * 출고전 반품처리대상 조회
	 * @param HashMap<String, Object>
	 * @return HashMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public HashMap<String, Object> selectDeliveryReqOutCancelTarget(HashMap<String, Object> hashMap) throws Exception{
		return (HashMap<String, Object>)selectByPk("pacommon.paclaim.selectDeliveryReqOutCancelTarget", hashMap);
	}
	
	
	/**
	 * TORDERRECEIPTS 잔여금액 조회
	 * @param null
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Orderreceipts> selectOrderRepayPbAmt(Orderreceipts orderreceipts) throws Exception{
        return list("pacommon.paclaim.selectOrderRepayPbAmt", orderreceipts);
    }
	
	
	/**
	 * 출고전 반품처리 - TCLAIMDT update
	 * @param HashMap<String, Object>
	 * @return Integer
	 * @throws Exception
	 */
	public int updatedirectClaimdt(ParamMap paramMap) throws Exception{
		return update("pacommon.paclaim.updatedirectClaimdt", paramMap.get());
	}
	
	/**
	 * 출고전 반품처리 - 회수지시/회수확정등록, 출고전반품처리 - 수정(TORDERGOODS)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateDeliveryReqOutCancelOrdergoods(ParamMap paramMap) throws Exception{
		return update("pacommon.paclaim.updateDeliveryReqOutCancelOrdergoods", paramMap.get());
	}
	
	/**
	 * 출고전 반품처리 - SLIP_I_NO 시퀀스 조회
	 * @param 
	 * @return String
	 * @throws Exception
	 */
	public String selectSlipINoSequence() throws Exception{
		return (String) selectByPk("pacommon.paclaim.selectSlipINoSequence", null);
	}
	
	/**
	 * 출고전 반품처리 - 저장(TSLIPM)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int insertCancelSlipm(ParamMap paramMap) throws Exception{
		return insert("pacommon.paclaim.insertCancelSlipm", paramMap.get());
	}
	
	/**
	 * 출고전 반품처리 - 저장(TSLIPDT)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int insertClaimSlipdt(ParamMap paramMap) throws Exception{
		return insert("pacommon.paclaim.insertClaimSlipdt", paramMap.get());
	}
	
	/**
	 * 출고전 반품처리 - 수정(TORDERSTOCK)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderstock(ParamMap paramMap) throws Exception{
		return update("pacommon.paclaim.updateOrderstock", paramMap.get());
	}
	
	/**
	 * 출고전 반품처리 - 재고수정
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updatetStock(ParamMap paramMap) throws Exception{
		return update("pacommon.paclaim.updatetStock", paramMap.get());
	}
	
	/**
	 * 출고전 반품처리 - 재고수정
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateRack(ParamMap paramMap) throws Exception{
		return update("pacommon.paclaim.updateRack", paramMap.get());
	}
	
	/**
	 * 출고전 반품처리 - STOCK_MOD_NO 시퀀스 조회
	 * @param 
	 * @return String
	 * @throws Exception
	 */
	public String selectStockModReqSeqNo() throws Exception{
		return (String) selectByPk("pacommon.paclaim.selectStockModReqSeqNo", null);
	}
	
	/**
	 * 출고전 반품처리 - 재고등급변경 INSERT
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int insertStockModReq(ParamMap paramMap) throws Exception{
		return insert("pacommon.paclaim.insertStockModReq", paramMap.get());
	}
	
	/**
	 * 출고전 반품처리 - 회수확정 (대기재고(WAIT_QTY) 저장)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateReturnStockWaitQty(ParamMap paramMap) throws Exception{
		return update("pacommon.paclaim.updateReturnStockWaitQty", paramMap.get());
	}
	
	/**
	 * 출고전 반품처리 - 수정(TSLIPCREATE)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateSlipcreate40(ParamMap paramMap) throws Exception{
		return update("pacommon.paclaim.updateSlipcreate40", paramMap.get());
	}
	
	/**
	 * 출고전 반품처리 - 수정(TSLIPM)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateSlipM(ParamMap paramMap) throws Exception{
		return update("pacommon.paclaim.updateSlipM40", paramMap.get())
				+ update("pacommon.paclaim.updateSlipM80", paramMap.get())
				;
	}
	
	/**
	 * 출고전 반품처리 - 주문수량 배송완료수량 확인
	 * @param HashMap<String, Object>
	 * @return List
	 * @throws Exception
	 */
	public int chkSyslastDelyQty(HashMap<String, Object> hashMap) throws Exception{
		return (Integer) selectByPk("pacommon.paclaim.chkSyslastDelyQty", hashMap);
	}
	
	/**
	 * 출고전 반품처리 - 수정(TORDERDT)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderDt(ParamMap paramMap) throws Exception{
		return update("pacommon.paclaim.updateOrderDt", paramMap.get());
	}
	
	/**
	 * 출고전 반품처리 - SLIP_PROC MIN 값 확인
	 * @param HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int chkSlipProcMin(HashMap<String, Object> hashMap) throws Exception{
		return (Integer) selectByPk("pacommon.paclaim.chkSlipProcMin", hashMap);
	}
	
	/**
	 * 출고전 반품처리 - 수정(TORDERDT)
	 * @param  HashMap<String, Object> hashMap
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderSlipDoFlagSync(HashMap<String, Object> hashMap) throws Exception{
		return update("pacommon.paclaim.updateOrderSlipDoFlagSync", hashMap);
	}

	/**
	 * C/S처리
	 * @param Canceldt
	 * @return HashMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public HashMap selectExchangeGoodsdtCode(OrderdtVO orderdt) throws Exception{
		return (HashMap)selectByPk("pacommon.paclaim.selectExchangeGoodsdtCode", orderdt);
	}
	
	/**
	* 출고전 반품처리 - 동일 주문 반품 건 확인 체크
	* @param  
	* @return PaClaimdtVO
	* @throws Exception
	*/
	public int checkDeliveryReqOutCancel(PaClaimdtVO regClaimdt) throws Exception{
	       return (Integer) selectByPk("pacommon.paclaim.checkDeliveryReqOutCancel", regClaimdt);
	}
	
	/**
	 * 업체회수확정등록 - 주문상품정보수정
	 * 
	 * @param Claimdt
	 * @return
	 * @throws Exception
	 */
	public int updateTordergoods(PaClaimdtVO regClaimdt) throws Exception {
		return update("pacommon.paclaim.updateTordergoods", regClaimdt);
	}
	
	/**
	 * 직송 출고전반품처리 - 저장전 데이터 조회
	 * @param  HashMap
	 * @return int
	 * @throws Exception
	 */
	public int selectEntpDirectDeliveryCount(ParamMap paramMap) throws Exception{
		return (Integer) selectByPk("pacommon.paclaim.selectEntpDirectDeliveryCount", paramMap.get());
	}
	
	/**
	 * 직송 출고전반품처리 - 저장(TSLIPCJORDERINFO)
	 * @param  HashMap
	 * @return int
	 * @throws Exception
	 */
	public int insertEntpDirectReturnBeforeOut(ParamMap paramMap) throws Exception{
		return update("pacommon.paclaim.insertEntpDirectReturnBeforeOut", paramMap.get());
	}
	
	/**
	 * 직송 출고전반품처리 - 저장전 데이터 조회(반품)
	 * @param  HashMap
	 * @return int
	 * @throws Exception
	 */
	public int selectEntpDirectReturnCount(ParamMap paramMap) throws Exception{
		return (Integer) selectByPk("pacommon.paclaim.selectEntpDirectReturnCount", paramMap.get());
	}
	
	/**
	 * 직송 출고전반품처리 - 저장(TSLIPCJORDERINFO)반품
	 * @param  Claimdt
	 * @return int
	 * @throws Exception
	 */
	public int insertEntpDirectReturnBeforeReturn(ParamMap paramMap) throws Exception{
		return update("pacommon.paclaim.insertEntpDirectReturnBeforeReturn", paramMap.get());
	}
	
    /**
     * LOTTE 출고전반품처리 - 데이터 조회(TSLIPLOTTEORDERINFO)
     * 
     * @param ParamMap
     * @return String
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public  List<HashMap<String, Object>> selectEntpDirectLotteDeliveryList(ParamMap paramMap) throws Exception {
    	return  (List<HashMap<String, Object>>) list("pacommon.paclaim.selectEntpDirectLotteDeliveryList", paramMap.get());
    }
	
	/**
	 * LOTTE 출고전반품처리 - TSLIPLOTTEORDERINFO REQUEST_YN UPDATE
	 * 
	 * @param Claimdt
	 * @return
	 * @throws Exception
	 */
	public int updateLotteOrderInfoRequestYn(ParamMap paramMap) throws Exception {
		return update("pacommon.paclaim.updateLotteOrderInfoRequestYn", paramMap.get());
	}
	
    /**
     * 한진택배 출고전반품처리 - 데이터 조회(TSLIPHANJINORDERINFO)
     * 
     * @param ParamMap
     * @return String
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public  List<HashMap<String, Object>> selectEntpDirectHanjinDeliveryList(ParamMap paramMap) throws Exception {
    	return  (List<HashMap<String, Object>>) list("pacommon.paclaim.selectEntpDirectHanjinDeliveryList", paramMap.get());
    }
	
	/**
	 * 한진택배 직송/직택배 예약취소 API - TSLIPHANJINORDERINFO REQUEST_YN UPDATE
	 * 
	 * @param String
	 * @return
	 * @throws Exception
	 */
	public int updateHanjinOrderInfoRequestYn(ParamMap paramMap) throws Exception {
		return update("pacommon.paclaim.updateHanjinOrderInfoRequestYn", paramMap.get());
	}
	
	/**
	 * 출고전반품 API 연동결과 저장
	 * @return int
	 * @throws Exception
	 */
	public int insertReqoutApiHistory(ReqoutApiHistory reqoutApiHistory) throws Exception{
		return insert("pacommon.paclaim.insertReqoutApiHistory", reqoutApiHistory);
	}
	
	/**
	 * 출고전반품처리 - 히스토리 insert(TREQOUTHISTORY)
	 * @param  Claimdt, String 
	 * @return int
	 * @throws Exception
	 */
	public int insertReqOutHistory(ParamMap paramMap) throws Exception{
		return insert("pacommon.paclaim.insertReqOutHistory", paramMap.get());
	}
	
	/**
	 * 최근접속정보 갱신
	 * @param custsystem
	 * @return
	 * @throws Exception
	 */
	public int updateCustsystemLastCon(String custNo) throws Exception {
		return update("pacommon.paclaim.updateCustsystemLastCon", custNo);
	}
	
	/**
	 * 조회-상담분류
	 * 
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String selectCsLmsName(String csLmsCode) throws Exception {
		return (String) selectByPk("pacommon.paclaim.selectCsLmsName", csLmsCode);
	}
	
	/**
	 * 조회- 출고담당자 번호 조회
	 * 
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String selectEntpManHp(ParamMap param) throws Exception {
		return (String) selectByPk("pacommon.paclaim.selectEntpManHp", param.get());
	}
	
	/**
	 * 조회- 출고전반품 약속일 계산
	 * 
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String selectHcReqDate() throws Exception {
		return (String) selectByPk("pacommon.paclaim.selectHcReqDate", null);
	}
	
	/**
     * CJ대한통운 직송/직택배 출고전반품처리 - 주문 데이터 조회(TSLIPCJORDERINFO)
     * 
     * @param ParamMap
     * @return String
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectEntpDirectCjDeliveryList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String, Object>>) list("pacommon.paclaim.selectEntpDirectCjDeliveryList", paramMap.get());
	}
	
	/**
     * CJ대한통운 직송/직택배 출고전반품처리 - 반품 데이터 조회(TSLIPCJORDERINFO)
     * 
     * @param ParamMap
     * @return String
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectEntpDirectCjReturnList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String, Object>>) list("pacommon.paclaim.selectEntpDirectCjReturnList", paramMap.get());
	}
	
	/**
	 * CJ대한통운 직송/직택배 API 중복 토큰 존재 유무 
	 * 
	 * @param CjOneDayToken
	 * @return
	 * @throws Exception
	 */
	public int selectCjToken(CjOneDayToken cjOneDayToken) {
		return (Integer) selectByPk("pacommon.paclaim.selectCjToken", cjOneDayToken);
	}
	
	/**
	 * cj 대한통운 직송/직택배 api Oneday Token 조회
	 * 
	 * @param 
	 * @return String
	 * @throws Exception
	 */
	public String getCjOnedayTokenNo(CjOneDayToken onedayToken) throws Exception {
		return (String) selectByPk("pacommon.paclaim.getCjOnedayTokenNo", onedayToken);
	}
	
	/**
	 * CJ대한통운 직송/직택배 API 토큰 seq 
	 * 
	 * @param 
	 * @return String
	 * @throws Exception
	 */
	public String getTokenSequenceNo() throws Exception {
		return (String) selectByPk("pacommon.paclaim.getTokenSequenceNo", null);
	}
	
	/**
	 * CJ대한통운 직송/직택배 API 토큰 발행 데이터 insert
	 * 
	 * @param CjOneDayToken
	 * @return int
	 * @throws Exception
	 */
	public int insertCjOneDayToken(CjOneDayToken cjOneDayToken) {
		return insert("pacommon.paclaim.insertCjOneDayToken", cjOneDayToken);
	}
	
	/**
	 * CJ대한통운 직송/직택배 API 에러 저장
	 * @return int
	 * @throws Exception
	 */
	public int insertCjSlipError(CjSlipError cjSlipError) throws Exception{
		return insert("pacommon.paclaim.insertCjSlipError", cjSlipError);
	}
	
	/**
	 * CJ대한통운 직송/직택배 예약취소 API - TSLIPCJORDERINFO REQUEST_YN UPDATE
	 * 
	 * @param String
	 * @return
	 * @throws Exception
	 */
	public int updateCjOrderInfoRequestYn(ParamMap paramMap) throws Exception {
		return update("pacommon.paclaim.updateCjOrderInfoRequestYn", paramMap.get());
	}

	/**
	 * 택배사명 조회
	 * 
	 * @param delyGb
	 * @return
	 * @throws Exception
	 */
	public String selectDelyName(String delyGb) throws Exception{
		return  (String)selectByPk("pacommon.paclaim.selectDelyName", delyGb);
	}
	
}