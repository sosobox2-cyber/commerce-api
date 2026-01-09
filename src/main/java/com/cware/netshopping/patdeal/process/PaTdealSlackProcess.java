package com.cware.netshopping.patdeal.process;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cware.netshopping.domain.model.PaTdealSlackGoods;

public interface PaTdealSlackProcess {
	
	/**
	 * 상품 변경 정보 Tdeal slack 공유
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<PaTdealSlackGoods> slackTransferTdealList(HttpServletRequest request) throws Exception; 
}
