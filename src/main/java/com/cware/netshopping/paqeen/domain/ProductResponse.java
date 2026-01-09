package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ProductResponse {
	
	private ProductProposal productProposal;

	public ProductProposal getProductProposal() {
		return productProposal;
	}

	public void setProductProposal(ProductProposal productProposal) {
		this.productProposal = productProposal;
	}

	
    
    
	
}
