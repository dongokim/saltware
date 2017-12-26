
<%@ page contentType="text/json; charset=UTF-8"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<%
	//System.out.println("####### portlet=" + request.getAttribute("results"));
%>
{
  "Status": "<util:json value="${inform.resultStatus}"/>",
  "Reason": "<util:json value="${inform.failureReason}"/>",
  "TotalSize": "<util:json value="${inform.totalSize}"/>",
  "ResultSize": "<util:json value="${inform.resultSize}"/>",
  "SelectPortletIds": "<util:json value="${inform.selectPortletIds}"/>",
  "Data":
  [ 
<c:forEach items="${results}" var="portlet" varStatus="status">
	{
		"id" : "<util:json value="${portlet.ID}"/>", 
		"name" : "<util:json value="${portlet.NAME}"/>", 
		"applicationId" :" <util:json value="${portlet.APPLICATION_ID}"/>", 
		"categoryId" :" <util:json value="${portlet.CATEGORY_ID}"/>",
		"appName" : "<util:json value="${portlet.APP_NAME}"/>",
		"displayName" : "<util:json value="${portlet.DISPLAY_NAME}"/>", 
		"description" : "<util:json value="${portlet.DESCRIPTION}"/>",
		"path" : "<util:json value="${portlet.PATH}"/>",
		"title" : "<util:json value="${portlet.TITLE}"/>",
		"icon" : "<util:json value="${portlet.ICON}"/>"
	} ${!status.last ? ',' : ''}
</c:forEach>
  ]
}
