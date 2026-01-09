package com.cware.netshopping.pacommon.claim.process;

import java.util.HashMap;
import com.cware.netshopping.domain.OrderClaimVO;


public interface PaClaimProcess {

	HashMap<String, Object> saveOrderClaim(OrderClaimVO orderClaimVO) throws Exception;
	
	HashMap<String, Object> saveClaimCancel(OrderClaimVO orderClaimVO) throws Exception;

	HashMap<String, Object>[] saveOrderChange(OrderClaimVO[] orderClaimVO) throws Exception;
	
	HashMap<String, Object>[] saveChangeCancel(OrderClaimVO[] orderClaimVO) throws Exception;
	
}