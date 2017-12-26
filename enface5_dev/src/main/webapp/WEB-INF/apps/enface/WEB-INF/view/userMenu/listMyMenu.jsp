<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.saltware.enview.security.EnviewMenu" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Iterator" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<c:forEach items="${results}" var="myMenu">
	<li id="mymenu<c:out value="${myMenu.pageId}"/>" onmouseover="onMouseOverMenu('mymenuButton<c:out value="${myMenu.pageId}"/>')" onmouseout="onMouseOutMenu('mymenuButton<c:out value="${myMenu.pageId}"/>')">
		<span style="width:100px">
			<a href="javascript:goMyMenu('<c:out value="${myMenu.fullUrl}"/>')" ><img src="<%=request.getContextPath()%>/decorations/layout/images/icon_menu01.gif" title="Up" align="absmiddle" border="1">&nbsp;<c:out value="${myMenu.shortTitle}"/></a>&nbsp;&nbsp;
		</span>
		<span id="mymenuButton<c:out value="${myMenu.pageId}"/>" style="display:none;"> 
			<a href="javascript:changeOrderMenu('false', '<c:out value="${myMenu.pageId}"/>')"><img src="<%=request.getContextPath()%>/decorations/layout/images/up.png" title="Up" align="absmiddle" border="1"></a>
			<a href="javascript:changeOrderMenu('true', '<c:out value="${myMenu.pageId}"/>')"><img src="<%=request.getContextPath()%>/decorations/layout/images/down.png" title="Down" align="absmiddle" border="1"></a>
			<a href="javascript:deleteMenu('<c:out value="${myMenu.pageId}"/>', '<c:out value="${myMenu.domainId}"/>')"><img src="<%=request.getContextPath()%>/decorations/layout/images/icon_del.gif" title="Remove" align="absmiddle" border="1"></a>
		</span>
	</li>
</c:forEach>
