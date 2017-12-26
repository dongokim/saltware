package com.saltware.enface.portlet.academic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;

import org.apache.portals.bridges.common.GenericServletPortlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enview.Enview;
import com.saltware.enview.PortalReservedParameters;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.portlet.service.PortletService;
import com.saltware.enview.portlet.service.impl.EnviewPortletServiceFactory;
import com.saltware.enview.request.RequestContext;
import com.saltware.enview.security.EVSubject;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.sso.EnviewSSOManager;

/**
 * @Class Name : LatestMailPortlet.java
 * @Description : 최근메일 Portlet @
 * @author kevin
 * @since 2014.03.04 12:58:385
 * @version 1.0
 * @see Copyright (C) by Saltware All right reserved.
 */
public class LatestMailPortlet extends GenericServletPortlet {
	private final Log log = LogFactory.getLog(getClass());

	/**
	 * latestMailPortlet을 초기화한다.
	 */
	public void init(PortletConfig config) throws PortletException {
		super.init(config);

	}

	/**
	 * latestMailPortlet의 화면을 출력한다.
	 * 
	 * @see javax.portlet.GenericPortlet#doView
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		try {
			PortletService portletService = EnviewPortletServiceFactory.getInstance(getClass().getName());

			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			HttpServletResponse res = requestContext.getResponse();

			response.setContentType("text/html; charset=UTF-8");

			UserInfo userInfo = EnviewSSOManager.getUserInfo(req);
			if (userInfo == null) {
				throw new BaseException("You have to login !!!");
			}

			Map paramSet = new HashMap();
			paramSet.put("userId", userInfo.getUserId());
			paramSet.put("langKnd", userInfo.getLocale());

			List results = null;
			if (portletService != null) {
				results = (List) portletService.queryForList(paramSet);
			} else {
				throw new PortletException("There is no portletService about latestMail");
			}

			request.setAttribute("results", results);

			// EXTRA DATA
			String moreTarget = Enview.getConfiguration().getString("portletService.latestMail.moreTarget");
			if (moreTarget != null && moreTarget.length() > 0) {
				request.setAttribute("moreTarget", moreTarget);
			}
			String moreSrc = Enview.getConfiguration().getString("portletService.latestMail.moreSrc");
			if (moreSrc != null && moreSrc.length() > 0) {
				request.setAttribute("moreSrc", moreSrc);
			} else {
				request.setAttribute("moreSrc", "#");
			}

			super.doView(request, response);

		} catch (BaseException e) {
			log.error(e.getMessage(), e);
			throw new PortletException(e);
		}
	}

	/**
	 * latestMailPortlet의 헤더정보를 출력한다.
	 * 
	 * @see javax.portlet.GenericPortlet#doHeaders
	 */
	public void doHeaders(RenderRequest request, RenderResponse response) {
		super.doHeaders(request, response);
	}

	/**
	 * latestMailPortlet의 편집화면을 출력한다.
	 * 
	 * @see javax.portlet.GenericPortlet#doEdit
	 */
	public void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		super.doEdit(request, response);
	}

	/**
	 * latestMailPortlet의 도움말 화면을 출력한다.
	 * 
	 * @see javax.portlet.GenericPortlet#doView
	 */
	public void doHelp(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		super.doHelp(request, response);
	}

	/**
	 * 이 기능의 기본적인 행위는 ResourceRequest의 ResourceID로 RequestDispatcher.foward를 하는것이다. ResourceID가 없는 경우에는 아무런 행위를 안한다.
	 * 
	 * @since 2.0
	 */
	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
		super.serveResource(request, response);
	}

	/**
	 * 기본적인 기능은 주석이 '@ProcessEvent'로 된 메소드 중 주어진 event이름에 해당하는 메소드를 수행한다. 해당하는 메소드를 찾지 못하면 response에 현재 render parameter들을 저장한다. 이 메소드들은 모두 public으로 정의 되어야 한다.
	 * 
	 * @see javax.portlet.EventPortlet#processEvent(javax.portlet.EventRequest, javax.portlet.EventResponse)
	 * @since 2.0
	 */
	public void processEvent(EventRequest request, EventResponse response) throws PortletException, IOException {
		super.processEvent(request, response);
	}

	/**
	 * latestMailPortlet의 액션을 처리한다.
	 * 
	 * @see javax.portlet.GenericPortlet#processAction
	 */
	public void processAction(ActionRequest request, ActionResponse actionResponse) throws PortletException, IOException {
		try {
			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			HttpServletResponse res = requestContext.getResponse();

			PortletSession session = request.getPortletSession();

			String langKnd = EnviewSSOManager.getUserInfo(req).getLocale();
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(req);
			if (userInfoMap == null) {
				throw new BaseException("You have to login !!!");
			}

			// this.portletService
		} catch (BaseException e) {
			log.error(e.getMessage(), e);
			throw new PortletException(e);
		}
	}
}
