package com.cware.netshopping.pacommon.counsel.service;

import java.util.List;

import com.cware.netshopping.domain.model.Paqnamoment;

public interface PaCounselService {

	/**
	 * 제휴 - 상담 문의 저장
	 * @param List<Paqnamoment>
	 * @return String
	 * @throws Exception
	 */
	public String savePaQnaTx(List<Paqnamoment> paQnaMomentList, String msgGb) throws Exception;
}
