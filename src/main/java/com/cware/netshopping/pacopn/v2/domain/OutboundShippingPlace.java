package com.cware.netshopping.pacopn.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OutboundShippingPlace {
	
	//	판매자 ID
	//	쿠팡에서 업체에게 발급한 고유 코드
	//	예) A00012345
	String vendorId; 

	// 사용자 아이디(쿠팡 WING 로그인 계정)
	String userId;
	
	// 출고지 코드
	// "null"인 경우, 출고지이름은 변하지 않음
	String outboundShippingPlaceCode;

	// 출고지 이름, 최대 50 자
	// 동일한 명칭의 출고지 중복 등록 불가
	String shippingPlaceName;

	// 사용가능여부
	// true: 사용가능
	// false: 사용불가
	// 기본값: true
	boolean usable;

	// 기본값: false
	// 국내(domestic) or 해외(overseas)
	// false: 국내(domestic)
	// true: 해외(overseas)
	boolean global;
	
	// 출고지 주소
	List<PlaceAddress> placeAddresses;
	
	// 도서산간 추가배송비
	List<RemoteInfo> remoteInfos;

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOutboundShippingPlaceCode() {
		return outboundShippingPlaceCode;
	}

	public void setOutboundShippingPlaceCode(String outboundShippingPlaceCode) {
		this.outboundShippingPlaceCode = outboundShippingPlaceCode;
	}
	
	public String getShippingPlaceName() {
		return shippingPlaceName;
	}

	public void setShippingPlaceName(String shippingPlaceName) {
		this.shippingPlaceName = shippingPlaceName;
	}

	public boolean isUsable() {
		return usable;
	}

	public void setUsable(boolean usable) {
		this.usable = usable;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public List<PlaceAddress> getPlaceAddresses() {
		return placeAddresses;
	}

	public void setPlaceAddresses(List<PlaceAddress> placeAddresses) {
		this.placeAddresses = placeAddresses;
	}

	public List<RemoteInfo> getRemoteInfos() {
		return remoteInfos;
	}

	public void setRemoteInfos(List<RemoteInfo> remoteInfos) {
		this.remoteInfos = remoteInfos;
	}

	@Override
	public String toString() {
		return "OutboundShippingPlace [vendorId=" + vendorId + ", userId=" + userId + ", outboundShippingPlaceCode="
				+ outboundShippingPlaceCode + ", shippingPlaceName=" + shippingPlaceName + ", usable=" + usable
				+ ", global=" + global + ", placeAddresses=" + placeAddresses + ", remoteInfos=" + remoteInfos + "]";
	}

}
