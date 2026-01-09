package com.cware.netshopping.paintp.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class DelvCostResult {

	private String title;
	private String description;

	private DelvCostPlc delvCostPlc;

	private Error error;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DelvCostPlc getDelvCostPlc() {
		return delvCostPlc;
	}

	public void setDelvCostPlc(DelvCostPlc delvCostPlc) {
		this.delvCostPlc = delvCostPlc;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "DelvCostResult [title=" + title + ", description=" + description + ", delvCostPlc=" + delvCostPlc
				+ ", error=" + error + "]";
	}

}
