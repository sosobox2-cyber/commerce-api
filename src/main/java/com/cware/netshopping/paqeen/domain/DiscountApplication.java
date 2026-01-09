package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class DiscountApplication {
	private String id;
	private String state;
	private String applicationType;
	private String discountMethod;
	private String orderLineId;
	private Integer appliedDiscountAmount;
	private DutyRatio dutyRatio;
	private DutyAmount dutyAmount;
	private Integer directCouponId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	public String getDiscountMethod() {
		return discountMethod;
	}
	public void setDiscountMethod(String discountMethod) {
		this.discountMethod = discountMethod;
	}
	public String getOrderLineId() {
		return orderLineId;
	}
	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}
	public Integer getAppliedDiscountAmount() {
		return appliedDiscountAmount;
	}
	public void setAppliedDiscountAmount(Integer appliedDiscountAmount) {
		this.appliedDiscountAmount = appliedDiscountAmount;
	}
	public DutyRatio getDutyRatio() {
		return dutyRatio;
	}
	public void setDutyRatio(DutyRatio dutyRatio) {
		this.dutyRatio = dutyRatio;
	}
	public DutyAmount getDutyAmount() {
		return dutyAmount;
	}
	public void setDutyAmount(DutyAmount dutyAmount) {
		this.dutyAmount = dutyAmount;
	}
	public Integer getDirectCouponId() {
		return directCouponId;
	}
	public void setDirectCouponId(Integer directCouponId) {
		this.directCouponId = directCouponId;
	}
}
