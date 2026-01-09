package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.cware.partner.sync.domain.id.PaNoticeSeq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TPANOTICETARGET")
@IdClass(PaNoticeSeq.class)
public class PaNoticeTarget {

	@Id
	private String noticeNo;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqNoticetargetNo")
	@SequenceGenerator(sequenceName = "SEQ_NOTICETARGET_NO", name = "seqNoticetargetNo", allocationSize = 50) 
	// 시퀀스 한 번 호출 증가하는 수 50으로 설정, 할당한 값만큼 한번에 시퀀스 값을 증가시키고 나서 메모리에 시퀀스 값을 할당.
	private int noticeSeq;
	private String goodsCode;
	private String targetGb;
	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
}
