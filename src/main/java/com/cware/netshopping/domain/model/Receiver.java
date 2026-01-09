package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Receiver extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String custNo;
	private String receiverSeq;
	private String receiverGb;
	private String useYn;
	private String defaultYn;
	private String receiver;
	private String receiver1;
	private String receiver2;
	private String receiver3;
	private String tel;
	private String receiverDdd;
	private String receiverTel1;
	private String receiverTel2;
	private String receiverTel3;
	private String receiverPost;
	private String receiverPostAddr;
	private String receiverPostSeq;
	private String receiverAddr;
	private String receiverHp1;
	private String receiverHp2;
	private String receiverHp3;
	private String receiverHp1Se;
	private String receiverHp2Se;
	private String receiverHp3Se;
	private Timestamp lastUseDate;
	private String beforeSeq;
	private String validTelYn;
	private String receiverHp;
	private String receiverAddr1;
	private String receiverAddr2;
	private String receiverTel;
	private String nickName;

	private String roadAddrYn;
	private String seqRoadNo;
	private String roadPostNo;
	private String roadPostSeq;
	private String roadPostAddr;
	private String roadAddr;
	private String roadAddrNo;
	

	private String smartOrderYn;
	
	private String receiverRoadPost;
	private String receiverRoadPostSeq;
	private String receiverRoadPostAddr;
	private String receiverRoadPostAddr2;
	
	private String receiverStdPost;
	private String receiverStdPostSeq;
	private String receiverStdPostAddr;
	private String receiverStdPostAddr2;
	private String selectAddr;
	private String fullAddr;
	
	private String deliveryDefaultYn;
	private String refineResultCode;
	
	private String stdPostLngx;
	private String stdPostLaty;
	private String stdRoadPostLngx;
	private String stdRoadPostLaty;
	
	
	public String getStdPostLngx() {
		return stdPostLngx;
	}
	public void setStdPostLngx(String stdPostLngx) {
		this.stdPostLngx = stdPostLngx;
	}
	public String getStdPostLaty() {
		return stdPostLaty;
	}
	public void setStdPostLaty(String stdPostLaty) {
		this.stdPostLaty = stdPostLaty;
	}
	public String getStdRoadPostLngx() {
		return stdRoadPostLngx;
	}
	public void setStdRoadPostLngx(String stdRoadPostLngx) {
		this.stdRoadPostLngx = stdRoadPostLngx;
	}
	public String getStdRoadPostLaty() {
		return stdRoadPostLaty;
	}
	public void setStdRoadPostLaty(String stdRoadPostLaty) {
		this.stdRoadPostLaty = stdRoadPostLaty;
	}
	public String getRefineResultCode() {
		return refineResultCode;
	}
	public void setRefineResultCode(String refineResultCode) {
		this.refineResultCode = refineResultCode;
	}
	public String getRoadAddrNo() {
		return roadAddrNo;
	}
	public void setRoadAddrNo(String roadAddrNo) {
		this.roadAddrNo = roadAddrNo;
	}
	
	public String getRoadPostAddr() {
		return roadPostAddr;
	}
	public void setRoadPostAddr(String roadPostAddr) {
		this.roadPostAddr = roadPostAddr;
	}
	
	
	public String getReceiverPostAddr() {
		return receiverPostAddr;
	}
	public void setReceiverPostAddr(String receiverPostAddr) {
		this.receiverPostAddr = receiverPostAddr;
	}
	
	
	public String getRoadAddrYn() {
		return roadAddrYn;
	}
	public void setRoadAddrYn(String roadAddrYn) {
		this.roadAddrYn = roadAddrYn;
	}
	public String getSeqRoadNo() {
		return seqRoadNo;
	}
	public void setSeqRoadNo(String seqRoadNo) {
		this.seqRoadNo = seqRoadNo;
	}
	public String getRoadPostNo() {
		return roadPostNo;
	}
	public void setRoadPostNo(String roadPostNo) {
		this.roadPostNo = roadPostNo;
	}
	public String getRoadPostSeq() {
		return roadPostSeq;
	}
	public void setRoadPostSeq(String roadPostSeq) {
		this.roadPostSeq = roadPostSeq;
	}
	public String getRoadAddr() {
		return roadAddr;
	}
	public void setRoadAddr(String roadAddr) {
		this.roadAddr = roadAddr;
	}
	public String getCustNo() { 
		return this.custNo;
	}
	public String getReceiverSeq() { 
		return this.receiverSeq;
	}
	public String getReceiverGb() { 
		return this.receiverGb;
	}
	public String getUseYn() { 
		return this.useYn;
	}
	public String getDefaultYn() { 
		return this.defaultYn;
	}
	public String getReceiver() { 
		return this.receiver;
	}
	public String getReceiver1() { 
		return this.receiver1;
	}
	public String getReceiver2() { 
		return this.receiver2;
	}
	public String getReceiver3() { 
		return this.receiver3;
	}
	public String getTel() { 
		return this.tel;
	}
	public String getReceiverDdd() { 
		return this.receiverDdd;
	}
	public String getReceiverTel1() { 
		return this.receiverTel1;
	}
	public String getReceiverTel2() { 
		return this.receiverTel2;
	}
	public String getReceiverTel3() { 
		return this.receiverTel3;
	}
	public String getReceiverPost() { 
		return this.receiverPost;
	}
	public String getReceiverPostSeq() { 
		return this.receiverPostSeq;
	}
	public String getReceiverAddr() { 
		return this.receiverAddr;
	}
	public String getReceiverHp1() { 
		return this.receiverHp1;
	}
	public String getReceiverHp2() { 
		return this.receiverHp2;
	}
	public String getReceiverHp3() { 
		return this.receiverHp3;
	}
	public String getReceiverHp1Se() { 
		return this.receiverHp1Se;
	}
	public String getReceiverHp2Se() { 
		return this.receiverHp2Se;
	}
	public String getReceiverHp3Se() { 
		return this.receiverHp3Se;
	}
	public Timestamp getLastUseDate() { 
		return this.lastUseDate;
	}
	public String getBeforeSeq() { 
		return this.beforeSeq;
	}
	public String getValidTelYn() { 
		return this.validTelYn;
	}
	public String getReceiverHp() { 
		return this.receiverHp;
	}
	public String getReceiverAddr1() { 
		return this.receiverAddr1;
	}
	public String getReceiverAddr2() { 
		return this.receiverAddr2;
	}
	public String getReceiverTel() { 
		return this.receiverTel;
	}
	public void setCustNo(String custNo) { 
		this.custNo = custNo;
	}
	public void setReceiverSeq(String receiverSeq) { 
		this.receiverSeq = receiverSeq;
	}
	public void setReceiverGb(String receiverGb) { 
		this.receiverGb = receiverGb;
	}
	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	public void setDefaultYn(String defaultYn) { 
		this.defaultYn = defaultYn;
	}
	public void setReceiver(String receiver) { 
		this.receiver = receiver;
	}
	public void setReceiver1(String receiver1) { 
		this.receiver1 = receiver1;
	}
	public void setReceiver2(String receiver2) { 
		this.receiver2 = receiver2;
	}
	public void setReceiver3(String receiver3) { 
		this.receiver3 = receiver3;
	}
	public void setTel(String tel) { 
		this.tel = tel;
	}
	public void setReceiverDdd(String receiverDdd) { 
		this.receiverDdd = receiverDdd;
	}
	public void setReceiverTel1(String receiverTel1) { 
		this.receiverTel1 = receiverTel1;
	}
	public void setReceiverTel2(String receiverTel2) { 
		this.receiverTel2 = receiverTel2;
	}
	public void setReceiverTel3(String receiverTel3) { 
		this.receiverTel3 = receiverTel3;
	}
	public void setReceiverPost(String receiverPost) { 
		this.receiverPost = receiverPost;
	}
	public void setReceiverPostSeq(String receiverPostSeq) { 
		this.receiverPostSeq = receiverPostSeq;
	}
	public void setReceiverAddr(String receiverAddr) { 
		this.receiverAddr = receiverAddr;
	}
	public void setReceiverHp1(String receiverHp1) { 
		this.receiverHp1 = receiverHp1;
	}
	public void setReceiverHp2(String receiverHp2) { 
		this.receiverHp2 = receiverHp2;
	}
	public void setReceiverHp3(String receiverHp3) { 
		this.receiverHp3 = receiverHp3;
	}
	public void setReceiverHp1Se(String receiverHp1Se) { 
		this.receiverHp1Se = receiverHp1Se;
	}
	public void setReceiverHp2Se(String receiverHp2Se) { 
		this.receiverHp2Se = receiverHp2Se;
	}
	public void setReceiverHp3Se(String receiverHp3Se) { 
		this.receiverHp3Se = receiverHp3Se;
	}
	public void setLastUseDate(Timestamp lastUseDate) { 
		this.lastUseDate = lastUseDate;
	}
	public void setBeforeSeq(String beforeSeq) { 
		this.beforeSeq = beforeSeq;
	}
	public void setValidTelYn(String validTelYn) { 
		this.validTelYn = validTelYn;
	}
	public void setReceiverHp(String receiverHp) { 
		this.receiverHp = receiverHp;
	}
	public void setReceiverAddr1(String receiverAddr1) { 
		this.receiverAddr1 = receiverAddr1;
	}
	public void setReceiverAddr2(String receiverAddr2) { 
		this.receiverAddr2 = receiverAddr2;
	}
	public void setReceiverTel(String receiverTel) { 
		this.receiverTel = receiverTel;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getReceiverRoadPost() {
		return receiverRoadPost;
	}
	public void setReceiverRoadPost(String receiverRoadPost) {
		this.receiverRoadPost = receiverRoadPost;
	}
	public String getReceiverRoadPostSeq() {
		return receiverRoadPostSeq;
	}
	public void setReceiverRoadPostSeq(String receiverRoadPostSeq) {
		this.receiverRoadPostSeq = receiverRoadPostSeq;
	}
	public String getReceiverRoadPostAddr() {
		return receiverRoadPostAddr;
	}
	public void setReceiverRoadPostAddr(String receiverRoadPostAddr) {
		this.receiverRoadPostAddr = receiverRoadPostAddr;
	}
	public String getReceiverRoadPostAddr2() {
		return receiverRoadPostAddr2;
	}
	public void setReceiverRoadPostAddr2(String receiverRoadPostAddr2) {
		this.receiverRoadPostAddr2 = receiverRoadPostAddr2;
	}
	public String getReceiverStdPost() {
		return receiverStdPost;
	}
	public void setReceiverStdPost(String receiverStdPost) {
		this.receiverStdPost = receiverStdPost;
	}
	public String getReceiverStdPostSeq() {
		return receiverStdPostSeq;
	}
	public void setReceiverStdPostSeq(String receiverStdPostSeq) {
		this.receiverStdPostSeq = receiverStdPostSeq;
	}
	public String getReceiverStdPostAddr() {
		return receiverStdPostAddr;
	}
	public void setReceiverStdPostAddr(String receiverStdPostAddr) {
		this.receiverStdPostAddr = receiverStdPostAddr;
	}
	public String getReceiverStdPostAddr2() {
		return receiverStdPostAddr2;
	}
	public void setReceiverStdPostAddr2(String receiverStdPostAddr2) {
		this.receiverStdPostAddr2 = receiverStdPostAddr2;
	}
	public String getSelectAddr() {
		return selectAddr;
	}
	public void setSelectAddr(String selectAddr) {
		this.selectAddr = selectAddr;
	}
	public String getFullAddr() {
		return fullAddr;
	}
	public void setFullAddr(String fullAddr) {
		this.fullAddr = fullAddr;
	}
	public String getDeliveryDefaultYn() {
		return deliveryDefaultYn;
	}
	public void setDeliveryDefaultYn(String deliveryDefaultYn) {
		this.deliveryDefaultYn = deliveryDefaultYn;
	}
	public String getSmartOrderYn() {
		return smartOrderYn;
	}
	public void setSmartOrderYn(String smartOrderYn) {
		this.smartOrderYn = smartOrderYn;
	}
	
}
