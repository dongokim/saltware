/**
 * Copyright (c) 2010 Saltware, Inc.
 * 
 * http://www.saltware.co.kr
 * 
 * Kolon Science Valley Bldg 2th. 901, Guro-dong 811, Guro-gu,
 * Seoul, 152-878, South Korea.
 * All Rights Reserved.
 * 
 * This software is the Java based Enterprise Portal of Saltware, Inc.
 * Making any change or distributing this without permission from us is out of law.
 */
package com.saltware.enface.filters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.saltware.enface.util.HttpUtil;
import com.saltware.enpass.client.util.HttpClient3Factory;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.sso.EnviewSSOManager;

/**
 * 세션탈취방지 필터<br>
 * 
 * @author smna
 * 
 */
public class SessionHijackingFilter implements Filter {

	protected final Log log = LogFactory.getLog(this.getClass());
	private String errorMessage = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		errorMessage = filterConfig.getInitParameter( "errorMessage");
		if( errorMessage==null) {
			errorMessage = "로그인 후 IP나 브라우저가 변경되었습니다. 다시 로그인 후 사용하시기 바랍니다.";
		}
	}

	/**
	 * 필터사용 중지 처리를 한다.
	 */
	public void destroy() {
		//
	}
	

	public boolean isSessionValid(HttpServletRequest request, HttpServletResponse response) {

		// skip static resource
		String uri = request.getRequestURI();
		if (uri.endsWith(".gif") || uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".js") || uri.endsWith(".css")) {
			return true;
		}
		
		log.debug("=====================================");
		log.debug("checking session hijacking on " + uri);
		try {
			// config
			HttpSession session = request.getSession();
			
			// 모바일환경에서는 체크안함
			String isMobile = (String)session.getAttribute("isMobile");
			if("t".equals(isMobile)) {
				log.debug("isMobile");
				return true;
			}
			
			Map userInfo = EnviewSSOManager.getUserInfoMap(request);
			if( userInfo != null) {
				String clientIp = HttpUtil.getClientIp(request);
				String clientAgent = HttpUtil.getUserAgent(request);
				
				String userId = (String)userInfo.get("userId"); 
				String ipProtect = (String)userInfo.get("ipProtect"); 
				String loginIp = (String)userInfo.get("loginIp");
				String loginAgent = (String)userInfo.get("loginAgent");

				// 에이전트 체크
				if (!clientAgent.equals( loginAgent)) {
					logAgentError( userId, loginAgent, clientAgent);
					return false;
				}
				
				// IP체크
				boolean invalidIp = false;
				if( "1".equals(ipProtect)) {
					// C급 주소체크
					String clientIp2 = toIp( clientIp, 3);
					String loginIp2 = toIp( loginIp, 3);
					log.debug("compare class c client ip " + clientIp2 + " to login ip " + loginIp2 );
					invalidIp = !clientIp2.equals( loginIp2);
				} else if( "2".equals(ipProtect)) {
					List loginIps = (List)session.getAttribute("loginIps");
					log.debug("compare client ip " + clientIp + " to login ip " + loginIp + " and ip history " + loginIps );
					if( clientIp.equals(loginIp) || ( loginIps != null && loginIps.contains(clientIp)) ) {
						invalidIp = false;
					}
				} else if( "3".equals(ipProtect)) {
					log.debug("compare client ip " + clientIp + " to login ip " + loginIp );
					invalidIp = !clientIp.equals( loginIp);
				}
				
				if( invalidIp) {
					logIpError(userId, ipProtect, clientIp, loginIp);
					return false;
				} else {
					log.debug("Ip check ok!");
				}
			}
			
			return true;
		} finally {
			log.debug("=====================================");
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		if (! isSessionValid(req, res)) {
			log.debug("session is invalid. invalidate session.");
			req.getSession().invalidate();
			req.getSession().setAttribute("errorMessage", errorMessage);
		}

		// Pass control on to the next filter
		chain.doFilter(request, response);

	}

	protected int toInt(Object obj, int d) {
		if (obj == null) {
			return d;
		}
		if (obj instanceof Number) {
			return ((Number) obj).intValue();
		} else {
			try {
				return Integer.parseInt(obj.toString());
			} catch (NumberFormatException e) {
				return d;
			}
		}
	}

	protected String toIp(String ip, int n) {
		String ads[] = ip.split("\\.");
		if (ads == null || ads.length != 4) {
			return ip;
		}
		String s = "";
		for (int i = 0; i < n; i++) {
			if (n > 0)
				s += ".";
			s += ads[i];
		}
		return s;
	}

	public static final String getClientIp(HttpServletRequest request) {
		String clientIp = request.getHeader("Proxy-Client-IP");
		if (clientIp == null) {
			clientIp = request.getHeader("WL-Proxy-Client-IP");
			if (clientIp == null) {
				clientIp = request.getHeader("X-Forwarded-For");
				if (clientIp == null) {
					clientIp = request.getRemoteAddr();
					// clientIp =
					// InetAddress.getByName(request.getRemoteAddr()).getHostAddress()
				}
			}
		}
		return clientIp;
	}

	protected void logDebug(int ipCheck, int agentCheck, String clientIp, String loginIp, String clientAgent, String sessionAgent) {
		if (log.isDebugEnabled()) {
			String msg = "ipCheck=" + ipCheck + ":" + getIpCheckLabel(ipCheck) + ", agentCheck=" + agentCheck;
			msg += ", clientIp=" + clientIp;
			msg += ", loginIp=" + loginIp;
			msg += ", clientAgent=" + clientAgent;
			msg += ", sessionAgent=" + sessionAgent;

			log.debug("checking session : " + msg);
		}

	}

	
	protected void logAgentError( String userId, String loginAgent, String clientAgent) {
		if (log.isErrorEnabled()) {
			log.error("Session hjjacking detected! ");
			log.error("userId=" + userId);			
			log.error("loginAgent=" + loginAgent);			
			log.error("clientAgent=" + clientAgent);			
		}
	}
	
	protected void logIpError(String userId, String ipProtect, String clientIp, String loginIp) {
		if (log.isErrorEnabled()) {
			log.error("Session hjjacking detected! ");
			log.error("userId=" + userId);			
			log.error("ipProtect=" + ipProtect);
			log.error("clientIp=" + clientIp);			
			log.error("loginIp=" + loginIp);			
		}
	}
	

	protected String getIpCheckLabel(int ipCheck) {
		if (ipCheck == 0) {
			return "none";
		}
		if (ipCheck == 1) {
			return "class C check";
		}
		if (ipCheck == 3) {
			return "exact ip & ip history";
		}
		if (ipCheck == 3) {
			return "exact ip";
		}
		return "unknown:" + ipCheck;
	}
	
}
