package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Logo {
	
	private String landscapeUrl;
    private MultiResolutionLandscape multiResolutionLandscape;
    private String squareUrl;
    private MultiResolutionSquare multiResolutionSquare;
    
	public String getLandscapeUrl() {
		return landscapeUrl;
	}
	public void setLandscapeUrl(String landscapeUrl) {
		this.landscapeUrl = landscapeUrl;
	}
	public MultiResolutionLandscape getMultiResolutionLandscape() {
		return multiResolutionLandscape;
	}
	public void setMultiResolutionLandscape(MultiResolutionLandscape multiResolutionLandscape) {
		this.multiResolutionLandscape = multiResolutionLandscape;
	}
	public String getSquareUrl() {
		return squareUrl;
	}
	public void setSquareUrl(String squareUrl) {
		this.squareUrl = squareUrl;
	}
	public MultiResolutionSquare getMultiResolutionSquare() {
		return multiResolutionSquare;
	}
	public void setMultiResolutionSquare(MultiResolutionSquare multiResolutionSquare) {
		this.multiResolutionSquare = multiResolutionSquare;
	}
}
