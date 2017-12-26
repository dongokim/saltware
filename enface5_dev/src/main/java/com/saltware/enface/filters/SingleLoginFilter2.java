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

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enface.user.service.SiteUserManager;
import com.saltware.enview.Enview;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.sso.EnviewSSOManager;

public class SingleLoginFilter2 {
	
	protected final Log log = LogFactory.getLog(getClass());

	protected FilterConfig filterConfig = null;
	private String logoutUrl = "/user/logout.face";
	private long checkInterval = 5;
	private long lastCheckTime = 0;
	private SiteUserManager siteUserManager = null;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
    public SiteUserManager  getSiteUserManager() {
    	if( this.siteUserManager==null) {
            siteUserManager = (SiteUserManager)Enview.getComponentManager().getComponent("com.saltware.enface.user.service.UserManagerImpl");
    	}
    	return siteUserManager;    	
    }	
	
    /**
     * 필터사용 중지 처리를 한다.
     */
    public void destroy() {
    	//
    }

	public boolean doFilter(){
	 
		HttpServletRequest req = (HttpServletRequest)request;
    	HttpServletResponse res = (HttpServletResponse)response;
    	
    	String uri = req.getRequestURI();
		log.debug("do filter : " + uri);	
    	if( ( System.currentTimeMillis() - lastCheckTime > checkInterval * 1000)) {
    		if( isSessionExpired( req, res)) {
    			
    			log.debug("Session expired by other user");	
    			return false;
    		}else {
    			lastCheckTime = System.currentTimeMillis();
    			return true;
    		}
    		
    	}
    	
    	return true;    	
	}
	
	public boolean isSessionExpired( HttpServletRequest request, HttpServletResponse response)  {
    	
    	String uri = request.getRequestURI();
    	
	    if (uri.endsWith(".gif") || uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".js") || uri.endsWith(".css"))
	    	return false;
	    
	    // logout url이면 통과
	    if( uri.indexOf( logoutUrl) !=-1) return false;
    	
		UserInfo userInfo = EnviewSSOManager.getUserInfo( request);
		if( userInfo == null) {
			// 세션이 없으면 expire = false;
			return false;
		}
		String userId = userInfo.getUserId();
		
		String cookieSessionId = null;

		cookieSessionId = request.getSession().getId();
				
    	if( cookieSessionId !=null) {
    		//String userSessionId = getSiteUserManager().getSessionId(userId);
    		String userSessionId = "";
    		log.debug("cookieSessionId=" + cookieSessionId);
    		log.debug("userSessionId=" + userSessionId);
    		if( ! cookieSessionId.equals( userSessionId)) {
    			return true;
    		}
    		
    	}
	    return false;
    }
}