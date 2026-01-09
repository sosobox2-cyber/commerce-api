package com.cware.netshopping.common;

import java.util.ArrayList;
import java.util.Arrays;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class SqlLoggerFilter extends Filter<ILoggingEvent> {
	  @Override
	  public FilterReply decide(ILoggingEvent event) {    
	    if (event.getMessage().contains("Parameters")) {
	    	
	    	ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList("selectAuthInfo"));
	    	
	    	for(String str : arrayList){
	    		if(event.getLoggerName().contains(str)) return FilterReply.DENY;
	    	}
	    	return FilterReply.ACCEPT;
	    } else {
	      return FilterReply.ACCEPT;
	    }
	  }
}
