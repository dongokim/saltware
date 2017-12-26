package com.saltware.enface.sns.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.saltware.enface.user.web.UserForm;
import com.saltware.enface.util.HttpUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.administration.PortalConfiguration;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.multiresource.EnviewMultiResourceManager;
import com.saltware.enview.multiresource.MultiResourceBundle;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.sso.EnviewSSOManager;
import com.saltware.enview.statistics.PortalStatistics;
import com.saltware.enview.util.EnviewLocale;

public class SnsLoginController extends com.saltware.enface.user.web.LoginController {

	public SnsLoginController() {
		super();
	}

	
	private boolean isLocalId( String userId) {
		if( userId.toLowerCase().endsWith("admin")) {
			return true;
		}
		
		PortalConfiguration conf = Enview.getConfiguration();
		List localIds =  conf.getList("sns.portal.localIds");
		if( localIds.contains(userId)) {
			return true;
		}
		return false;
	}
		
	
	/**
	 * SNS 사용자를 인증하고 로그인 프로세스를 진행한다.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData UserForm
	 * @throws Exception
	 */
	public void snsLoginProcessForAjax(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String langKnd =  request.getParameter("langKnd");
		
		
		boolean authOk = false;
		PortalConfiguration conf = Enview.getConfiguration();
		String userSuffix = conf.getString("sns.portal.userId.suffix", "");

		// 접미사가 있고 아이디에 접미사가 안붙어 있으면서 관리자 계정이 아니면 접미사를 붙인다. 
		if( !userSuffix.equals("") && !userId.endsWith(userSuffix) && ! isLocalId(userId)) {
			userId += userSuffix;
		}
		
		try {
			siteUserManager.authenticate(userId, password);
			authOk = true;
		} catch (BaseException se) {
			String msgKey = se.getMessageKey();
			if (msgKey != null) {
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				String errorMessage = enviewMessages.getString(msgKey);
				request.setAttribute("errorMessage", errorMessage);
			} else {
				request.setAttribute("errorMessage", se.getMessage());
			}
		}
		
		request.getSession().setAttribute( LoginConstants.AUTO_LOGIN_ID, userId);
		
		StringBuffer buffer = new StringBuffer();
		if( authOk) {
			try {
				authOk = processLogin(request, response);
			} catch (Throwable e) {
				logger.error( e, e);
			}
			
		}
		if ( authOk && processLogin(request, response)) {
			UserInfo userInfo = EnviewSSOManager.getUserInfo(request);
			this.rememberMeServices.loginSuccess(request, response, userInfo.getUserId());

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");
		} else {
			HttpSession session = request.getSession(false);
			if (session != null)
				session.invalidate();

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(request.getAttribute("errorMessage")).append("\"");
			buffer.append("}");
		}

		response.setContentType("text/json;charset=UTF-8");
//		response.setContentType("text/plain;charset=UTF-8");
		response.getWriter().print(buffer.toString());
		response.getWriter().flush();
	}

	/**
	 * SNS 사용자를 로그아웃한다.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws Exception
	 */
	public void snsLogoutProcessForAjax(HttpServletRequest request, HttpServletResponse response) throws Exception {

		StringBuffer buffer = new StringBuffer();

		response.addCookie(new Cookie("evSSOCookie", "loggedout"));
		String destination = request.getParameter(LoginConstants.DESTINATION);
		HttpSession session = request.getSession();
		String ssoLoginId = (String) session.getAttribute(LoginConstants.SSO_LOGIN_ID);
		if (ssoLoginId != null) {

			try {
				this.rememberMeServices.logout(request, response, ssoLoginId);

				session.invalidate();

				removeSessionIDCookie(request, response);

				if (ssoLoginHandler != null) {
					ssoLoginHandler.processLogout(request, response);
				}

				buffer.append("{");
				buffer.append("\"Status\": \"success\"");
				buffer.append("}");
			} catch (BaseException e) {
				log.info("*** " + e.getMessage());
				HttpUtil.sendRedirect( response, request.getContextPath() + destination);

				buffer.append("{");
				buffer.append("\"Status\": \"fail\",");
				buffer.append("\"Reason\": \"").append(e.getMessage()).append("\"");
				buffer.append("}");
			}
		}

		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().print(buffer.toString());
		response.getWriter().flush();
	}
	
	public void snsSyncUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {

		StringBuffer buffer = new StringBuffer();
		String batchDay = request.getParameter("batchDay");
		String batchSize = request.getParameter("batchSize");
		String batchPage = request.getParameter("batchPage");
		
		
		try {
			UserInfo userInfo = EnviewSSOManager.getUserInfo(request);
			if( userInfo==null) {
				throw new Exception("Not logged in!");
			}
			
			if( ! userInfo.getHasAdminRole()) {
				throw new Exception("Admin only!");
			}
			
			Map paramMap= new HashMap();
			paramMap.put("batchDay", batchDay);
			paramMap.put("batchSize", batchSize);
			paramMap.put("batchPage", batchPage);
			siteUserManager.syncUsers( paramMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");
		} catch (BaseException e) {
			log.info("*** " + e.getMessage());
			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(e.getMessage()).append("\"");
			buffer.append("}");
		}

		response.setContentType("text/plain;charset=UTF-8");
		response.getWriter().print(buffer.toString());
		response.getWriter().flush();
		
		
	}
	
	/**
	 * 비밀번호변경을 처리한다.  
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 폼정보
	 * @throws Exception
	 */
	public void  snsChangePasswordProcessForAjax(HttpServletRequest request, HttpServletResponse response, UserForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle( langKnd );

		StringBuffer buffer = new StringBuffer();
		try {
			UserInfo userInfo = EnviewSSOManager.getUserInfo(request);
			if( userInfo==null) {
				throw new Exception("Not logged in!");
			}
			String userId = userInfo.getUserId();
        
        String password = formData.getPassword();
        String passwordNew = formData.getPasswordNew();
        String passwordConfirm = formData.getPasswordConfirm();
        
        //if( passwordNew.equals( passwordConfirm) == false ) {
        if( !passwordNew.equals( passwordConfirm) ) {
			throw new Exception(enviewMessages.getString("ev.error.user.ErrorCode.9"));
        }
        
		siteUserManager.changePassword(userId, password, passwordNew);
		
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		paramMap.put("status", "" + PortalStatistics.STATUS_CHANGE_PASSWORD);
		paramMap.put("remoteAddress", HttpUtil.getClientIp(request));
		paramMap.put("userAgent", HttpUtil.getUserAgent(request));
		
		siteUserManager.log(paramMap);

    	buffer.append("{");
		buffer.append("\"Status\": \"success\"");
		buffer.append("}");
	} catch (BaseException e) {
		String msg = enviewMessages.getString( e.getMessage());
		if( msg==null) {
			msg = e.getMessage();
		}
		buffer.append("{");
		buffer.append("\"Status\": \"fail\",");
		buffer.append("\"Reason\": \"").append( msg).append("\"");
		buffer.append("}");
	}

	response.setContentType("text/json;charset=UTF-8");
	response.getWriter().print(buffer.toString());
	response.getWriter().flush();
	
	}
	
}
