<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${isMobile == true }">
		<jsp:include page="mobile_list.jsp" />
	</c:when>
	<c:when test="${boardSttVO.listOpt == 'miniWide'}">
		<jsp:include page="miniWideList.jsp" />
	</c:when>
	<c:when test="${boardSttVO.listOpt == 'miniNarr'}">
		<jsp:include page="miniNarrList.jsp" />
	</c:when>
	<c:otherwise>
		<jsp:include page="list.jsp" />
	</c:otherwise>
</c:choose>
