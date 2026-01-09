package com.cware.netshopping.domain;

import com.cware.framework.core.basic.AbstractVO;

public class CopnGoodsDeleteVO extends AbstractVO {
	private static final long serialVersionUID = 1L;
	
	private int successCount;
    private int errorCount;
    
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public int getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	
    
}
