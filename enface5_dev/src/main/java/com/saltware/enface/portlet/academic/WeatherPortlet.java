package com.saltware.enface.portlet.academic;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.saltware.enview.portlet.service.PortletService;
import com.saltware.enview.portlet.service.impl.EnviewPortletServiceFactory;
import com.saltware.enview.request.RequestContext;
import com.saltware.enview.sso.EnviewSSOManager;

/**
 * @Class Name : WeatherPortlet.java
 * @Description : 수강과목 Portlet @
 * @author kevin
 * @since 2014.03.04 12:58:385
 * @version 1.0
 * @see Copyright (C) by Saltware All right reserved.
 */
public class WeatherPortlet extends AcademicPortlet {
	private final Log log = LogFactory.getLog(getClass());

	private static DecimalFormat wsDf = new DecimalFormat("#,##0.0");

	private static Map changeData(Map data, String langKnd) {
		// 날씨아이콘
		String wf = (String) data.get("wfEn");
		wf = wf.replaceAll("/", "").replaceAll(" ", "");
		String night = "";
		if ("Clear".equals(wf) || wf.indexOf("Cloudy") > 0) {
			int hour = Integer.parseInt((String) data.get("hour"));
			if (hour <= 6 || hour >= 21) {
				night = "Night";
			}
		}
		String icon = wf + night + ".png";
		data.put("wfIcon", icon);
		// 날씨
		if ("ko".equals(langKnd)) {
			data.put("wf", data.get("wfKor"));
		} else {
			data.put("wf", data.get("wfEn"));
		}
		// 풍향
		if ("ko".equals(langKnd)) {
			data.put("wd", data.get("wdKor"));
		} else {
			data.put("wd", data.get("wdEn"));
		}

		// 최고기온
		String tmx = (String) data.get("tmx");
		if ("-999.0".equals(tmx)) {
			data.put("tmx", "-");
		}
		// 최저기온
		String tmn = (String) data.get("tmn");
		if ("-999.0".equals(tmn)) {
			data.put("tmn", "-");
		}
		// 풍속
		String ws = (String) data.get("ws");
		ws = wsDf.format(Double.parseDouble(ws));
		data.put("ws", ws);
		return data;
	}

	public static List getWeatherList(String zone, String langKnd) throws BaseException {
		String weatherService = "academic.WeatherKmaDfsService";

		Map paramMap = new HashMap();
		paramMap.put("zone", zone);
		PortletService service = EnviewPortletServiceFactory.getInstance(weatherService);

		List resultList = service.queryForList(paramMap);
		List newList = new ArrayList();
		for (int i = 0; i < resultList.size(); i++) {
			if (i == 0) {
				// 오늘 첫번째 예보
				newList.add(changeData((Map) resultList.get(i), langKnd));
			} else {
				Map map = (Map) resultList.get(i);
				String day = (String) map.get("day");
				String hour = (String) map.get("hour");
				if ("1".equals(day) && "12".equals(hour)) {
					// 다음말 12시
					newList.add(changeData(map, langKnd));
				}
				if ("2".equals(day) && "12".equals(hour)) {
					// 모레 12시
					newList.add(changeData(map, langKnd));
				}
			}

		}
		return newList;
	}

	public static Map getWeather(String zone, String langKnd) throws Exception {
		List list = getWeatherList(zone, langKnd);
		if (list != null && list.size() > 0) {
			return (Map) list.get(0);
		}
		return null;
	}

	/**
	 * Portlet의 화면을 출력한다.
	 * 
	 * @see javax.portlet.GenericPortlet#doView
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		try {
			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			HttpServletResponse res = requestContext.getResponse();

			String langKnd = EnviewSSOManager.getLangKnd(req);

			response.setContentType("text/html; charset=UTF-8");

			String zone = getProperty(request, "ZONE");
			if (zone == null || zone.length() == 0) {
				zone = "4573025000";
			}

			request.setAttribute("resultList", getWeatherList(zone, langKnd));
			super.doView(request, response);
		}

		catch (BaseException e) {
			log.error(e.getMessage(), e);
			throw new PortletException(e);
		}
	}
}
