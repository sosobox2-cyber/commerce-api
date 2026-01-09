package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for BaseProductResponseType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="BaseProductResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseResponseType">
 *       &lt;sequence>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseProductResponseType", propOrder = {
	"version",
	"timestamp"
})
@XmlSeeAlso({
	ManageOptionResponseType.class,
	ManageProductResponseType.class,
	UploadImageResponseType.class,
	GetAllCategoryListResponseType.class,
	GetAllOriginAreaListResponseType.class
//    GetModelListRequestType.class,
//    GetCategoryListRequestType.class
})
public abstract class BaseProductResponseType
	extends BaseResponseType {

	@XmlElement(name = "Version")
	protected String version;
	@XmlElement(name = "Timestamp")
	protected XMLGregorianCalendar timestamp;

	/**
	 * Gets the value of the version property.
	 *
	 * @return possible object is
	 *         {@link String }
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the value of the version property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setVersion(String value) {
		this.version = value;
	}

	/**
	 * Gets the value of the timestamp property.
	 *
	 * @return possible object is
	 *         {@link XMLGregorianCalendar }
	 */
	public XMLGregorianCalendar getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the value of the timestamp property.
	 *
	 * @param value allowed object is
	 *              {@link XMLGregorianCalendar }
	 */
	public void setTimestamp(XMLGregorianCalendar value) {
		this.timestamp = value;
	}

}
