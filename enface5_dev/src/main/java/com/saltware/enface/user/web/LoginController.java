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

import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.security.AccessControlException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


import com.saltware.enface.security.UserInfomationHandler;
import com.saltware.enface.sso.SSOLoginHandler;
import com.saltware.enface.user.service.SiteUserManager;
import com.saltware.enface.user.service.UserVO;
import com.saltware.enface.user.service.impl.SiteUserManagerDAO;
import com.saltware.enface.user.service.impl.UserException;
import com.saltware.enface.util.HttpUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.code.EnviewCodeManager;
import com.saltware.enview.codebase.CodeBundle;
import com.saltware.enview.domain.DomainInfo;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.login.LoginHandler;
import com.saltware.enview.multiresource.EnviewMultiResourceManager;
import com.saltware.enview.multiresource.MultiResourceBundle;
import com.saltware.enview.security.RememberMeServices;
import com.saltware.enview.security.SecurityPolicyManager;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.session.SessionManager;
import com.saltware.enview.sso.EnviewSSOManager;
import com.saltware.enview.statistics.PortalStatistics;
import com.saltware.enview.util.EnviewLocale;
import com.saltware.enview.util.json.JSONObject;

/**
 * 회원 로그인
 * 
 * @author kevin
 */

public class LoginController extends MultiActionController implements LoginHandler {

	protected final Log log = LogFactory.getLog(getClass());
	protected SiteUserManager siteUserManager;
	protected UserInfomationHandler userInfomationHandler;
	protected SSOLoginHandler ssoLoginHandler;
	protected RememberMeServices rememberMeServices;
	protected SessionManager enviewSessionManager; 
	
	
	/**
	 * Login Controller의 생성자
	 */
	public LoginController() {
		this.userInfomationHandler = (UserInfomationHandler) Enview.getComponentManager().getComponent("com.saltware.enface.security.UserInfomationHandler");
		this.enviewSessionManager = (SessionManager)Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
		this.rememberMeServices = (RememberMeServices)Enview.getComponentManager().getComponent("com.saltware.enview.security.RememberMeServices");
	}
	
	/**
	 * siteUserManager를리턴한다.
	 * 
	 * @return SiteuserManager
	 */
	public SiteUserManager getUserManager() {
		return siteUserManager;
	}

	/**
	 * siteUserManager를 설정한다.
	 * 
	 * @param siteUserManager SiteuserManager
	 */
	public void setUserManager(SiteUserManager siteUserManager) {
		this.siteUserManager = siteUserManager;
	}
	
	/**
	 * SSO 로그인 핸들러를 리턴한다.
	 * @return SSO로그인핸들러
	 */
	public SSOLoginHandler getSsoLoginHandler() {
		return ssoLoginHandler;
	}

	/**
	 * SSO로그인 핸들러를 설정한다.
	 * @param ssoLoginHandler SSO로그인핸들러 
	 */
	public void setSsoLoginHandler(SSOLoginHandler ssoLoginHandler) {
		this.ssoLoginHandler = ssoLoginHandler;
	}
	
	/**
	 * 로그인 화면을 보여준다.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ModelAndView
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String langKnd = request.getParameter("langKnd") == null ? request.getLocale().getLanguage() : request.getParameter("langKnd");
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		CodeBundle enviewCodeBundle = EnviewCodeManager.getInstance().getBundle(langKnd);

		List langKndList = (List) enviewCodeBundle.getCodes("PT", "105", 1, true);

		request.setAttribute("langKndList", langKndList);
		request.setAttribute("langKnd", langKnd);

		String loginUrl = Enview.getConfiguration().getString("sso.login.destination");
		
		String destination = request.getParameter("destination");
		if (destination != null && destination.length() > 0) {
			loginUrl += "?destination=" + URLEncoder.encode(destination, "utf-8");
			request.setAttribute("destination", destination);
		}
		request.setAttribute("useCaptcha", Enview.getConfiguration().getString("sso.login.captcha", "false"));
		request.setAttribute("loginUrl", loginUrl);

		log.info("*** loginUrl=" + loginUrl);

		return new ModelAndView(this.getLoginPage(request));
	}
	
	
	private Cookie getCookie( HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			if( cookies[i].getName().equals(name)) {
				return cookies[i];
			}
			
		}
		return null;
	}
	
	private String getCookieValue( HttpServletRequest request, String name, String defaultValue) {
		Cookie cookie = getCookie(request, name);
		return cookie == null ? defaultValue : cookie.getValue();
	}
	
	
	/**
	 * 로그인 화면(jsp 파일)을 리턴한다.
	 * @return String loginJsp
	 */
	protected String getLoginPage(HttpServletRequest request){
		DomainInfo domain = Enview.getUserDomain();
		String postfix = "";
		HttpSession session = request.getSession();
		String isMobile = (String)session.getAttribute("isMobile");
		if( "t".equals(isMobile)) {
			postfix = "_mobile";
		}
		String defaultLoginJsp = "/user/domain/public/login" + postfix;
		String loginJsp = defaultLoginJsp;
		if (domain != null && domain.getDomain() != null) {
			loginJsp = "/user/domain/" + domain.getLoginPage() + "/login" + postfix;
			File loginFile = new File(Enview.getRealPath("") + "/WEB-INF/apps/enface/WEB-INF/view" + loginJsp + ".jsp");
			if(!loginFile.exists()){
				//jsp 파일이 없으면 다시 기본 jsp 로그인 파일로 리턴한다.		
				log.info("*** '" + loginJsp + ".jsp' file is not exist. Replace view to '" + defaultLoginJsp + ".jsp' file. ");
				loginJsp = defaultLoginJsp;
			}
		}
		return loginJsp;
	}

	/**
	 * 사용자를 인증하고 로그인 프로세스를 진행한다.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData UserForm
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView loginProcess(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String destination = null;
		// 2014.03.21 
		// DB컨넥션과 커밋이 여러번 이루어지는 문제 때문에 SiteUsermanager로 로직이동
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle( EnviewSSOManager.getLangKnd(request));

		
		UserInfo userInfo = EnviewSSOManager.getUserInfo(request);
		// 아ㅣ미 로그인 되어 있는 경우 별도 처리 없이 첫화면으로 이동
		if( userInfo!=null && userInfo.getUserId()!=null) {
			destination = getDestination(request);
			return new ModelAndView("redirect:" + destination);
		}
		
		
		if( processLogin(request, response)) {
			userInfo = EnviewSSOManager.getUserInfo(request);
				try {
					this.rememberMeServices.loginSuccess(request, response, userInfo.getUserId());
		
					if ( ssoLoginHandler != null) {
						try {
							ssoLoginHandler.processLogin(request, response, userInfo);
							if (ssoLoginHandler.isRedirectRequired()) {
								// sso 서버로 리다이렉트하는데 destination이 있다면 세션에 보관한다.
								if( request.getParameter("destination")!=null) {
									request.getSession().setAttribute("destination", destination);
								}
								return null;
							}
						} catch (BaseException e) {
							request.setAttribute("errorMessage", e.getMessage());
							throw new BaseException( e.getMessage());
						}
					}
	
					destination = getDestination(request);
				} catch( BaseException e){
					log.error( e);
					String msgKey = e.getMessageKey();
					if (msgKey != null) {
						String errorMessage = enviewMessages.getString(msgKey);
						request.setAttribute("errorMessage", errorMessage);
						log.debug("*** errorMessage=" + errorMessage);
					} else {
						request.setAttribute("errorMessage", e.getMessage());
					}
					destination = Enview.getConfiguration().getString("sso.login.page");
					request.getRequestDispatcher(destination).forward(request, response);
					return null;
				}
				
					
			destination = getDestination(request);
			return new ModelAndView("redirect:" + destination);
		} else {
			destination = Enview.getConfiguration().getString("sso.login.page");
			request.getRequestDispatcher(destination).forward(request, response);
			return null;
		}
	}
	
	
	/**
	 * 로그인 후 이동할 목적지를 설정한다.
	 * 
	 * @param request HttpServletRequest
	 * @param userInfo사용자정보
	 * @return 이동할 목적지
	 * @throws Exception
	 */
	public String getDestination(HttpServletRequest request) throws Exception {
		UserInfo userInfo = EnviewSSOManager.getUserInfo(request);
		String current = request.getParameter("current");
		String destination = request.getParameter("destination");
		log.debug("request.current=" + current);
		log.debug("request.destination=" + destination);
		if( destination==null) {
			// 세션에 목적지가 있으면 해당 값을 사용한다.
			destination = (String)request.getSession().getAttribute("destination");
			log.debug("session.destination=" + destination);
		}
		//세션에 목적지가 있다면 삭제한다
		request.getSession().removeAttribute("destination");
		// 목적지가 ajax이면 목적지 사용안함
		if( destination!= null && destination.toLowerCase().indexOf("ajax") != -1) {
			destination = null;
			log.debug("ajax! destination=" + destination);
		}

		boolean isMobile = EnviewSSOManager.isMobile(request);
		String defaultPage = "";
		if (destination == null) {
			log.debug("destination is null");
			if (current != null && current.length() > 0 && current.indexOf(".page") > -1) {
				if (current.indexOf("?") > -1) {
					destination = current.substring(0, current.indexOf("?"));
				} else {
					destination = current;
				}
				log.debug("curent is not null! destination=" + destination);
			} else {
				destination = "/";
				if (userInfo != null) {
					if(isMobile) {
						defaultPage = userInfo.getString("sub_page", userInfo.getString("default_page", ""));
					} else {
						defaultPage = userInfo.getString("default_page", "");
					}
				}
			}
			destination = "/portal" + defaultPage;
			log.debug("destination=" + destination);
		} else {
//			if (destination.indexOf("?") > -1) {
//				destination = destination.substring(0, destination.indexOf("?"));
//			}
			if ("/".equals(destination)) {
				if (userInfo != null) {
					defaultPage = userInfo.getString("default_page", "");
				}
			}
		}
		
//		int pos = destination.indexOf(request.getContextPath());
//		if ("".equals(request.getContextPath()) == false && "/".equals(request.getContextPath()) == false && pos > -1) {
//			destination = destination.substring(request.getContextPath().length());
//		}
		
		if(userInfo != null && userInfo.getObject("update_required", "0") != null){
			if (userInfo.getObject("update_required", "0").toString().equals("1")) {
				//destination = "/user/changePassword.face";	//비밀번호 변경 단독 호출 방식
				destination = destination + "?pwdChgReq=true";	//비밀번호 변경 메인페이지에 모달창으로 호출 방식 (tile 테마에 적용해놧으니 참고.)
			}
		}

		return destination;
	}
	
	public void removeSessionIDCookie( HttpServletRequest request, HttpServletResponse response) {
		String cookieName = EnviewSSOManager.getEnviewSessionIDName(request);
		HttpUtil.addCookie(response, cookieName, "", "/", null, 0, request.isSecure(), true);
		HttpUtil.addCookie(response, cookieName, "", "/", HttpUtil.getDomain(request), 0, request.isSecure(), true);
	}
	
	/**
	 * 사용자를 로그아웃한다.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String destination = request.getParameter(LoginConstants.DESTINATION);
		HttpSession session = request.getSession();
		String ssoLoginId = (String) session.getAttribute(LoginConstants.SSO_LOGIN_ID);
		String cookieSessionId = session.getId();
		
		if (ssoLoginId != null) {
			if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page");
			}

			try {
				this.rememberMeServices.logout(request, response, ssoLoginId);

				try {
					session.invalidate();
				} catch( IllegalStateException e) {
				}
				
				removeSessionIDCookie(request, response);

				if( ssoLoginHandler!=null) {
					ssoLoginHandler.processLogout(request, response);
					if( ssoLoginHandler.isRedirectRequired()) {
						return null;
					}
				}
				
				Map cookiSeMap = new HashMap();
				cookiSeMap.put("userId", ssoLoginId);
				cookiSeMap.put("cookieSessionId", cookieSessionId);
				cookiSeMap.put("doubleCheck", "D");
				
				siteUserManager.logoutSessionId(cookiSeMap);
				
				return new ModelAndView("redirect:" + destination);
			} catch (BaseException e) {
				log.info("*** " + e.getMessage());
				HttpUtil.sendRedirect( response, request.getContextPath() + destination);
			}
		} else {
			if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page");
			}
			return new ModelAndView("redirect:" + destination);
		}
		return null;
	}
	
	
	//중복로그인 밀어내기 기능
	public void getCurrentUserSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();	
		
		int inCount = 0;
		String cookieSessionId = null;
		
		try {
			
			UserInfo userInfo = EnviewSSOManager.getUserInfo(request);			
			
			if(userInfo == null || userInfo.getUserId() == null) {
				jsonObj.put("result", "error");
				jsonObj.put("msg", "로그인 후 이용해주세요.");
			} 
			
			String userId = userInfo.getUserId();
			
			cookieSessionId = request.getSession().getId();
			
			Map<String,Object> userSessionMap = siteUserManager.getSessionId(userId);
			
			String prevUserSessionId = (String) userSessionMap.get("USER_INFO08");
    		String userSessionId = (String) userSessionMap.get("USER_INFO09");
    		String doubleCheck = (String) userSessionMap.get("USER_INFO10");
    		
    		if(!prevUserSessionId.equals(userSessionId) && userSessionId.equals(userSessionId) && userSessionId.equals(cookieSessionId) && doubleCheck.equals("D")) {
    			jsonObj.put("result", "Y");
				jsonObj.put("msg", "앞서 로그인한 사용자가 있습니다.");
    		}else {
    			jsonObj.put("result", "N");
				jsonObj.put("msg", "밀어내기 사용자 체크(X).");
    		}
    		
		} catch(Exception e) {
			jsonObj.put("result", "error");
			jsonObj.put("msg", e.getMessage());
		} finally {
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
		    response.getWriter().print(jsonObj.toString());
		}
	}
	
	//중복로그인 밀어내기 선택유무
	public void prevUserLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();	
		
		int upCheckValue = 0;
		String cookieSessionId = null;
		String checkType = request.getParameter("checkType"); //밀어내기 선택 유무 값
		
		try {
			
			UserInfo userInfo = EnviewSSOManager.getUserInfo(request);			
			
			if(userInfo == null || userInfo.getUserId() == null) {
				jsonObj.put("result", "error");
				jsonObj.put("msg", "로그인 후 이용해주세요.");
			} 
			
			String userId = userInfo.getUserId();
			cookieSessionId = request.getSession().getId();
			
			Map paramMap = new HashMap();
			
			paramMap.put("checkType", checkType);
			paramMap.put("userId", userId);
			paramMap.put("cookieSessionId", cookieSessionId);			
			
			upCheckValue = siteUserManager.updateCurrentSession(paramMap);			
    		
    		if(upCheckValue > 0) {
    			jsonObj.put("result", "Y");
				jsonObj.put("msg", "업데이트 완료.");
    		}else {
    			jsonObj.put("result", "N");
				jsonObj.put("msg", "업데이트 실패.");
    		}
    		
		} catch(Exception e) {
			jsonObj.put("result", "error");
			jsonObj.put("msg", e.getMessage());
		} finally {
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
		    response.getWriter().print(jsonObj.toString());
		}
	}

	
	
	//ReLogin 리로그인, 마이페이지 등에서 사용
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ModelAndView relogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession(true);
		UserInfo userInfo = null;
		String langKnd = request.getParameter("langKnd") == null ? 
				(session.getAttribute("langKnd") == null ? request.getLocale().getLanguage() : session.getAttribute("langKnd").toString()) :request.getParameter("langKnd");
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		
		String userId = (String)session.getAttribute(LoginConstants.SSO_LOGIN_ID);
		
		String destination = "";

		try {
			session.invalidate();
		} catch( IllegalStateException e) {
		}
		
		try {
			session = request.getSession(true);
			
			session.setAttribute(LoginConstants.SSO_LOGIN_ID, userId);
			userInfo = this.userInfomationHandler.createUserInfomation(request, response, userId, null);
			
			userInfo.setString("sid", session.getId());

			Map paramMap = new HashMap();
			paramMap.put("userId", userId);
			paramMap.put("status", "" + PortalStatistics.STATUS_LOGGED_IN);
			paramMap.put("remoteAddress", HttpUtil.getClientIp(request));
			paramMap.put("userAgent", HttpUtil.getUserAgent(request));
			siteUserManager.log(paramMap);
			
			paramMap = null;
			paramMap = userInfo.getUserInfoMap();
			String groupCode = (String)paramMap.get("groupCode");
			
			session.setAttribute(LoginConstants.USERNAME, userInfo.getUserName());
			session.setAttribute("langKnd", langKnd);
			
			this.rememberMeServices.loginSuccess(request, response, userId);

			if ( ssoLoginHandler != null) {
				try {
					ssoLoginHandler.processLogin(request, response, userInfo);
					//if (ssoLoginHandler.isRedirectRequired()) {
						//return false;
					//}
				} catch (BaseException e) {
					request.setAttribute("errorMessage", e.getMessage());
				}
			}
			
			destination = getDestination(request);
			
		} catch (UserException se) {
			log.error( se);
			String msgKey = se.getMessageKey();
			if (msgKey != null) {
				String errorMessage = enviewMessages.getString(msgKey);
				request.setAttribute("errorMessage", errorMessage);
				log.debug("*** errorMessage=" + errorMessage);
				//if ("ev.error.user.ErrorCode.1".equals(msgKey)) {
					// 사용자 없음
				//} else if ("ev.error.user.ErrorCode.2".equals(msgKey)) {
				if ("ev.error.user.ErrorCode.2".equals(msgKey)) {
					// 비밀번호 오류
					Map paramMap = new HashMap();
					paramMap.put("userId", userId);
					paramMap.put("status", "" + PortalStatistics.STATUS_ERROR_PASSWORD);
					paramMap.put("remoteAddress", HttpUtil.getClientIp(request));
					paramMap.put("userAgent", HttpUtil.getUserAgent(request));

					siteUserManager.updateAuthFailure(paramMap);
					siteUserManager.log(paramMap);
				} //else if ("ev.error.user.ErrorCode.3".equals(msgKey)) {
					// 비활성 ID
				//} else if ("ev.error.user.ErrorCode.8".equals(msgKey)) {
					// 비밀번호 오류횟수 초과
				//}
			}
			destination = Enview.getConfiguration().getString("sso.login.page");
			request.getRequestDispatcher(destination).forward(request, response);
			return null;
		} catch (BaseException e) {
			log.error( e.getMessage(), e);
		} 
		return new ModelAndView("redirect:" + destination);
	}
	
	
	/*
	//언어변경 
	public ModelAndView changeLang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//언어변경 했을 때 언어변경 후 다시 해당 페이지로 이동 - gjrjf
		String referer = request.getHeader("referer");
		log.info("User Lang Change. Request Header referer : " + referer);
		String destination = "";
		HttpSession session = request.getSession();
		
		if(referer == null){
			destination = getDestination(request);
		}else{
			if(referer.length() == 0) destination = getDestination(request);
			else destination = referer;
		}
		String oldLangKnd = EnviewSSOManager.getLangKnd(request);
		String langKnd = request.getParameter("langKnd");
		if( langKnd==null) {
			if( "ko".equals(oldLangKnd)) {
				langKnd = "en";
			} else {
				langKnd = "ko";
			}
		}
		if( !oldLangKnd.equals(langKnd)) {
			CommonUserMenu userMenuService = null;
	    	userMenuService = (CommonUserMenu)Enview.getComponentManager().getComponent("com.saltware.enface.userMenu.service.UserMenuService");
	    	userMenuService.invalidateCache( request);
			String cookieName = Enview.getConfiguration().getString("enview.cookie.langKnd.name");
			Cookie cookie = new Cookie(cookieName, langKnd);
			cookie.setPath("/");
			setCookieDomain( request, cookie);
			response.addCookie( cookie);
			
			session.setAttribute(cookieName, langKnd);
			enviewSessionManager.setUserDataValue( enviewSessionManager.getEnviewSessionID(request), "lang_knd", langKnd);
		}
		
		response.sendRedirect( destination);
		return null;
//		return new ModelAndView("redirect:" + destination);
	}
	*/
	
	//언어변경 changeLang
	@SuppressWarnings({ "rawtypes", "unchecked", "finally" })
	public ModelAndView changeLang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession(true);
		UserInfo userInfo = null;
		String userId = (String)session.getAttribute(LoginConstants.SSO_LOGIN_ID);
		
		//언어변경 했을 때 언어변경 후 다시 해당 페이지로 이동 - gjrjf
		String referer = request.getHeader("referer");
		log.info("User Lang Change. Request Header referer : " + referer);
		String destination = "";
		
		if(referer == null){
			destination = getDestination(request);
		}else{
			if(referer.length() == 0) destination = getDestination(request);
			else destination = referer;
		}
		
		String oldLangKnd = EnviewSSOManager.getLangKnd(request);
		String langKnd = request.getParameter("langKnd");
		if( langKnd==null) {
			if( "ko".equals(oldLangKnd)) {
				langKnd = "en";
			} else {
				langKnd = "ko";
			}
		}
		if( oldLangKnd.equals(langKnd)) {
			return new ModelAndView("redirect:" + destination);
		}
		

		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		try {
			session.invalidate();
		} catch( IllegalStateException e) {
		}
		
		try {
			session = request.getSession(true);
			
			session.setAttribute(LoginConstants.SSO_LOGIN_ID, userId);
			userInfo = this.userInfomationHandler.createUserInfomation(request, response, userId, null);
			
			userInfo.setString("sid", session.getId());

			Map paramMap = new HashMap();
			paramMap.put("userId", userId);
			paramMap.put("status", "" + PortalStatistics.STATUS_LOGGED_IN);
			paramMap.put("remoteAddress", HttpUtil.getClientIp(request));
			paramMap.put("userAgent", HttpUtil.getUserAgent(request));
			siteUserManager.log(paramMap);
			
			paramMap = null;
			paramMap = userInfo.getUserInfoMap();
			String groupCode = (String)paramMap.get("groupCode");
			
			session.setAttribute(LoginConstants.USERNAME, userInfo.getUserName());
			session.setAttribute("langKnd", langKnd);
			
			String langKndCookieName = Enview.getConfiguration().getString("enview.cookie.langKnd.name");
			
			HttpUtil.addCookie(response, langKndCookieName, langKnd, "/",  HttpUtil.getCookieDomain(request), -1, request.isSecure(), true);
			
			this.rememberMeServices.loginSuccess(request, response, userId);

			if ( ssoLoginHandler != null) {
				try {
					ssoLoginHandler.processLogin(request, response, userInfo);
					//if (ssoLoginHandler.isRedirectRequired()) {
						//return false;
					//}
				} catch (BaseException e) {
					request.setAttribute("errorMessage", e.getMessage());
				}
			}
		} catch (UserException se) {
			log.error( se);
			String msgKey = se.getMessageKey();
			if (msgKey != null) {
				String errorMessage = enviewMessages.getString(msgKey);
				request.setAttribute("errorMessage", errorMessage);
				log.debug("*** errorMessage=" + errorMessage);
				//if ("ev.error.user.ErrorCode.1".equals(msgKey)) {
					// 사용자 없음
				//} else if ("ev.error.user.ErrorCode.2".equals(msgKey)) {
				if ("ev.error.user.ErrorCode.2".equals(msgKey)) {
					// 비밀번호 오류
					Map paramMap = new HashMap();
					paramMap.put("userId", userId);
					paramMap.put("status", "" + PortalStatistics.STATUS_ERROR_PASSWORD);
					paramMap.put("remoteAddress", HttpUtil.getClientIp(request));
					paramMap.put("userAgent", HttpUtil.getUserAgent(request));

					siteUserManager.updateAuthFailure(paramMap);
					siteUserManager.log(paramMap);
				} //else if ("ev.error.user.ErrorCode.3".equals(msgKey)) {
					// 비활성 ID
				//} else if ("ev.error.user.ErrorCode.8".equals(msgKey)) {
					// 비밀번호 오류횟수 초과
				//}
			}
			destination = Enview.getConfiguration().getString("sso.login.page");
			request.getRequestDispatcher(destination).forward(request, response);
			return null;
		} catch (BaseException e) {
			log.error( e.getMessage(), e);
		} 
		return new ModelAndView("redirect:" + destination);
	}
	
	
	//모바일여부 
	public ModelAndView changeMobile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//언어변경 했을 때 언어변경 후 다시 해당 페이지로 이동 - gjrjf
		String referer = request.getHeader("referer");
		log.info("User Lang Change. Request Header referer : " + referer);
		String destination = "";
		
		if(referer == null){
			destination = getDestination(request);
		}else{
			if(referer.length() == 0) destination = getDestination(request);
			else destination = referer;
		}
		
		String isMobile = null;
		// 지정되지 않았으면 토글
		if( EnviewSSOManager.isMobile(request)) {
			isMobile = "f";
		} else {
			isMobile = "t";
		}
		
		String mobileCookieName = Enview.getConfiguration().getString(LoginConstants.ENVIEW_COOKIE_ISMOBILE_NAME);
		HttpUtil.addCookie(response, mobileCookieName, isMobile, "/", HttpUtil.getCookieDomain(request), -1, request.isSecure(), true);
		
		return new ModelAndView("redirect:" + destination);
	}
	
	
	public ModelAndView myPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map userInfoMap =EnviewSSOManager.getUserInfoMap(request); 
		if(  userInfoMap == null ) {
    		log.debug("*** You have to login !!!");
    		throw new BaseException("You have to login !!!");
        } else {
    		String myPageNameKey =  Enview.getConfiguration().getString("mypage.pageName.key", "userId");
    		String mypageName =   (String)userInfoMap.get( myPageNameKey);
        	
        	String userId = EnviewSSOManager.getUserId(request);
        	String domainPath = Enview.getUserDomain().getPagePath();
//        	return new ModelAndView("redirect:/portal" + domainPath + "/user/" + userId + ".page?isMyPage=true" );
        	return new ModelAndView("redirect:/portal/user/" + mypageName + ".page?isMyPage=true" );
		}
	}

	public ModelAndView changeGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle( langKnd );
		
		String destination = request.getParameter(LoginConstants.DESTINATION);
		String groupId = request.getParameter("groupId");
	    HttpSession session = request.getSession();
	
	    UserInfo currentUserInfo = EnviewSSOManager.getUserInfo( request);
	    String oldGroupId = currentUserInfo.getGroupId();
	    String userId =  currentUserInfo.getUserId();
	    
	    // 사용자 ID가 지정되지 않았거나 현재 로그인 사용자와 같으면 
	    if( groupId == null || groupId.length()==0 || oldGroupId==null || oldGroupId.length()==0) {
	    	if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page", "/login.do");
	        }
	    	return new ModelAndView( "redirect:" + destination );
	    }
	    
	    // TODO : 새 그룹아이디가 현재 사용자의 그룹리스트에 있는지 확인 
	    
	    
	    // 사용자 ID가 지정되지 않았거나 현재 로그인 사용자와 같으면 그룹 CHANGE 완료 
	    if( groupId.equals( oldGroupId)) {
	    	if (destination == null) {
	    		/*
				String defaultPage = currentUserInfo.getString("default_page", "");
				if( defaultPage != null ) {
					if( defaultPage.indexOf(".page") > -1 ) {
						destination = defaultPage;
						//destination = "/portal" + defaultPage;
					}
					else {
						destination = "/";
					}
				}
				else {
					destination = "";
				}
				*/
	    		destination = getDestination(request);
	        }
	    	return new ModelAndView( "redirect:" + destination );
	    }
	    
	    
	    log.debug("*** groupId=" + groupId);

	    try {
			session.invalidate();
		} catch( IllegalStateException e) {
		}
	    
	    session = request.getSession(true);
	    
	    try {
			UserInfo userInfo = siteUserManager.getUserInfo(userId, groupId, langKnd);
//			UserInfo userInfo = this.userInfomationHandler.createUserInfomation(request, response, userId, groupId);

			String dbLangKnd = userInfo.getLocale();
			if( dbLangKnd == null ) {
				userInfo.setLocale(langKnd);
			}
			
			userInfo.setString("remote_address", InetAddress.getByName(request.getRemoteAddr()).getHostAddress());
			userInfo.setString("user-agent", HttpUtil.getUserAgent(request));
			
			request.getSession().removeAttribute(LoginConstants.ERRORCODE);
			session.setAttribute(LoginConstants.SSO_LOGIN_ID, userId);
	
	    	enviewSessionManager.setUserData(request, userInfo);
	
	    	if (destination == null) {
	    		destination = getDestination(request);
	        }
	    	
			removeSessionIDCookie(request, response);
	        
	        // TODO : SSO변경이 필요하지여부 체크 
	        boolean ssoChangeRequired= true;
	        
	        if( ssoChangeRequired && ssoLoginHandler!=null) {
	        	ssoLoginHandler.processChangeUser(request, response, userInfo);
	        	if( ssoLoginHandler.isRedirectRequired()) {
	        		return null;
	        	}
	        }
	        
		}
		catch(UserException se) 
		{
			log.error( se);
			
			
	        String msgKey = se.getMessageKey();
	        if( msgKey != null ) {
	        	String errorMessage = enviewMessages.getString( msgKey );
		        request.setAttribute("errorMessage", errorMessage);
		        log.debug("*** errorMessage=" + errorMessage);
	        }
	        
	        if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page", "/user/login.face");
	        }
	        
	        return new ModelAndView( "redirect:" + destination );
	    }
		catch(BaseException e)
		{
			log.error( e.getMessage(), e);
			throw e;
		}
		
	    int pos = destination.indexOf( request.getContextPath() );
	    //if( "/".equals(request.getContextPath()) == false && pos > -1 ) {
	    if( !"/".equals(request.getContextPath()) && pos > -1 ) {
	    	destination = destination.substring(request.getContextPath().length());
	    	log.debug("*** destination=" + destination);
	    }
	    
		return new ModelAndView("redirect:" + destination);
	}

	public ModelAndView changeUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle( langKnd );
		
		String destination = request.getParameter(LoginConstants.DESTINATION);
		String userId = request.getParameter("userId");
	    HttpSession session = request.getSession();
	    UserInfo currentUserInfo = EnviewSSOManager.getUserInfo( request);
	    
	    
	    
	    String oldUserId = (String)session.getAttribute( LoginConstants.SSO_LOGIN_ID);
	    
	    
	    // 사용자 ID가 지정되지 않았거나 현재 로그인 사용자와 같으면 
	    if(userId == null || userId.length()==0 || oldUserId==null || oldUserId.length()==0) {
	    	if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page", "/login.do");
	        }
	    	return new ModelAndView( "redirect:" + destination );
	    }
	    
	    // 사용자 ID가 지정되지 않았거나 현재 로그인 사용자와 같으면 
	    if( userId.equals( currentUserInfo.getUserId())) {
	    	if (destination == null) {
				String defaultPage = currentUserInfo.getString("default_page", "");
				if( defaultPage != null ) {
					if( defaultPage.indexOf(".page") > -1 ) {
						destination = defaultPage;
						//destination = "/portal" + defaultPage;
					}
					else {
						destination = "/";
					}
				}
				else {
					destination = "";
				}
	        }
	    	return new ModelAndView( "redirect:" + destination );
	    }
	    
	    // TODO : 사용자 변경이 가능한지 여부 체크. 즉 원래 사용자가 admin이거나 두 사용자의 주민번호가 같은 경우
//	    boolean changeUserPermitted = "admin".equals(oldUserId);
	    boolean changeUserPermitted = currentUserInfo.getHasAdminRole();
	    // 사용자 ID가 지정되지 않았거나 현재 로그인 사용자와 같으면 
	    if( !changeUserPermitted ) {
	    	if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page", "/login.do");
	        }
	    	return new ModelAndView( "redirect:" + destination );
	    }
	    
	
	    
	    log.debug("*** userId=" + userId);
		try {
			session.invalidate();
		} catch( IllegalStateException e) {
		}
	    
	    session = request.getSession(true);
	    
	    try {
			UserInfo userInfo = siteUserManager.getUserInfo(userId, null, langKnd);
			String dbLangKnd = userInfo.getLocale();
			if( dbLangKnd == null ) {
				userInfo.setLocale(langKnd);
			}
			
			userInfo.setString("remote_address", InetAddress.getByName(request.getRemoteAddr()).getHostAddress());
			userInfo.setString("user-agent", HttpUtil.getUserAgent(request));
			
			request.getSession().removeAttribute(LoginConstants.ERRORCODE);
			session.setAttribute(LoginConstants.SSO_LOGIN_ID, userId);
	
	    	enviewSessionManager.setUserData(request, userInfo);
	
	    	if (destination == null) {
				String defaultPage = userInfo.getString("default_page", "");
				if( defaultPage != null ) {
					if( defaultPage.indexOf(".page") > -1 ) {
						destination = defaultPage;
						//destination = "/portal" + defaultPage;
					}
					else {
						destination = defaultPage;
					}
				}
				else {
					destination = "";
				}
	        }
	    	
			removeSessionIDCookie(request, response);
			
	        if( ssoLoginHandler!=null) {
	        	ssoLoginHandler.processChangeUser(request, response, userInfo);
	        	if( ssoLoginHandler.isRedirectRequired()) {
	        		return null;
	        	}
	        }
	        
		}
		catch(UserException se) 
		{
			log.error( se);
			
			
	        String msgKey = se.getMessageKey();
	        if( msgKey != null ) {
	        	String errorMessage = enviewMessages.getString( msgKey );
		        request.setAttribute("errorMessage", errorMessage);
		        log.debug("*** errorMessage=" + errorMessage);
	        }
	        
	        if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page", "/user/login.face");
	        }
	        
	        return new ModelAndView( "redirect:" + destination );
	    }
		catch(BaseException e)
		{
			log.error( e.getMessage(), e);
			throw e;
		}
		
	    int pos = destination.indexOf( request.getContextPath() );
	    //if( "/".equals(request.getContextPath()) == false && pos > -1 ) {
	    if( !"/".equals(request.getContextPath()) && pos > -1 ) {
	    	destination = destination.substring(request.getContextPath().length());
	    	log.debug("*** destination=" + destination);
	    }
	    
		return new ModelAndView("redirect:" + destination);
	}

	@Override
	public boolean processLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return siteUserManager.processLogin(request, response);
	}
	
    public void authenticate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String langKnd = EnviewSSOManager.getLangKnd(request);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle( langKnd);

		String status = "Success";
		String reason = "";
		
    	String userId = request.getParameter("userId");
    	String password = request.getParameter("password");
    	Map userInfoMap = null;
    	try {
        	siteUserManager.authenticate(userId, password);
        	userInfoMap = siteUserManager.getUserInfo(userId, null, langKnd).getUserInfoMap();
        	
		} catch( BaseException be){
			status = "Fail";
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				reason = enviewMessages.getString(msgKey);
			} else {
				reason = be.getMessage();
			}
		}

    	StringBuilder sb = new StringBuilder();
    	if( "Success".equals(status)) {
    		StringBuilder groups = new StringBuilder();
    		List groupList = (List)userInfoMap.get("groups");
    		for (int i = 0; i < groupList.size(); i++) {
    			if( groups.length() > 0) {
    				groups.append(",");
    			}
    			groups.append( groupList.get(i));
			}
    		sb.append( "{")
    		.append("Status : ").append( JSONObject.quote(status))
    		.append( ",").append( "Reason : ").append( JSONObject.quote(reason))
    		.append( ",").append( "UserInfo : ")
    			.append("{")
    			.append("userId : ").append( JSONObject.quote((String)userInfoMap.get("userId")))
				.append( ",").append("nmKor : ").append( JSONObject.quote((String)userInfoMap.get("userNameKo")))
				.append( ",").append("orgName : ").append( JSONObject.quote((String)userInfoMap.get("orgNameKo")))
				.append( ",").append("emailAddr : ").append( JSONObject.quote((String)userInfoMap.get("emailAddr")))
				.append( ",").append("mobileTel : ").append( JSONObject.quote((String)userInfoMap.get("mobileTel")))
				.append( ",").append("groups : ").append( JSONObject.quote( groups.toString()))
				.append( "}")
    		.append( "}");
    		
    	} else {
    		sb.append( "{")
    		.append("Status : ").append( JSONObject.quote(status))
    		.append( ",").append( "Reason : ").append( JSONObject.quote(reason))
    		.append( "}");
    	}
//    		response.setContentType("text/json;charset=UTF-8");
    		response.setContentType("text/plain;charset=UTF-8");
    		response.getWriter().print(sb.toString());
    		response.getWriter().flush();
    }
    
        
	public ModelAndView loginInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("userInfo", EnviewSSOManager.getUserInfoMap(request));
		request.setAttribute("langKnd", EnviewSSOManager.getLangKnd(request));
		return new ModelAndView("/user/loginInfo");
	}
	
	public ModelAndView securityInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String blockAbroad = request.getParameter("blockAbroad");
		if( blockAbroad!=null) {
			siteUserManager.updateBlockAbroad(EnviewSSOManager.getUserId(request), blockAbroad);
		}
		
		request.setAttribute("clientIp", HttpUtil.getClientIp(request));
		request.setAttribute("userInfo", EnviewSSOManager.getUserInfoMap(request));
		request.setAttribute("securityInfo", siteUserManager.findUser( EnviewSSOManager.getUserId(request)));
		request.setAttribute("langKnd", EnviewSSOManager.getLangKnd(request));
		return new ModelAndView("/user/securityInfo");
	}
	
    
    public void updateBlockAbroadForAjax(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String langKnd = EnviewSSOManager.getLangKnd(request);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle( langKnd);

		String status = "Success";
		String reason = "";
		
    	String userId = EnviewSSOManager.getUserId(request);
    	String blockAbroad = request.getParameter("blockAbroad");
    	UserVO userVO = null;
    	try {
        	siteUserManager.updateBlockAbroad(userId, blockAbroad);
        	userVO = siteUserManager.findUser(userId);
		} catch( BaseException be){
			status = "Fail";
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				reason = enviewMessages.getString(msgKey);
			} else {
				reason = be.getMessage();
			}
		}

    	StringBuilder sb = new StringBuilder();
    		sb.append( "{")
    		.append("Status : ").append( JSONObject.quote("Success"))
			.append( ",").append("blockAbroad : ").append( JSONObject.quote( userVO.getPrincipalInfo01()))
    		.append( "}");
    		response.setContentType("text/plain;charset=UTF-8");
    		response.getWriter().print(sb.toString());
    		response.getWriter().flush();
    }
    
	public ModelAndView captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
        	// http://simplecaptcha.sourceforge.net/custom_images.html 참조
            // 200 * 60 에해당하는 이미지 사이즈를 지정하고, 자동가입방지 문자 길이를 설정한다.
            Captcha captcha = new Captcha.Builder(250, 60)
                                    .addText()
                                    .addBackground()
                                    .addNoise()
                                    .addBorder()
                                    .gimp()
                                    .build();
 
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            // 캐쉬를 지우기 위해 헤더값을 설정
            response.setDateHeader("Expires", 0);
            // 리턴값을 image형태로 설정
            response.setContentType("image/jpeg");
            // Image를 write 한다
            CaptchaServletUtil.writeImage(response, captcha.getImage());
            // 세션에 자동가입방지 문자를 저장한다.
            request.getSession().setAttribute("captchaAnswer", captcha.getAnswer());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
	}
		
}



