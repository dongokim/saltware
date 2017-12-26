<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${isMobile == true}">
		<jsp:include page="mobile_read.jsp" />
	</c:when>
	<c:otherwise>
		<jsp:include page="read.jsp" />
	</c:otherwise>
</c:choose>
