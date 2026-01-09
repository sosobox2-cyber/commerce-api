package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaNoticeSeq implements Serializable {

	private static final long serialVersionUID = -3764533268917630520L;
	private String noticeNo;
	private int noticeSeq;

}