
package com.cware.netshopping.pa11st.order.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11storderlistVO;
import com.cware.netshopping.domain.model.Pa11storderlist;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.domain.OrderpromoVO;


@Service("pa11st.order.pa11stOrderDAO")
public class Pa11stOrderDAO extends AbstractPaDAO{
	
	/**
	 * 11번가 판매불가처리 - TPAORDERM update
	 * @param ParamMap
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePaOrdermPreCancel(ParamMap paramMap) throws Exception{
		return update("pa11st.order.updatePaOrdermPreCancel", paramMap.get());
	}
	
	/**
	 * 11번가 취소신청목록조회 - TPA11STORDERLIST insert
	 * @param Pa11storderlist pa11storderlist
	 * @return int
	 * @throws Exception
	 */
	public int insertCancelPa11stOrderList(Pa11storderlist pa11storderlist) throws Exception{
		return insert("pa11st.order.insertCancelPa11stOrderList", pa11storderlist);
	}
	
	/**
	 * 11번가 취소처리대상조회 
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectPa11stOrdCancelList() throws Exception{
		return list("pa11st.order.selectPa11stOrdCancelList", null);
	}
	
	/**
	 * 11번가 취소승인처리 - TPA11STORDERLIST update
	 * @param HashMap<String, Object>
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePa11stOrderListProcFlag(HashMap<String, Object> hashMap) throws Exception{
		return update("pa11st.order.updatePa11stOrderListProcFlag", hashMap);
	}
	
	/**
	 * 11번가 취소철회목록 조회 - 처리여부 확인
	 * @param HashMap<String, Object>
	 * @return HashMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public HashMap selectCancelWithdrawExists(Pa11storderlistVO pa11storderlistVO) throws Exception{
		return (HashMap) selectByPk("pa11st.order.selectCancelWithdrawExists", pa11storderlistVO);
	}
	
	/**
	 * 11번가 취소철회목록 조회 - 취소철회여부 저장
	 * @param HashMap<String, Object>
	 * @return Integer
	 * @throws Exception
	 */
	public int updateWithdrawYn(Pa11storderlistVO pa11storderlistVO) throws Exception{
		return update("pa11st.order.updateWithdrawYn", pa11storderlistVO);
	}
	
	/**
	 * 11번가 취소신청일자 기준 영업일 계산
	 * @param HashMap<String, Object>
	 * @return Integer
	 * @throws Exception
	 */
	public int selectBusinessDayAccount(HashMap<String, Object> hashMap) throws Exception{
		return (Integer) selectByPk("pa11st.order.selectBusinessDayAccount", hashMap);
	}
	
	/**
	 * 11번가 취소거부처리 - TPAORDERM update
	 * @param HashMap<String, Object>
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePaOrdermCancelRefusal(HashMap<String, Object> hashMap) throws Exception{
		return update("pa11st.order.updatePaOrdermCancelRefusal", hashMap);
	}
	
	/**
	 * 11번가 출고전반품처리대상
	 * @param HashMap<String, Object>
	 * @return HashMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public HashMap selectDeliveryReqOutCancelTarget(HashMap<String, Object> hashMap) throws Exception{
		return (HashMap) selectByPk("pa11st.order.selectDeliveryReqOutCancelTarget", hashMap);
	}
	
	/**
	 * 11번가 출고전반품대상 - TCLAIMDT update
	 * @param HashMap<String, Object>
	 * @return Integer
	 * @throws Exception
	 */
	public int updatedirectClaimdt(HashMap<String, Object> hashMap) throws Exception{
		return update("pa11st.order.updatedirectClaimdt", hashMap);
	}
	
	/**
	 * 11번가 출고전반품대상 - 회수지시/회수확정등록, 출고전반품처리 - 수정(TORDERGOODS)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateTordergoods(HashMap<String, Object> hashMap) throws Exception{
		return update("pa11st.order.updateTordergoods", hashMap);
	}
	
	/**
	 * 11번가 출고전반품대상 - SLIP_I_NO 시퀀스 조회
	 * @param 
	 * @return String
	 * @throws Exception
	 */
	public String selectSlipINoSequence() throws Exception{
		return (String) selectByPk("pa11st.order.selectSlipINoSequence", null);
	}
	
	/**
	 * 11번가 출고전반품대상 - 저장(TSLIPM)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int insertCancelSlipm(HashMap<String, Object> hashMap) throws Exception{
		return insert("pa11st.order.insertCancelSlipm", hashMap);
	}
	
	/**
	 * 11번가 출고전반품대상 - 저장(TSLIPM)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int insertClaimSlipdt(HashMap<String, Object> hashMap) throws Exception{
		return insert("pa11st.order.insertClaimSlipdt", hashMap);
	}
	
	/**
	 * 11번가 출고전반품대상 - 수정(TORDERSTOCK)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderstock(HashMap<String, Object> hashMap) throws Exception{
		return update("pa11st.order.updateOrderstock", hashMap);
	}
	
	/**
	 * 11번가 출고전반품대상 - 수정(TSLIPCREATE)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateSlipcreate40(HashMap<String, Object> hashMap) throws Exception{
		return update("pa11st.order.updateSlipcreate40", hashMap);
	}
	
	/**
	 * 11번가 출고전반품대상 - 수정(TSLIPM)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateSlipM(HashMap<String, Object> hashMap) throws Exception{
		return update("pa11st.order.updateSlipM", hashMap);
	}
	
	/**
	 * 11번가 출고전반품대상 - 주문수량 배송완료수량 확인
	 * @param HashMap<String, Object>
	 * @return List
	 * @throws Exception
	 */
	public int chkSyslastDelyQty(HashMap<String, Object> hashMap) throws Exception{
		return (Integer) selectByPk("pa11st.order.chkSyslastDelyQty", hashMap);
	}
	
	/**
	 * 11번가 출고전반품대상 - 수정(TORDERDT)
	 * @param  HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderDt(HashMap<String, Object> hashMap) throws Exception{
		return update("pa11st.order.updateOrderDt", hashMap);
	}
	
	/**
	 * 11번가 출고전반품대상 - SLIP_PROC MIN 값 확인
	 * @param HashMap<String, Object>
	 * @return int
	 * @throws Exception
	 */
	public int chkSlipProcMin(HashMap<String, Object> hashMap) throws Exception{
		return (Integer) selectByPk("pa11st.order.chkSlipProcMin", hashMap);
	}
	
	/**
	 * 11번가 출고전반품대상 - 수정(TORDERDT)
	 * @param  HashMap<String, Object> hashMap
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderSlipDoFlagSync(HashMap<String, Object> hashMap) throws Exception{
		return update("pa11st.order.updateOrderSlipDoFlagSync", hashMap);
	}
	
	/**
	 * 11번가 취소신청대상 중복체크
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectCancelPa11stOrderListExists(Pa11storderlistVO pa11storderlist) throws Exception{
		return (Integer) selectByPk("pa11st.order.selectCancelPa11stOrderListExists", pa11storderlist);
	}
	
	/**
	 * 11번가 취소거부처리 - TPA11STORDERLIST update
	 * @param HashMap<String, Object>
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePa11stOrderListRefusalProcFlag(HashMap<String, Object> hashMap) throws Exception{
		return update("pa11st.order.updatePa11stOrderListRefusalProcFlag", hashMap);
	}
	
	/**
	 * 11번가 취소거부처리 중복체크
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectCancelRefusalExists(HashMap<String, Object> hashMap) throws Exception{
		return (Integer) selectByPk("pa11st.order.selectCancelRefusalExists", hashMap);
	}
	
	/**
	 * 11번가 취소완료목록 조회 - proc_flag 조회
	 * @param Pa11storderlist pa11storderlist
	 * @return String
	 * @throws Exception
	 */
	public String selectPa11stOrderListProcFlag(Pa11storderlist pa11storderlist) throws Exception{
		return (String) selectByPk("pa11st.order.selectPa11stOrderListProcFlag", pa11storderlist);
	}
	
	/**
	 * 11번가 취소완료목록 조회 - TPA11STORDERLIST MERGE
	 * @param Pa11storderlist pa11storderlist
	 * @return int
	 * @throws Exception
	 */
	public int mergePa11stOrderList(Pa11storderlist pa11storderlist) throws Exception{
		return insert("pa11st.order.mergePa11stOrderList", pa11storderlist);
	}
	
	/**
	 * 11번가 취소완료목록 조회 - 원주문건의 do_flag 조회
	 * @param Pa11storderlist pa11storderlist
	 * @return String
	 * @throws Exception
	 */
	public String selectOrderdtDoFlag(Pa11storderlist pa11storderlist) throws Exception{
		return (String) selectByPk("pa11st.order.selectOrderdtDoFlag", pa11storderlist);
	}
	
	/**
	 * 11번가 주문생성 대상조회 
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderInputTargetList() throws Exception{
		return list("pa11st.order.selectOrderInputTargetList", null);
	}
	
	/**
	 * 11번가 주문생성 대상조회 - 상세
	 * @param String
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderMap) throws Exception{
		return list("pa11st.order.selectOrderInputTargetDtList", orderMap);
	}

	/**
	 * 11번가 주문생성 결과처리 - 판매불가정보 parameter 조회.
	 * @param String
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception {
		return (HashMap<String, String>) selectByPk("pa11st.order.selectRefusalInfo", mappingSeq);
	}
   
   /**
	* 11번가 주문생성 대상조회 
	* @param 
	* @return List<Object>
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	public List<Object> selectCancelInputTargetList() throws Exception{
		return list("pa11st.order.selectCancelInputTargetList", null);
	}
	
	/**
	 * 11번가 주문취소 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception{
		return list("pa11st.order.selectCancelInputTargetDtList", paramMap.get());
	}

	/**
	 * 11번가 주문취소 처리 - 기취소
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception{
		return update("pa11st.order.updatePreCancelYn", preCancelMap.get());
	}

	/**
	 * 11번가 취소승인처리 - 취소승인대상조회
	 * @param String
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPa11stOrdCancelApprovalList(ParamMap cancelMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("pa11st.order.selectPa11stOrdCancelApprovalList", cancelMap.get());
	}
	
	/**
	 * 11번가 주문생성이전취소 - 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectCancelWithoutOrderList(ParamMap paramMap) throws Exception{
		return list("pa11st.order.selectCancelWithoutOrderList", paramMap.get());
	}
	
	/**
	 * 11번가 주문생성이전취소 - TPAORDERM update
	 * @param List<Object>
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePreCancelReason(Paorderm paorderm) throws Exception{
		return update("pa11st.order.updatePreCancelReason", paorderm);
	}
	
   public int selectPaOrderM(Paorderm paorderm) throws Exception{
	   return (Integer) selectByPk("pa11st.order.selectPaOrderM", paorderm);
   }
	
   
 	/**
 	 * 11번가 프로모션 조회 - 조회
 	 * @param HashMap<String, String>
 	 * @return OrderpromoVO
 	 * @throws Exception
 	 */
    public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception{
 	   return (OrderpromoVO)(selectByPk("pa11st.order.selectOrderPromo", orderMap));
    }	
    
  	/**
  	 * 11번가 제휴OUT프로모션 조회 - 조회
  	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
  	 * @param HashMap<String, String>
  	 * @return OrderpromoVO
  	 * @throws Exception
  	 */
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception{
  	   return (OrderpromoVO)(selectByPk("pa11st.order.selectOrderPaPromo", orderMap));
     }	
	
	/**
	 * 11번가 취소완료목록 조회 - 원주문 데이터 조회
	 * @param Pa11storderlist pa11storderlist
	 * @return Integer
	 * @throws Exception
	 */
	public int countOrderList(Pa11storderlist pa11storderlist) throws Exception{
		return (Integer) selectByPk("pa11st.order.countOrderList", pa11storderlist);
	}

	public String selectDelyType(String prdNo) throws Exception{
		return (String) selectByPk("pa11st.order.selectDelyType", prdNo);
	}

	public Pa11storderlistVO selectPa11stOrderList(Pa11storderlistVO pa11storderlist) {
	 	return (Pa11storderlistVO)(selectByPk("pa11st.order.selectPa11stOrderList", pa11storderlist));
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception{
		return list("pa11st.order.selectPaMobileOrderAutoCancelList", paramMap.get());
	}

}