package com.cware.netshopping.pafaple.domain;

import java.util.List;

import com.cware.framework.core.basic.AbstractModel;
import com.cware.netshopping.pafaple.domain.model.Program;

public class Programs extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
		
	private String broadDate;
	private List<Program> program;
		
	public String getBroadDate() {
		return broadDate;
	}

	public void setBroadDate(String broadDate) {
		this.broadDate = broadDate;
	}

	public List<Program> getProgram() {
		return program;
	}

	public void setProgram(List<Program> program) {
		this.program = program;
	}	

	
}