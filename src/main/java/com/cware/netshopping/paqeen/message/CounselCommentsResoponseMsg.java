package com.cware.netshopping.paqeen.message;

import com.cware.framework.core.basic.AbstractMessage;
import com.cware.netshopping.paqeen.domain.QeenCounselCommentsResponse;

public class CounselCommentsResoponseMsg extends AbstractMessage {

	private static final long serialVersionUID = 3389878192227221199L;
	private QeenCounselCommentsResponse commentsResponse; 
	
	public CounselCommentsResoponseMsg(String code, String message, QeenCounselCommentsResponse commentsResponse) {
    	this.setCode(code);
    	this.setMessage(message);
    	this.setCommentsResponse(commentsResponse);
    }

	public QeenCounselCommentsResponse getCommentsResponse() {
		return commentsResponse;
	}

	public void setCommentsResponse(QeenCounselCommentsResponse commentsResponse) {
		this.commentsResponse = commentsResponse;
	}







}
