package com.cware.netshopping.paqeen.message;

import com.cware.framework.core.basic.AbstractMessage;
import com.cware.netshopping.paqeen.domain.QeenCounselDetail;

public class CounselDetailResoponseMsg extends AbstractMessage {

	private static final long serialVersionUID = -3864908384885408381L;
	
	private QeenCounselDetail qeenCounselDetail; 
	
	public CounselDetailResoponseMsg(String code, String message, QeenCounselDetail qeenCounselDetail) {
    	this.setCode(code);
    	this.setMessage(message);
    	this.setQeenCounselDetail(qeenCounselDetail);
    }

	public QeenCounselDetail getQeenCounselDetail() {
		return qeenCounselDetail;
	}

	public void setQeenCounselDetail(QeenCounselDetail qeenCounselDetail) {
		this.qeenCounselDetail = qeenCounselDetail;
	}







}
