package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Custcounseldt extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String counselSeq;
	private String counselDtSeq;
	private String doFlag;
	private String title;
	private String displayYn;
	private String procNote;
	private Timestamp procDate;
	private String procId;
	private Timestamp endDate;
	private String receiverSeq;
	private Timestamp hcReqDate;

	public String getCounselSeq() { 
		return this.counselSeq;
	}
	public String getCounselDtSeq() { 
		return this.counselDtSeq;
	}
	public String getDoFlag() { 
		return this.doFlag;
	}
	public String getTitle() { 
		return this.title;
	}
	public String getDisplayYn() { 
		return this.displayYn;
	}
	public String getProcNote() { 
		return this.procNote;
	}
	public Timestamp getProcDate() { 
		return this.procDate;
	}
	public String getProcId() { 
		return this.procId;
	}
	public Timestamp getEndDate() { 
		return this.endDate;
	}

	public void setCounselSeq(String counselSeq) { 
		this.counselSeq = counselSeq;
	}
	public void setCounselDtSeq(String counselDtSeq) { 
		this.counselDtSeq = counselDtSeq;
	}
	public void setDoFlag(String doFlag) { 
		this.doFlag = doFlag;
	}
	public void setTitle(String title) { 
		this.title = title;
	}
	public void setDisplayYn(String displayYn) { 
		this.displayYn = displayYn;
	}
	public void setProcNote(String procNote) { 
		this.procNote = procNote;
	}
	public void setProcDate(Timestamp procDate) { 
		this.procDate = procDate;
	}
	public void setProcId(String procId) { 
		this.procId = procId;
	}
	public void setEndDate(Timestamp endDate) { 
		this.endDate = endDate;
	}
	public String getReceiverSeq() {
		return receiverSeq;
	}
	public void setReceiverSeq(String receiverSeq) {
		this.receiverSeq = receiverSeq;
	}
	public Timestamp getHcReqDate() {
		return hcReqDate;
	}
	public void setHcReqDate(Timestamp hcReqDate) {
		this.hcReqDate = hcReqDate;
	}
}
