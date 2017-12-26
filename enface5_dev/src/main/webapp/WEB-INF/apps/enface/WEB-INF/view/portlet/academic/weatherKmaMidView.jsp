<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>

<div class="p_10">
	<p>
		<c:forEach items="${resultList}" var="location" varStatus="status" end="0">
		도시:<c:out value="${location.city.text}"></c:out><br>
		<c:forEach items="${location.data}" var="data" varStatus="status">
		${data.tmEf} ${data.wf} (${data.tmn}/${data.tmx})<br>
		</c:forEach>
		</c:forEach>
	</p>
</div>

