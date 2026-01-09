package com.cware.netshopping.paqeen.domain;



import com.cware.netshopping.paqeeen.domain.model.BrandSourcing;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class BrandSourcingVO extends BrandSourcing {
	
}
