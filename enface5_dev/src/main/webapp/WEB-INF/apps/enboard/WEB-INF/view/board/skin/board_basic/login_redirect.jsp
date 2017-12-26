<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.saltware.enview.Enview"%>
<%
	response.sendRedirect (request.getContextPath() + Enview.getConfiguration().getString("sso.login.page") + "?destination="+(String)request.getSession().getAttribute("rtnURI"));
%>