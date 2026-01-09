package com.cware.netshopping.paintp.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class DelvCost {

	private String title;
	private String description;

	private DelvCostPlc item;

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

	public DelvCostPlc getItem() {
		return item;
	}

	public void setItem(DelvCostPlc item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "DelvCost [title=" + title + ", description=" + description + ", item=" + item + "]";
	}

}
