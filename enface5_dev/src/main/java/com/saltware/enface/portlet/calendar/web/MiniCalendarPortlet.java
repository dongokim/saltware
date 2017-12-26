package com.saltware.enface.portlet.calendar.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.saltware.enview.PortalReservedParameters;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.request.RequestContext;
import com.saltware.enview.security.EVSubject;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.sso.EnviewSSOManager;

/**
 * @Class Name : MiniCalendarPortlet.java
 * @Description : 미니 캘린더 Portlet
 * @author secrain
 * @since 2014.04.28
 * @version 1.0
 * @see Copyright (C) by Saltware All right reserved.
 */
public class MiniCalendarPortlet extends CalendarBasePortlet {
	private final Log log = LogFactory.getLog(getClass());

	/**
	 * MiniCalendarPortlet을 초기화한다.
	 */
	public void init(PortletConfig config) throws PortletException {
		super.init(config);
	}

	/**
	 * MiniCalendarPortlet의 화면을 출력한다.
	 * 
	 * @see javax.portlet.MiniCalendarPortlet#doView
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		try {
			log.info("doView");
			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			HttpServletResponse res = requestContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			UserInfo userInfo = EnviewSSOManager.getUserInfo(req);
			if (userInfo == null) {
				throw new BaseException("You have to login !!!");
			}
			String calendarId = request.getParameter("calendarId");
			if (calendarId == null)
				calendarId = "1";

			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			int date = Calendar.getInstance().get(Calendar.DATE);

			userInfo.setString("user_id", EVSubject.getUserId());
			userInfo.setString("calendarId", calendarId);
			userInfo.setString("start_datim", year + "-" + (month < 10 ? "0" + month : month) + "-" + (date < 10 ? "0" + date : date));
			userInfo.setString("end_datim", year + "-" + (month < 10 ? "0" + month : month) + "-" + (date < 10 ? "0" + date : date));

			List scheduleList = queryForList("calendar.OndayScheduleService", userInfo, request.getPreferences());
			request.setAttribute("scheduleList", scheduleList);
			request.setAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
			request.setAttribute("month", Calendar.getInstance().get(Calendar.MONTH) + 1);

			userInfo.setString("start_datim", year + "-" + (month < 10 ? "0" + month : month) + "-01");
			userInfo.setString("end_datim", getEndDate(year, month));
			log.info("before service");
			List scheduleCountList = queryForList("calendar.ScheduleCountByMonthService", userInfo, request.getPreferences());
			log.info("after service");
			request.setAttribute("scheduleCountList", scheduleCountList);
			super.doView(request, response);
		} catch (BaseException e) {
			log.error(e.getMessage(), e);
			;
			throw new PortletException(e);
		}
	}

	// 현재 월의 시작일, 마지막 일 (년-월-일)
	public String getEndDate(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, 1);
		int end_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return year + "-" + (month < 10 ? "0" + month : month) + "-" + end_day;
	}

	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
		String resourceId = request.getResourceID();
		if (resourceId.equals("scheduleCount")) {
			this.scheduleCountByMonth(request, response);
		} else {
			this.scheduleListByDate(request, response);
		}
	}

	private void scheduleCountByMonth(ResourceRequest request, ResourceResponse response) throws PortletException {
		try {
			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			String result = "";
			try {
				UserInfo userInfo = EnviewSSOManager.getUserInfo(req);
				if (userInfo == null) {
					throw new BaseException("You have to login !!!");
				}

				String calendarId = request.getParameter("calendarId");
				if (calendarId == null)
					calendarId = "1";

				String pYear = request.getParameter("year");
				String pMonth = request.getParameter("month");
				String pDate = request.getParameter("date");

				int year = pYear == null ? Calendar.getInstance().get(Calendar.YEAR) : Integer.parseInt(pYear);
				int month = pMonth == null ? Calendar.getInstance().get(Calendar.MONTH) + 1 : Integer.parseInt(pMonth);
				int date = pDate == null ? 1 : Integer.parseInt(pMonth);

				userInfo.setString("user_id", EVSubject.getUserId());
				userInfo.setString("calendarId", calendarId);
				userInfo.setString("start_datim", year + "-" + (month < 10 ? "0" + month : month) + "-01");
				userInfo.setString("end_datim", getEndDate(year, month));

				List scheduleCountList = queryForList("calendar.ScheduleCountByMonthService", userInfo, request.getPreferences());
				request.setAttribute("scheduleCountList", scheduleCountList);

				result = "{\"year\" : " + year + ", \"month\" : " + month + ", ";
				result += "\"scheduleCountList\":";
				result += "[\n";
				for (Iterator it = scheduleCountList.iterator(); it.hasNext();) {
					Map schedule = (Map) it.next();
					result += "{ \"startDate\": \"" + schedule.get("startDate") + "\",";
					result += "\"endDate\": \"" + schedule.get("endDate") + "\" }\n";
					if (it.hasNext())
						result += ",";
				}
				result += "] }";
			} catch (BaseException e) {
				result = "{\"msg\" : \"" + e.getMessage() + "\"}";
			}

			response.setContentType("text/json;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.println(result);
			pw.flush();
		}

		catch (IOException e) {
			log.error(e.getMessage(), e);
			;
			throw new PortletException(e);
		}
	}

	private void scheduleListByDate(ResourceRequest request, ResourceResponse response) throws PortletException {
		try {
			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			String result = "";
			try {
				UserInfo userInfo = EnviewSSOManager.getUserInfo(req);
				if (userInfo == null) {
					throw new BaseException("You have to login !!!");
				}

				String calendarId = request.getParameter("calendarId");
				if (calendarId == null)
					calendarId = "1";

				String pYear = request.getParameter("year");
				String pMonth = request.getParameter("month");
				String pDate = request.getParameter("date");

				int year = pYear == null ? Calendar.getInstance().get(Calendar.YEAR) : Integer.parseInt(pYear);
				int month = pMonth == null ? Calendar.getInstance().get(Calendar.MONTH) + 1 : Integer.parseInt(pMonth);
				int date = pDate == null ? 1 : Integer.parseInt(pDate);

				userInfo.setString("user_id", EVSubject.getUserId());
				userInfo.setString("calendarId", calendarId);
				userInfo.setString("start_datim", year + "-" + (month < 10 ? "0" + month : month) + "-" + (date < 10 ? "0" + date : date));
				userInfo.setString("end_datim", year + "-" + (month < 10 ? "0" + month : month) + "-" + (date < 10 ? "0" + date : date));

				List scheduleList = queryForList("calendar.OndayScheduleService", userInfo, request.getPreferences());
				request.setAttribute("scheduleList", scheduleList);

				result = "{\"year\" : " + year + ", \"month\" : " + month + ", \"date\": " + date + ",";
				result += "\"scheduleList\":";
				result += "[\n";
				for (Iterator it = scheduleList.iterator(); it.hasNext();) {
					Map schedule = (Map) it.next();
					result += "{ \"name\": \"" + schedule.get("name") + "\",";
					result += "\"startDatim\": \"" + schedule.get("startDatim") + "\",";
					result += "\"endDatim\": \"" + schedule.get("endDatim") + "\" }\n";
					if (it.hasNext())
						result += ",";
				}
				result += "] }";
			} catch (BaseException e) {
				result = "{\"msg\" : \"" + e.getMessage() + "\"}";
			}

			response.setContentType("text/json;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.println(result);
			pw.flush();
		}

		catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new PortletException(e);
		}
	}
}
