package com.cware.netshopping.pacommon.claim.service;

import java.util.HashMap;
import com.cware.netshopping.domain.OrderClaimVO;

public interface PaClaimService {

	/**
	 * 제휴사 반품접수
	 * @param orderClaimVO
	 * @return HashMap<String, Object>
	 * @throws Exception
	 */
	HashMap<String, Object> saveOrderClaimTx(OrderClaimVO orderClaimVO) throws Exception;
	
	/**
	 * 제휴사 반품취소
	 * @param orderClaimVO
	 * @return HashMap<String, Object>
	 * @throws Exception
	 */
	HashMap<String, Object> saveClaimCancelTx(OrderClaimVO orderClaimVO) throws Exception;

	/**
	 * 제휴사 교환접수
	 * @param orderClaimVO
	 * @return HashMap<String, Object>
	 * @throws Exception
	 */
	HashMap<String, Object>[] saveOrderChangeTx(OrderClaimVO[] orderClaimVO) throws Exception;
	
	/**
	 * 제휴사 교환취소
	 * @param orderClaimVO
	 * @return HashMap<String, Object>
	 * @throws Exception
	 */
	HashMap<String, Object>[] saveChangeCancelTx(OrderClaimVO[] orderClaimVO) throws Exception;

	
}