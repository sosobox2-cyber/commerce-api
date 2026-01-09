package com.cware.netshopping.pacopn.common.process;

import java.util.List;
import java.util.Map;

import com.cware.netshopping.domain.model.PaCopnCertification;
import com.cware.netshopping.domain.model.PaCopnDocment;
import com.cware.netshopping.domain.model.PaCopnOption;
import com.cware.netshopping.domain.model.PaEntpSlip;

public interface PaCopnCommonProcess {

	/**
	 * 제휴 쿠팡 - 카테고리 메타 정보 저장
	 * @param Lsit<PaCopnOption>
	 * @param List<PaCopnDocment>
	 * @param List<PaCopnCertification>
	 * @return String
	 * @throws Exception
	 */
	public String savePaCopnCategoryMetaInfo(List<PaCopnOption> attrList, List<PaCopnDocment> docsList, List<PaCopnCertification> certiList) throws Exception;

	/**
	 * 출고지 상세정보 조회
	 * 
	 * @param PaEntpSlip
	 * @return HashMap
	 * @throws Exception
	 */
	public Map<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * 출고지 전송결과 저장
	 * 
	 * @param PaEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public String savePaCopnEntpSlip(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * 출고지 수정대상 리스트 조회
	 * 
	 * @param String
	 * @return List
	 * @throws Exception
	 */
	public List<Object> selectEntpShipUpdateList(String paAddrGb) throws Exception;

	/**
	 * 출고지 수정대상 전송결과 저장
	 * 
	 * @param PaEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public String updatePaCopnEntpSlip(PaEntpSlip paEntpSlip) throws Exception;

}
