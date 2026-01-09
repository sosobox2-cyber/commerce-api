
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetQuestionAnswerListRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetQuestionAnswerListRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductRequestType">
 *       &lt;sequence>
 *         &lt;element name="SellerId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FromDate" type="{http://shopn.platform.nhncorp.com/}string"/>
 *         &lt;element name="ToDate" type="{http://shopn.platform.nhncorp.com/}string"/>
 *         &lt;element name="Answered" type="{http://shopn.platform.nhncorp.com/}string"/>
 *         &lt;element name="Page" type="{http://shopn.platform.nhncorp.com/}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetQuestionAnswerListRequestType", propOrder = {
    "sellerId",
    "fromDate",
    "toDate",
    "answered",
    "page"
})
public class GetQuestionAnswerListRequestType
    extends BaseProductRequestType
{

    @XmlElement(name = "SellerId", namespace = "", required = true)
    protected String sellerId;
    @XmlElement(name = "FromDate", namespace = "", required = true)
    protected String fromDate;
    @XmlElement(name = "ToDate", namespace = "", required = true)
    protected String toDate;
    @XmlElement(name = "Answered", namespace = "", required = true)
    protected String answered;
    @XmlElement(name = "Page", namespace = "", required = true)
    protected int page;
    
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
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
	public String getAnswered() {
		return answered;
	}
	public void setAnswered(String answered) {
		this.answered = answered;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
}
