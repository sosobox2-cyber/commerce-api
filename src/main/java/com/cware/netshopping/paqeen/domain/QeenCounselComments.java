package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class QeenCounselComments {
	
	private int id;
	private Writer writer;
	private String content;
	private long createdAtMillis;
	private List<String> fileUrls;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Writer getWriter() {
		return writer;
	}
	public void setWriter(Writer writer) {
		this.writer = writer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreatedAtMillis() {
		return createdAtMillis;
	}
	public void setCreatedAtMillis(long createdAtMillis) {
		this.createdAtMillis = createdAtMillis;
	}
	public List<String> getFileUrls() {
		return fileUrls;
	}
	public void setFileUrls(List<String> fileUrls) {
		this.fileUrls = fileUrls;
	}
	

	
	
	
}
