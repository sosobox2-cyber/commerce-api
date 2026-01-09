package com.cware.netshopping.common;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class AntiCrossSiteScriptingFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		req.setCharacterEncoding("UTF-8");
		
//		System.out.println("------------------------------------------------------------");
//		System.out.println("CrossSiteScript Defence Init..");
//		System.out.println("------------------------------------------------------------");
		
		// XSS Defense code
		String attack_str = "";
		
		if ((attack_str = XSS_ATTACK_DEFENCE(req)) != null) {
			
//			System.out.println("------------------------------------------------------------");
//			System.out.println("Find CrossSiteScript !!");
//			System.out.println("------------------------------------------------------------");
			
			response.setContentType("text/html;charset=utf-8"); 
			response.getWriter().println("<script>alert('해킹 공격이 가능한 코드가 포함되어 있습니다.\\n \"" + attack_str +"\" 코드가 있다면 제거해주세요.');history.back();</script>");
			
			
			response.getWriter().close();
			return;
		} else {

//			System.out.println("------------------------------------------------------------");
//			System.out.println("Not Find CrossSiteScript");
//			System.out.println("Next Process..");
//			System.out.println("------------------------------------------------------------");
			
			//필터 통과
			chain.doFilter(request, response);
		}
		
	}
		
	//XSS  공격  필터링.. 
	protected String XSS_ATTACK_DEFENCE(HttpServletRequest request){
		
		String[] ATTACK_TMP_VALUE_GET = {"\"\"","javascript","<script","<iframe"," or ", "#","\\", "alert", "document", "<img" , "<body" , "<meta" , "<table" , "<frameset" , "<div" , "<a" , "onload", "onerror", "vbscript", "livescript"}; //GET방식
		String[] ATTACK_TMP_VALUE_POST = {"\"\"","javascript","<script","<iframe"," or ", "#","\\", "alert", "<img" , "<body" , "<meta" , "<table" , "<frameset" , "<div" , "<a" , "onload", "onerror", "vbscript", "livescript" }; //POST방식
		
		Enumeration<?> list = request.getParameterNames();
//		System.out.println("------------------------------------------------------------");
//		System.out.println("Request Method = " + request.getMethod());
//		System.out.println("------------------------------------------------------------");
		
		while (list.hasMoreElements()) {
			String paramName = (String)list.nextElement();
			String strParam = request.getParameter(paramName).toLowerCase();

//			System.out.println("------------------------------------------------------------");
//			System.out.println("Request paramName = " + paramName + ", Request paramValue = " + strParam);
//			System.out.println("------------------------------------------------------------");
			
			if ( request.getMethod().equals("GET"))  {
				for ( int i = 0 ;i < ATTACK_TMP_VALUE_GET.length ; i++) {
					if (strParam.indexOf(ATTACK_TMP_VALUE_GET[i]) >= 0) {
						return ATTACK_TMP_VALUE_GET[i];
					}
				}
			}else {
				for ( int i = 0 ;i < ATTACK_TMP_VALUE_POST.length ; i++) {
					if (strParam.indexOf(ATTACK_TMP_VALUE_POST[i]) >= 0) {
						return ATTACK_TMP_VALUE_POST[i];
					}
				}
			}
		}

		return null;
	}
	
}
