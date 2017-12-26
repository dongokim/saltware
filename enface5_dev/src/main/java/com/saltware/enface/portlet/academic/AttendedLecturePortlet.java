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

import com.saltware.enview.PortalReservedParameters;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.request.RequestContext;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.sso.EnviewSSOManager;

/**
 * @Class Name : AttendedLecturePortlet.java
 * @Description : 수강과목 Portlet @
 * @author kevin
 * @since 2014.03.04 12:58:385
 * @version 1.0
 * @see Copyright (C) by Saltware All right reserved.
 */
public class AttendedLecturePortlet extends AcademicPortlet {
	private final Log log = LogFactory.getLog(getClass());

	private Map[][] makeTimeTable(List lectureList) {

		Map[][] ttData = new Map[10][7];

		for (int i = 0; i < lectureList.size(); i++) {
			Map data = (Map) lectureList.get(i);
			int dow = getInt(data.get("lectDow")) - 1;
			int strtHr = getInt(data.get("strtHr")) - 1;
			int endHr = getInt(data.get("endHr")) - 1;
			for (int hr = strtHr; hr <= endHr; hr++) {
				ttData[hr][dow] = data;
			}
		}
		return ttData;
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

			List ttList = queryForList("academic.TimeTableService", "lecture", userInfo, request.getPreferences());
			Map[][] ttData = makeTimeTable(ttList);
			request.setAttribute("ttList", ttList);
			request.setAttribute("ttData", ttData);

			super.doView(request, response);
		}

		catch (BaseException e) {
			log.error(e.getMessage(), e);
			throw new PortletException(e);
		}
	}
}
