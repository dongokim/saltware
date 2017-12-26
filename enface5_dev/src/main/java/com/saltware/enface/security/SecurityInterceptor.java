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
package com.saltware.enface.security;

import java.security.AccessControlException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.saltware.enview.Enview;
import com.saltware.enview.EnviewActions;
import com.saltware.enview.administration.PortalConfiguration;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.security.SecurityPermissionManager;
import com.saltware.enview.session.SessionManager;
import com.saltware.enview.sso.EnviewSSOManager;

/**
 * Spring 페이지 호출전에 사용자 체크. (Optional) 통상 enview 세션을 체크하여 존재하지 않는경우, SSO쪽에 사용자정보를 요청하고, 존재하지 않을경우 로그인 하지 않은 사용자로 간주한다.
 * 
 * @version 3.2.2
 * @since 1.0-SNAPSHOT
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
	private final Log log = LogFactory.getLog(getClass());

	private SessionManager sessionManager;
	protected PortalConfiguration configuration;
	private SecurityPermissionManager securityPermissionManager;

	/**
	 * 생성자
	 */
	public SecurityInterceptor() {
		this.configuration = Enview.getConfiguration();
		this.sessionManager = (SessionManager) Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
		this.securityPermissionManager = (SecurityPermissionManager) Enview.getComponentManager().getComponent("com.saltware.enview.security.SecurityPermissionManager");
	}

	/**
	 * 실행전 보안 점검을 한다.<br>
	 * 1. 세션이 없거나, 세션에 sessionUserId 가 없거나, 사용자맵이 없으면 로그인 페이지로 보낸다.<br>
	 * 2. 포틀릿 보안체크가 true이고 페이지안에서 호출하지 않은 경우 URL체크를 한다. <br>
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Map userInfoMap = null;
		String sessionUserId = null;
		HttpSession session = request.getSession();
		boolean isAjaxCall = (request.getParameter("__ajax_call__") != null) ? true : false;
		if (session == null || (sessionUserId = (String) session.getAttribute(configuration.getString("sso.login.id.key"))) == null) {

			try {
				sessionUserId = EnviewSSOManager.getUserId(request, null);
				// log.debug("*** session userId=" + sessionUserId +
				// " from SessionManager");
				if (sessionUserId == null) {
					if (isAjaxCall) {
						response.setHeader("enview.ajax.control", request.getContextPath() + configuration.getString("sso.login.page"));
					} else {
						response.sendRedirect(request.getContextPath() + configuration.getString("sso.login.page"));
					}
				}
			} catch (BaseException e) {
				log.error(e.getMessage(), e);
				if (isAjaxCall) {
					response.setHeader("enview.ajax.control", request.getContextPath() + configuration.getString("sso.login.page"));
				} else {
					response.sendRedirect(request.getContextPath() + configuration.getString("sso.login.page"));
				}
				return false;
			}
		} else {
			userInfoMap = (Map) EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				try {
					if (isAjaxCall) {
						response.setHeader("enview.ajax.control", request.getContextPath() + configuration.getString("sso.login.page"));
					} else {
						response.sendRedirect(request.getContextPath() + configuration.getString("sso.login.page"));
					}
					return false;
//				} catch (BaseException e) {
//					log.error(e.getMessage(), e);
//					if (isAjaxCall) {
//						response.setHeader("enview.ajax.control", request.getContextPath() + configuration.getString("sso.login.page"));
//					} else {
//						response.sendRedirect(request.getContextPath() + configuration.getString("sso.login.page"));
//					}
//					return false; 
				} finally {
				}
				// } else {
				// log.info("*** session check userId=" +
				// (String)session.getAttribute(configuration.getString("sso.login.id.key")));
			}
		}

		// if (configuration.getBoolean("security.portlet.check") == true) {
		if (configuration.getBoolean("security.portlet.check")) {
			// windowId는 포틀릿 ID이므로 이 값이 있으면 페이지안에 포함된 형태이므로 권한체크를 안한다.
			String windowId = (String) request.getAttribute("windowId");
			if (windowId == null) {
				// Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
				if (userInfoMap != null) {
					String requestPath = request.getRequestURI();
					// String requestPath = request.getPathInfo();
					requestPath = requestPath.substring(request.getContextPath().length());
					// log.info("*** requestPath=" + requestPath);

					try {
						this.securityPermissionManager.checkExtraUrl(requestPath, EnviewActions.MASK_VIEW);

					} catch (AccessControlException ace) {
						log.error("*** Access denied [" + requestPath + "]");
						if (isAjaxCall) {
							response.setHeader("enview.ajax.control", request.getContextPath() + configuration.getString("permission.access.error.page"));
							// response.setHeader("enview.ajax.control",
							// request.getContextPath() + "/portal" +
							// configuration.getString("permission.access.error.page"));
							return false;
						} else {
							response.sendRedirect(request.getContextPath() + configuration.getString("permission.access.error.page"));
							// response.sendRedirect( request.getContextPath() +
							// "/portal" +
							// configuration.getString("permission.access.error.page")
							// );
							return false;
						}
					}
				}
			}
		}

		return true;
	}
}
