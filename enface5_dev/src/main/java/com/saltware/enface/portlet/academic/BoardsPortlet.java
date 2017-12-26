
package com.saltware.enface.portlet.academic;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enface.util.DataCacheManager;
import com.saltware.enview.Enview;
import com.saltware.enview.PortalReservedParameters;
import com.saltware.enview.domain.DomainInfo;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.request.RequestContext;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.sso.EnviewSSOManager;

/**  
 * @Class Name : BannerPortlet.java
 * @Description : 배너 Portlet
 * @
 * @author kevin
 * @since 2014.03.04 12:58:385
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by Saltware All right reserved.
 */
public class BoardsPortlet extends AcademicPortlet {
	private final Log log = LogFactory.getLog(getClass());
	
    /**
	 * Portlet을 초기화한다.
	 */
	public void init(PortletConfig config) throws PortletException{
        super.init(config);
	}
	
	public static DataCacheManager boardCacheManager = new DataCacheManager(100, 60);
	
	public List getBltnList( String boardId) throws BaseException {
		HashMap paramMap = new HashMap();
		paramMap.put("boardId", boardId);
		return queryForList("academic.BoardListService", paramMap);
	}
	/**
	 * Portlet의 화면을 출력한다.
	 *
	 * @see javax.portlet.lecturePortlet#doView
	 */
	public void doView(RenderRequest request, RenderResponse response)	throws PortletException, IOException {
		try {
			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			HttpServletResponse res = requestContext.getResponse();
		
			response.setContentType("text/html; charset=UTF-8");
			UserInfo userInfo = EnviewSSOManager.getUserInfo(req);
			
			String boardIds = getProperty(request, "BOARD-IDS");
			String menuPath = getProperty(request, "MENU-PATH");
			
			DomainInfo domainInfo = (DomainInfo)req.getSession().getAttribute(LoginConstants.USER_DOMAIN);
			if( domainInfo !=null) {
				request.setAttribute( "domainPath", domainInfo.getPagePath());
			}
			request.setAttribute( "menuPath", menuPath);
			
			String[] boardIdList = null;
			if( boardIds!=null && boardIds.length() >0) {
				boardIdList = boardIds.split(",");
			}
			if( boardIdList!=null && boardIdList.length > 0) {
				List boardBltnList = new ArrayList();
				for (int i = 0; i < boardIdList.length; i++) {
					List bltnList = getBltnList(boardIdList[i]);
					boardBltnList.add( bltnList);
				}
				request.setAttribute("boardIdList", boardIdList);
				request.setAttribute("boardBltnList", boardBltnList);
			}
//			request.setAttribute("bltnList", getBltnList(boardId));
			super.doView(request, response); 
		}
		catch (BaseException e) {
            log.error( e.getMessage(), e);
			throw new PortletException(e);
		}
	}
	
	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException	{
		try {
			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();

			String boardId = request.getResourceID();
			
			HashMap paramMap = new HashMap();
			paramMap.put("boardId", boardId);
			request.setAttribute("bltnList", queryForList("academic.BoardListService", paramMap));
			super.serveResource(request, response);
		} catch (BaseException e) {
            log.error( e.getMessage(), e);
			throw new PortletException(e);
		}
	}
	
}
