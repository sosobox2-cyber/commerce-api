package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DisplayCategories {
	
	// 4depth 카테고리 명
	private String depth4DisplayCategoryName;
	// 3depth 전시 여부 [ Y: Yes, N: No ]
	private String depth3DisplayYn;
	// 3depth 카테고리 명
	private String depth3DisplayCategoryName;
	// 2depth 카테고리 전시순서
	private int depth2DisplayOrder;
	// 5depth 카테고리 명
	private String depth5DisplayCategoryName;
	// 3depth 카테고리 아이콘
	private String depth3Icon;
	// 1depth 카테고리 전시순서
	private int depth1DisplayOrder;
	// 1depth 카테고리 번호
	private String depth1DisplayCategoryNo;
	// 2depth 카테고리 아이콘
	private String depth2Icon;
	// 1depth 카테고리 명
	private String depth1DisplayCategoryName;
	// 2depth 카테고리 명
	private String depth2DisplayCategoryName;
	// 4depth 카테고리 상단 이미지
	private String depth4TopImageContent;
	// 3depth 카테고리 상단 이미지
	private String depth3TopImageContent;
	// 2depth 전시 여부 [ Y: Yes, N: No ]
	private String depth2DisplayYn;
	// 4depth 카테고리 아이콘
	private String depth4Icon;
	// 5depth 전시 여부 [ Y: Yes, N: No ]
	private String depth5DisplayYn;
	// 2depth 카테고리 번호
	private String depth2DisplayCategoryNo;
	// 4depth 카테고리 번호
	private String depth4DisplayCategoryNo;
	// 2depth 카테고리 상단 이미지
	private String depth2TopImageContent;
	// 5depth 카테고리 아이콘
	private String depth5Icon;
	// 4depth 전시 여부 [ Y: Yes, N: No ]
	private String depth4DisplayYn;
	// 5depth 카테고리 번호
	private String depth5DisplayCategoryNo;
	// 3depth 카테고리 번호
	private String depth3DisplayCategoryNo;
	// 1depth 카테고리 아이콘
	private String depth1Icon;
	// 전체 카테고리 명
	private String fullCategoryName;
	// 5depth 카테고리 전시순서
	private int depth5DisplayOrder;
	// 1depth 카테고리 상단 이미지
	private String depth1TopImageContent;
	// 4depth 카테고리 전시순서
	private int depth4DisplayOrder;
	// 5depth 카테고리 상단 이미지
	private String depth5TopImageContent;
	// 1depth 전시 여부 [ Y: Yes, N: No ]
	private String depth1DisplayYn;
	// 삭제 여부 (Y: 삭제됨, N:삭제되지 않음)
	private String deleteYn;
	// 3depth 카테고리 전시순서
	private int depth3DisplayOrder;
	
	public String getDepth4DisplayCategoryName() {
		return depth4DisplayCategoryName;
	}
	public void setDepth4DisplayCategoryName(String depth4DisplayCategoryName) {
		this.depth4DisplayCategoryName = depth4DisplayCategoryName;
	}
	public String getDepth3DisplayYn() {
		return depth3DisplayYn;
	}
	public void setDepth3DisplayYn(String depth3DisplayYn) {
		this.depth3DisplayYn = depth3DisplayYn;
	}
	public String getDepth3DisplayCategoryName() {
		return depth3DisplayCategoryName;
	}
	public void setDepth3DisplayCategoryName(String depth3DisplayCategoryName) {
		this.depth3DisplayCategoryName = depth3DisplayCategoryName;
	}
	public int getDepth2DisplayOrder() {
		return depth2DisplayOrder;
	}
	public void setDepth2DisplayOrder(int depth2DisplayOrder) {
		this.depth2DisplayOrder = depth2DisplayOrder;
	}
	public String getDepth5DisplayCategoryName() {
		return depth5DisplayCategoryName;
	}
	public void setDepth5DisplayCategoryName(String depth5DisplayCategoryName) {
		this.depth5DisplayCategoryName = depth5DisplayCategoryName;
	}
	public String getDepth3Icon() {
		return depth3Icon;
	}
	public void setDepth3Icon(String depth3Icon) {
		this.depth3Icon = depth3Icon;
	}
	public int getDepth1DisplayOrder() {
		return depth1DisplayOrder;
	}
	public void setDepth1DisplayOrder(int depth1DisplayOrder) {
		this.depth1DisplayOrder = depth1DisplayOrder;
	}
	public String getDepth1DisplayCategoryNo() {
		return depth1DisplayCategoryNo;
	}
	public void setDepth1DisplayCategoryNo(String depth1DisplayCategoryNo) {
		this.depth1DisplayCategoryNo = depth1DisplayCategoryNo;
	}
	public String getDepth2Icon() {
		return depth2Icon;
	}
	public void setDepth2Icon(String depth2Icon) {
		this.depth2Icon = depth2Icon;
	}
	public String getDepth1DisplayCategoryName() {
		return depth1DisplayCategoryName;
	}
	public void setDepth1DisplayCategoryName(String depth1DisplayCategoryName) {
		this.depth1DisplayCategoryName = depth1DisplayCategoryName;
	}
	public String getDepth2DisplayCategoryName() {
		return depth2DisplayCategoryName;
	}
	public void setDepth2DisplayCategoryName(String depth2DisplayCategoryName) {
		this.depth2DisplayCategoryName = depth2DisplayCategoryName;
	}
	public String getDepth4TopImageContent() {
		return depth4TopImageContent;
	}
	public void setDepth4TopImageContent(String depth4TopImageContent) {
		this.depth4TopImageContent = depth4TopImageContent;
	}
	public String getDepth3TopImageContent() {
		return depth3TopImageContent;
	}
	public void setDepth3TopImageContent(String depth3TopImageContent) {
		this.depth3TopImageContent = depth3TopImageContent;
	}
	public String getDepth2DisplayYn() {
		return depth2DisplayYn;
	}
	public void setDepth2DisplayYn(String depth2DisplayYn) {
		this.depth2DisplayYn = depth2DisplayYn;
	}
	public String getDepth4Icon() {
		return depth4Icon;
	}
	public void setDepth4Icon(String depth4Icon) {
		this.depth4Icon = depth4Icon;
	}
	public String getDepth5DisplayYn() {
		return depth5DisplayYn;
	}
	public void setDepth5DisplayYn(String depth5DisplayYn) {
		this.depth5DisplayYn = depth5DisplayYn;
	}
	public String getDepth2DisplayCategoryNo() {
		return depth2DisplayCategoryNo;
	}
	public void setDepth2DisplayCategoryNo(String depth2DisplayCategoryNo) {
		this.depth2DisplayCategoryNo = depth2DisplayCategoryNo;
	}
	public String getDepth4DisplayCategoryNo() {
		return depth4DisplayCategoryNo;
	}
	public void setDepth4DisplayCategoryNo(String depth4DisplayCategoryNo) {
		this.depth4DisplayCategoryNo = depth4DisplayCategoryNo;
	}
	public String getDepth2TopImageContent() {
		return depth2TopImageContent;
	}
	public void setDepth2TopImageContent(String depth2TopImageContent) {
		this.depth2TopImageContent = depth2TopImageContent;
	}
	public String getDepth5Icon() {
		return depth5Icon;
	}
	public void setDepth5Icon(String depth5Icon) {
		this.depth5Icon = depth5Icon;
	}
	public String getDepth4DisplayYn() {
		return depth4DisplayYn;
	}
	public void setDepth4DisplayYn(String depth4DisplayYn) {
		this.depth4DisplayYn = depth4DisplayYn;
	}
	public String getDepth5DisplayCategoryNo() {
		return depth5DisplayCategoryNo;
	}
	public void setDepth5DisplayCategoryNo(String depth5DisplayCategoryNo) {
		this.depth5DisplayCategoryNo = depth5DisplayCategoryNo;
	}
	public String getDepth3DisplayCategoryNo() {
		return depth3DisplayCategoryNo;
	}
	public void setDepth3DisplayCategoryNo(String depth3DisplayCategoryNo) {
		this.depth3DisplayCategoryNo = depth3DisplayCategoryNo;
	}
	public String getDepth1Icon() {
		return depth1Icon;
	}
	public void setDepth1Icon(String depth1Icon) {
		this.depth1Icon = depth1Icon;
	}
	public String getFullCategoryName() {
		return fullCategoryName;
	}
	public void setFullCategoryName(String fullCategoryName) {
		this.fullCategoryName = fullCategoryName;
	}
	public int getDepth5DisplayOrder() {
		return depth5DisplayOrder;
	}
	public void setDepth5DisplayOrder(int depth5DisplayOrder) {
		this.depth5DisplayOrder = depth5DisplayOrder;
	}
	public String getDepth1TopImageContent() {
		return depth1TopImageContent;
	}
	public void setDepth1TopImageContent(String depth1TopImageContent) {
		this.depth1TopImageContent = depth1TopImageContent;
	}
	public int getDepth4DisplayOrder() {
		return depth4DisplayOrder;
	}
	public void setDepth4DisplayOrder(int depth4DisplayOrder) {
		this.depth4DisplayOrder = depth4DisplayOrder;
	}
	public String getDepth5TopImageContent() {
		return depth5TopImageContent;
	}
	public void setDepth5TopImageContent(String depth5TopImageContent) {
		this.depth5TopImageContent = depth5TopImageContent;
	}
	public String getDepth1DisplayYn() {
		return depth1DisplayYn;
	}
	public void setDepth1DisplayYn(String depth1DisplayYn) {
		this.depth1DisplayYn = depth1DisplayYn;
	}
	public String getDeleteYn() {
		return deleteYn;
	}
	public void setDeleteYn(String deleteYn) {
		this.deleteYn = deleteYn;
	}
	public int getDepth3DisplayOrder() {
		return depth3DisplayOrder;
	}
	public void setDepth3DisplayOrder(int depth3DisplayOrder) {
		this.depth3DisplayOrder = depth3DisplayOrder;
	}
	
	@Override
	public String toString() {
		return "DisplayCategories [depth4DisplayCategoryName=" + depth4DisplayCategoryName + ", depth3DisplayYn="
				+ depth3DisplayYn + ", depth3DisplayCategoryName=" + depth3DisplayCategoryName + ", depth2DisplayOrder="
				+ depth2DisplayOrder + ", depth5DisplayCategoryName=" + depth5DisplayCategoryName + ", depth3Icon="
				+ depth3Icon + ", depth1DisplayOrder=" + depth1DisplayOrder + ", depth1DisplayCategoryNo="
				+ depth1DisplayCategoryNo + ", depth2Icon=" + depth2Icon + ", depth1DisplayCategoryName="
				+ depth1DisplayCategoryName + ", depth2DisplayCategoryName=" + depth2DisplayCategoryName
				+ ", depth4TopImageContent=" + depth4TopImageContent + ", depth3TopImageContent="
				+ depth3TopImageContent + ", depth2DisplayYn=" + depth2DisplayYn + ", depth4Icon=" + depth4Icon
				+ ", depth5DisplayYn=" + depth5DisplayYn + ", depth2DisplayCategoryNo=" + depth2DisplayCategoryNo
				+ ", depth4DisplayCategoryNo=" + depth4DisplayCategoryNo + ", depth2TopImageContent="
				+ depth2TopImageContent + ", depth5Icon=" + depth5Icon + ", depth4DisplayYn=" + depth4DisplayYn
				+ ", depth5DisplayCategoryNo=" + depth5DisplayCategoryNo + ", depth3DisplayCategoryNo="
				+ depth3DisplayCategoryNo + ", depth1Icon=" + depth1Icon + ", fullCategoryName=" + fullCategoryName
				+ ", depth5DisplayOrder=" + depth5DisplayOrder + ", depth1TopImageContent=" + depth1TopImageContent
				+ ", depth4DisplayOrder=" + depth4DisplayOrder + ", depth5TopImageContent=" + depth5TopImageContent
				+ ", depth1DisplayYn=" + depth1DisplayYn + ", deleteYn=" + deleteYn + ", depth3DisplayOrder="
				+ depth3DisplayOrder + "]";
	}
	
}
