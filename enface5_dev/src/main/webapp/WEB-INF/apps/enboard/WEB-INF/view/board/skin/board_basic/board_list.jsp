<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:choose>
	<c:when test="${isMobile==true || isMobilePath==true}">
		<jsp:include page="mobile_list.jsp"/>
	</c:when>
	<c:when test="${boardSttVO.listOpt == 'mini'}">
		<jsp:include page="miniList.jsp"/>
	</c:when>
	<c:when test="${boardSttVO.listOpt == 'mini1'}">
		<jsp:include page="miniList1.jsp"/>
	</c:when>
	<c:when test="${boardSttVO.listOpt == 'mini2'}">
		<jsp:include page="miniList2.jsp"/>
	</c:when>
	<c:when test="${boardSttVO.listOpt == 'mini3'}">
		<jsp:include page="miniList3.jsp"/>
	</c:when>
	<c:when test="${boardSttVO.listOpt == 'inqueryMini1'}">
		<jsp:include page="inqueryMini1.jsp"/>
	</c:when>
	<c:when test="${boardSttVO.listOpt == 'inqueryMiniMobile1'}">
		<jsp:include page="inqueryMiniMobile1.jsp"/>
	</c:when>
	<c:when test="${boardSttVO.listOpt == 'mainMini'}">
		<jsp:include page="mainMini.jsp"/>
	</c:when>
	<c:otherwise> 
		<jsp:include page="list.jsp"/>
	</c:otherwise> 
</c:choose>
