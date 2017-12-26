<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri='/WEB-INF/tld/portlet.tld' prefix='portlet'%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>미니보드</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="${skinPath}/css/style.css?version=20131101" rel="stylesheet" type="text/css" />
<c:if test="${empty miniBoardScript}">
<script language="javascript">
	function openMiniBoardBltn( boardId, bltnNo, target, width, height) {
		src = "${pageContext.request.contextPath}/board/read.brd?boardId=" + boardId + "&bltnNo=" + bltnNo + "&cmd=READ";
		openPortletMore( src, target, width, height);
	}
</script>	
<c:set var="miniboardScript" value="true" ></c:set>
</c:if>	

</head>
<body class="login">
	<table width="100%" border="0" cellspacing="2" cellpadding="0" style="table-layout: fixed;">
		<col width="8">
		<col width="65">
		<col width="*">
		<c:forEach items="${results}" var="bltn">
		<tr align="left">
			<td><img src="${skinPath}/images/notice_icon.gif" /></td>
			<td><fmt:formatDate value="${bltn.regDatim}" pattern="yyyy-MM-dd"></fmt:formatDate></td>
			<td><span style="cursor:pointer" onclick="openMiniBoardBltn('${boardId}', '${bltn.bltnNo}', '${moreTarget}', ${moreWidth}, ${moreHeight})">${bltn.bltnSubj}</span> </td>
		</tr>
		</c:forEach>
	</table>
</body>
</html>
