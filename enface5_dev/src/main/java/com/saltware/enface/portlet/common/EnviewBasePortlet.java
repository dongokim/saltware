
package com.saltware.enface.portlet.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.portals.bridges.common.GenericServletPortlet;

import com.saltware.enview.PortalReservedParameters;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.portlet.service.PortletService;
import com.saltware.enview.portlet.service.config.PortletServiceConfig;
import com.saltware.enview.portlet.service.impl.EnviewPortletServiceFactory;
import com.saltware.enview.request.RequestContext;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.sso.EnviewSSOManager;

/**  
 * @Class Name : AttendedLecturePortlet.java
 * @Description : 수강과목 Portlet
 * @
 * @author kevin
 * @since 2014.03.04 12:58:385
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by Saltware All right reserved.
 */
public class EnviewBasePortlet extends GenericServletPortlet {
	private final Log   log = LogFactory.getLog(getClass());

	
	/**
	 * Portlet을 초기화한다.
	 */
	public void init(PortletConfig config) throws PortletException {
        super.init(config);
	}
	

	/**
	 * 포틀릿서비스 설정을 바탕으로  사용자정보, 포틀릿 실행정보에서 필요한 정보를 꺼내서 파라미터 맵을 만든다. 
	 * @param config 포틀릿 섭스 설정
	 * @param userInfoMap 사용자정보 맵
	 * @param prefs 포틀릿 실행설정
	 * @return
	 */
	public final Map getParamMap( PortletServiceConfig config,  Map userInfoMap, PortletPreferences prefs) {
		Map paramMap = new HashMap();
	
		String userParam = config.getUserParam();
		String param;
		if( userParam != null) {
			String[] params = userParam.split(",");
			for (int j = 0; j < params.length; j++) {
				param = params[j].trim();	
				paramMap.put( param, userInfoMap.get( param));
			}
		}
		
		String portletParam = config.getPortletParam();
		if( portletParam != null) {
			String[] params = portletParam.split(",");
			String value = null;
			for (int j = 0; j < params.length; j++) {
				param = params[j].trim();
				value = prefs.getValue(param, null);
				if( value == null) {
					value = getPortletConfig().getInitParameter(param);
				}
				paramMap.put( param, value);
			}
		}
		
		return paramMap;
	}

	/**
	 * 포틀릿 서비스를 호춯하여 목록을 가져온다
	 * @param serviceName 포틀릿 서비스명
	 * @param userInfo 사용자정보
	 * @param prefs 포틀릿실행정보
	 * @return 포틀릿서비스 호출결과
	 * @throws BaseException
	 */
	protected final List queryForList( String serviceName, UserInfo userInfo, PortletPreferences prefs) throws BaseException {
		PortletService service = EnviewPortletServiceFactory.getInstance( serviceName);
		Map userInfoMap = userInfo == null ? new HashMap() : userInfo.getUserInfoMap();
		Map paramMap = getParamMap( service.getConfig(), userInfoMap, prefs);
		return service.queryForList( paramMap);
	}
	
	/**
	 * 포틀릿 서비스를 호춯하여 목록을 가져온다
	 * @param service 포틀릿 서비스
	 * @param userInfo 사용자정보
	 * @param prefs 포틀릿실행정보
	 * @return 포틀릿서비스 호출결과
	 * @throws BaseException
	 */
	protected final List queryForList( PortletService service, UserInfo userInfo, PortletPreferences prefs) throws BaseException {
		Map userInfoMap = userInfo == null ? new HashMap() : userInfo.getUserInfoMap();
		Map paramMap = getParamMap( service.getConfig(), userInfoMap, prefs);
		return service.queryForList( paramMap);
	}
	
	/**
	 * 포틀릿 서비스를 호춯하여 목록을 가져온다
	 * @param serviceName 포틀릿 서비스명
	 * @param paramMap 파리미터맵
	 * @return 포틀릿서비스 호출결과
	 * @throws BaseException
	 */
	protected final List queryForList( String serviceName, Map paramMap) throws BaseException{
		PortletService service = EnviewPortletServiceFactory.getInstance( serviceName);
		return service.queryForList( paramMap);
	}
	
	
	/**
	 * 포틀릿 서비스를 호출하여 하나의 결과를 가져온다
	 * @param serviceName 포틀릿 서비스명
	 * @param userInfo 사용자정보
	 * @param prefs 포틀릿실행정보
	 * @return 포틀릿서비스 호출결과
	 * @throws BaseException
	 */
	protected final String queryForString( String serviceName, UserInfo userInfo, PortletPreferences prefs) throws BaseException {
		PortletService service = EnviewPortletServiceFactory.getInstance( serviceName);
		Map userInfoMap = userInfo == null ? new HashMap() : userInfo.getUserInfoMap();
		Map paramMap = getParamMap( service.getConfig(), userInfoMap, prefs);
		return service.queryForString( paramMap);
	}
	
	/**
	 * 포틀릿 서비스를 호출하여 하나의 결과를 가져온다
	 * @param service 포틀릿 서비스
	 * @param userInfo 사용자정보
	 * @param prefs 포틀릿실행정보
	 * @return 포틀릿서비스 호출결과
	 * @throws BaseException
	 */
	protected final String queryForString( PortletService service, UserInfo userInfo, PortletPreferences prefs) throws BaseException {
		Map userInfoMap = userInfo == null ? new HashMap() : userInfo.getUserInfoMap();
		Map paramMap = getParamMap( service.getConfig(), userInfoMap, prefs);
		return service.queryForString( paramMap);
	}
	
	/**
	 * value 가 null이아니면 value를 그대로 null이면 defaultValue를 리턴한다
	 * @param value 테스트하려는 값
	 * @param defaultValue null일때의 값 
	 * @return
	 */
	public String nvl( String value, String defaultValue) {
		return value != null ? value  : defaultValue;
	}
	
	
	public String getProperty( PortletRequest request, String key, String defaultValue) {
		String value = getPortletConfig().getInitParameter(key);
		value =  value != null ? value : request.getProperty(key);
		return value != null ? value : defaultValue;
	}
	public String getProperty( PortletRequest request, String key) {
		return getProperty(request, key, null);
	}
	
	protected int getInt( Object obj) {
		if( obj==null) return 0;
		if( obj instanceof Number ) {
			return ((Number)obj).intValue();
		} else {
			try {
				return Integer.parseInt( obj.toString());
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		
	}
	
	
	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException	{

		String resourcePage = getProperty(request, "ResourcePage");
	    
	    if (resourcePage != null)    {
	      PortletContext context = getPortletContext();
	      PortletRequestDispatcher rd = context.getRequestDispatcher(resourcePage);
	      rd.include(request, response);
	    }		
	}
	
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException{
		RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
		HttpServletRequest req = requestContext.getRequest();
		//HttpServletResponse res = requestContext.getResponse();
		boolean isMobile = EnviewSSOManager.isMobile(req);
		if(!isMobile) super.doView(request, response);
		else {
			String viewPage = getProperty(request, "MobileViewPage");
			if(viewPage == null || "".equals(viewPage)) super.doView(request, response);
			else {
				PortletContext context = getPortletContext();
			      PortletRequestDispatcher rd = context.getRequestDispatcher(viewPage);
			      rd.include(request, response);
			}
		}
	}

	
}
