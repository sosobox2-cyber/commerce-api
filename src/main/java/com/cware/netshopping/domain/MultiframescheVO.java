package com.cware.netshopping.domain;

import com.cware.framework.core.basic.AbstractVO;

public class MultiframescheVO extends AbstractVO{

	private static final long serialVersionUID = 1L;
	
	private String seqFrameNo;	
	private String bdDate;
	private String bdBtime;
	private String bdEtime;	
	private String progCode;
	private String progName;
	private String hostSraff;
	private String itemCode;	
	private String makeItemCode;
	private String itemName;
	private String bannerImgUrl;
	private String itemImgUrl;	
	private int itemPrice;
	private String publicItemYn;
	private String intngItemYn;
	private String repItemYn;
	private String bdImgUrl;
	
	
	private String useYn;
	private String modifyFlag;
	private String seqNo;	
	private String broadCastDate;
	private String programType;
	private String broadcastProgramType;
	private String broadcastProgramDetailType;
	private String broadcastHour;
	private String shManName;
	private String bundleGoodsYn;
	private String paGoodsCode;
	private String goodsCode;
	private String goodsName;
	private String vodUrl;
	private String videoImage;
	private String alternativeurlUseYn;
	private String alternativeVodUrl;
	private int 	cycleSeq;
	private int 	minCycleSeq;
	
	private String paGroupCode;

	public int getMinCycleSeq() {
		return minCycleSeq;
	}
	public void setMinCycleSeq(int minCycleSeq) {
		this.minCycleSeq = minCycleSeq;
	}
	public int getCycleSeq() {
		return cycleSeq;
	}
	public void setCycleSeq(int cycleSeq) {
		this.cycleSeq = cycleSeq;
	}
	public String getPaGoodsCode() {
		return paGoodsCode;
	}
	public void setPaGoodsCode(String paGoodsCode) {
		this.paGoodsCode = paGoodsCode;
	}
	public String getAlternativeurlUseYn() {
		return alternativeurlUseYn;
	}
	public void setAlternativeurlUseYn(String alternativeurlUseYn) {
		this.alternativeurlUseYn = alternativeurlUseYn;
	}
	public String getAlternativeVodUrl() {
		return alternativeVodUrl;
	}
	public void setAlternativeVodUrl(String alternativeVodUrl) {
		this.alternativeVodUrl = alternativeVodUrl;
	}
	public String getModifyFlag() {
		return modifyFlag;
	}
	public void setModifyFlag(String modifyFlag) {
		this.modifyFlag = modifyFlag;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public String getBroadcastProgramType() {
		return broadcastProgramType;
	}
	public void setBroadcastProgramType(String broadcastProgramType) {
		this.broadcastProgramType = broadcastProgramType;
	}
	public String getBroadcastProgramDetailType() {
		return broadcastProgramDetailType;
	}
	public void setBroadcastProgramDetailType(String broadcastProgramDetailType) {
		this.broadcastProgramDetailType = broadcastProgramDetailType;
	}
	public String getBroadCastDate() {
		return broadCastDate;
	}
	public void setBroadCastDate(String broadCastDate) {
		this.broadCastDate = broadCastDate;
	}
	public String getBroadcastHour() {
		return broadcastHour;
	}
	public void setBroadcastHour(String broadcastHour) {
		this.broadcastHour = broadcastHour;
	}
	public String getShManName() {
		return shManName;
	}
	public void setShManName(String shManName) {
		this.shManName = shManName;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getVodUrl() {
		return vodUrl;
	}
	public void setVodUrl(String vodUrl) {
		this.vodUrl = vodUrl;
	}
	public String getVideoImage() {
		return videoImage;
	}
	public void setVideoImage(String videoImage) {
		this.videoImage = videoImage;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getBundleGoodsYn() {
		return bundleGoodsYn;
	}
	public void setBundleGoodsYn(String bundleGoodsYn) {
		this.bundleGoodsYn = bundleGoodsYn;
	}
	public String getBdImgUrl() {
		return bdImgUrl;
	}
	public void setBdImgUrl(String bdImgUrl) {
		this.bdImgUrl = bdImgUrl;
	}
	public String getItemImgUrl() {
		return itemImgUrl;
	}
	public void setItemImgUrl(String itemImgUrl) {
		this.itemImgUrl = itemImgUrl;
	}
	public String getRepItemYn() {
		return repItemYn;
	}
	public void setRepItemYn(String repItemYn) {
		this.repItemYn = repItemYn;
	}
	private int priority;
	
	
	public String getSeqFrameNo() {
		return seqFrameNo;
	}
	public void setSeqFrameNo(String seqFrameNo) {
		this.seqFrameNo = seqFrameNo;
	}
	public String getBdDate() {
		return bdDate;
	}
	public void setBdDate(String bdDate) {
		this.bdDate = bdDate;
	}
	public String getBdBtime() {
		return bdBtime;
	}
	public void setBdBtime(String bdBtime) {
		this.bdBtime = bdBtime;
	}
	public String getBdEtime() {
		return bdEtime;
	}
	public void setBdEtime(String bdEtime) {
		this.bdEtime = bdEtime;
	}
	public String getProgCode() {
		return progCode;
	}
	public void setProgCode(String progCode) {
		this.progCode = progCode;
	}
	public String getProgName() {
		return progName;
	}
	public void setProgName(String progName) {
		this.progName = progName;
	}
	public String getHostSraff() {
		return hostSraff;
	}
	public void setHostSraff(String hostSraff) {
		this.hostSraff = hostSraff;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getMakeItemCode() {
		return makeItemCode;
	}
	public void setMakeItemCode(String makeItemCode) {
		this.makeItemCode = makeItemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getBannerImgUrl() {
		return bannerImgUrl;
	}
	public void setBannerImgUrl(String bannerImgUrl) {
		this.bannerImgUrl = bannerImgUrl;
	}
	public int getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getPublicItemYn() {
		return publicItemYn;
	}
	public void setPublicItemYn(String publicItemYn) {
		this.publicItemYn = publicItemYn;
	}
	public String getIntngItemYn() {
		return intngItemYn;
	}
	public void setIntngItemYn(String intngItemYn) {
		this.intngItemYn = intngItemYn;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	
	
}