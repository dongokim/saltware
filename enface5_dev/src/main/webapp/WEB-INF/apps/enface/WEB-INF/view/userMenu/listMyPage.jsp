<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.saltware.enview.security.EnviewMenu" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Iterator" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<c:forEach items="${myPageList}" var="myPage" varStatus="status">
	<li id="mymenu<c:out value="${myPage.pageId}"/>" onmouseover="onMouseOverMenu('mymenuButton<c:out value="${myPage.pageId}"/>')" onmouseout="onMouseOutMenu('mymenuButton<c:out value="${myPage.pageId}"/>')">
		<span style="width:100px">
			<a href="javascript:goMenu('<c:out value="${myPage.fullUrl}"/>')" ><img src="<%=request.getContextPath()%>/decorations/layout/images/icon_menu01.gif" title="Up" align="absmiddle" border="1">&nbsp;<c:out value="${myPage.title}"/></a>&nbsp;&nbsp;
		</span>
		<span id="mymenuButton<c:out value="${myPage.pageId}"/>" style="display:none;"> 
			<a href="javascript:changeOrderMyPage('false', '<c:out value="${myPage.pageId}"/>')"><img src="<%=request.getContextPath()%>/decorations/layout/images/up.png" title="Up" align="absmiddle" border="1"></a>
			<a href="javascript:changeOrderMyPage('true', '<c:out value="${myPage.pageId}"/>')"><img src="<%=request.getContextPath()%>/decorations/layout/images/down.png" title="Down" align="absmiddle" border="1"></a>
			<a onclick="portalMyPageEditor.doRemove('<c:out value="${myPage.pageId}"/>', '<c:out value="${myPage.path}"/>')" ><img src="<%=request.getContextPath()%>/decorations/layout/images/icon_del.gif" align="absmiddle" alt="<util:message key='pt.ev.mypage.remove'/>" title="<util:message key='pt.ev.mypage.remove'/>"></a>
			<a href="javascript:portalMyPageEditor.setDefaultMyPage('<c:out value="${logoutUrl}"/>', '<c:out value="${myPage.pageId}"/>', '<c:out value="${myPage.path}"/>')"><img src="<%=request.getContextPath()%>/decorations/layout/images/home.gif" align="absmiddle" alt="<util:message key='pt.ev.mypage.homepage'/>" title="<util:message key='pt.ev.mypage.homepage'/>"></a>
		</span>
	</li>
</c:forEach>
						