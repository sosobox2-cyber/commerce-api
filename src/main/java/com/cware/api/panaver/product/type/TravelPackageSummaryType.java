
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>TravelPackageSummaryType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="TravelPackageSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseSummaryType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TravelAgency" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Flight" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TravelPeriod" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Schedule" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="MaximumNumberOfPeople" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="MinimumNumberOfPeople" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="AccomodationInfo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Details" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="AdditionalCharge" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CancelationPolicy" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TravelWarnings" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "TravelPackageSummaryType", propOrder = {
    "travelAgency",
    "flight",
    "travelPeriod",
    "schedule",
    "maximumNumberOfPeople",
    "minimumNumberOfPeople",
    "accomodationInfo",
    "details",
    "additionalCharge",
    "cancelationPolicy",
    "travelWarnings",
    "reservationistContact"
})
public class TravelPackageSummaryType
    extends BaseSummaryType
{

    @XmlElement(name = "TravelAgency", required = true)
    protected String travelAgency;
    @XmlElement(name = "Flight", required = true)
    protected String flight;
    @XmlElement(name = "TravelPeriod", required = true)
    protected String travelPeriod;
    @XmlElement(name = "Schedule", required = true)
    protected String schedule;
    @XmlElement(name = "MaximumNumberOfPeople", required = true)
    protected String maximumNumberOfPeople;
    @XmlElement(name = "MinimumNumberOfPeople", required = true)
    protected String minimumNumberOfPeople;
    @XmlElement(name = "AccomodationInfo", required = true)
    protected String accomodationInfo;
    @XmlElement(name = "Details", required = true)
    protected String details;
    @XmlElement(name = "AdditionalCharge", required = true)
    protected String additionalCharge;
    @XmlElement(name = "CancelationPolicy", required = true)
    protected String cancelationPolicy;
    @XmlElement(name = "TravelWarnings")
    protected String travelWarnings;
    @XmlElement(name = "ReservationistContact", required = true)
    protected String reservationistContact;

    /**
     * travelAgency 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTravelAgency() {
        return travelAgency;
    }

    /**
     * travelAgency 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTravelAgency(String value) {
        this.travelAgency = value;
    }

    /**
     * flight 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlight() {
        return flight;
    }

    /**
     * flight 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlight(String value) {
        this.flight = value;
    }

    /**
     * travelPeriod 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTravelPeriod() {
        return travelPeriod;
    }

    /**
     * travelPeriod 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTravelPeriod(String value) {
        this.travelPeriod = value;
    }

    /**
     * schedule 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     * schedule 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchedule(String value) {
        this.schedule = value;
    }

    /**
     * maximumNumberOfPeople 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaximumNumberOfPeople() {
        return maximumNumberOfPeople;
    }

    /**
     * maximumNumberOfPeople 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaximumNumberOfPeople(String value) {
        this.maximumNumberOfPeople = value;
    }

    /**
     * minimumNumberOfPeople 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinimumNumberOfPeople() {
        return minimumNumberOfPeople;
    }

    /**
     * minimumNumberOfPeople 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinimumNumberOfPeople(String value) {
        this.minimumNumberOfPeople = value;
    }

    /**
     * accomodationInfo 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccomodationInfo() {
        return accomodationInfo;
    }

    /**
     * accomodationInfo 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccomodationInfo(String value) {
        this.accomodationInfo = value;
    }

    /**
     * details 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetails() {
        return details;
    }

    /**
     * details 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetails(String value) {
        this.details = value;
    }

    /**
     * additionalCharge 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalCharge() {
        return additionalCharge;
    }

    /**
     * additionalCharge 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalCharge(String value) {
        this.additionalCharge = value;
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
     * travelWarnings 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTravelWarnings() {
        return travelWarnings;
    }

    /**
     * travelWarnings 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTravelWarnings(String value) {
        this.travelWarnings = value;
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
