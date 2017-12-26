package com.saltware.enface.portlet.academic;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;
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
 * @Class Name : UserInfoPortlet.java
 * @Description : 수강과목 Portlet @
 * @author kevin
 * @since 2014.03.04 12:58:385
 * @version 1.0
 * @see Copyright (C) by Saltware All right reserved.
 */
public class UserInfoPortlet extends AcademicPortlet {
	private final Log log = LogFactory.getLog(getClass());

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

			// 신규메일건수
			String mailCount = queryForString("academic.NewMailCountService", userInfo, request.getPreferences());
			request.setAttribute("mailCount", nvl(mailCount, "N/A"));

			// 신규쪽지건수
			String noteCount = queryForString("academic.NewNoteCountService", userInfo, request.getPreferences());
			request.setAttribute("noteCount", nvl(noteCount, "N/A"));

			// 사용자그룹정보
			List userGroupList = queryForList("academic.UserGroupListService", userInfo, request.getPreferences());
			// System.out.println( userGroupList);
			request.setAttribute("userGroupList", userGroupList);

			// 사용자정보
			request.setAttribute("userInfo", userInfo);
			request.setAttribute("userInfoMap", userInfo.getUserInfoMap());

			ResourceURL url = response.createResourceURL();

			request.setAttribute("resourceURL", url.toString());

			super.doView(request, response);
		}

		catch (BaseException e) {
			log.error(e.getMessage(), e);
			throw new PortletException(e);
		}
	}

	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
		try {
			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			String result = "";
			try {
				UserInfo userInfo = EnviewSSOManager.getUserInfo(req);
				if (userInfo == null) {
					throw new BaseException("You have to login !!!");
				}

				// 신규메일건수
				String mailCount = queryForString("academic.NewMailCountService", userInfo, request.getPreferences());

				// 신규쪽지건수
				String noteCount = queryForString("academic.NewNoteCountService", userInfo, request.getPreferences());

				result = "{\"mailCount\" : " + mailCount + ", \"noteCount\" : " + noteCount + "}";

			} catch (BaseException e) {
				result = "{\"msg\" : \"" + e.getMessage() + "\"}";

			}
			response.setContentType("text/html;charset=UTF-8");
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
