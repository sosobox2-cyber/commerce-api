package com.cware.netshopping.panaver.v3.domain;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductOrderInfoAll {
		
	@JsonProperty("cancel")
	private CancelOrderInfo cancelOrder;
	
	@JsonProperty("delivery")
	private DeliveryOrderInfo deliveryOrder;
	
	@JsonProperty("exchange")
	private ExchangeOrderInfo exchangeOrder;
	
	@JsonProperty("order")
	private OrderInfo order;
	
	@JsonProperty("productOrder")
	private ProductOrderInfo productOrder;
	
	@JsonProperty("return")
	private ReturnOrderInfo returnOrder;

	public CancelOrderInfo getCancelOrder() {
		return cancelOrder;
	}

	public void setCancelOrder(CancelOrderInfo cancelOrder) {
		this.cancelOrder = cancelOrder;
	}

	public DeliveryOrderInfo getDeliveryOrder() {
		return deliveryOrder;
	}

	public void setDeliveryOrder(DeliveryOrderInfo deliveryOrder) {
		this.deliveryOrder = deliveryOrder;
	}

	public ExchangeOrderInfo getExchangeOrder() {
		return exchangeOrder;
	}

	public void setExchangeOrder(ExchangeOrderInfo exchangeOrder) {
		this.exchangeOrder = exchangeOrder;
	}

	public OrderInfo getOrder() {
		return order;
	}

	public void setOrder(OrderInfo order) {
		this.order = order;
	}

	public ProductOrderInfo getProductOrder() {
		return productOrder;
	}

	public void setProductOrder(ProductOrderInfo productOrder) {
		this.productOrder = productOrder;
	}

	public ReturnOrderInfo getReturnOrder() {
		return returnOrder;
	}

	public void setReturnOrder(ReturnOrderInfo returnOrder) {
		this.returnOrder = returnOrder;
	}

	@Override
	public String toString() {
		return "ProductOrderInfoList [cancelOrder=" + cancelOrder + ", deliveryOrder=" + deliveryOrder
				+ ", exchangeOrder=" + exchangeOrder + ", order=" + order + ", productOrder=" + productOrder
				+ ", returnOrder=" + returnOrder + "]";
	}

	
}
