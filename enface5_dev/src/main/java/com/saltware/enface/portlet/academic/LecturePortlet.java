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
 * @Class Name : LecturePortlet.java
 * @Description : 수업리스트 Portlet @
 * @author kevin
 * @since 2014.03.04 12:58:385
 * @version 1.0
 * @see Copyright (C) by Saltware All right reserved.
 */
public class LecturePortlet extends AcademicPortlet {
	private final Log log = LogFactory.getLog(getClass());

	/**
	 * lecturePortlet을 초기화한다.
	 */
	public void init(PortletConfig config) throws PortletException {
		super.init(config);

	}

	/**
	 * attendedLecturePortlet의 화면을 출력한다.
	 * 
	 * @see javax.portlet.GenericPortlet#doView
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		try {

			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			HttpServletResponse res = requestContext.getResponse();

			response.setContentType("text/html; charset=UTF-8");

			UserInfo userInfo = EnviewSSOManager.getUserInfo(req);
			if (userInfo == null) {
				throw new BaseException("You have to login !!!");
			}

			// 수강과목
			List atlcList = queryForList("academic.AttendedLectureService", "lecture", userInfo, request.getPreferences());
			request.setAttribute("atlcList", atlcList);

			// 시간표
			List ttList = queryForList("academic.TimeTableService", "lecture", userInfo, request.getPreferences());
			request.setAttribute("ttList", ttList);

			super.doView(request, response);

		}

		catch (BaseException e) {
			log.error(e.getMessage(), e);
			throw new PortletException(e);
		}
	}
}
