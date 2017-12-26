/**
 * Copyright (c) 2010 Saltware, Inc.
 * 
 * http://www.saltware.co.kr
 * 
 * Kolon Science Valley Bldg 2th. 901, Guro-dong 811, Guro-gu,
 * Seoul, 152-878, South Korea.
 * All Rights Reserved.
 * 
 * This software is the Java based Enterprise Portal of Saltware, Inc.
 * Making any change or distributing this without permission from us is out of law.
 */
package com.saltware.enface.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Attribute 덤프 필터. 디버깅용.<br> 
 * ServletContext, Session, Request의 Attribute를 console에 출력한다.<br>
 * 운영시에는 반드시 web.xml 에서 제거한다.
 * 
 * @version 3.2.2
 */
public class AttributesDumperFilter implements Filter {

	private Log console = LogFactory.getLog("console");
    protected FilterConfig filterConfig = null;
    protected boolean ignore = false;

    /**
     * 필터 사용을 중지한다. 
     */
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * 필터를 적용한다.
     * ServletContext, Session, Request의 Attribute를 console에 출력한다.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest hrequest = (HttpServletRequest) request;
	    
	    String uri = hrequest.getRequestURI();
	    if (uri.endsWith(".gif") || uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".js") || uri.endsWith(".css"))
	    	return;
		
		console.info("********************************************************************************");
		Enumeration ctxtAttrs = ((HttpServletRequest)request).getSession().getServletContext().getAttributeNames();
		Enumeration sessAttrs = ((HttpServletRequest)request).getSession().getAttributeNames();
		Enumeration rqstAttrs = ((HttpServletRequest)request).getAttributeNames();
		
		
		console.info("ServletContext's attributes");
		while(ctxtAttrs.hasMoreElements()) {
			String attr = (String)ctxtAttrs.nextElement();
			if(!(attr.startsWith("org.apache.catalina"))) {
				Object obj = ((HttpServletRequest)request).getSession().getServletContext().getAttribute(attr);
				console.info("\t[" + attr + "] = [" + obj.getClass().getName() + "]"
		                     //+ ( obj instanceof String ? "[" + ((String)obj).toString() + "]" : ""));
					         + "[" + obj.toString() + "]");
		
			}
		}
		
		console.info("--------------------------------------------------------------------------------");
		console.info("Sessiion's attributes");
		while(sessAttrs.hasMoreElements()) {
			String attr = (String)sessAttrs.nextElement();
			if(!(attr.startsWith("org.apache.jetspeed.resovler.cache"))) {
				Object obj = ((HttpServletRequest)request).getSession().getAttribute(attr);
				console.info("\t[" + attr + "]=[" + obj.getClass().getName() + "]"
			             //+ ( obj instanceof String ? "[" + ((String)obj).toString() + "]" : ""));
						 + "[" + obj.toString() + "]");
			}
		}
		console.info("--------------------------------------------------------------------------------");
		console.info("Request's attributes");
		while(rqstAttrs.hasMoreElements()) {
			String attr = (String)rqstAttrs.nextElement();
			Object obj = ((HttpServletRequest)request).getAttribute(attr);
			console.info("\t[" + attr + "]=[" + obj.getClass().getName() + "]"
		             //+ ( obj instanceof String ? "[" + ((String)obj).toString() + "]" : ""));
					 + "[" + obj.toString() + "]");
		}
		console.info("********************************************************************************");
	
		chain.doFilter(request, response);
	}

    /**
     * 필터를 초기화한다.
     */
    public void init(FilterConfig filterConfig) throws ServletException {

    	this.filterConfig = filterConfig;
        String value = filterConfig.getInitParameter("ignore");
        if (value == null)
            this.ignore = true;
        else if (value.equalsIgnoreCase("true"))
            this.ignore = true;
        else if (value.equalsIgnoreCase("yes"))
            this.ignore = true;
        else
            this.ignore = false;
    }
}
