
package com.saltware.enface.portlet.academic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enhancer.banner.service.BannerUserVO;
import com.saltware.enhancer.banner.service.impl.BannerUserDAO;
import com.saltware.enhancer.event.service.EventUserVO;
import com.saltware.enhancer.event.service.impl.EventUserDAO;
import com.saltware.enview.Enview;
import com.saltware.enview.PortalReservedParameters;
import com.saltware.enview.exception.BaseException;
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
public class EventPortlet extends AcademicPortlet {
	private final Log log = LogFactory.getLog(getClass());
	private EventUserDAO eventUserDAO;
	
    /**
	 * lecturePortlet을 초기화한다.
	 */
	public void init(PortletConfig config) throws PortletException{
		this.eventUserDAO = (EventUserDAO)Enview.getComponentManager().getComponent("eventUserDAO");
        super.init(config);
	}
	
	/**
	 * lecturePortlet의 화면을 출력한다.
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
			
			HashMap paramMap = new HashMap();
			request.setAttribute("eventList", queryForList("academic.EventListService", paramMap));

			
			Map userInfoMap = EnviewSSOManager.getUserInfo(req).getUserInfoMap();
			List<EventUserVO> eventPortletList = new ArrayList<EventUserVO>();
			
			String flag_group = request.getParameter("flag_group");         // 관리자계정 접속시 각 그룹별 포틀릿확인.
			String langKnd = "ko";
			String site_name ="";
			String groupid = null;
			
			if(userInfoMap != null ) {
				String userId = (String) userInfoMap.get("user_id");   //로그인아이디
				List groups = (List)userInfoMap.get("groups");
				HashMap groupId = new HashMap();
				groupId.put("groupId", userInfoMap.get("groupPrincipalId"));
				langKnd = (String)userInfoMap.get("lang_knd");
				if(langKnd == null || "".equals(langKnd)){
					langKnd = "ko";
				}else if(!"ko".equals(langKnd) && !"en".equals(langKnd)){
					langKnd = "ko";
				}
				site_name = (String)userInfoMap.get("site_name");	
				groupid = groupId.get("groupId").toString();
				
				if(groupId.get("groupId").toString().equals("103")){
					groupid = "342";
				}
			}
			
			eventPortletList = eventUserDAO.getEventPortletList(langKnd, groupid);
			req.setAttribute("eventPortletList", eventPortletList);
			req.setAttribute("site_name", site_name);
			req.setAttribute("langKnd", langKnd);
			
			super.doView(request, response); 
		}
		catch (BaseException e) {
            log.error( e.getMessage(), e);
			throw new PortletException(e);
		}
	}
}
