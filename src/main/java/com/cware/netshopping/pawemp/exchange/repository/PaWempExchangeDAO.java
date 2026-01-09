package com.cware.netshopping.pawemp.exchange.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;

@Service("pawemp.exchange.paWempExchangeDAO")
public class PaWempExchangeDAO extends AbstractPaDAO{
	
	/**
	 * 교환신청 목록 교환 배송 상품 정보 조회
	 *
	 * @param exchangeitemlist
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectExchangeGoodsdt(String productNo) throws Exception {
		return list("pawemp.exchange.selectExchangeGoodsdt", productNo);
	}
	
	/**
	 * 교환접수 대상조회 
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Paorderm> selectOrderChangeTargetList() throws Exception{
		return list("pawemp.exchange.selectOrderChangeTargetList", null);
	}
	
	/**
	 * 위메프 교환접수 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception{
		return list("pawemp.exchange.selectOrderChangeTargetDtList", paramMap.get());
	}
	
	/**
	 * 교환접수 결과처리 - 교환거부처리 parameter 조회.
	 * @param String
	 * @return HPaorderm
	 * @throws Exception
	 */
	public Paorderm selectPaOrdermInfo(String mappingSeq) throws Exception {
		return (Paorderm) selectByPk("pawemp.exchange.selectPaOrdermInfo", mappingSeq);
	}
	
	/**
	 * 재고부족 교환보류후 CHANGE_FLAG = '06' 업데이트
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrdermChangeFlag(ParamMap paramMap) throws Exception{
		return update("pawemp.exchange.updatePaOrdermChangeFlag", paramMap.get());
	}
	
	/**
	 * 교환철회 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Paorderm> selectChangeCancelTargetList() throws Exception{
		return list("pawemp.exchange.selectChangeCancelTargetList", null);
	}
	
	/**
	 * 교환철회 대상 상세 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception{
		return list("pawemp.exchange.selectChangeCancelTargetDtList", paramMap.get());
	}
	
	/**
	 * 교환회수 수거요청 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectExchangePickupList() throws Exception{
		return list("pawemp.exchange.selectExchangePickupList", null);
	}
	
	/**
	 * 수거요청 전송성공
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updatePickupProc(Paorderm paorderm) throws Exception{
		return update("pawemp.exchange.updatePickupProc", paorderm);
	}
	
	/**
	 * API연동 오류
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updateProcFail(Paorderm paorderm) throws Exception{
		return update("pawemp.exchange.updateProcFail", paorderm);
	}

	/**
	 * 수거요청 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectPickupCompleteList() throws Exception{
		return list("pawemp.exchange.selectPickupCompleteList", null);
	}
	
	/**
	 * 교환회수 수거완료 전송송공
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updatePickupCompleteProc(HashMap<String, Object> hashMap) throws Exception{
		return update("pawemp.exchange.updatePickupCompleteProc", hashMap);
	}

	/**
	 * 교환 배송등록 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectExchangeSlipOutTargetList() throws Exception {
		return list("pawemp.exchange.selectExchangeSlipOutTargetList", null);
	}
	
	/**
	 * 교환회수 클레임 완료
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updatePickupEnd(HashMap<String, Object> exchangeCompleteVo) throws Exception{
		return update("pawemp.exchange.updatePickupEnd", exchangeCompleteVo);
	}
	
	/**
	 * 교환배송 등록성공
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updateDeliveryProc(HashMap<String, Object> exchageDeliveryVo) throws Exception{
		return update("pawemp.exchange.updateDeliveryProc", exchageDeliveryVo);
	}

	/**
	 * 교환배송 클레임완료 전송성공
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updateExchangeCompleteProc(HashMap<String, Object> exchangeCompleteVo) throws Exception{
		return update("pawemp.exchange.updateExchangeCompleteProc", exchangeCompleteVo);
	}
	
	/**
	 * 교환배송완료 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectExchangeCompleteList() throws Exception{
		return list("pawemp.exchange.selectExchangeCompleteList", null);
	}
}
