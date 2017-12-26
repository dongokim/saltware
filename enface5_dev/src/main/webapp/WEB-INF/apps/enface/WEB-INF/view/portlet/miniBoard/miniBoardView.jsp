<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri='/WEB-INF/tld/portlet.tld' prefix='portlet'%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div>
	<ul>
		<c:forEach items="${results}" var="bltn">
		<li><fmt:formatDate value="${bltn.regDatim}" pattern="yyyy-MM-dd"></fmt:formatDate> <span style="cursor:pointer" onclick="openMiniBoardBltn('${boardId}', '${bltn.bltnNo}', '${moreTarget}', ${moreWidth}, ${moreHeight})">${bltn.bltnSubj}</span> </li>
		</c:forEach>
	</ul>
</div>