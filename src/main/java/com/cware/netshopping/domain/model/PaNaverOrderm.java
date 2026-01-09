package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaNaverOrderm extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private Timestamp orderDate;
	private String orderId;
	private String ordererId;
	private String ordererName;
	private String ordererTel1;
	private String ordererTel2;
	private Timestamp paymentDate;
	private String paymentMeans;
	private Timestamp paymentDueDate;
	private long orderDiscountAmt;
	private long generalPaymentAmt;
	private long naverPaymentAmt;
	private long chargeAmtPaymentAmt;
	private long accumulationPaymentAmt;
	private String isDeliveryMemoInput;
	private String payLocationType;
	private String ordererNo;

	public Timestamp getOrderDate() { 
		return this.orderDate;
	}
	public String getOrderId() { 
		return this.orderId;
	}
	public String getOrdererId() { 
		return this.ordererId;
	}
	public String getOrdererName() { 
		return this.ordererName;
	}
	public String getOrdererTel1() { 
		return this.ordererTel1;
	}
	public String getOrdererTel2() { 
		return this.ordererTel2;
	}
	public Timestamp getPaymentDate() { 
		return this.paymentDate;
	}
	public String getPaymentMeans() { 
		return this.paymentMeans;
	}
	public Timestamp getPaymentDueDate() { 
		return this.paymentDueDate;
	}
	public long getOrderDiscountAmt() { 
		return this.orderDiscountAmt;
	}
	public long getGeneralPaymentAmt() { 
		return this.generalPaymentAmt;
	}
	public long getNaverPaymentAmt() { 
		return this.naverPaymentAmt;
	}
	public long getChargeAmtPaymentAmt() { 
		return this.chargeAmtPaymentAmt;
	}
	public long getAccumulationPaymentAmt() { 
		return this.accumulationPaymentAmt;
	}
	public String getIsDeliveryMemoInput() { 
		return this.isDeliveryMemoInput;
	}
	public String getPayLocationType() { 
		return this.payLocationType;
	}
	public String getOrdererNo() { 
		return this.ordererNo;
	}

	public void setOrderDate(Timestamp orderDate) { 
		this.orderDate = orderDate;
	}
	public void setOrderId(String orderId) { 
		this.orderId = orderId;
	}
	public void setOrdererId(String ordererId) { 
		this.ordererId = ordererId;
	}
	public void setOrdererName(String ordererName) { 
		this.ordererName = ordererName;
	}
	public void setOrdererTel1(String ordererTel1) { 
		this.ordererTel1 = ordererTel1;
	}
	public void setOrdererTel2(String ordererTel2) { 
		this.ordererTel2 = ordererTel2;
	}
	public void setPaymentDate(Timestamp paymentDate) { 
		this.paymentDate = paymentDate;
	}
	public void setPaymentMeans(String paymentMeans) { 
		this.paymentMeans = paymentMeans;
	}
	public void setPaymentDueDate(Timestamp paymentDueDate) { 
		this.paymentDueDate = paymentDueDate;
	}
	public void setOrderDiscountAmt(long orderDiscountAmt) { 
		this.orderDiscountAmt = orderDiscountAmt;
	}
	public void setGeneralPaymentAmt(long generalPaymentAmt) { 
		this.generalPaymentAmt = generalPaymentAmt;
	}
	public void setNaverPaymentAmt(long naverPaymentAmt) { 
		this.naverPaymentAmt = naverPaymentAmt;
	}
	public void setChargeAmtPaymentAmt(long chargeAmtPaymentAmt) { 
		this.chargeAmtPaymentAmt = chargeAmtPaymentAmt;
	}
	public void setAccumulationPaymentAmt(long accumulationPaymentAmt) { 
		this.accumulationPaymentAmt = accumulationPaymentAmt;
	}
	public void setIsDeliveryMemoInput(String isDeliveryMemoInput) { 
		this.isDeliveryMemoInput = isDeliveryMemoInput;
	}
	public void setPayLocationType(String payLocationType) { 
		this.payLocationType = payLocationType;
	}
	public void setOrdererNo(String ordererNo) { 
		this.ordererNo = ordererNo;
	}
}
