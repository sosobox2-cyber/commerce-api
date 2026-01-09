
package com.cware.netshopping.pa11st.claim.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11stclaimlistVO;
import com.cware.netshopping.domain.model.Paorderm;


@Service("pa11st.claim.pa11stClaimDAO")
public class Pa11stClaimDAO extends AbstractPaDAO{
	
	/**
	 * 11번가 반품신청목록 조회 - 데이터 중복 체크
	 * @param Pa11stclaimlistVO
	 * @return Integer
	 * @throws Exception
	 */
	public int selectPa11stClaimListExists(Pa11stclaimlistVO pa11stClaimList) throws Exception{
		return (Integer) selectByPk("pa11st.claim.selectPa11stClaimListExists", pa11stClaimList);
	}
	
	/**
	 * 11번가 반품완료목록 조회 - 데이터 중복 체크
	 * @param Pa11stclaimlistVO
	 * @return Integer
	 * @throws Exception
	 */
	public int selectPa11stClaim60(Pa11stclaimlistVO pa11stClaimList) throws Exception{
		return (Integer) selectByPk("pa11st.claim.selectPa11stClaim60", pa11stClaimList);
	}
	
	/**
	 * 11번가 반품신청목록 조회 - 데이터 저장[TPA11STCLAIMLIST]
	 * @param Pa11stclaimlistVO
	 * @return Integer
	 * @throws Exception
	 */
	public int insertPa11stClaimList(Pa11stclaimlistVO pa11stClaimList) throws Exception{
		return insert("pa11st.claim.insertPa11stClaimList", pa11stClaimList);
	}
	
	/**
	 * 11번가 반품승인처리 - 회수확정대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReturnOkList() throws Exception{
		return list("pa11st.claim.selectReturnOkList", null);
	}
	
	/**
	 * 11번가 반품승인처리 - TPAORDERM 발송처리 결과 UPDATE
	 * @param  Paorderm
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePaOrdermResult(Paorderm paorderm) throws Exception{
		return update("pa11st.claim.updatePaOrdermResult", paorderm);
	}
	
	/**
	 * 11번가  - 회수확정대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReturnHoldList() throws Exception{
		return list("pa11st.claim.selectReturnHoldList", null);
	}
	
	/**
	 * 11번가 반품완료보류 - TPAORDERM 보류여부 결과 UPDATE
	 * @param  Paorderm
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePaOrdermHoldYn(Paorderm paorderm) throws Exception{
		return update("pa11st.claim.updatePaOrdermHoldYn", paorderm);
	}
	
	/**
	 * 11번가 반품생성 대상조회 
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderClaimTargetList() throws Exception{
		return list("pa11st.claim.selectOrderClaimTargetList", null);
	}

	/**
	 * 11번가 반품접수 대상조회 - 상세(반품접수건)
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception{
		return list("pa11st.claim.selectOrderCalimTargetDt30List", paramMap.get());
	}
	
	/**
	 * 11번가 반품접수 대상조회 - 상세(출하지시 이후 취소건)
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception{
		return list("pa11st.claim.selectOrderCalimTargetDt20List", paramMap.get());
	}

	/**
	 * 11번가 반품취소 대상조회 
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetList() throws Exception{
		return list("pa11st.claim.selectClaimCancelTargetList", null);
	}
	
	/**
	 * 11번가 반품취소 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception{
		return list("pa11st.claim.selectClaimCancelTargetDtList", paramMap.get());
	}
	
	/**
	 * 11번가 반품/교환 취소 처리 - 기취소
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception{
		return update("pa11st.claim.updatePreCancelYn", preCancelMap.get());
	}
	
	/**
	 * 기존 배송지와 비교
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String compareAddress(ParamMap paramMap) throws Exception {
		return (String) selectByPk("pa11st.claim.compareAddress", paramMap.get());
	}
	
	/**
	 * 반품송장입력 - 반품송장등록대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReturnSlipProcList() throws Exception{
		return list("pa11st.claim.selectReturnSlipProcList", null);
	}
}