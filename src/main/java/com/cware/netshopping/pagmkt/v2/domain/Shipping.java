package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Shipping {

	// 배송방법 타입
	// 배송방식 타입 입력
	// 1 : 택배소포 (G마켓 단독 등록시 무조건 1번만 설정 가능)
	// 2 : 화물배달 (동시등록, 옥션만 등록시 2번 설정 가능)
	// 3 : 판매자직접배송 (옥션 3번 선택시, 일반우편, 퀵서비스, 방문수령 중 선택 필요, 22년 2월 8일부터 사용 불가)
	// *판매자직접배송 선택시 가격비교 사이트 및 구매자에게 배송비 무료로 안내됨
	private int type;

	// 택배사코드
	// 1 : 배송방법 타입 1번 선택 시 입력
	// 2 : 택배사코드 리스트는 발송처리 페이지 내 택배사코드 리스트 참조
	// 3 : 허용 불가 택배사 코드 입력시 기타택배로 처리됨
	// ( https://etapi.ebaykorea.com/order-delivery/orders-docid-789/)
	private Integer companyNo;

	// 출하지 정보
	private Policy policy;

	// 회수지 정보
	private ReturnAndExchange returnAndExchange;

	// 발송 타입 정책 번호
	private SiteNoValue dispatchPolicyNo;

	// 일반 우편 (사용X)
	// 22년 2월 8일부터 사용 중단
	private Object generalPost;

	// 방문수령 제공여부
	// 사용하지 않을 경우 Entity null로 보내도 무관
	private Object visitAndTake;

	// 퀵서비스 제공여부
	// 사용하지 않을 경우 Entity null로 보내도 무관
	private Object quickService;

	// 제주/도서산간배송불가여부
	// (특수카테고리/상품제한기능)
	// 제주/도서산간배송불가여부 설정
	// Y : 배송가능
	// N : 배송불가
	// *권한제한 항목
	private String backwoodsDeliveryYn;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(Integer companyNo) {
		this.companyNo = companyNo;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public ReturnAndExchange getReturnAndExchange() {
		return returnAndExchange;
	}

	public void setReturnAndExchange(ReturnAndExchange returnAndExchange) {
		this.returnAndExchange = returnAndExchange;
	}

	public SiteNoValue getDispatchPolicyNo() {
		return dispatchPolicyNo;
	}

	public void setDispatchPolicyNo(SiteNoValue dispatchPolicyNo) {
		this.dispatchPolicyNo = dispatchPolicyNo;
	}

	public Object getGeneralPost() {
		return generalPost;
	}

	public void setGeneralPost(Object generalPost) {
		this.generalPost = generalPost;
	}

	public Object getVisitAndTake() {
		return visitAndTake;
	}

	public void setVisitAndTake(Object visitAndTake) {
		this.visitAndTake = visitAndTake;
	}

	public Object getQuickService() {
		return quickService;
	}

	public void setQuickService(Object quickService) {
		this.quickService = quickService;
	}

	public String getBackwoodsDeliveryYn() {
		return backwoodsDeliveryYn;
	}

	public void setBackwoodsDeliveryYn(String backwoodsDeliveryYn) {
		this.backwoodsDeliveryYn = backwoodsDeliveryYn;
	}

	@Override
	public String toString() {
		return "Shipping [type=" + type + ", companyNo=" + companyNo + ", policy=" + policy + ", returnAndExchange="
				+ returnAndExchange + ", dispatchPolicyNo=" + dispatchPolicyNo + ", generalPost=" + generalPost
				+ ", visitAndTake=" + visitAndTake + ", quickService=" + quickService + ", backwoodsDeliveryYn="
				+ backwoodsDeliveryYn + "]";
	}

}
