package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Thumbnail {

	private String thumbnailUrl;
	private MultiResolutionThumbnail multiResolutionThumbnail;
	private Label label;
	 
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public MultiResolutionThumbnail getMultiResolutionThumbnail() {
		return multiResolutionThumbnail;
	}
	public void setMultiResolutionThumbnail(MultiResolutionThumbnail multiResolutionThumbnail) {
		this.multiResolutionThumbnail = multiResolutionThumbnail;
	}
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
}
