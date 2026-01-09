package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaWempGoods;

public class PaWempGoodsVO extends PaWempGoods {

    private static final long serialVersionUID = 1L;

    //상세기술서
    private String describeExt;

    //상품 이미지
    private String imageP; 
    private String imageAP;
    private String imageBP;
    private String imageCP;
    private String imageDP;
    private String imageUrl;
    private String imageNm;
	
    //as안내,반품교환안내 전화번호
    private String csTel;
    
    //상품가격
    private Timestamp applyDate;
    private String transApplyDate;
    private String transId;
    private Timestamp transDate;
    private long salePrice;
    private long supplyPrice;
    private long commision;
    private long dcAmt;
    // 판매기간 
    private String saleStartDate; //시작일
    private String saleEndDate;   //종료일
    
    //상단이미지
    private String topImage;
    //하단이미지
    private String bottomImage;
    
    private String makecoName;
    private String installYn;
    
    //일시불 금액
  	private long lumpSumDcAmt;
  	private long lumpSumOwnDcAmt;
  	private long lumpSumEntpDcAmt;
  	
  	private String collectImage;
  	private String collectYn;
  	
  	private Timestamp lastModifyDate;
  	private String massTargetYn;
  	
  	private String goodsCom;
  	private String noticeExt;
  	
  	private String luxuryYn;
  	
  	
	public String getMassTargetYn() {
		return massTargetYn;
	}

	public void setMassTargetYn(String massTargetYn) {
		this.massTargetYn = massTargetYn;
	}

	public String getCollectImage() {
		return collectImage;
	}

	public void setCollectImage(String collectImage) {
		this.collectImage = collectImage;
	}
   
    public String getDescribeExt() {
        return describeExt;
    }

    public void setDescribeExt(String describeExt) {
        this.describeExt = describeExt;
    }

    public String getImageP() {
        return imageP;
    }

    public String getImageAP() {
        return imageAP;
    }

    public String getImageBP() {
        return imageBP;
    }

    public String getImageCP() {
        return imageCP;
    }

    public String getImageDP() {
        return imageDP;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageNm() {
        return imageNm;
    }

    public void setImageP(String imageP) {
        this.imageP = imageP;
    }

    public void setImageAP(String imageAP) {
        this.imageAP = imageAP;
    }

    public void setImageBP(String imageBP) {
        this.imageBP = imageBP;
    }

    public void setImageCP(String imageCP) {
        this.imageCP = imageCP;
    }

    public void setImageDP(String imageDP) {
        this.imageDP = imageDP;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageNm(String imageNm) {
        this.imageNm = imageNm;
    }

    public String getCsTel() {
        return csTel;
    }

    public void setCsTel(String csTel) {
        this.csTel = csTel;
    }

    public String getTransId() {
        return transId;
    }

    public Timestamp getTransDate() {
        return transDate;
    }

    public long getSalePrice() {
        return salePrice;
    }

    public long getSupplyPrice() {
        return supplyPrice;
    }

    public long getCommision() {
        return commision;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public void setTransDate(Timestamp transDate) {
        this.transDate = transDate;
    }

    public void setSalePrice(long salePrice) {
        this.salePrice = salePrice;
    }

    public void setSupplyPrice(long supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public void setCommision(long commision) {
        this.commision = commision;
    }

	public String getSaleStartDate() {
		return saleStartDate;
	}

	public void setSaleStartDate(String saleStartDate) {
		this.saleStartDate = saleStartDate;
	}

	public String getSaleEndDate() {
		return saleEndDate;
	}

	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}
    
	public String getTopImage() {
		return topImage;
	}
	public void setTopImage(String topImage) {
		this.topImage = topImage;
	}
	public String getBottomImage() {
		return bottomImage;
	}
	public void setBottomImage(String bottomImage) {
		this.bottomImage = bottomImage;
	}
	public String getMakecoName() {
		return makecoName;
	}
	public void setMakecoName(String makecoName) {
		this.makecoName = makecoName;
	}
	public String getInstallYn() {
		return installYn;
	}
	public void setInstallYn(String installYn) {
		this.installYn = installYn;
	}

	public long getDcAmt() {
		return dcAmt;
	}

	public void setDcAmt(long dcAmt) {
		this.dcAmt = dcAmt;
	}

	public Timestamp getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
	}

	public String getTransApplyDate() {
		return transApplyDate;
	}

	public void setTransApplyDate(String transApplyDate) {
		this.transApplyDate = transApplyDate;
	}
	public long getLumpSumDcAmt() {
		return lumpSumDcAmt;
	}
	public void setLumpSumDcAmt(long lumpSumDcAmt) {
		this.lumpSumDcAmt = lumpSumDcAmt;
	}
	public long getLumpSumOwnDcAmt() {
		return lumpSumOwnDcAmt;
	}
	public void setLumpSumOwnDcAmt(long lumpSumOwnDcAmt) {
		this.lumpSumOwnDcAmt = lumpSumOwnDcAmt;
	}
	public long getLumpSumEntpDcAmt() {
		return lumpSumEntpDcAmt;
	}
	public void setLumpSumEntpDcAmt(long lumpSumEntpDcAmt) {
		this.lumpSumEntpDcAmt = lumpSumEntpDcAmt;
	}

	public String getCollectYn() {
		return collectYn;
	}

	public void setCollectYn(String collectYn) {
		this.collectYn = collectYn;
	}

	public Timestamp getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Timestamp lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public String getGoodsCom() {
		return goodsCom;
	}

	public void setGoodsCom(String goodsCom) {
		this.goodsCom = goodsCom;
	}

	public String getNoticeExt() {
		return noticeExt;
	}

	public void setNoticeExt(String noticeExt) {
		this.noticeExt = noticeExt;
	}

	public String getLuxuryYn() {
		return luxuryYn;
	}

	public void setLuxuryYn(String luxuryYn) {
		this.luxuryYn = luxuryYn;
	}
	
}
