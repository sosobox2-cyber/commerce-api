package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

import java.sql.Timestamp;

public class Couponissue extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String seq;
	private String promoNo;
	private String custNo;
	private String getOrderNo;
	private String useYn;
	private String useOrderNo;
	private String cancelYn;
	private Timestamp useStartDate;
	private Timestamp useEndDate;
	private String remark;

	public String getSeq() { 
		return this.seq;
	}
	public String getPromoNo() { 
		return this.promoNo;
	}
	public String getCustNo() { 
		return this.custNo;
	}
	public String getGetOrderNo() { 
		return this.getOrderNo;
	}
	public String getUseYn() { 
		return this.useYn;
	}
	public String getUseOrderNo() { 
		return this.useOrderNo;
	}
	public String getCancelYn() { 
		return this.cancelYn;
	}
	public Timestamp getUseStartDate() { 
		return this.useStartDate;
	}
	public Timestamp getUseEndDate() { 
		return this.useEndDate;
	}
	public String getRemark() { 
		return this.remark;
	}

	public void setSeq(String seq) { 
		this.seq = seq;
	}
	public void setPromoNo(String promoNo) { 
		this.promoNo = promoNo;
	}
	public void setCustNo(String custNo) { 
		this.custNo = custNo;
	}
	public void setGetOrderNo(String getOrderNo) { 
		this.getOrderNo = getOrderNo;
	}
	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	public void setUseOrderNo(String useOrderNo) { 
		this.useOrderNo = useOrderNo;
	}
	public void setCancelYn(String cancelYn) { 
		this.cancelYn = cancelYn;
	}
	public void setUseStartDate(Timestamp useStartDate) { 
		this.useStartDate = useStartDate;
	}
	public void setUseEndDate(Timestamp useEndDate) { 
		this.useEndDate = useEndDate;
	}
	public void setRemark(String remark) { 
		this.remark = remark;
	}
}
