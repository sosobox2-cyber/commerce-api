package com.cware.api.panaver.order.seller;

public class NaverSignature {
	private String accessLicense;
	private String timeStamp;
	private String signature;
	private byte[] encryptKey = null;
	
	public String getAccessLicense() {
		return accessLicense;
	}
	public void setAccessLicense(String accessLicense) {
		this.accessLicense = accessLicense;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public byte[] getEncryptKey() {
		return encryptKey;
	}
	public void setEncryptKey(byte[] encryptKey) {
		this.encryptKey = encryptKey;
	} 
}
