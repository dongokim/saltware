
package com.saltware.enface.portlet.academic;

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
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enface.portlet.common.EnviewBasePortlet;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.portlet.service.PortletService;
import com.saltware.enview.portlet.service.impl.EnviewPortletServiceFactory;
import com.saltware.enview.security.UserInfo;

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
public class AcademicPortlet extends EnviewBasePortlet {
	private final Log   log = LogFactory.getLog(getClass());

	/**
	 * 년도학기 서버스
	 */
	PortletService yearSemeService = EnviewPortletServiceFactory.getInstance("academic.YearSemeService");
	
	/**
	 * 년도학기가 캐시된시간
	 */
	long yearSemeCachedTime = 0;
	/**
	 * 연도학기 캐시 사용기간
	 */
	long yearSemeMaxAge = 60000; // 1분
	
	/**
	 * 연도학기 캐시
	 */
	Map yearSemeCache = new Hashtable();
	
	
	/**
	 * Portlet을 초기화한다.
	 */
	public void init(PortletConfig config) throws PortletException {
        super.init(config);
	}
	

	/**
	 * 년도학기 서비스에서 년도학기를 가져온다
	 * @param service 년도학기 서비스
	 * @return
	 * @throws Exception
	 */
	public Map getYearSeme( String yearSemeId) throws BaseException {
		long cacheAge = System.currentTimeMillis() - yearSemeCachedTime;
		if( cacheAge > yearSemeMaxAge) {
			yearSemeCache.clear();
			yearSemeCachedTime = System.currentTimeMillis();
		}
		Map yearSeme = (Map)yearSemeCache.get( yearSemeId);
		if( yearSeme != null) {
			return yearSeme;
		}
		
		Map paramMap = new HashMap();
		paramMap.put( "yearSemeId", yearSemeId);
		List resultList = yearSemeService.queryForList( paramMap);
		if( resultList != null && resultList.size() > 0) {
			yearSeme =  (Map)resultList.get(0);
			yearSemeCache.put( yearSemeId, yearSeme);
			return yearSeme;
		}
		return null;
	}
	
	/**
	 * 년도학기를 파리미터로  포틀릿 서비스를 호춯하여 목록을 가져온다 
	 * @param serviceName 포틀릿 서비스명
	 * @param userInfo 사용자정보
	 * @param prefs 포틀릿실행정보
	 * @return 포틀릿서비스 호출결과
	 * @throws Exception
	 */
	protected List queryForList( String serviceName, String yearSemeId, UserInfo userInfo, PortletPreferences prefs)  throws BaseException {
		PortletService service = EnviewPortletServiceFactory.getInstance( serviceName);
		Map userInfoMap = userInfo == null ? new HashMap() : userInfo.getUserInfoMap();
		Map paramMap = getParamMap( service.getConfig(), userInfoMap, prefs);
		paramMap.putAll( getYearSeme(yearSemeId));
		return service.queryForList( paramMap);
	}
	
	/**
	 * 년도학기를 파리미터로 포틀릿 서비스를 호출하여 하나의 결과를 가져온다
	 * @param serviceName 포틀릿 서비스명
	 * @param userInfo 사용자정보
	 * @param prefs 포틀릿실행정보
	 * @return 포틀릿서비스 호출결과
	 * @throws Exception
	 */
	protected String queryForString( String serviceName, String yearSemeId, UserInfo userInfo, PortletPreferences prefs) throws BaseException {
		PortletService service = EnviewPortletServiceFactory.getInstance( serviceName);
		Map userInfoMap = userInfo == null ? new HashMap() : userInfo.getUserInfoMap();
		Map paramMap = getParamMap( service.getConfig(), userInfoMap, prefs);
		paramMap.putAll( getYearSeme(yearSemeId));
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
}
