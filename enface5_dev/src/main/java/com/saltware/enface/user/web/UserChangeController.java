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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.saltware.enface.sso.SSOLoginHandler;
import com.saltware.enface.user.service.SiteUserManager;
import com.saltware.enface.user.service.impl.UserException;
import com.saltware.enface.util.HttpUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.multiresource.EnviewMultiResourceManager;
import com.saltware.enview.multiresource.MultiResourceBundle;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.session.SessionManager;
import com.saltware.enview.sso.EnviewSSOManager;
import com.saltware.enview.util.EnviewLocale;

/**
 * 사용자전환
 * 
 * @author kevin
 */

public class UserChangeController extends MultiActionController {

	private final Log log = LogFactory.getLog(getClass());

	private SiteUserManager siteUserManager;
	private SessionManager enviewSessionManager;
	private SSOLoginHandler ssoLoginHandler;

	public UserChangeController() {
		this.enviewSessionManager = (SessionManager) Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
	}

	public SiteUserManager getUserManager() {
		return siteUserManager;
	}

	public void setUserManager(SiteUserManager siteUserManager) {
		this.siteUserManager = siteUserManager;
	}

	public ModelAndView changeUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);

		String destination = request.getParameter(LoginConstants.DESTINATION);
		String userId = request.getParameter("userId");
		HttpSession session = request.getSession();
		UserInfo currentUserInfo = EnviewSSOManager.getUserInfo(request);

		String oldUserId = (String) session.getAttribute(LoginConstants.SSO_LOGIN_ID);

		// 사용자 ID가 지정되지 않았거나 현재 로그인 사용자와 같으면
		if (userId == null || userId.length() == 0 || oldUserId == null || oldUserId.length() == 0) {
			if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page", "/login.do");
			}
			return new ModelAndView("redirect:" + destination);
		}

		// 사용자 ID가 지정되지 않았거나 현재 로그인 사용자와 같으면
		if (userId.equals(currentUserInfo.getUserId())) {
			if (destination == null) {
				String defaultPage = currentUserInfo.getString("default_page", "");
				if (defaultPage != null) {
					if (defaultPage.indexOf(".page") > -1) {
						destination = defaultPage;
						// destination = "/portal" + defaultPage;
					} else {
						destination = "/";
					}
				} else {
					destination = "";
				}
			}
			return new ModelAndView("redirect:" + destination);
		}

		// TODO : 사용자 변경이 가능한지 여부 체크. 즉 원래 사용자가 admin이거나 두 사용자의 주민번호가 같은 경우
//		boolean changeUserPermitted = "admin".equals(oldUserId);
	    boolean changeUserPermitted = currentUserInfo.getHasAdminRole();
		
		// 사용자 ID가 지정되지 않았거나 현재 로그인 사용자와 같으면
		if (!changeUserPermitted) {
			if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page", "/login.do");
			}
			return new ModelAndView("redirect:" + destination);
		}

		log.debug("*** userId=" + userId);
		try {
			session.invalidate();
		} catch (IllegalStateException e) {
		}

		session = request.getSession(true);

		try {
			UserInfo userInfo = siteUserManager.getUserInfo(userId, null, langKnd);
			String dbLangKnd = userInfo.getLocale();
			if (dbLangKnd == null) {
				userInfo.setLocale(langKnd);
			}

			userInfo.setString("remote_address", InetAddress.getByName(request.getRemoteAddr()).getHostAddress());
			userInfo.setString("user-agent", HttpUtil.getUserAgent(request));

			request.getSession().removeAttribute(LoginConstants.ERRORCODE);
			session.setAttribute(LoginConstants.SSO_LOGIN_ID, userId);

			enviewSessionManager.setUserData(request, userInfo);

			if (destination == null) {
				String defaultPage = userInfo.getString("default_page", "");
				if (defaultPage != null) {
					if (defaultPage.indexOf(".page") > -1) {
						destination = defaultPage;
						// destination = "/portal" + defaultPage;
					} else {
						destination = defaultPage;
					}
				} else {
					destination = "";
				}
			}

			HttpUtil.addCookie(response, EnviewSSOManager.getEnviewSessionIDName(request), session.getId(), "/", HttpUtil.getCookieDomain(request), -1, request.isSecure(), true);

			if (ssoLoginHandler != null) {
				ssoLoginHandler.processChangeUser(request, response, userInfo);
				if (ssoLoginHandler.isRedirectRequired()) {
					return null;
				}
			}

		} catch (UserException se) {
			log.error(se);

			String msgKey = se.getMessageKey();
			if (msgKey != null) {
				String errorMessage = enviewMessages.getString(msgKey);
				request.setAttribute("errorMessage", errorMessage);
				log.debug("*** errorMessage=" + errorMessage);
			}

			if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page", "/user/login.face");
			}

			return new ModelAndView("redirect:" + destination);
		} catch (BaseException e) {
			log.error(e.getMessage(), e);
			throw e;
		}

		int pos = destination.indexOf(request.getContextPath());
		// if( "/".equals(request.getContextPath()) == false && pos > -1 ) {
		if (!"/".equals(request.getContextPath()) && pos > -1) {
			destination = destination.substring(request.getContextPath().length());
			log.debug("*** destination=" + destination);
		}

		return new ModelAndView("redirect:" + destination);
	}

	public ModelAndView changeGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);

		String destination = request.getParameter(LoginConstants.DESTINATION);
		String groupId = request.getParameter("groupId");
		HttpSession session = request.getSession();

		UserInfo currentUserInfo = EnviewSSOManager.getUserInfo(request);
		String oldGroupId = currentUserInfo.getGroupId();
		String userId = currentUserInfo.getUserId();

		// 사용자 ID가 지정되지 않았거나 현재 로그인 사용자와 같으면
		if (groupId == null || groupId.length() == 0 || oldGroupId == null || oldGroupId.length() == 0) {
			if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page", "/login.do");
			}
			return new ModelAndView("redirect:" + destination);
		}

		// TODO : 새 그룹아이디가 현재 사용자의 그룹리스트에 있는지 확인

		// 사용자 ID가 지정되지 않았거나 현재 로그인 사용자와 같으면 그룹 CHANGE 완료
		if (groupId.equals(oldGroupId)) {
			if (destination == null) {
				String defaultPage = currentUserInfo.getString("default_page", "");
				if (defaultPage != null) {
					if (defaultPage.indexOf(".page") > -1) {
						destination = defaultPage;
						// destination = "/portal" + defaultPage;
					} else {
						destination = "/";
					}
				} else {
					destination = "";
				}
			}
			return new ModelAndView("redirect:" + destination);
		}

		log.debug("*** groupId=" + groupId);
		try {
			session.invalidate();
		} catch( IllegalStateException e) {
		}

		session = request.getSession(true);

		try {
			UserInfo userInfo = siteUserManager.getUserInfo(userId, groupId, langKnd);
			String dbLangKnd = userInfo.getLocale();
			if (dbLangKnd == null) {
				userInfo.setLocale(langKnd);
			}

			userInfo.setString("remote_address", InetAddress.getByName(request.getRemoteAddr()).getHostAddress());
			userInfo.setString("user-agent", HttpUtil.getUserAgent(request));

			request.getSession().removeAttribute(LoginConstants.ERRORCODE);
			session.setAttribute(LoginConstants.SSO_LOGIN_ID, userId);

			enviewSessionManager.setUserData(request, userInfo);

			if (destination == null) {
				String defaultPage = userInfo.getString("default_page", "");
				if (defaultPage != null) {
					if (defaultPage.indexOf(".page") > -1) {
						destination = defaultPage;
						// destination = "/portal" + defaultPage;
					} else {
						destination = defaultPage;
					}
				} else {
					destination = "";
				}
			}

			HttpUtil.addCookie(response, EnviewSSOManager.getEnviewSessionIDName(request), session.getId(), "/", HttpUtil.getCookieDomain(request), -1, request.isSecure(), true);

			boolean ssoChangeRequired = true;

			if (ssoChangeRequired && ssoLoginHandler != null) {
				ssoLoginHandler.processChangeUser(request, response, userInfo);
				if (ssoLoginHandler.isRedirectRequired()) {
					return null;
				}
			}

		} catch (UserException se) {
			log.error(se);

			String msgKey = se.getMessageKey();
			if (msgKey != null) {
				String errorMessage = enviewMessages.getString(msgKey);
				request.setAttribute("errorMessage", errorMessage);
				log.debug("*** errorMessage=" + errorMessage);
			}

			if (destination == null) {
				destination = Enview.getConfiguration().getString("sso.login.page", "/user/login.face");
			}

			return new ModelAndView("redirect:" + destination);
		} catch (BaseException e) {
			log.error(e.getMessage(), e);
			throw e;
		}

		int pos = destination.indexOf(request.getContextPath());
		// if( "/".equals(request.getContextPath()) == false && pos > -1 ) {
		if (!"/".equals(request.getContextPath()) && pos > -1) {
			destination = destination.substring(request.getContextPath().length());
			log.debug("*** destination=" + destination);
		}

		return new ModelAndView("redirect:" + destination);
	}

	public SSOLoginHandler getSsoLoginHandler() {
		return ssoLoginHandler;
	}

	public void setSsoLoginHandler(SSOLoginHandler ssoLoginHandler) {
		this.ssoLoginHandler = ssoLoginHandler;
	}
}
