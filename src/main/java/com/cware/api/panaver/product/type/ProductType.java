
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProductType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProductType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProductId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="StatusType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SaleType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomMade" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CategoryId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LayoutType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PublicityPhraseContent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SellerManagementCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SellerBarCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Model" type="{http://shopn.platform.nhncorp.com/}ModelType" minOccurs="0"/>
 *         &lt;element name="AttributeValueList" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CertificationList" type="{http://shopn.platform.nhncorp.com/}CertificationListType" minOccurs="0"/>
 *         
 *         &lt;element name="ChildCertifiedProductExclusion" type="{http://shopn.platform.nhncorp.com/}string" minOccurs="0"/>
 *         &lt;element name="KCCertifiedProductExclusion" type="{http://shopn.platform.nhncorp.com/}string" minOccurs="0"/>
 *         &lt;element name="GreenCertifiedProductExclusion" type="{http://shopn.platform.nhncorp.com/}string" minOccurs="0"/>
 *         
 *         &lt;element name="OriginArea" type="{http://shopn.platform.nhncorp.com/}OriginAreaType"/>
 *         &lt;element name="ManufactureDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ValidDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TaxType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MinorPurchasable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Image" type="{http://shopn.platform.nhncorp.com/}ImageType"/>
 *         &lt;element name="DetailContent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SellerNoticeId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="AfterServiceTelephoneNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AfterServiceGuideContent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PurchaseReviewExposure" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RegularCustomerExclusiveProduct" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="KnowledgeShoppingProductRegistration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GalleryId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="SaleStartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SaleEndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SalePrice" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="StockQuantity" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="MinPurchaseQuantity" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         
 *         &lt;element name="MaxPurchaseQuantityPerId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="MaxPurchaseQuantityPerOrder" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         
 *         &lt;element name="Delivery" type="{http://shopn.platform.nhncorp.com/}DeliveryType" minOccurs="0"/>
 *         &lt;element name="SellerDiscount" type="{http://shopn.platform.nhncorp.com/}SellerDiscountType" minOccurs="0"/>
 *         &lt;element name="MultiPurchaseDiscount" type="{http://shopn.platform.nhncorp.com/}MultiPurchaseDiscountType" minOccurs="0"/>
 *         &lt;element name="Mileage" type="{http://shopn.platform.nhncorp.com/}MileageType" minOccurs="0"/>
 *         &lt;element name="Stamp" type="{http://shopn.platform.nhncorp.com/}StampType" minOccurs="0"/>
 *         &lt;element name="FreeInterest" type="{http://shopn.platform.nhncorp.com/}FreeInterestType" minOccurs="0"/>
 *         &lt;element name="Gift" type="{http://shopn.platform.nhncorp.com/}GiftType" minOccurs="0"/>
 *         &lt;element name="ECoupon" type="{http://shopn.platform.nhncorp.com/}ECouponType" minOccurs="0"/>
 *         &lt;element name="ProductSummary" type="{http://shopn.platform.nhncorp.com/}ProductSummaryType"/>
 *         &lt;element name="Dvd" type="{http://shopn.platform.nhncorp.com/}DvdType" minOccurs="0"/>
 *         &lt;element name="Album" type="{http://shopn.platform.nhncorp.com/}AlbumType" minOccurs="0"/>
 *         &lt;element name="PurchaseApplicationUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CellPhonePrice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomProductYn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         
// *         &lt;element name="SellerCommentUsable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
// *         &lt;element name="IndependentPublicationYn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
// *         &lt;element name="CultureCostIncomeDeductionYn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
// *         &lt;element name="ItselfProdctionProductYn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductType", propOrder = {
    "productId",
    "statusType",
    "saleType",
    "customMade",
    "categoryId",
    "layoutType",
    "name",
    "publicityPhraseContent",
    "sellerManagementCode",
    "sellerBarCode",
    "model",
    "attributeValueList",
    "certificationList",
    "childCertifiedProductExclusion",
    "kCCertifiedProductExclusion",
    "greenCertifiedProductExclusion",
    "originArea",
    "manufactureDate",
    "validDate",
    "taxType",
    "minorPurchasable",
    "image",
    "detailContent",
    "sellerNoticeId",
    "afterServiceTelephoneNumber",
    "afterServiceGuideContent",
    "purchaseReviewExposure",
    "regularCustomerExclusiveProduct",
    "knowledgeShoppingProductRegistration",
    "galleryId",
    "saleStartDate",
    "saleEndDate",
    "salePrice",
    "stockQuantity",
    "minPurchaseQuantity",
    "maxPurchaseQuantityPerId",
    "maxPurchaseQuantityPerOrder",
    "delivery",
    "sellerDiscount",
    "multiPurchaseDiscount",
    "mileage",
    "stamp",
    "freeInterest",
    "gift",
    "eCoupon",
    "productSummary",
    "dvd",
    "album",
    "purchaseApplicationUrl",
    "cellPhonePrice",
//    "sellerCommentUsable",
//    "IndependentPublicationYn",
//    "CultureCostIncomeDeductionYn",
    "customProductYn"
//    "ItselfProdctionProductYn"
})
public class ProductType {

    @XmlElement(name = "ProductId")
    protected Long productId;
    @XmlElement(name = "StatusType", required = true)
    protected String statusType;
    @XmlElement(name = "SaleType")
    protected String saleType;
    @XmlElement(name = "CustomMade", required = true)
    protected String customMade;
	@XmlElement(name = "CategoryId")
    protected String categoryId;
    @XmlElement(name = "LayoutType")
    protected String layoutType;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "PublicityPhraseContent")
    protected String publicityPhraseContent;
    @XmlElement(name = "SellerManagementCode")
    protected String sellerManagementCode;
    @XmlElement(name = "SellerBarCode")
    protected String sellerBarCode;
    @XmlElement(name = "Model")
    protected ModelType model;
    @XmlElement(name = "AttributeValueList")
    protected String attributeValueList;
    @XmlElement(name = "CertificationList")
    protected CertificationListType certificationList;

    @XmlElement(name = "ChildCertifiedProductExclusion", required = true)
    protected String childCertifiedProductExclusion;
    @XmlElement(name = "KCCertifiedProductExclusion", required = true)
    protected String kCCertifiedProductExclusion;
    @XmlElement(name = "GreenCertifiedProductExclusion", required = true)
    protected String greenCertifiedProductExclusion;
    
    @XmlElement(name = "OriginArea", required = true)
    protected OriginAreaType originArea;
    @XmlElement(name = "ManufactureDate")
    protected String manufactureDate;
    @XmlElement(name = "ValidDate")
    protected String validDate;
    @XmlElement(name = "TaxType", required = true)
    protected String taxType;
    @XmlElement(name = "MinorPurchasable", required = true)
    protected String minorPurchasable;
    @XmlElement(name = "Image", required = true)
    protected ImageType image;
    @XmlElement(name = "DetailContent", required = true)
    protected String detailContent;
    @XmlElement(name = "SellerNoticeId")
    protected Long sellerNoticeId;
    @XmlElement(name = "AfterServiceTelephoneNumber", required = true)
    protected String afterServiceTelephoneNumber;
    @XmlElement(name = "AfterServiceGuideContent", required = true)
    protected String afterServiceGuideContent;
    @XmlElement(name = "PurchaseReviewExposure")
    protected String purchaseReviewExposure;
    @XmlElement(name = "RegularCustomerExclusiveProduct")
    protected String regularCustomerExclusiveProduct;
    @XmlElement(name = "KnowledgeShoppingProductRegistration")
    protected String knowledgeShoppingProductRegistration;
    @XmlElement(name = "GalleryId")
    protected Long galleryId;
    @XmlElement(name = "SaleStartDate")
    protected String saleStartDate;
    @XmlElement(name = "SaleEndDate")
    protected String saleEndDate;
    @XmlElement(name = "SalePrice")
    protected long salePrice;
    @XmlElement(name = "StockQuantity")
    protected long stockQuantity;
    @XmlElement(name = "MinPurchaseQuantity")
    protected Integer minPurchaseQuantity;
    
    @XmlElement(name = "MaxPurchaseQuantityPerId")
    protected long maxPurchaseQuantityPerId;
    @XmlElement(name = "MaxPurchaseQuantityPerOrder")
    protected long maxPurchaseQuantityPerOrder;
    
    @XmlElement(name = "Delivery")
    protected DeliveryType delivery;
    @XmlElement(name = "SellerDiscount")
    protected SellerDiscountType sellerDiscount;
    @XmlElement(name = "MultiPurchaseDiscount")
    protected MultiPurchaseDiscountType multiPurchaseDiscount;
    @XmlElement(name = "Mileage")
    protected MileageType mileage;
    @XmlElement(name = "Stamp")
    protected StampType stamp;
    @XmlElement(name = "FreeInterest")
    protected FreeInterestType freeInterest;
    @XmlElement(name = "Gift")
    protected GiftType gift;
    @XmlElement(name = "ECoupon")
    protected ECouponType eCoupon;
    @XmlElement(name = "ProductSummary")
    protected ProductSummaryType productSummary;
	@XmlElement(name = "Dvd")
    protected DvdType dvd;
    @XmlElement(name = "Album")
    protected AlbumType album;
    @XmlElement(name = "PurchaseApplicationUrl")
    protected String purchaseApplicationUrl;
    @XmlElement(name = "CellPhonePrice")
    protected String cellPhonePrice;
    @XmlElement(name = "CustomProductYn")
    protected String customProductYn;
//    @XmlElement(name = "SellerCommentUsable")
//    protected String sellerCommentUsable;
//    @XmlElement(name = "IndependentPublicationYn")
//    protected String independentPublicationYn;
//    @XmlElement(name = "CultureCostIncomeDeductionYn")
//    protected String cultureCostIncomeDeductionYn;
//    @XmlElement(name = "ItselfProdctionProductYn")
//    protected String itselfProdctionProductYn;

    
    public String getCustomProductYn() {
		return customProductYn;
	}

	public String getChildCertifiedProductExclusion() {
		return childCertifiedProductExclusion;
	}

	public void setChildCertifiedProductExclusion(String childCertifiedProductExclusion) {
		this.childCertifiedProductExclusion = childCertifiedProductExclusion;
	}

	public String getkCCertifiedProductExclusion() {
		return kCCertifiedProductExclusion;
	}

	public void setkCCertifiedProductExclusion(String kCCertifiedProductExclusion) {
		this.kCCertifiedProductExclusion = kCCertifiedProductExclusion;
	}

	public String getGreenCertifiedProductExclusion() {
		return greenCertifiedProductExclusion;
	}

	public void setGreenCertifiedProductExclusion(String greenCertifiedProductExclusion) {
		this.greenCertifiedProductExclusion = greenCertifiedProductExclusion;
	}

	public void setCustomProductYn(String customProductYn) {
		this.customProductYn = customProductYn;
	}
//
//	public String getItselfProdctionProductYn() {
//		return itselfProdctionProductYn;
//	}
//
//	public void setItselfProdctionProductYn(String itselfProdctionProductYn) {
//		this.itselfProdctionProductYn = itselfProdctionProductYn;
//	}
//
//	public String getCultureCostIncomeDeductionYn() {
//		return cultureCostIncomeDeductionYn;
//	}
//
//	public void setCultureCostIncomeDeductionYn(String cultureCostIncomeDeductionYn) {
//		this.cultureCostIncomeDeductionYn = cultureCostIncomeDeductionYn;
//	}
//
//	public String getIndependentPublicationYn() {
//		return independentPublicationYn;
//	}
//
//	public void setIndependentPublicationYn(String independentPublicationYn) {
//		this.independentPublicationYn = independentPublicationYn;
//	}

//	public ECouponType geteCoupon() {
//		return eCoupon;
//	}
//
//	public void seteCoupon(ECouponType eCoupon) {
//		this.eCoupon = eCoupon;
//	}

//	public String getSellerCommentUsable() {
//		return sellerCommentUsable;
//	}
//
//	public void setSellerCommentUsable(String sellerCommentUsable) {
//		this.sellerCommentUsable = sellerCommentUsable;
//	}


	
	/**
     * Gets the value of the productId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProductId() {
        return productId;
    }

	public long getMaxPurchaseQuantityPerId() {
		return maxPurchaseQuantityPerId;
	}

	public void setMaxPurchaseQuantityPerId(long maxPurchaseQuantityPerId) {
		this.maxPurchaseQuantityPerId = maxPurchaseQuantityPerId;
	}

	public long getMaxPurchaseQuantityPerOrder() {
		return maxPurchaseQuantityPerOrder;
	}

	public void setMaxPurchaseQuantityPerOrder(long maxPurchaseQuantityPerOrder) {
		this.maxPurchaseQuantityPerOrder = maxPurchaseQuantityPerOrder;
	}

	/**
     * Sets the value of the productId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProductId(Long value) {
        this.productId = value;
    }

    /**
     * Gets the value of the statusType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusType() {
        return statusType;
    }

    /**
     * Sets the value of the statusType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusType(String value) {
        this.statusType = value;
    }

    /**
     * Gets the value of the saleType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaleType() {
        return saleType;
    }

    /**
     * Sets the value of the saleType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaleType(String value) {
        this.saleType = value;
    }

    /**
     * Gets the value of the categoryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the value of the categoryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoryId(String value) {
        this.categoryId = value;
    }

    /**
     * Gets the value of the layoutType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLayoutType() {
        return layoutType;
    }

    /**
     * Sets the value of the layoutType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLayoutType(String value) {
        this.layoutType = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the publicityPhraseContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicityPhraseContent() {
        return publicityPhraseContent;
    }

    /**
     * Sets the value of the publicityPhraseContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicityPhraseContent(String value) {
        this.publicityPhraseContent = value;
    }

    /**
     * Gets the value of the sellerManagementCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellerManagementCode() {
        return sellerManagementCode;
    }

    /**
     * Sets the value of the sellerManagementCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellerManagementCode(String value) {
        this.sellerManagementCode = value;
    }

    /**
     * Gets the value of the sellerBarCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellerBarCode() {
        return sellerBarCode;
    }

    /**
     * Sets the value of the sellerBarCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellerBarCode(String value) {
        this.sellerBarCode = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link ModelType }
     *     
     */
    public ModelType getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModelType }
     *     
     */
    public void setModel(ModelType value) {
        this.model = value;
    }

    /**
     * Gets the value of the attributeValueList property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttributeValueList() {
        return attributeValueList;
    }

    /**
     * Sets the value of the attributeValueList property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttributeValueList(String value) {
        this.attributeValueList = value;
    }

    /**
     * Gets the value of the certificationList property.
     * 
     * @return
     *     possible object is
     *     {@link CertificationListType }
     *     
     */
    public CertificationListType getCertificationList() {
        return certificationList;
    }

    /**
     * Sets the value of the certificationList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CertificationListType }
     *     
     */
    public void setCertificationList(CertificationListType value) {
        this.certificationList = value;
    }

    /**
     * Gets the value of the originArea property.
     * 
     * @return
     *     possible object is
     *     {@link OriginAreaType }
     *     
     */
    public OriginAreaType getOriginArea() {
        return originArea;
    }

    /**
     * Sets the value of the originArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link OriginAreaType }
     *     
     */
    public void setOriginArea(OriginAreaType value) {
        this.originArea = value;
    }

    /**
     * Gets the value of the manufactureDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufactureDate() {
        return manufactureDate;
    }

    /**
     * Sets the value of the manufactureDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufactureDate(String value) {
        this.manufactureDate = value;
    }

    /**
     * Gets the value of the validDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidDate() {
        return validDate;
    }

    /**
     * Sets the value of the validDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidDate(String value) {
        this.validDate = value;
    }

    /**
     * Gets the value of the taxType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxType() {
        return taxType;
    }

    /**
     * Sets the value of the taxType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxType(String value) {
        this.taxType = value;
    }

    /**
     * Gets the value of the minorPurchasable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinorPurchasable() {
        return minorPurchasable;
    }

    /**
     * Sets the value of the minorPurchasable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinorPurchasable(String value) {
        this.minorPurchasable = value;
    }

    /**
     * Gets the value of the image property.
     * 
     * @return
     *     possible object is
     *     {@link ImageType }
     *     
     */
    public ImageType getImage() {
        return image;
    }

    /**
     * Sets the value of the image property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageType }
     *     
     */
    public void setImage(ImageType value) {
        this.image = value;
    }

    /**
     * Gets the value of the detailContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetailContent() {
        return detailContent;
    }

    /**
     * Sets the value of the detailContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetailContent(String value) {
        this.detailContent = value;
    }

    /**
     * Gets the value of the sellerNoticeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSellerNoticeId() {
        return sellerNoticeId;
    }

    /**
     * Sets the value of the sellerNoticeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSellerNoticeId(Long value) {
        this.sellerNoticeId = value;
    }

    /**
     * Gets the value of the afterServiceTelephoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAfterServiceTelephoneNumber() {
        return afterServiceTelephoneNumber;
    }

    /**
     * Sets the value of the afterServiceTelephoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAfterServiceTelephoneNumber(String value) {
        this.afterServiceTelephoneNumber = value;
    }

    /**
     * Gets the value of the afterServiceGuideContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAfterServiceGuideContent() {
        return afterServiceGuideContent;
    }

    /**
     * Sets the value of the afterServiceGuideContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAfterServiceGuideContent(String value) {
        this.afterServiceGuideContent = value;
    }

    /**
     * Gets the value of the purchaseReviewExposure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurchaseReviewExposure() {
        return purchaseReviewExposure;
    }

    /**
     * Sets the value of the purchaseReviewExposure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurchaseReviewExposure(String value) {
        this.purchaseReviewExposure = value;
    }

    /**
     * Gets the value of the regularCustomerExclusiveProduct property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegularCustomerExclusiveProduct() {
        return regularCustomerExclusiveProduct;
    }

    /**
     * Sets the value of the regularCustomerExclusiveProduct property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegularCustomerExclusiveProduct(String value) {
        this.regularCustomerExclusiveProduct = value;
    }

    /**
     * Gets the value of the knowledgeShoppingProductRegistration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKnowledgeShoppingProductRegistration() {
        return knowledgeShoppingProductRegistration;
    }

    /**
     * Sets the value of the knowledgeShoppingProductRegistration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKnowledgeShoppingProductRegistration(String value) {
        this.knowledgeShoppingProductRegistration = value;
    }

    /**
     * Gets the value of the galleryId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getGalleryId() {
        return galleryId;
    }

    /**
     * Sets the value of the galleryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setGalleryId(Long value) {
        this.galleryId = value;
    }

    /**
     * Gets the value of the saleStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaleStartDate() {
        return saleStartDate;
    }

    /**
     * Sets the value of the saleStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaleStartDate(String value) {
        this.saleStartDate = value;
    }

    /**
     * Gets the value of the saleEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaleEndDate() {
        return saleEndDate;
    }

    /**
     * Sets the value of the saleEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaleEndDate(String value) {
        this.saleEndDate = value;
    }

    /**
     * Gets the value of the salePrice property.
     * 
     */
    public long getSalePrice() {
        return salePrice;
    }

    /**
     * Sets the value of the salePrice property.
     * 
     */
    public void setSalePrice(long value) {
        this.salePrice = value;
    }

    /**
     * Gets the value of the stockQuantity property.
     * 
     */
    public long getStockQuantity() {
        return stockQuantity;
    }

    /**
     * Sets the value of the stockQuantity property.
     * 
     */
    public void setStockQuantity(long value) {
        this.stockQuantity = value;
    }

    /**
     * Gets the value of the minPurchaseQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinPurchaseQuantity() {
        return minPurchaseQuantity;
    }

    /**
     * Sets the value of the minPurchaseQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinPurchaseQuantity(Integer value) {
        this.minPurchaseQuantity = value;
    }



    /**
     * Gets the value of the delivery property.
     * 
     * @return
     *     possible object is
     *     {@link DeliveryType }
     *     
     */
    public DeliveryType getDelivery() {
        return delivery;
    }

    /**
     * Sets the value of the delivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeliveryType }
     *     
     */
    public void setDelivery(DeliveryType value) {
        this.delivery = value;
    }

    /**
     * Gets the value of the sellerDiscount property.
     * 
     * @return
     *     possible object is
     *     {@link SellerDiscountType }
     *     
     */
    public SellerDiscountType getSellerDiscount() {
        return sellerDiscount;
    }

    /**
     * Sets the value of the sellerDiscount property.
     * 
     * @param value
     *     allowed object is
     *     {@link SellerDiscountType }
     *     
     */
    public void setSellerDiscount(SellerDiscountType value) {
        this.sellerDiscount = value;
    }

    /**
     * Gets the value of the multiPurchaseDiscount property.
     * 
     * @return
     *     possible object is
     *     {@link MultiPurchaseDiscountType }
     *     
     */
    public MultiPurchaseDiscountType getMultiPurchaseDiscount() {
        return multiPurchaseDiscount;
    }

    /**
     * Sets the value of the multiPurchaseDiscount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiPurchaseDiscountType }
     *     
     */
    public void setMultiPurchaseDiscount(MultiPurchaseDiscountType value) {
        this.multiPurchaseDiscount = value;
    }

    /**
     * Gets the value of the mileage property.
     * 
     * @return
     *     possible object is
     *     {@link MileageType }
     *     
     */
    public MileageType getMileage() {
        return mileage;
    }

    /**
     * Sets the value of the mileage property.
     * 
     * @param value
     *     allowed object is
     *     {@link MileageType }
     *     
     */
    public void setMileage(MileageType value) {
        this.mileage = value;
    }

    /**
     * Gets the value of the stamp property.
     * 
     * @return
     *     possible object is
     *     {@link StampType }
     *     
     */
    public StampType getStamp() {
        return stamp;
    }

    /**
     * Sets the value of the stamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link StampType }
     *     
     */
    public void setStamp(StampType value) {
        this.stamp = value;
    }

    /**
     * Gets the value of the freeInterest property.
     * 
     * @return
     *     possible object is
     *     {@link FreeInterestType }
     *     
     */
    public FreeInterestType getFreeInterest() {
        return freeInterest;
    }

    /**
     * Sets the value of the freeInterest property.
     * 
     * @param value
     *     allowed object is
     *     {@link FreeInterestType }
     *     
     */
    public void setFreeInterest(FreeInterestType value) {
        this.freeInterest = value;
    }

    /**
     * Gets the value of the gift property.
     * 
     * @return
     *     possible object is
     *     {@link GiftType }
     *     
     */
    public GiftType getGift() {
        return gift;
    }

    /**
     * Sets the value of the gift property.
     * 
     * @param value
     *     allowed object is
     *     {@link GiftType }
     *     
     */
    public void setGift(GiftType value) {
        this.gift = value;
    }

    /**
     * Gets the value of the eCoupon property.
     * 
     * @return
     *     possible object is
     *     {@link ECouponType }
     *     
     */
    public ECouponType getECoupon() {
        return eCoupon;
    }

    /**
     * Sets the value of the eCoupon property.
     * 
     * @param value
     *     allowed object is
     *     {@link ECouponType }
     *     
     */
    public void setECoupon(ECouponType value) {
        this.eCoupon = value;
    }

    /**
     * Gets the value of the dvd property.
     * 
     * @return
     *     possible object is
     *     {@link DvdType }
     *     
     */
    public DvdType getDvd() {
        return dvd;
    }

    /**
     * Sets the value of the dvd property.
     * 
     * @param value
     *     allowed object is
     *     {@link DvdType }
     *     
     */
    public void setDvd(DvdType value) {
        this.dvd = value;
    }

    /**
     * Gets the value of the album property.
     * 
     * @return
     *     possible object is
     *     {@link AlbumType }
     *     
     */
    public AlbumType getAlbum() {
        return album;
    }

    /**
     * Sets the value of the album property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlbumType }
     *     
     */
    public void setAlbum(AlbumType value) {
        this.album = value;
    }

    /**
     * Gets the value of the purchaseApplicationUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurchaseApplicationUrl() {
        return purchaseApplicationUrl;
    }

    /**
     * Sets the value of the purchaseApplicationUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurchaseApplicationUrl(String value) {
        this.purchaseApplicationUrl = value;
    }

    /**
     * Gets the value of the cellPhonePrice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCellPhonePrice() {
        return cellPhonePrice;
    }

    /**
     * Sets the value of the cellPhonePrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCellPhonePrice(String value) {
        this.cellPhonePrice = value;
    }
    
    public String getCustomMade() {
		return customMade;
	}

	public void setCustomMade(String customMade) {
		this.customMade = customMade;
	}
	
    public ProductSummaryType getProductSummary() {
		return productSummary;
	}

	public void setProductSummary(ProductSummaryType productSummary) {
		this.productSummary = productSummary;
	}

}
