package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetProductListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetProductListResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="ProductList" type="{http://shopn.platform.nhncorp.com/}ProductListMapType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Page" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TotalPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetProductListResponseType", propOrder = {
    "productList",
    "page",
    "totalPage"
})
public class GetProductListResponseType extends BaseProductResponseType {
	
	@XmlElement(name = "ProductList", namespace = "")
    protected List<ProductListMapType> productList;
	@XmlElement(name = "Page", namespace = "")
    protected int page;
	@XmlElement(name = "TotalPage", namespace = "")
    protected int totalPage;
	
	public List<ProductListMapType> getProductList() {
		if (productList == null) {
			productList = new ArrayList<ProductListMapType>();
        }
		return this.productList;
	}
	public int getPage() {
		return page;
	}
	public int getTotalPage() {
		return totalPage;
	}
}
