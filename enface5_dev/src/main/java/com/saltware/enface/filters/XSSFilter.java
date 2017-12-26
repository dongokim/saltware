package com.saltware.enface.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XSSFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest hreq = (HttpServletRequest) request;
			if (isInvalid(hreq.getQueryString()) || isInvalid(hreq.getRequestURI())) {
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		chain.doFilter(request, response);

	}
	
	public boolean isInvalid( String value) {
		if( value==null) return false;
		value = value.toLowerCase();
        return value.indexOf('<') != -1 
        		|| value.indexOf('>') != -1 
                || value.indexOf("%3c") != -1 
                || value.indexOf("%3e") != -1
                || value.indexOf("&lt;") != -1
                || value.indexOf("&gt;") != -1
                //|| value.toLowerCase().indexOf("script") != -1
                ;
	}

	public void init(FilterConfig arg0) throws ServletException {
		//
	}

	public void destroy() {
		//
	}

}
