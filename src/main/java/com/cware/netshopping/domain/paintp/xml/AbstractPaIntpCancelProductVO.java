package com.cware.netshopping.domain.paintp.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * abstract 주문취소상품 xml mapping VO
 * @author FIC05202
 *
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public abstract class AbstractPaIntpCancelProductVO extends PaIntpProductVO {
	private static final long serialVersionUID = 1L;
	
	/** 클레임요청상태 */
	@XmlElement(name = "CLMREQ_STATNM")
	private String clmreqStatnm;
	
	/** 클레임요청상태코드(1: 요청, 2:요청철회) */
	@XmlElement(name = "CLMREQ_STAT")
	private String clmreqStat;
	
	/** 클레임요청수량 */
	@XmlElement(name = "CLMREQ_QTY")
	private long clmreqQty;
	
	/** 클레임요청사유 */
	@XmlElement(name = "CLMREQ_RSN_TPNM")
	private String clmreqRsnTpnm;
	
	/** 클레임요청사유코드 */
	@XmlElement(name = "CLMREQ_RSN_TP")
	private String clmreqRsnTp;
	
	/** 클레임요청상세사유 */
	@XmlElement(name = "CLMREQ_RSN_DTL")
	private String clmreqRsnDtl;

	public String getClmreqStatnm() {
		return clmreqStatnm;
	}

	public void setClmreqStatnm(String clmreqStatnm) {
		this.clmreqStatnm = clmreqStatnm;
	}

	public String getClmreqStat() {
		return clmreqStat;
	}

	public void setClmreqStat(String clmreqStat) {
		this.clmreqStat = clmreqStat;
	}

	public long getClmreqQty() {
		return clmreqQty;
	}

	public void setClmreqQty(long clmreqQty) {
		this.clmreqQty = clmreqQty;
	}

	public String getClmreqRsnTpnm() {
		return clmreqRsnTpnm;
	}

	public void setClmreqRsnTpnm(String clmreqRsnTpnm) {
		this.clmreqRsnTpnm = clmreqRsnTpnm;
	}

	public String getClmreqRsnTp() {
		return clmreqRsnTp;
	}

	public void setClmreqRsnTp(String clmreqRsnTp) {
		this.clmreqRsnTp = clmreqRsnTp;
	}

	public String getClmreqRsnDtl() {
		return clmreqRsnDtl;
	}

	public void setClmreqRsnDtl(String clmreqRsnDtl) {
		this.clmreqRsnDtl = clmreqRsnDtl;
	}

}
