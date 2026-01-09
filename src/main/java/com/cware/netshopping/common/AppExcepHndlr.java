package com.cware.netshopping.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cware.framework.common.exception.handler.ExceptionHandler;


/**
 * @Class Name : AppExcepHndlr.java
 * @Description : AppExcepHndlr Class
 * @Modification Information
 * @author Commerceware
 * @since 2009.08.05
 * @version 1.0
 *
 */
public class AppExcepHndlr implements ExceptionHandler {

    protected Log log = LogFactory.getLog(this.getClass());

    /**
    * @param ex
    * @param packageName
    * @see Commerceware
    */
    public void occur(Exception ex, String packageName) {
//		log.debug(" AppExcepHndlr run..............." + packageName);
    	log.error(packageName);
    	log.error(ex);
    }
}
