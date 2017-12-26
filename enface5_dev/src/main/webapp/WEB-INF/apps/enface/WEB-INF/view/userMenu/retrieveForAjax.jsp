<%@ page contentType="text/json; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<ul>
<c:forEach items="${results}" var="enviewMenu" varStatus="status">
	<li id="<c:out value="${enviewMenu.id}"/>">
		<a href="<%=request.getContextPath()%>/<c:out value="${enviewMenu.url}"/>" target="<c:out value="${enviewMenu.target}"/>">&nbsp;<c:out value="${enviewMenu.shortTitle}"/> </a>
	</li>
</c:forEach>
</ul>