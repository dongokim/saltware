
package com.saltware.enface.portlet.academic;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enface.user.service.impl.SiteUserManagerDAO;
import com.saltware.enview.Enview;
import com.saltware.enview.PortalReservedParameters;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.page.PageManager;
import com.saltware.enview.request.RequestContext;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.sso.EnviewSSOManager;

/**  
 * @Class Name : GroupWareCntPortlet.java
 * @Description : 그룹웨어 Portlet
 * @
 * @author kevin
 * @since 2014.03.04 12:58:385
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by Saltware All right reserved.
 */
public class GroupWareCountPortlet extends AcademicPortlet {
	private final Log   log = LogFactory.getLog(getClass());
    private SiteUserManagerDAO siteUserManagerDAO = null;
    
    public GroupWareCountPortlet() {
    	this.siteUserManagerDAO = (SiteUserManagerDAO) Enview.getComponentManager().getComponent("com.saltware.enface.user.service.impl.SiteUserManagerDAO");
    }
    
	/**
	 * attendedLecturePortlet의 화면을 출력한다.
	 *
	 * @see javax.portlet.GenericPortlet#doView
	 */
	public void doView(RenderRequest request, RenderResponse response)	throws PortletException, IOException 
	{
		try {

			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			HttpServletResponse res = requestContext.getResponse();
		
			response.setContentType("text/html; charset=UTF-8");
			
			UserInfo userInfo = EnviewSSOManager.getUserInfo(req);
 			if( userInfo == null ) {
            	throw new BaseException("You have to login !!!");
            }
 			
 			// 결재, 수신, 공람의  카운팅  조회
 			List groupWareCntInfo = queryForList("academic.GroupWareCountService", userInfo, request.getPreferences());
			request.setAttribute("groupWareCountInfo", groupWareCntInfo);
			
			super.doView(request, response); 
		}
		
		catch (BaseException e) {
            log.error( e.getMessage(), e);
			throw new PortletException(e);
		}
	}
}
