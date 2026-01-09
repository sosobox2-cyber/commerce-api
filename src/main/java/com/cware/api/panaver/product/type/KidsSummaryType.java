
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>KidsSummaryType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="KidsSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseSummaryType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ItemName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ModelName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Certified" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Size" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Weight" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Color" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Material" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RecommendedAge" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="NumberLimit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ReleaseDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ReleaseDateText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Manufacturer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Caution" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "KidsSummaryType", propOrder = {
    "itemName",
    "modelName",
    "certified",
    "size",
    "weight",
    "color",
    "material",
    "recommendedAge",
    "numberLimit",
    "releaseDate",
    "releaseDateText",
    "manufacturer",
    "caution",
    "warrantyPolicy",
    "afterServiceDirector"
})
public class KidsSummaryType
    extends BaseSummaryType
{

    @XmlElement(name = "ItemName", required = true)
    protected String itemName;
    @XmlElement(name = "ModelName", required = true)
    protected String modelName;
    @XmlElement(name = "Certified", required = true)
    protected String certified;
    @XmlElement(name = "Size", required = true)
    protected String size;
    @XmlElement(name = "Weight", required = true)
    protected String weight;
    @XmlElement(name = "Color", required = true)
    protected String color;
    @XmlElement(name = "Material", required = true)
    protected String material;
    @XmlElement(name = "RecommendedAge", required = true)
    protected String recommendedAge;
    @XmlElement(name = "NumberLimit")
    protected String numberLimit;
    @XmlElement(name = "ReleaseDate")
    protected String releaseDate;
    @XmlElement(name = "ReleaseDateText")
    protected String releaseDateText;
    @XmlElement(name = "Manufacturer", required = true)
    protected String manufacturer;
    @XmlElement(name = "Caution", required = true)
    protected String caution;
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
     * color 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColor() {
        return color;
    }

    /**
     * color 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor(String value) {
        this.color = value;
    }

    /**
     * material 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterial() {
        return material;
    }

    /**
     * material 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterial(String value) {
        this.material = value;
    }

    /**
     * recommendedAge 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecommendedAge() {
        return recommendedAge;
    }

    /**
     * recommendedAge 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecommendedAge(String value) {
        this.recommendedAge = value;
    }

    /**
     * numberLimit 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberLimit() {
        return numberLimit;
    }

    /**
     * numberLimit 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberLimit(String value) {
        this.numberLimit = value;
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
     * caution 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaution() {
        return caution;
    }

    /**
     * caution 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaution(String value) {
        this.caution = value;
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
