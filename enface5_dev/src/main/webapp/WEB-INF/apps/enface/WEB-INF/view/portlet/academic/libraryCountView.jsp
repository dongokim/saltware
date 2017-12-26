<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<div class="p_10 additional">
	<h4><util:message key='ev.prop.bookStatus'/></h4>
	<div class="app_mail">
		<ul>
			<c:if test="${empty libraryCountInfo}">
				<li class="first"><a href="#"><span><util:message key='ev.prop.libraryCount.checkout'/></span><em>00</em></a></li>
				<li><a href="#"><span><util:message key='ev.prop.libraryCount.reservation'/></span><em>00</em></a></li>
				<li><a href="#"><span><util:message key='ev.prop.libraryCount.overdue'/></span><em>00</em></a></li>
			</c:if>
			<c:if test="${not empty libraryCountInfo}">
				<c:forEach items="${libraryCountInfo}" var="cntInfo" varStatus="status">
					<li class="first"><a href="#"><span><util:message key='ev.prop.libraryCount.checkout'/></span><em><c:out value="${cntInfo.checkoutCnt}"/></em></a></li>
					<li><a href="#"><span><util:message key='ev.prop.libraryCount.reservation'/></span><em><c:out value="${cntInfo.reservationCnt}"/></em></a></li>
					<li class="return"><a href="#"><span><util:message key='ev.prop.libraryCount.overdue'/></span><em><c:out value="${cntInfo.overdueCnt}"/></em></a></li>
				</c:forEach>
			</c:if>
		</ul>
	</div>
</div>