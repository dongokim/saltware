<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.saltware.enview.Enview"%>
<%
	String url = request.getContextPath() + Enview.getConfiguration().getString("sso.login.page") + "?destination="+(String)request.getSession().getAttribute("rtnURI");
	url = url.replaceAll("\r", "").replaceAll("\r", "");
	response.sendRedirect ( url);
%>