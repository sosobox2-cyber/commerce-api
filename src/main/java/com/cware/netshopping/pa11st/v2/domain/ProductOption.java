package com.cware.netshopping.pa11st.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductOption {

	// 옵션상태 (필수)
	// 멀티옵션일 경우는 지원하지 않는 기능입니다.
	// Y : 사용함
	// N : 품절
	private String useYn;

	// 옵션가 (필수)
	// 기본 판매가의 +100%/-50%까지 설정하실 수 있습니다.
	// 옵션가격이 0원인 상품이 반드시 1개 이상 있어야 합니다.
	private String colOptPrice;

	// 옵션값 (필수)
	// 20Byte 까지만 입력가능하며 특수 문자[&#39;,",%,&,<,>,#,†]는 입력할 수 없습니다.
	// 한 상품안에서 옵션값은 중복이 될수 없습니다.
	// * 11번가 단일상품의 경우 옵션 값 20byte까지 지정 가능합니다.
	private String colValue0;

	// 옵션재고수량 (필수)
	// 멀티옵션일 경우는 일괄설정이 되므로 입력하시면 안됩니다.
	// 옵션상태(useYn)가 N일 때만 0 입력 가능합니다.
	private String colCount;

	// 셀러재고번호 (필수)
	// 셀러가 사용하는 재고번호
	private String colSellerStockCd;

	// 선택형 멀티옵션 재고수량 (필수)
	// 옵션 조합여부 구분 값을 N으로 사용하는 경우, 해당 컬럼을 이용하여 재고수량의 입력이 가능합니다.
	private String colOptCount;

	// 옵션추가무게 (필수)
	// 배송 주체(dlvClf) 코드가 03(11번가 해외 배송)인 경우 또는 "전세계배송 상품" 인경우 옵션 등록시 필수입니다.
	// 단위 g
	private String optWght;

	// 옵션매핑Key (필수)
	// 멀티옵션의 조합된 옵션을 매핑하기 위한 Key(예: 옵션명1:옵션값1†옵션명2:옵션값2)
	// * 11번가 단일상품의 경우 표준옵션만 허용 가능합니다.
	// 허용 카테고리/옵션 참조 : http://image.11st.co.kr/product/sell/optionInfoByCategory.xls
	private String optionMappingKey;

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getColOptPrice() {
		return colOptPrice;
	}

	public void setColOptPrice(String colOptPrice) {
		this.colOptPrice = colOptPrice;
	}

	public String getColValue0() {
		return colValue0;
	}

	public void setColValue0(String colValue0) {
		this.colValue0 = colValue0;
	}

	public String getColCount() {
		return colCount;
	}

	public void setColCount(String colCount) {
		this.colCount = colCount;
	}

	public String getColSellerStockCd() {
		return colSellerStockCd;
	}

	public void setColSellerStockCd(String colSellerStockCd) {
		this.colSellerStockCd = colSellerStockCd;
	}

	public String getColOptCount() {
		return colOptCount;
	}

	public void setColOptCount(String colOptCount) {
		this.colOptCount = colOptCount;
	}

	public String getOptWght() {
		return optWght;
	}

	public void setOptWght(String optWght) {
		this.optWght = optWght;
	}

	public String getOptionMappingKey() {
		return optionMappingKey;
	}

	public void setOptionMappingKey(String optionMappingKey) {
		this.optionMappingKey = optionMappingKey;
	}

	@Override
	public String toString() {
		return "ProductOption [useYn=" + useYn + ", colOptPrice=" + colOptPrice + ", colValue0=" + colValue0
				+ ", colCount=" + colCount + ", colSellerStockCd=" + colSellerStockCd + ", colOptCount=" + colOptCount
				+ ", optWght=" + optWght + ", optionMappingKey=" + optionMappingKey + "]";
	}

}
