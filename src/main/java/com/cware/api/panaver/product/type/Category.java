package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Category", propOrder = {
    "category",
})
public class Category {
	
	@XmlElement(name = "CategoryListType")
    protected CategoryListType categoryListType;

	public CategoryListType getCategoryListType() {
		return categoryListType;
	}

	public void setCategoryListType(CategoryListType categoryListType) {
		this.categoryListType = categoryListType;
	}

}
