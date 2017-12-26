<%@page import="com.saltware.enview.Enview"%>
<%@page import="com.saltware.enview.sso.EnviewSSOManager"%>
<%@page import="com.saltware.enview.security.EnviewMenu"%>
<%@page import="com.saltware.enview.PortalReservedParameters"%>
<%@page import="com.saltware.enview.portalsite.PortalSiteRequestContext"%>
<%@page import="com.saltware.enview.request.RequestContext"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<%
	RequestContext rc = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
	PortalSiteRequestContext site = (PortalSiteRequestContext)request.getAttribute("com.saltware.enview.portalsite.PortalSiteRequestContext");
	String domainPath = Enview.getUserDomain().getPagePath();
	request.setAttribute("majorServices", site.getTopMenu( request, domainPath + "/link/majorServices" ));
	
 %>
 <div class="p_11 main_icon">
	<div class="view">
		<ul>
			<c:forEach items="${majorServices}" var="service" varStatus="status">
				<c:if test="${!service.hidden}">
				<li><span class="icon"><a href="<c:out value="${service.fullUrl}"/>" target='<c:out value="${service.target}"/>' title="${service.shortTitle}"><img src="${themePath}/images/majorServices/${service.name}.png" alt="${service.shortTitle}"/></a></span><span class="txt"><c:out value="${service.shortTitle}"/></span></li>
				</c:if>
			</c:forEach>
		</ul>
	</div>
</div>
