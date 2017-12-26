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
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enface.user.service.SiteUserManager;
import com.saltware.enface.util.StringUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.sso.EnviewSSOManager;


/**
 * 단일로그인 필터<br>
 * EnviewSessionID쿠키 값과 사용자정보의 세션ID가 다르면 로그아웃시킨다.
 * @author smna
 *
 */
public class SingleLoginFilter implements Filter {
	
    protected final Log log = LogFactory.getLog(this.getClass());
	
	private String logoutUrl = null;
	private String loginUrl = null;
	private long checkInterval = 0;
	private long lastCheckTime = 0;
	private SiteUserManager siteUserManager = null;
	
	
    /**
     * 필터를 초기화한다.<br>
     * 필터설정에서 문자셋을 읽는다.
     * @param filterConfig - 필터설정
     * @throws - ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        logoutUrl = filterConfig.getInitParameter("logoutUrl");
        loginUrl = filterConfig.getInitParameter("loginUrl");
        String value = StringUtil.isNullTrim(filterConfig.getInitParameter("checkInterval"));
        //if( value != null && value.trim().length() > 0) {
        if( value.length() > 0) {
        	checkInterval = Long.parseLong(value);
        }
        if( checkInterval <= 0  ) {
        	checkInterval = 5;
        }
        log.debug("logoutUrl=" + logoutUrl);
        log.debug("checkInterval=" + checkInterval);
    }
	
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

		
//    	Cookie[] cookies = ((HttpServletRequest)request).getCookies();
//    	for (int i = 0; i < cookies.length; i++) {
//			if( cookies[i].getName().equals("EnviewSessionID")) {
//				cookieSessionId = cookies[i].getValue();
//			}
//		}
		
		//cookieSessionId = EnviewSSOManager.getEnviewSessionID(request);
		
		cookieSessionId = request.getSession().getId();
			
    	if( cookieSessionId !=null) {
    		Map userSessionMap = getSiteUserManager().getSessionId(userId);
    		String prevUserSessionId = (String) userSessionMap.get("USER_INFO08");
    		String userSessionId = (String) userSessionMap.get("USER_INFO09");
    		String doubleCheck = (String) userSessionMap.get("USER_INFO10");
    		    		
    		if( doubleCheck.equals("N") || doubleCheck.equals("D")) {  // 중복체크 안함.
    			return false;
    		}
    		
    		log.debug("cookieSessionId=" + cookieSessionId);
    		log.debug("userSessionId=" + userSessionId);
    		if( ! cookieSessionId.equals( userSessionId )) {
    			return true;
    		}
    		
    	}
	    return false;
    }

    /**
     * EnviewSessionID쿠키 값과 사용자정보의 세션ID가 다르면 로그아웃시킨다.
     * @param request - ServletRequest
     * @param response - ServletResponse
     * @param chain - FilterChain
     * @throws ServletException
     * @throws IOException
     */ 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
    	HttpServletRequest req = (HttpServletRequest)request;
    	HttpServletResponse res = (HttpServletResponse)response;
    	
    	String uri = req.getRequestURI();
    
		log.debug("do filter : " + uri);	
	    	if( ( System.currentTimeMillis() - lastCheckTime > checkInterval * 1000)) {
	    		if( isSessionExpired( req, res)) {
	    			
	    			log.debug("Session expired by other user");	
	    			if( logoutUrl==null) {
	    				req.getSession().invalidate();
	    				
	    				res.setContentType("text/html;char-conding=UFT-8");
	    				PrintWriter pw = response.getWriter();
	    				pw.println( "Session expired. Other user is logined with yout id");
	    				pw.flush();
	    			} else {
	    				
	    				req.getSession().invalidate();
	    				String errorMessage = "Session expired. Other user is logined with yout id";
	    				req.getSession().setAttribute("errorMessage", errorMessage);
	    				res.sendRedirect(loginUrl);
	    			//	res.sendRedirect(logoutUrl);
	    			//	res.sendRedirect( logoutUrl+"?destination=/user/login.face?errorMessage=A21");
	    			
	    			}
	    			return;
	    		}
	    		lastCheckTime = System.currentTimeMillis();
	    	}

        // Pass control on to the next filter
        chain.doFilter(request, response);

    }

}
