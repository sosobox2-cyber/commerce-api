package com.cware.netshopping.pawemp.order.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaWempDeliveryVO;
import com.cware.netshopping.domain.PaWempTargetVO;
import com.cware.netshopping.domain.model.PaWempClaimItemList;
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.domain.model.PaWempOrderItemList;
import com.cware.netshopping.domain.model.PaWempOrderList;
import com.cware.netshopping.domain.model.Paorderm;

@Service("pawemp.order.paWempOrderDAO")
public class PaWempOrderDAO extends AbstractPaDAO {

	/**
	 * 상품준비중 주문 저장 - 데이터 중복 체크
	 * @param paWempOrderList
	 * @return
	 */
	public int selectPaWempOrderListExists(PaWempOrderList paWempOrderList) throws Exception{
		return (Integer) selectByPk("pawemp.order.selectPaWempOrderListExists", paWempOrderList);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaWempOrderItemList> selectPaWempOrderItemList(ParamMap paramMap) throws Exception{
		return list("pawemp.order.selectPaWempOrderItemList", paramMap.get());
	}
	
	/**
	 * 주문정보 입력
	 * @param paWempOrderList
	 * @return
	 * @throws Exception
	 */
	public int insertPaWempOrderList(PaWempOrderList paWempOrderList) throws Exception{
		return insert("pawemp.order.insertPaWempOrderList", paWempOrderList);
	}

	/**
	 * 주문상품정보 입력
	 * @param paWempOrderItemList
	 * @return
	 * @throws Exception
	 */
	public int insertPaWempOrderItemList(PaWempOrderItemList paWempOrderItemList) throws Exception{
		return insert("pawemp.order.insertPaWempOrderItemList", paWempOrderItemList);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaWempTargetVO> selectOrderInputTargetList (ParamMap paramMap) throws Exception{
		return list("pawemp.order.selectOrderInputTargetList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderInputTargetDtList (ParamMap paramMap) throws Exception{
		return list("pawemp.order.selectOrderInputTargetDtList", paramMap.get());
	}
	
	public PaWempTargetVO selectRefusalInfo(String mappingSeq) throws Exception{
		return (PaWempTargetVO) selectByPk("pawemp.order.selectRefusalInfo", mappingSeq);
	}
	
	/**
	 * 위메프 주문취소 처리 - 기취소
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception{
		return update("pawemp.order.updatePreCancelYn", preCancelMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectSlipOutProcList() throws Exception {
		return list("pawemp.order.selectSlipOutProcList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectSlipOutProcDtList (String paShipNo) throws Exception{
		return list("pawemp.order.selectSlipOutProcDtList", paShipNo);
	}
	
	public int updateSlipOutProc(ParamMap paramMap) throws Exception{
		return update("pawemp.order.updateSlipOutProc", paramMap.get());
	}
	
	public int updateSlipOutProcFail(ParamMap paramMap) throws Exception{
		return update("pawemp.order.updateSlipOutProcFail", paramMap.get());
	}
	
	public int updateDeliveryComplete(Paorderm paorderm) throws Exception{
		return update("pawemp.order.updateDeliveryComplete", paorderm);
	}
	
	public int updatePaOrdermCancelRefusal(Paorderm paorderm) throws Exception{
		return update("pawemp.order.updatePaOrdermCancelRefusal", paorderm);
	}
	
	public int updateInvoiceInfo(PaWempOrderList paWempOrderList) throws Exception{
		return update("pawemp.order.updateInvoiceInfo", paWempOrderList);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaWempClaimList> selectCancelRequestList() throws Exception{
		return list("pawemp.order.selectCancelRequestList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaWempDeliveryVO> selectCancelRequestDtList(ParamMap paramMap) throws Exception{
		return list("pawemp.order.selectCancelRequestDtList", paramMap.get());
	}
	
	public int updateClaimItemProcFlag(PaWempClaimItemList claimItem) throws Exception{
		return update("pawemp.order.updateClaimItemProcFlag", claimItem);
	}
	
	/**
	 * 취소철회여부 수정
	 * @param paWempClaimList
	 * @return
	 * @throws Exception
	 */
	public int updateCancelWithdrawYn(PaWempClaimList paWempClaimList) throws Exception{
		return update("pawemp.order.updateCancelWithdrawYn", paWempClaimList);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaWempTargetVO> selectCancelInputTargetList() throws Exception{
		return list("pawemp.order.selectCancelInputTargetList", null);
	}
	
	/**
	 * 취소생성 - 상세조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception{
		return list("pawemp.order.selectCancelInputTargetDtList", paramMap.get());
	}
	
	/**
	 * 취소접수이후 택배사 휴일제외한 지난 시간 계산
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public int selectBusinessDayAccount(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("pawemp.order.selectBusinessDayAccount", paramMap.get());
	}
	
	/**
	 * 배송완료 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectDeliveryCompleteList() throws Exception{
		return list("pawemp.order.selectDeliveryCompleteList", null);
	}
	
	/**
	 * 취소승인처리(BO) 주문 정보 생성
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaWempDeliveryVO> selectOrgItemInfoByCancelInfo(String paClaimNo) throws Exception{
		return list("pawemp.order.selectOrgItemInfoByCancelInfo", paClaimNo);
	}

	/**
	 * 직송 배송완료처리 - 배송완료 처리
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public int updateDeliveryStatus(ParamMap paramMap) throws Exception{
		return update("pawemp.order.updateDeliveryStatus", paramMap.get());
	}
	
	/**
	 * 위메프 프로모션 조회 - 조회
	 * @param HashMap<String, String>
	 * @return OrderpromoVO
	 * @throws Exception
	 */
   public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception{
	   return (OrderpromoVO)(selectByPk("pawemp.order.selectOrderPromo", orderMap));
   }
   
   /**
    * 위메프 제휴OUT프로모션 조회 - 조회
    * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
    * @param HashMap<String, String>
    * @return OrderpromoVO
    * @throws Exception
    */
   public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception{
	   return (OrderpromoVO)(selectByPk("pawemp.order.selectOrderPaPromo", orderMap));
   }

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipUpdateProcList() {
		return list("pawemp.order.selectSlipUpdateProcList", null);
	}
	
	/**
	 * 품절취소 상품 재고 통신여부
	 * @return
	 * @throws Exception
	 */
	public String selectSoldoutTransYn(ParamMap paramMap) throws Exception {
		return (String) selectByPk("pawemp.order.selectSoldoutTransYn", paramMap.get());
	}
}
