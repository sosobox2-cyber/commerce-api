package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetProductListRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetProductListRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductRequestType">
 *       &lt;sequence>
 *         &lt;element name="SellerId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProductId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="SellerManagementCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StatusTypeList" type="{http://shopn.platform.nhncorp.com/}StatusType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Page" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PageSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OrderType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PeriodType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FromDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ToDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 * 	 &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetProductListRequestType")
public class GetProductListRequestType extends BaseProductRequestType {
	@XmlElement(name = "SellerId", namespace = "")
    protected String sellerId;
	@XmlElement(name = "ProductId", namespace = "")
    protected long productId;
	@XmlElement(name = "SellerManagementCode", namespace = "")
    protected String sellerManagementCode;
	@XmlElement(name = "StatusTypeList", namespace = "")
    protected List<StatusType> statusTypeList;
	@XmlElement(name = "Page", namespace = "")
    protected int page;
	@XmlElement(name = "PageSize", namespace = "")
    protected int pageSize;
	@XmlElement(name = "OrderType", namespace = "")
    protected String orderType;
	@XmlElement(name = "PeriodType", namespace = "")
    protected String periodType;
	@XmlElement(name = "FromDate", namespace = "")
    protected String fromDate;
	@XmlElement(name = "ToDate", namespace = "")
    protected String toDate;
	
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getSellerManagementCode() {
		return sellerManagementCode;
	}
	public void setSellerManagementCode(String sellerManagementCode) {
		this.sellerManagementCode = sellerManagementCode;
	}
	public List<StatusType> getStatusTypeList() {
		if (statusTypeList == null) {
			statusTypeList = new ArrayList<StatusType>();
        }
		return statusTypeList;
	}
	public void setStatusTypeList(List<StatusType> statusTypeList) {
		this.statusTypeList = statusTypeList;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getPeriodType() {
		return periodType;
	}
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}	
}
