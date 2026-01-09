
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>AirlineTicketSummaryType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="AirlineTicketSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseSummaryType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ChargeCondition" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RoundTrip" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Restriction" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TicketDeliveryMean" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="SeatType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="AdditionalCharge" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "AirlineTicketSummaryType", propOrder = {
    "chargeCondition",
    "roundTrip",
    "expirationDate",
    "restriction",
    "ticketDeliveryMean",
    "seatType",
    "additionalCharge",
    "cancelationPolicy",
    "reservationistContact"
})
public class AirlineTicketSummaryType
    extends BaseSummaryType
{

    @XmlElement(name = "ChargeCondition", required = true)
    protected String chargeCondition;
    @XmlElement(name = "RoundTrip", required = true)
    protected String roundTrip;
    @XmlElement(name = "ExpirationDate", required = true)
    protected String expirationDate;
    @XmlElement(name = "Restriction", required = true)
    protected String restriction;
    @XmlElement(name = "TicketDeliveryMean", required = true)
    protected String ticketDeliveryMean;
    @XmlElement(name = "SeatType", required = true)
    protected String seatType;
    @XmlElement(name = "AdditionalCharge", required = true)
    protected String additionalCharge;
    @XmlElement(name = "CancelationPolicy", required = true)
    protected String cancelationPolicy;
    @XmlElement(name = "ReservationistContact", required = true)
    protected String reservationistContact;

    /**
     * chargeCondition 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargeCondition() {
        return chargeCondition;
    }

    /**
     * chargeCondition 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargeCondition(String value) {
        this.chargeCondition = value;
    }

    /**
     * roundTrip 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoundTrip() {
        return roundTrip;
    }

    /**
     * roundTrip 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoundTrip(String value) {
        this.roundTrip = value;
    }

    /**
     * expirationDate 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * expirationDate 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpirationDate(String value) {
        this.expirationDate = value;
    }

    /**
     * restriction 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestriction() {
        return restriction;
    }

    /**
     * restriction 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestriction(String value) {
        this.restriction = value;
    }

    /**
     * ticketDeliveryMean 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicketDeliveryMean() {
        return ticketDeliveryMean;
    }

    /**
     * ticketDeliveryMean 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicketDeliveryMean(String value) {
        this.ticketDeliveryMean = value;
    }

    /**
     * seatType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeatType() {
        return seatType;
    }

    /**
     * seatType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeatType(String value) {
        this.seatType = value;
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
