package com.cware.netshopping.pafaple.domain;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Product {
	
	@JsonProperty("ItemNo")
	private String ItemNo;     
	
	@JsonProperty("ItemName")
	private String ItemName;   
	@JsonProperty("SalePrice")
	private double SalePrice; 
	@JsonProperty("ConsumerPrice")
	private double ConsumerPrice;
	@JsonProperty("BrandId")
	private Integer BrandId;    
	@JsonProperty("MakeYear")
	private String MakeYear;   
	@JsonProperty("Home")
	private String Home;       
	@JsonProperty("Category1")
	private Integer Category1;  
	@JsonProperty("DisplayItemName")
	private String DisplayItemName;
	@JsonProperty("Description")
	private String Description;
	@JsonProperty("ImageURL1")
	private String ImageURL1;
	@JsonProperty("ImageURL2")
	private String ImageURL2;  
	@JsonProperty("ImageURL3")
	private String ImageURL3;  
	@JsonProperty("ImageURL4")
	private String ImageURL4;
	@JsonProperty("Options")
	private List<Options> Options;
	@JsonProperty("Product_num")
	private Integer    Product_num;
	@JsonProperty("Product_Material")
	private String Product_Material;
	@JsonProperty("Product_Color")
	private String Product_Color;
	@JsonProperty("Product_Size")
	private String Product_Size	;
	@JsonProperty("Product_Maker")
	private String Product_Maker;
	@JsonProperty("Product_Home")
	private String Product_Home	;
	@JsonProperty("Product_Washing")
	private String Product_Washing;
	@JsonProperty("Product_MakeYear")
	private String Product_MakeYear;
	@JsonProperty("Product_Warranty")
	private String Product_Warranty;		
	@JsonProperty("Product_KindType")
	private String Product_KindType;
	@JsonProperty("Product_Comprisal")
	private String Product_Comprisal;
	@JsonProperty("Product_Model")
	private String Product_Model;
	@JsonProperty("Product_KC")
	private String Product_KC;
	@JsonProperty("Product_InstallCost")
	private String Product_InstallCost;
	@JsonProperty("Product_Voltage")
	private String Product_Voltage;			
	@JsonProperty("Product_Type")
	private String Product_Type	;		
	@JsonProperty("Product_Weight")
	private String Product_Weight;			
	@JsonProperty("Product_Period")
	private String Product_Period;			
	@JsonProperty("Product_Way")
	private String Product_Way;				
	@JsonProperty("Product_Ingredient")
	private String Product_Ingredient;		
	@JsonProperty("Product_EvaluateYN")
	private String Product_EvaluateYN;		
	@JsonProperty("Product_Careful")
	private String Product_Careful;			
	@JsonProperty("Product_Grade")
	private String Product_Grade;			
	@JsonProperty("Product_Function")
	private String Product_Function	;	
	@JsonProperty("Product_WarrantyofferYN")
	private String Product_WarrantyofferYN;	
	@JsonProperty("Product_UseAge")
	private String Product_UseAge;			
	@JsonProperty("Product_ASTel")
	private String Product_ASTel;			
	@JsonProperty("OverSeaDeliveryYN")
	private String OverSeaDeliveryYN;		
	@JsonProperty("Min_OverSeaDeliveryDay")
	private Integer  Min_OverSeaDeliveryDay;
	@JsonProperty("Max_OverSeaDeliveryDay")
	private Integer  Max_OverSeaDeliveryDay;	
    @JsonProperty("VideoURL")
    private String  VideoURL;	
	@JsonProperty("ItemID")
	private Integer ItemID;   
	@JsonProperty("IsImgUpdate")
	private String IsImgUpdate; 	
	@JsonProperty("ISBNCode")
	private String ISBNCode;
	
	@JsonProperty("ItemNo")
	public String getItemNo() {
		return ItemNo;
	}
	public void setItemNo(String itemNo) {
		ItemNo = itemNo;
	}
	
	@JsonProperty("ItemID")
	public Integer getItemID() {
		return ItemID;
	}
	public void setItemID(Integer itemId) {
		ItemID = itemId;
	}
	
	@JsonProperty("ItemName")
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	@JsonProperty("SalePrice")
	public double getSalePrice() {
		return SalePrice;
	}
	public void setSalePrice(double salePrice) {
		SalePrice = salePrice;
	}
	@JsonProperty("ConsumerPrice")
	public double getConsumerPrice() {
		return ConsumerPrice;
	}
	public void setConsumerPrice(double consumerPrice) {
		ConsumerPrice = consumerPrice;
	}
	@JsonProperty("BrandId")
	public Integer getBrandId() {
		return BrandId;
	}
	public void setBrandId(Integer brandId) {
		BrandId = brandId;
	}
	@JsonProperty("MakeYear")
	public String getMakeYear() {
		return MakeYear;
	}
	public void setMakeYear(String makeYear) {
		MakeYear = makeYear;
	}
	@JsonProperty("Home")
	public String getHome() {
		return Home;
	}
	public void setHome(String home) {
		Home = home;
	}
	@JsonProperty("Category1")
	public Integer getCategory1() {
		return Category1;
	}
	public void setCategory1(Integer category1) {
		Category1 = category1;
	}
	@JsonProperty("DisplayItemName")
	public String getDisplayItemName() {
		return DisplayItemName;
	}
	public void setDisplayItemName(String displayItemName) {
		DisplayItemName = displayItemName;
	}
	@JsonProperty("Description")
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	@JsonProperty("ImageURL1")
	public String getImageURL1() {
		return ImageURL1;
	}
	public void setImageURL1(String imageURL1) {
		ImageURL1 = imageURL1;
	}
	@JsonProperty("ImageURL2")
	public String getImageURL2() {
		return ImageURL2;
	}
	public void setImageURL2(String imageURL2) {
		ImageURL2 = imageURL2;
	}
	@JsonProperty("ImageURL3")
	public String getImageURL3() {
		return ImageURL3;
	}
	public void setImageURL3(String imageURL3) {
		ImageURL3 = imageURL3;
	}
	@JsonProperty("ImageURL4")
	public String getImageURL4() {
		return ImageURL4;
	}
	public void setImageURL4(String imageURL4) {
		ImageURL4 = imageURL4;
	}
	@JsonProperty("OverSeaDeliveryYN")
	public String getOverSeaDeliveryYN() {
		return OverSeaDeliveryYN;
	}
	public void setOverSeaDeliveryYN(String overSeaDeliveryYN) {
		OverSeaDeliveryYN = overSeaDeliveryYN;
	}
	@JsonProperty("Options")
	public List<Options> getOptions() {
		return Options;
	}
	public void setOptions(List<Options> options) {
		Options = options;
	}
	@JsonProperty("Product_num")
	public Integer getProduct_num() {
		return Product_num;
	}
	public void setProduct_num(Integer product_num) {
		Product_num = product_num;
	}
	@JsonProperty("Product_Material")
	public String getProduct_Material() {
		return Product_Material;
	}
	public void setProduct_Material(String product_Material) {
		Product_Material = product_Material;
	}
	@JsonProperty("Product_Color")
	public String getProduct_Color() {
		return Product_Color;
	}
	public void setProduct_Color(String product_Color) {
		Product_Color = product_Color;
	}
	@JsonProperty("Product_Size")
	public String getProduct_Size() {
		return Product_Size;
	}
	public void setProduct_Size(String product_Size) {
		Product_Size = product_Size;
	}
	@JsonProperty("Product_Maker")
	public String getProduct_Maker() {
		return Product_Maker;
	}
	public void setProduct_Maker(String product_Maker) {
		Product_Maker = product_Maker;
	}
	@JsonProperty("Product_Home")
	public String getProduct_Home() {
		return Product_Home;
	}
	public void setProduct_Home(String product_Home) {
		Product_Home = product_Home;
	}
	@JsonProperty("Product_Washing")
	public String getProduct_Washing() {
		return Product_Washing;
	}
	public void setProduct_Washing(String product_Washing) {
		Product_Washing = product_Washing;
	}
	@JsonProperty("Product_MakeYear")
	public String getProduct_MakeYear() {
		return Product_MakeYear;
	}
	public void setProduct_MakeYear(String product_MakeYear) {
		Product_MakeYear = product_MakeYear;
	}
	@JsonProperty("Product_Warranty")
	public String getProduct_Warranty() {
		return Product_Warranty;
	}
	public void setProduct_Warranty(String product_Warranty) {
		Product_Warranty = product_Warranty;
	}
	@JsonProperty("Product_KindType")
	public String getProduct_KindType() {
		return Product_KindType;
	}
	public void setProduct_KindType(String product_KindType) {
		Product_KindType = product_KindType;
	}
	@JsonProperty("Product_Comprisal")
	public String getProduct_Comprisal() {
		return Product_Comprisal;
	}
	public void setProduct_Comprisal(String product_Comprisal) {
		Product_Comprisal = product_Comprisal;
	}
	@JsonProperty("Product_Model")
	public String getProduct_Model() {
		return Product_Model;
	}
	public void setProduct_Model(String product_Model) {
		Product_Model = product_Model;
	}
	@JsonProperty("Product_KC")
	public String getProduct_KC() {
		return Product_KC;
	}
	public void setProduct_KC(String product_KC) {
		Product_KC = product_KC;
	}
	@JsonProperty("Product_InstallCost")
	public String getProduct_InstallCost() {
		return Product_InstallCost;
	}
	public void setProduct_InstallCost(String product_InstallCost) {
		Product_InstallCost = product_InstallCost;
	}
	@JsonProperty("Product_Voltage")
	public String getProduct_Voltage() {
		return Product_Voltage;
	}
	public void setProduct_Voltage(String product_Voltage) {
		Product_Voltage = product_Voltage;
	}
	@JsonProperty("Product_Type")
	public String getProduct_Type() {
		return Product_Type;
	}
	public void setProduct_Type(String product_Type) {
		Product_Type = product_Type;
	}
	@JsonProperty("Product_Weight")
	public String getProduct_Weight() {
		return Product_Weight;
	}
	public void setProduct_Weight(String product_Weight) {
		Product_Weight = product_Weight;
	}
	@JsonProperty("Product_Period")
	public String getProduct_Period() {
		return Product_Period;
	}
	public void setProduct_Period(String product_Period) {
		Product_Period = product_Period;
	}
	@JsonProperty("Product_Way")
	public String getProduct_Way() {
		return Product_Way;
	}
	public void setProduct_Way(String product_Way) {
		Product_Way = product_Way;
	}
	@JsonProperty("Product_Ingredient")
	public String getProduct_Ingredient() {
		return Product_Ingredient;
	}
	public void setProduct_Ingredient(String product_Ingredient) {
		Product_Ingredient = product_Ingredient;
	}
	@JsonProperty("Product_EvaluateYN")
	public String getProduct_EvaluateYN() {
		return Product_EvaluateYN;
	}
	public void setProduct_EvaluateYN(String product_EvaluateYN) {
		Product_EvaluateYN = product_EvaluateYN;
	}
	@JsonProperty("Product_Careful")
	public String getProduct_Careful() {
		return Product_Careful;
	}
	public void setProduct_Careful(String product_Careful) {
		Product_Careful = product_Careful;
	}
	@JsonProperty("Product_Grade")
	public String getProduct_Grade() {
		return Product_Grade;
	}
	public void setProduct_Grade(String product_Grade) {
		Product_Grade = product_Grade;
	}
	@JsonProperty("Product_Function")
	public String getProduct_Function() {
		return Product_Function;
	}
	public void setProduct_Function(String product_Function) {
		Product_Function = product_Function;
	}
	@JsonProperty("Product_WarrantyofferYN")
	public String getProduct_WarrantyofferYN() {
		return Product_WarrantyofferYN;
	}
	public void setProduct_WarrantyofferYN(String product_WarrantyofferYN) {
		Product_WarrantyofferYN = product_WarrantyofferYN;
	}
	@JsonProperty("Product_UseAge")
	public String getProduct_UseAge() {
		return Product_UseAge;
	}
	public void setProduct_UseAge(String product_UseAge) {
		Product_UseAge = product_UseAge;
	}
	@JsonProperty("Product_ASTel")
	public String getProduct_ASTel() {
		return Product_ASTel;
	}
	public void setProduct_ASTel(String product_ASTel) {
		Product_ASTel = product_ASTel;
	}
	@JsonProperty("Min_OverSeaDeliveryDay")
	public Integer getMin_OverSeaDeliveryDay() {
		return Min_OverSeaDeliveryDay;
	}
	public void setMin_OverSeaDeliveryDay(Integer min_OverSeaDeliveryDay) {
		Min_OverSeaDeliveryDay = min_OverSeaDeliveryDay;
	}
	@JsonProperty("Max_OverSeaDeliveryDay")
	public Integer getMax_OverSeaDeliveryDay() {
		return Max_OverSeaDeliveryDay;
	}
	public void setMax_OverSeaDeliveryDay(Integer max_OverSeaDeliveryDay) {
		Max_OverSeaDeliveryDay = max_OverSeaDeliveryDay;
	}
	@JsonProperty("IsImgUpdate")
	public String getIsImgUpdate() {
		return IsImgUpdate;
	}
	public void setIsImgUpdate(String isImgUpdate) {
		IsImgUpdate = isImgUpdate;
	}	
    public String getVideoURL() {
        return VideoURL;
    }
    public void setVideoURL(String videoURL) {
        VideoURL = videoURL;
    }
	public String getISBNCode() {
		return ISBNCode;
	}
	public void setISBNCode(String iSBNCode) {
		ISBNCode = iSBNCode;
	}	
}
