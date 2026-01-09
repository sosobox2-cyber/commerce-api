package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ModificationRequiredProductType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModificationRequiredProductType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProductId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="Reason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Action" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RestorationRequestAvailable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModificationRequiredProductType", propOrder = {
	    "productId",
	    "reason",
	    "action",
	    "restorationRequestAvailable"
})
public class ModificationRequiredProductType {

    @XmlElement(name = "ProductId")
    protected String productId;
    @XmlElement(name = "Reason")
    protected String reason;
    @XmlElement(name = "Action")
    protected String action;
    @XmlElement(name = "RestorationRequestAvailable")
    protected String restorationRequestAvailable;
    
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getRestorationRequestAvailable() {
		return restorationRequestAvailable;
	}
	public void setRestorationRequestAvailable(String restorationRequestAvailable) {
		this.restorationRequestAvailable = restorationRequestAvailable;
	}

}
