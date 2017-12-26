
package com.saltware.enface.portlet.academic;

import java.io.IOException;
import java.util.HashMap;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enview.PortalReservedParameters;
import com.saltware.enview.exception.BaseException;
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
public class BoardPortlet extends AcademicPortlet {
	private final Log   log = LogFactory.getLog(getClass());


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
			
			String boardId = getProperty(request, "BOARD-ID");
			
			request.setAttribute("boardId", boardId);

			HashMap paramMap = new HashMap();
			paramMap.put("boardId", boardId);
			request.setAttribute("bltnList", queryForList("academic.BoardListService", paramMap));

			super.doView(request, response); 
		}
		catch (BaseException e) {
            log.error( e.getMessage(), e);
			throw new PortletException(e);
		}
	}
}
