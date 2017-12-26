package com.saltware.enface.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enface.user.service.impl.SiteUserManagerDAO;
import com.saltware.enview.Enview;
import com.saltware.enview.sso.EnviewSSOManager;

public class AuthUtil {
    protected static Log log = LogFactory.getLog( AuthUtil.class);

    /**
     * 리소스의 권한코드를 가져온다.
     * @param request
     * @param resourcePath
     * @return
     */
	public static int getAuthCode( HttpServletRequest request, String resourcePath) {
		Integer auth = (Integer)getAuthMap(request).get(resourcePath);
		return auth == null 	? 0 : auth; 
	}
	
	/**
	 * URI를 기반으로 자동으로 권한을 가져온다. 
	 * @param request
	 * @return
	 */
	public static AuthInfo getAuthInfo( HttpServletRequest request) {
		String path = request.getRequestURI();
		path = "/default/system" + path.substring(0, path.lastIndexOf("."));
		return getAuthInfo(request, path);
	}

	/**
	 * 리소스의 권한정보를 가져온다
	 * @param request
	 * @param resourcePath 리소스 경로
	 * @return
	 */
	public static AuthInfo getAuthInfo( HttpServletRequest request, String resourcePath) {
		return new AuthInfo(resourcePath, getAuthCode(request, resourcePath));
	}
	
	/**
	 * 세션에서 권한맵을 가져온다. 없는경우 생성한다.
	 * @param request
	 * @return
	 */
	private static Map getAuthMap( HttpServletRequest request) {
		Map authMap = (Map)request.getSession().getAttribute("authMap");
		if (authMap == null) {
			authMap = new HashMap();
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			
			SiteUserManagerDAO userDAO = (SiteUserManagerDAO) Enview.getComponentManager().getComponent("com.saltware.enface.user.service.impl.SiteUserManagerDAO");
			if (userDAO != null) {
				List accessList = userDAO.getPermission(userInfoMap);
				if (accessList != null) {
					for (Iterator it = accessList.iterator(); it.hasNext();) {
						Map resultMap = (Map) it.next();

						String resUrl = (String) resultMap.get("res_url");
						int resType = ((BigDecimal) resultMap.get("res_type")).intValue();
						int authCode = ((BigDecimal) resultMap.get("action_mask")).intValue();
						int allow = ((BigDecimal) resultMap.get("is_allow")).intValue();
						
						// 메모리 절감을 위해 페이지 정보만 맵에 담는다.
						// resType - 0 : 페이지, 2 : 포틀릿, 4 : URL, 5 : 게시판, 50 : 게시판(로그인전누구나), 51 게시판(로그인후누구나)
						if (resType == 0 && allow == 1) {
							authMap.put(resUrl, authCode);
						}
					}
				}
			}
			request.getSession().setAttribute("authMap",authMap);
		}
		
		return authMap;
	}

}
