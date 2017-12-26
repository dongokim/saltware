<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<div class="p_10 additional">

	<h4><util:message key='ev.prop.groupWareCount.electronicApproval'/></h4>
	<div class="app_mail">
		<ul>
			<c:if test="${empty groupWareCountInfo}">
				<li class="first"><a href="#"><span><util:message key='ev.prop.groupWareCount.receive'/></span><em>00</em></a></li>
				<li><a href="#"><span><util:message key='ev.prop.groupWareCount.approval'/></span><em>00</em></a></li>
				<li><a href="#"><span><util:message key='ev.prop.groupWareCount.gongram'/></span><em>00</em></a></li>
			</c:if>
			<c:if test="${not empty groupWareCountInfo}">
				<c:forEach items="${groupWareCountInfo}" var="cntInfo" varStatus="status">
					<li class="first"><a href="#"><span><util:message key='ev.prop.groupWareCount.receive'/></span><em><c:out value="${cntInfo.recvCnt}"/></em></a></li>
					<li><a href="#"><span><util:message key='ev.prop.groupWareCount.approval'/></span><em><c:out value="${cntInfo.apprCnt}"/></em></a></li>
					<li class="return"><a href="#"><span><util:message key='ev.prop.groupWareCount.gongram'/></span><em><c:out value="${cntInfo.gramCnt}"/></em></a></li>
				</c:forEach>
			</c:if>
		</ul>
	</div>
</div>