package com.saltware.enface.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saltware.enview.security.UserInfo;

/**
 * SSO  처리를 위한 핸들러
 * @author smna
 *
 */
public interface SSOLoginHandler {
	
	/**
	 * 로그인 처리를 한다.
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @param userInfo 사용자정보
	 * @throws Exception
	 */
	public void processLogin( HttpServletRequest request, HttpServletResponse response, UserInfo userInfo ) throws Exception;
	
	/**
	 * 로그아웃 처리를 한다.
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws Exception
	 */
	public void processLogout( HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	/**
	 * 리다이렉트가 필요한지 여부를 리턴한다. 
	 * @return 리다이렉트 필요여부  
	 */
	public boolean isRedirectRequired();
	
	/**
	 * 사용자변경 처리를 한다.
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @param userInfo 사용자정보
	 * @throws Exception
	 */
	public void processChangeUser( HttpServletRequest request, HttpServletResponse response, UserInfo userInfo ) throws Exception;
	
}
