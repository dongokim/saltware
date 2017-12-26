package com.saltware.enface.sso;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saltware.enboard.exception.BaseException;
import com.saltware.enface.util.HttpUtil;
import com.saltware.enpass.client.auxil.IdPRegister;
import com.saltware.enpass.client.util.PropMngr;
import com.saltware.enview.security.UserInfo;

/**
 * Enpass IdP 방식 로그인처리 
 * @author smna
 *
 */
public class EnpassSSOLoginHandler implements SSOLoginHandler {
	boolean useDomainCookie = false;

	/**
	 * 로그인을 처리한다.
	 */
	public void processLogin(HttpServletRequest request, HttpServletResponse response, UserInfo userInfo) throws Exception{
			
		String metaDataNameList = null;
		String[] metaDataNameArray = null;
		
		// get mneta data name list from enpass cleint properties
		metaDataNameList =  PropMngr.getInst().getProp("enpass.client.metaDataNameList");
		if( metaDataNameList!=null) {
			metaDataNameArray = metaDataNameList.split(",");
		}

		Map userInfoMap = userInfo.getUserInfoMap();
		Map ssoUserInfoMap = new HashMap();
		
		// put basic info
		ssoUserInfoMap.put("authTime", new Timestamp(System.currentTimeMillis()));
		ssoUserInfoMap.put("_enpass_id_", request.getParameter("userId"));
		ssoUserInfoMap.put("nmKor", userInfoMap.get("nm_kor"));
		ssoUserInfoMap.put("groupId", userInfoMap.get("groupId"));
		
		if(useDomainCookie) {
			ssoUserInfoMap.put("useDomainCookie", userInfoMap.get("useDomainCookie"));
		}
		
		// put metadata
		if( metaDataNameArray!= null) {
			for (int i = 0; i < metaDataNameArray.length; i++) {
				ssoUserInfoMap.put( metaDataNameArray[i], userInfoMap.get( metaDataNameArray[i]));
			}
		}
		
		try {
			// IdPRegister는 IdP를 등록하고,
			// 등록이 성공하면 TGC를 구우러 enPass-Server로 redirect하고,
			// 서버는 다시 Client로 redirect해준다.2010.10.21.KWShin.
			IdPRegister.getInst().registInfo(request, response, ssoUserInfoMap);
		} catch (BaseException e) {
			// Idp is not allowed to this server
			if( e.getMessage().length() == 3) {
				throw new BaseException( "error.enpass." + e.getMessage());
			}
			throw e;
		}
	}

	/**
	 * 로그아웃을 처리한다.
	 */
	public void processLogout(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String requestUrl = request.getRequestURL().toString();
		HttpUtil.sendRedirect(response, PropMngr.getInst().getProp ("enpass.server.url.c2s")+"logout?service=" + requestUrl);
	}
	
	
	/**
	 * 사용자변경을 처리한다.
	 */
	public void processChangeUser(HttpServletRequest request, HttpServletResponse response, UserInfo userInfo) throws Exception{
		
		String metaDataNameList = null;
		String[] metaDataNameArray = null;
		
		// get mneta data name list from enpass cleint properties
		metaDataNameList =  PropMngr.getInst().getProp("enpass.client.metaDataNameList");
		if( metaDataNameList!=null) {
			metaDataNameArray = metaDataNameList.split(",");
		}

		Map userInfoMap = userInfo.getUserInfoMap();
		Map ssoUserInfoMap = new HashMap();
		
		// put basic info
		ssoUserInfoMap.put("authTime", new Timestamp(System.currentTimeMillis()));
		ssoUserInfoMap.put("_enpass_id_", userInfoMap.get("userId"));
		ssoUserInfoMap.put("nmKor", userInfoMap.get("nm_kor"));
		ssoUserInfoMap.put("groupId", userInfoMap.get("groupId"));
		
		// put metadata
		if( metaDataNameArray!= null) {
			for (int i = 0; i < metaDataNameArray.length; i++) {
				ssoUserInfoMap.put( metaDataNameArray[i], userInfoMap.get( metaDataNameArray[i]));
			}
		}
		
		try {
			// IdPRegister는 IdP를 등록하고,
			// 등록이 성공하면 TGC를 구우러 enPass-Server로 redirect하고,
			// 서버는 다시 Client로 redirect해준다.2010.10.21.KWShin.
			IdPRegister.getInst().changeInfo(request, response, ssoUserInfoMap);
		} catch (BaseException e) {
			// Idp is not allowed to this server
			if( e.getMessage().equals("001")) {
				throw new BaseException( "error.enpass." + e.getMessage());
			}
			throw e;
		}
	}
	
	/**
	 * 리타이렉트가 필요한지 여부를 리턴한다.
	 */
	public boolean isRedirectRequired() {
		return !useDomainCookie;
	}
	

}
