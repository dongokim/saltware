package com.saltware.enface.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.portals.bridges.common.GenericServletPortlet;

public abstract class BasePortlet extends GenericServletPortlet {
	
	protected final Log log = LogFactory.getLog(getClass());
	
//	protected String viewPrefix = "/WEB-INF/apps/enface/WEB-INF/view/portlet/";
	protected String viewPrefix = "/face/portlet/";
	protected String viewPostfix = ".jsp";
	
	protected void forward(RenderRequest request, RenderResponse response, String viewName) throws IOException, PortletException {
		String viewPath = viewPrefix + viewName + viewPostfix;
		PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher( viewPath);
		prd.include(request, response);
	}
	
	protected String getPortletPath( RenderRequest request) {
		return request.getContextPath() + "/face/portlet/";
		
	}
	

	public void doView(RenderRequest request, RenderResponse response)	throws PortletException, IOException {
//		try {
			String view = processView( request, response);
			if( view != null) {
				forward(request, response, view);
			}
//		} catch (BaseException e) {
//			response.setContentType("text/html");
//			PrintWriter pw = response.getWriter();
//			pw.println( "Exception");
//			log.error( e.getMessage(), e);
//		}
	}
	
	public String processView( RenderRequest request, RenderResponse response)	throws PortletException, IOException {
		return null;
	}
	
	public String getProperty( PortletRequest request, String key, String defaultValue) {
		String value = getPortletConfig().getInitParameter(key);
		value =  value != null ? value : request.getProperty(key);
		return value != null ? value : defaultValue;
	}
	public String getProperty( PortletRequest request, String key) {
		return getProperty(request, key, null);
	}

	
}
