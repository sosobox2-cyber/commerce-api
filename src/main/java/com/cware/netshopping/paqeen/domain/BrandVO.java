package com.cware.netshopping.paqeen.domain;



import com.cware.netshopping.paqeeen.domain.model.Brand;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class BrandVO extends Brand {
}
