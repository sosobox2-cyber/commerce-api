
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>LodgmentReservationSummaryType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="LodgmentReservationSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseSummaryType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Location" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="LodgmentType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Grade" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RoomType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RoomCapacity" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ExtraPersonCharge" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="AdditionalFacilities" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ProvidedService" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CancelationPolicy" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ReservationistContact" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LodgmentReservationSummaryType", propOrder = {
    "location",
    "lodgmentType",
    "grade",
    "roomType",
    "roomCapacity",
    "extraPersonCharge",
    "additionalFacilities",
    "providedService",
    "cancelationPolicy",
    "reservationistContact"
})
public class LodgmentReservationSummaryType
    extends BaseSummaryType
{

    @XmlElement(name = "Location", required = true)
    protected String location;
    @XmlElement(name = "LodgmentType", required = true)
    protected String lodgmentType;
    @XmlElement(name = "Grade", required = true)
    protected String grade;
    @XmlElement(name = "RoomType", required = true)
    protected String roomType;
    @XmlElement(name = "RoomCapacity", required = true)
    protected String roomCapacity;
    @XmlElement(name = "ExtraPersonCharge", required = true)
    protected String extraPersonCharge;
    @XmlElement(name = "AdditionalFacilities", required = true)
    protected String additionalFacilities;
    @XmlElement(name = "ProvidedService", required = true)
    protected String providedService;
    @XmlElement(name = "CancelationPolicy", required = true)
    protected String cancelationPolicy;
    @XmlElement(name = "ReservationistContact", required = true)
    protected String reservationistContact;

    /**
     * location 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * location 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * lodgmentType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLodgmentType() {
        return lodgmentType;
    }

    /**
     * lodgmentType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLodgmentType(String value) {
        this.lodgmentType = value;
    }

    /**
     * grade 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrade() {
        return grade;
    }

    /**
     * grade 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrade(String value) {
        this.grade = value;
    }

    /**
     * roomType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoomType() {
        return roomType;
    }

    /**
     * roomType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoomType(String value) {
        this.roomType = value;
    }

    /**
     * roomCapacity 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoomCapacity() {
        return roomCapacity;
    }

    /**
     * roomCapacity 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoomCapacity(String value) {
        this.roomCapacity = value;
    }

    /**
     * extraPersonCharge 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtraPersonCharge() {
        return extraPersonCharge;
    }

    /**
     * extraPersonCharge 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtraPersonCharge(String value) {
        this.extraPersonCharge = value;
    }

    /**
     * additionalFacilities 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalFacilities() {
        return additionalFacilities;
    }

    /**
     * additionalFacilities 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalFacilities(String value) {
        this.additionalFacilities = value;
    }

    /**
     * providedService 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvidedService() {
        return providedService;
    }

    /**
     * providedService 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvidedService(String value) {
        this.providedService = value;
    }

    /**
     * cancelationPolicy 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelationPolicy() {
        return cancelationPolicy;
    }

    /**
     * cancelationPolicy 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelationPolicy(String value) {
        this.cancelationPolicy = value;
    }

    /**
     * reservationistContact 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReservationistContact() {
        return reservationistContact;
    }

    /**
     * reservationistContact 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReservationistContact(String value) {
        this.reservationistContact = value;
    }

}
