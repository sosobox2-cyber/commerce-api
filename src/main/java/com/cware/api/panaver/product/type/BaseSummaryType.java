
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BaseSummaryType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="BaseSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NoRefundReason" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ReturnCostReason" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="QualityAssuranceStandard" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CompensationProcedure" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TroubleShootingContents" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseSummaryType", propOrder = {
    "noRefundReason",
    "returnCostReason",
    "qualityAssuranceStandard",
    "compensationProcedure",
    "troubleShootingContents"
})
@XmlSeeAlso({
    AirlineTicketSummaryType.class,
    BagSummaryType.class,
    BiochemistrySummaryType.class,
    BiocidalSummaryType.class,
    BooksSummaryType.class,
    CarArticlesSummaryType.class,
    CellphoneSummaryType.class,
    CosmeticSummaryType.class,
    DietFoodSummaryType.class,
    DigitalContentsSummaryType.class,
    EtcSummaryType.class,
    EtcServiceSummaryType.class,
    FashionItemsSummaryType.class,
    FoodSummaryType.class,
    FurnitureSummaryType.class,
    GeneralFoodSummaryType.class,
    GiftCardSummaryType.class,
    HomeAppliancesSummaryType.class,
    ImageAppliancesSummaryType.class,
    JewellerySummaryType.class,
    KidsSummaryType.class,
    KitchenUtensilsSummaryType.class,
    LodgmentReservationSummaryType.class,
    MedicalAppliancesSummaryType.class,
    MicroElectronicsSummaryType.class,
    MobileCouponSummaryType.class,
    MovieShowSummaryType.class,
    MusicalInstrumentSummaryType.class,
    NavigationSummaryType.class,
    OfficeAppliancesSummaryType.class,
    OpticsAppliancesSummaryType.class,
    RentCarSummaryType.class,
    RentalEtcSummaryType.class,
    RentalHaSummaryType.class,
    SeasonAppliancesSummaryType.class,
    ShoesSummaryType.class,
    SleepingGearSummaryType.class,
    SportsEquipmentSummaryType.class,
    TravelPackageSummaryType.class,
    WearSummaryType.class
})
public abstract class BaseSummaryType {

    @XmlElement(name = "NoRefundReason", required = true)
    protected String noRefundReason;
    @XmlElement(name = "ReturnCostReason", required = true)
    protected String returnCostReason;
    @XmlElement(name = "QualityAssuranceStandard", required = true)
    protected String qualityAssuranceStandard;
    @XmlElement(name = "CompensationProcedure", required = true)
    protected String compensationProcedure;
    @XmlElement(name = "TroubleShootingContents", required = true)
    protected String troubleShootingContents;

    /**
     * noRefundReason 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoRefundReason() {
        return noRefundReason;
    }

    /**
     * noRefundReason 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoRefundReason(String value) {
        this.noRefundReason = value;
    }

    /**
     * returnCostReason 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnCostReason() {
        return returnCostReason;
    }

    /**
     * returnCostReason 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnCostReason(String value) {
        this.returnCostReason = value;
    }

    /**
     * qualityAssuranceStandard 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQualityAssuranceStandard() {
        return qualityAssuranceStandard;
    }

    /**
     * qualityAssuranceStandard 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQualityAssuranceStandard(String value) {
        this.qualityAssuranceStandard = value;
    }

    /**
     * compensationProcedure 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompensationProcedure() {
        return compensationProcedure;
    }

    /**
     * compensationProcedure 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompensationProcedure(String value) {
        this.compensationProcedure = value;
    }

    /**
     * troubleShootingContents 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTroubleShootingContents() {
        return troubleShootingContents;
    }

    /**
     * troubleShootingContents 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTroubleShootingContents(String value) {
        this.troubleShootingContents = value;
    }

}
