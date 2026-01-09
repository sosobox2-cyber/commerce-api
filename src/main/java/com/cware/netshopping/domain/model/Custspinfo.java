package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Custspinfo extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String custNo;
	private String seq;
	private String type;
	private String content;
	private Timestamp spBdate;
	private Timestamp spEdate;
	private String msg;

	public String getCustNo() { 
		return this.custNo;
	}
	public String getSeq() { 
		return this.seq;
	}
	public String getType() { 
		return this.type;
	}
	public String getContent() { 
		return this.content;
	}
	public Timestamp getSpBdate() { 
		return this.spBdate;
	}
	public Timestamp getSpEdate() { 
		return this.spEdate;
	}
	public String getMsg() { 
		return this.msg;
	}

	public void setCustNo(String custNo) { 
		this.custNo = custNo;
	}
	public void setSeq(String seq) { 
		this.seq = seq;
	}
	public void setType(String type) { 
		this.type = type;
	}
	public void setContent(String content) { 
		this.content = content;
	}
	public void setSpBdate(Timestamp spBdate) { 
		this.spBdate = spBdate;
	}
	public void setSpEdate(Timestamp spEdate) { 
		this.spEdate = spEdate;
	}
	public void setMsg(String msg) { 
		this.msg = msg;
	}
}
