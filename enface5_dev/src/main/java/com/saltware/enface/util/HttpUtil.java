package com.saltware.enface.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enview.Enview;
import com.saltware.enview.capabilities.Capabilities;

public class HttpUtil {
    protected static Log log = LogFactory.getLog( HttpUtil.class);

	private static String proxyIpKey = Enview.getConfiguration().getString("portal.proxy.ipAddressKey", "client_ip");
	public static String[] validRedirectUrls = null;
	private static final String cookieDatePattern = "EEE, dd-MMM-yyyy HH:mm:ss z";
	private static final ThreadLocal<DateFormat> cookieDateFormat = new ThreadLocal() {
		protected DateFormat initialValue() {
			DateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss z", Locale.US);

			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			return df;
		}
	};

	private static final String ancientDate = ((DateFormat) cookieDateFormat.get()).format(new Date(10000L));

	public static final String getUserAgent(HttpServletRequest request) {
		Capabilities capabilities = (Capabilities) Enview.getComponentManager().getComponent("com.saltware.enview.capabilities.Capabilities");

		return capabilities.getShortAgent(request.getHeader("user-agent"));
	}

	public static final String getClientIp(HttpServletRequest request) {
		String clientIp = request.getHeader(proxyIpKey);
		if (clientIp == null) {
			clientIp = request.getHeader("Proxy-Client-IP");
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
		}
		return clientIp;
	}

	public static void addCookie(HttpServletResponse response, String name, String value) {
		addCookie(response, name, value, null, null, -1, false, true);
	}

	public static void addCookie(HttpServletResponse response, String name, String value, String path, String domain, int maxAge) {
		addCookie(response, name, value, path, domain, maxAge, false, true);
	}

	public static void addCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
		addCookie(response, name, value, path, null, maxAge, false, true);
	}

	public static void addCookie(HttpServletResponse response, String name, String value, String path, String domain, int maxAge, boolean isSecure, boolean isHttpOnly) {
		log.debug( "Add cookie. name=" + name + ",value=" + value + ",path=" + path + ",domain=" + domain + ",maxAge=" + maxAge + ",isSecure=" + isSecure + ",isHttpPnly=" +  isHttpOnly);
		StringBuffer buf = new StringBuffer();
		buf.append(name);
		buf.append("=");
		buf.append(value);

		if (domain != null) {
			buf.append("; Domain=");
			buf.append(domain);
		}

		if (maxAge >= 0) {
			buf.append("; Expires=");
			if (maxAge == 0)
				buf.append(ancientDate);
			else {
				buf.append(((DateFormat) cookieDateFormat.get()).format(new Date(System.currentTimeMillis() + maxAge * 1000L), buf, new FieldPosition(0)));
			}
		}

		if (path != null) {
			buf.append("; Path=");
			buf.append(path);
		}

		if (isSecure) {
			buf.append("; Secure");
		}

		if (isHttpOnly) {
			buf.append("; HttpOnly");
		}
		String s = buf.toString().replace('\r', ' ').replace('\n', ' ');
		response.addHeader("Set-Cookie", s);
	}

	public static String getDomain(HttpServletRequest request) {
		String host = request.getServerName();
		int index;
		if ((index = host.indexOf(".")) != -1) {
			return host.substring(index + 1);
		} else {
			return null;
		}
	}

	public static void sendRedirect(HttpServletResponse response, String url) throws IOException {
		boolean isValid = false;

		if (!url.startsWith("http")) {
			// 동일서버 url
			isValid = true;
		} else {
			// 다른 서버 URL
			if (validRedirectUrls == null) {
				validRedirectUrls = Enview.getConfiguration().getString("portal.redirect.urls", "http://localhost;").split(";");
			}

			for (int i = 0; i < validRedirectUrls.length; i++) {
				if (url.startsWith(validRedirectUrls[i])) {
					isValid = true;
				}
			}
		}

		//
		if (!isValid) {
			response.getWriter().write("Invalid redirect url. " + StringEscapeUtils.escapeHtml(url));
		} else {
			response.sendRedirect(url);
		}
	}
	
	public static String getCookieDomain( HttpServletRequest request) {
		String domain = Enview.getConfiguration().getString("portal.session.domain", "false");
		log.debug("=============================");
		log.debug("** portal.session.domain=" + domain);
		if( "false".equals(domain)) {
			// 도메인 사용안함
			domain = null;
		} else if( "true".equals(domain)) {
			// 호스트명에서 자동 도메인 설정 www.saltware.co.kr -> saltware.co.kr
			domain = request.getServerName();
			log.debug("** host=" + domain);
			int p = domain.indexOf(".");
			if( p!=-1) {
				domain =  domain.substring(p+1);
			}
		}
		log.debug("** domain=" + domain);
		return domain;
	}
}
