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
package com.saltware.enface.user.web;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.saltware.enview.Enview;
import com.saltware.enface.user.service.SiteUserManager;
import com.saltware.enface.user.service.UserVO;
import com.saltware.enface.user.service.impl.UserException;
import com.saltware.enview.code.EnviewCodeManager;
import com.saltware.enview.codebase.CodeBundle;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.multiresource.EnviewMultiResourceManager;
import com.saltware.enview.multiresource.MultiResourceBundle;
import com.saltware.enview.session.SessionManager;
import com.saltware.enview.statistics.PortalStatistics;
import com.saltware.enview.util.EnviewLocale;
import com.saltware.enview.util.HttpUtil;

/**
 * 사용자 비밀번호변경 Controller
 * @author kevin
 */

public class UserPasswordChangeController extends MultiActionController {

	private final Log   log = LogFactory.getLog(getClass());
	
	private SiteUserManager siteUserManager;
	private SessionManager enviewSessionManager; 

	/**
	 * 생성자
	 */
	public UserPasswordChangeController()
	{
		this.enviewSessionManager = (SessionManager)Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
	}
	
	/**
	 * 사용자관리자를 리턴한다
	 * @return 사용자관리자
	 */
	public SiteUserManager getUserManager() {
		return siteUserManager;
	}

	/**
	 * 사용자관리자를 설정한다
	 * @param siteUserManager 사용자관리자
	 */
	public void setUserManager(SiteUserManager siteUserManager) {
		this.siteUserManager = siteUserManager;
	}
	
	/**
	 * 비밀번호를 변경화면을 출력한다.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView changePassword(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = EnviewCodeManager.getInstance().getBundle( langKnd );
		
		UserVO user = new UserVO();
		
		List langKndList = (List)enviewCodeBundle.getCodes("PT", "105", 1, true);
		user.setLangKndList( langKndList );
		
		request.setAttribute("langKndList", langKndList);
		request.setAttribute("langKnd", request.getLocale().getLanguage());
		
		String loginUrl = request.getContextPath() + Enview.getConfiguration().getString("sso.login.page");
		String destination = request.getParameter("destination");
		if( destination != null && destination.length()>0 ) {
			loginUrl += "?destination=" + destination;
		}
		request.setAttribute("loginUrl", loginUrl);
		
		return new ModelAndView("/user/passwordChange");
	}
	
	/**
	 * 비밀번호변경을 처리한다.  
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 폼정보
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView changePasswordProcess(HttpServletRequest request, HttpServletResponse response, UserForm formData) throws Exception 
	{
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle( langKnd );

        HttpSession session = request.getSession(true);

        String current = formData.getCurrent();
        String destination = formData.getDestination();
        String userId = (String)session.getAttribute(LoginConstants.SSO_LOGIN_ID);
        
        if( userId == null ) {
        	destination = Enview.getConfiguration().getString("sso.login.page", "/user/login.face");
        	return new ModelAndView( "redirect:" + destination );
        }
        
        String password = null;
        String encorderPassword = formData.getPassword();
        String passwordNew = null;
        String encorderPasswordNew = formData.getPasswordNew();
        String encorderPasswordConfirm = formData.getPasswordConfirm();
        
        //if( encorderPasswordNew.equals(encorderPasswordConfirm) == false ) {
        if( !encorderPasswordNew.equals(encorderPasswordConfirm) ) {
	        request.setAttribute("errorMessage", enviewMessages.getString("ev.error.user.ErrorCode.9"));
	        return new ModelAndView("/user/passwordChange");
        }
        
        if (encorderPassword != null) {
            boolean enableInterEncryption = Enview.getConfiguration().getBoolean("sso.interEncryption", true);
            if( enableInterEncryption ) {
	        	byte[] value = encorderPassword.getBytes();
		        for (int i=0; i<value.length; i++) {
		        	value[i] = (byte)(value[i] + ((i+2) % 7));
		        }
		        password = new String(value);
		        
		        value = encorderPasswordNew.getBytes();
		        for (int i=0; i<value.length; i++) {
		        	value[i] = (byte)(value[i] + ((i+2) % 7));
		        }
		        passwordNew = new String(value);
            }
            else {
            	password = encorderPassword;
            	passwordNew = encorderPasswordNew;
            }
        }
        
    	try {
    		
    		siteUserManager.changePassword(userId, password, passwordNew);
    		
    		Map paramMap = new HashMap();
    		paramMap.put("userId", userId);
    		paramMap.put("status", "" + PortalStatistics.STATUS_CHANGE_PASSWORD);
    		paramMap.put("remoteAddress", HttpUtil.getClientIp(request));
    		paramMap.put("userAgent", HttpUtil.getUserAgent(request));
    		
    		siteUserManager.log(paramMap);

	        return new ModelAndView("/user/passwordChanged");
    		
    	}
		catch(UserException se) 
		{
	        String msgKey = se.getMessageKey();
	        if( msgKey != null ) {
	        	String errorMessage = enviewMessages.getString( msgKey );
		        request.setAttribute("errorMessage", errorMessage);
		        log.debug("*** errorMessage=" + errorMessage);
		        
		        if( "ev.error.user.ErrorCode.2".equals(msgKey) ) {
			        Map paramMap = new HashMap();
		    		paramMap.put("userId", userId);
		    		paramMap.put("status", "" + PortalStatistics.STATUS_ERROR_PASSWORD);
		    		paramMap.put("remoteAddress", HttpUtil.getClientIp(request));
		    		paramMap.put("userAgent", HttpUtil.getUserAgent(request));
		    		
		    		siteUserManager.log(paramMap);
		        }
	        }
	        
	        return new ModelAndView("/user/passwordChange");
        }
	}

}
