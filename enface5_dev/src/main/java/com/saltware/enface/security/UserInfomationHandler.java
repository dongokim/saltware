package com.saltware.enface.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enface.user.service.SiteUserManager;
import com.saltware.enface.user.service.impl.UserException;
import com.saltware.enface.util.HttpUtil;
import com.saltware.enface.util.IpUtil;
import com.saltware.enface.util.StringUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.domain.DomainInfo;
import com.saltware.enview.domain.EnviewDomainManager;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.session.SessionManager;
import com.saltware.enview.sso.EnviewSSOManager;

/**
 * 사용자정보핸들러<br>
 * 사용자ID로 사용자정보를 조회하여 세션에 넣는다.
 * @author smna
 *
 */
public class UserInfomationHandler {
	
	private static Log log = LogFactory.getLog(UserInfomationHandler.class);

	private SessionManager sm = null;
	private SiteUserManager siteUserManager = null;
    private EnviewDomainManager enviewDomainManager;

	/**
	 * 생성자
	 */
	public UserInfomationHandler() {
		this.sm = (SessionManager) Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
		this.siteUserManager = (SiteUserManager) Enview.getComponentManager().getComponent("com.saltware.enface.user.service.UserManager");
        this.enviewDomainManager = (EnviewDomainManager)Enview.getComponentManager().getComponent("com.saltware.enview.domain.EnviewDomainManager");
}
	
	public String getLangKnd( HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		UserInfo userInfo = null;
		String langKnd = request.getParameter("langKnd");
		if( StringUtil.isEmpty( langKnd)) {
			langKnd = (String) session.getAttribute("langKnd");
		}
		if( StringUtil.isEmpty( langKnd)) {
			langKnd = request.getLocale().getLanguage();
		}
		return langKnd;
	}
	

	private void setCookieDomain( HttpServletRequest request, Cookie cookie) {
		String host = request.getServerName();
		int index;
		if( ( index = host.indexOf("."))!=-1) {
			cookie.setDomain(host.substring(index+1));
		}
	}
	
	/**
	 * 주어진 사용자ID로 사용자정보를 조회하고 세션매지저를 사용하여 세션에 저장한다.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse 
	 * @param userId 사용자ID
	 * @return 사용자정보
	 * @throws Exception
	 */
	public UserInfo createUserInfomation(HttpServletRequest request, HttpServletResponse response, String userId, String groupId) throws BaseException {
		UserInfo userInfo = null;
		
			HttpSession session = request.getSession();
			if (session != null) {
				session.removeAttribute(LoginConstants.ERRORCODE);
	
				String langKnd = getLangKnd(request);
				userInfo = siteUserManager.getUserInfo(userId, groupId, langKnd);
				
				//  보안관련 내용을 사용자정보에 보관 .
				String ipProtect = request.getParameter("ipProtect");
				if( ipProtect==null ) {
					ipProtect="0";
				}
				
				String clientIp = HttpUtil.getClientIp(request);
				String clientAgent = HttpUtil.getUserAgent(request);

				userInfo.getUserInfoMap().put("loginDt", (new Date()));
				userInfo.getUserInfoMap().put("loginIp", clientIp);
				userInfo.getUserInfoMap().put("loginAgent", clientAgent);

				userInfo.getUserInfoMap().put("ipProtect", ipProtect);
				//---------------------------
				
				session.setAttribute(LoginConstants.USERNAME, userInfo.getUserName());
				session.setAttribute("langKnd", langKnd);
	
				// 쓰레드 로컬에 로케일을 설정한다.
				Enview.setLocale( new Locale( (String)userInfo.getUserInfoMap().get("langKnd")));
				// 세션, 쓰레드 로컬에 도메인정보를 설정한다.
				log.debug("====================================================");
				String domainId = (String)userInfo.getUserInfoMap().get("domainId");
				log.debug( "domainId=" + domainId);
				DomainInfo domainInfo =enviewDomainManager.getDomain( domainId);
				log.debug( "domainInfo=" + domainInfo);
				
				session.setAttribute( LoginConstants.USER_DOMAIN, domainInfo);
				Enview.setUserDomain( domainInfo);
	
				// 세션ID 쿠키설정
				sm.setUserData(request, userInfo);
	        	HttpUtil.addCookie(response, EnviewSSOManager.getEnviewSessionIDName(request), session.getId(), "/", HttpUtil.getCookieDomain(request), -1, request.isSecure(), true);
				
	        	String cookieSessionId = request.getSession().getId();
								
				Map cookiSeMap = new HashMap();
				cookiSeMap.put("userId", userInfo.getUserId());
				cookiSeMap.put("cookieSessionId", cookieSessionId);
				cookiSeMap.put("doubleCheck", "D");
				
				siteUserManager.updateSessionId(cookiSeMap);
				
				// 언어 쿠키설정
	        	HttpUtil.addCookie(response, Enview.getConfiguration().getString( LoginConstants.ENVIEW_COOKIE_LANGKND_NAME), langKnd, "/", HttpUtil.getCookieDomain(request), -1, request.isSecure(), true);
			}
        	return userInfo;
	}

}
