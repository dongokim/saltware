package com.saltware.enface.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enview.Enview;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.pipeline.PipelineException;
import com.saltware.enview.security.SSOHandler;
import com.saltware.enview.security.UserInfo;

/**
 * Enview SSO Handler <br>
 * 사용자정보가 없고 SSO정보만 있는 경우 사용자정보를 복구한다.
 * 
 * @author smna
 * 
 */
public class EnviewSSOHandler implements SSOHandler {
	private static Log log = LogFactory.getLog(EnviewSSOHandler.class);

	private UserInfomationHandler userInfomationHandler;

	/**
	 * 생성자
	 */
	public EnviewSSOHandler() {
		this.userInfomationHandler = (UserInfomationHandler) Enview.getComponentManager().getComponent("com.saltware.enface.security.UserInfomationHandler");
	}

	/**
	 * 파이프라인에서 사용하기전에 초기화를 한다. 아무일도 하지 않는다.
	 */
	public void initialize() throws PipelineException {
		//
	}

	/**
	 * SSO 핸들러를 수행한다.<br> 
	 * 세션에 SSO정보가 있고 사용자 정보가 없으면 사용자정보핸들러를 사용하여 DB에서 사용자정보를 읽어 세션에 저장한다.   
	 */
	public void invoke(HttpServletRequest request, HttpServletResponse response) throws PipelineException {
		// log.info("[ EnviewSSOHandler ] invoke() start");
		String encoding = request.getCharacterEncoding();

		try {
			HttpSession session = request.getSession();
			String userId = (String) session.getAttribute(LoginConstants.SSO_LOGIN_ID);
			if (userId != null) {
				UserInfo userInfo = (UserInfo) session.getAttribute(LoginConstants.USER_INFO);
				if (userInfo == null) {
					userInfo = this.userInfomationHandler.createUserInfomation(request, response, userId, null);
					log.info("[ EnviewSSOHandler ] Userinfo is acquired again from DB.");
				}
			}
		} catch (BaseException e) {
			log.error( e.getMessage(), e); 
		}

		// log.info("[ EnviewSSOHandler ] invoke() end");
	}

	/**
	 * 후속작업처리. 아무일도 하지 않는다.
	 */
	public boolean postHandle(HttpServletRequest request, HttpServletResponse response) {
		return true;
	}
}
