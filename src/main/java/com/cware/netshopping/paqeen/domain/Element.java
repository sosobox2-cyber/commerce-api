package com.cware.netshopping.paqeen.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Element {
	
	private String name;
    private boolean classified;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isClassified() {
		return classified;
	}
	public void setClassified(boolean classified) {
		this.classified = classified;
	}
    
    
    
    
    
    
	
}
