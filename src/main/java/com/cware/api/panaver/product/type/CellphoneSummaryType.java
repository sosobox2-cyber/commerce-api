
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>CellphoneSummaryType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="CellphoneSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseSummaryType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ItemName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ModelName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Certified" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ReleaseDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ReleaseDateText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Manufacturer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Importer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Producer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Size" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Weight" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TelecomType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="JoinProcess" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ExtraBurden" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Specification" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="WarrantyPolicy" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="AfterServiceDirector" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CellphoneSummaryType", propOrder = {
    "itemName",
    "modelName",
    "certified",
    "releaseDate",
    "releaseDateText",
    "manufacturer",
    "importer",
    "producer",
    "size",
    "weight",
    "telecomType",
    "joinProcess",
    "extraBurden",
    "specification",
    "warrantyPolicy",
    "afterServiceDirector"
})
public class CellphoneSummaryType
    extends BaseSummaryType
{

    @XmlElement(name = "ItemName", required = true)
    protected String itemName;
    @XmlElement(name = "ModelName", required = true)
    protected String modelName;
    @XmlElement(name = "Certified", required = true)
    protected String certified;
    @XmlElement(name = "ReleaseDate")
    protected String releaseDate;
    @XmlElement(name = "ReleaseDateText")
    protected String releaseDateText;
    @XmlElement(name = "Manufacturer", required = true)
    protected String manufacturer;
    @XmlElement(name = "Importer")
    protected String importer;
    @XmlElement(name = "Producer", required = true)
    protected String producer;
    @XmlElement(name = "Size", required = true)
    protected String size;
    @XmlElement(name = "Weight", required = true)
    protected String weight;
    @XmlElement(name = "TelecomType", required = true)
    protected String telecomType;
    @XmlElement(name = "JoinProcess", required = true)
    protected String joinProcess;
    @XmlElement(name = "ExtraBurden", required = true)
    protected String extraBurden;
    @XmlElement(name = "Specification", required = true)
    protected String specification;
    @XmlElement(name = "WarrantyPolicy", required = true)
    protected String warrantyPolicy;
    @XmlElement(name = "AfterServiceDirector", required = true)
    protected String afterServiceDirector;

    /**
     * itemName 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * itemName 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemName(String value) {
        this.itemName = value;
    }

    /**
     * modelName 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * modelName 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelName(String value) {
        this.modelName = value;
    }

    /**
     * certified 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertified() {
        return certified;
    }

    /**
     * certified 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertified(String value) {
        this.certified = value;
    }

    /**
     * releaseDate 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * releaseDate 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReleaseDate(String value) {
        this.releaseDate = value;
    }

    /**
     * releaseDateText 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReleaseDateText() {
        return releaseDateText;
    }

    /**
     * releaseDateText 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReleaseDateText(String value) {
        this.releaseDateText = value;
    }

    /**
     * manufacturer 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * manufacturer 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturer(String value) {
        this.manufacturer = value;
    }

    /**
     * importer 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImporter() {
        return importer;
    }

    /**
     * importer 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImporter(String value) {
        this.importer = value;
    }

    /**
     * producer 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProducer() {
        return producer;
    }

    /**
     * producer 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProducer(String value) {
        this.producer = value;
    }

    /**
     * size 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSize() {
        return size;
    }

    /**
     * size 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSize(String value) {
        this.size = value;
    }

    /**
     * weight 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeight() {
        return weight;
    }

    /**
     * weight 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeight(String value) {
        this.weight = value;
    }

    /**
     * telecomType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelecomType() {
        return telecomType;
    }

    /**
     * telecomType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelecomType(String value) {
        this.telecomType = value;
    }

    /**
     * joinProcess 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJoinProcess() {
        return joinProcess;
    }

    /**
     * joinProcess 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJoinProcess(String value) {
        this.joinProcess = value;
    }

    /**
     * extraBurden 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtraBurden() {
        return extraBurden;
    }

    /**
     * extraBurden 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtraBurden(String value) {
        this.extraBurden = value;
    }

    /**
     * specification 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecification() {
        return specification;
    }

    /**
     * specification 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecification(String value) {
        this.specification = value;
    }

    /**
     * warrantyPolicy 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarrantyPolicy() {
        return warrantyPolicy;
    }

    /**
     * warrantyPolicy 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarrantyPolicy(String value) {
        this.warrantyPolicy = value;
    }

    /**
     * afterServiceDirector 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAfterServiceDirector() {
        return afterServiceDirector;
    }

    /**
     * afterServiceDirector 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAfterServiceDirector(String value) {
        this.afterServiceDirector = value;
    }

}
