package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Classification {
	
    private String criterionId;
    private String criterionText;
    private List<Element> elements;
    
	public String getCriterionId() {
		return criterionId;
	}
	public void setCriterionId(String criterionId) {
		this.criterionId = criterionId;
	}
	public String getCriterionText() {
		return criterionText;
	}
	public void setCriterionText(String criterionText) {
		this.criterionText = criterionText;
	}
	public List<Element> getElements() {
		return elements;
	}
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
    
    
    
    
	
}
