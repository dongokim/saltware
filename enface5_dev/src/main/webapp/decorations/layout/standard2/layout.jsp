<%@page import="com.saltware.enview.om.page.Fragment"%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="../initLayoutDecorators.jsp" %>
<%@page import="com.saltware.enview.aggregator.PortletRenderer"%>
<%@page import="com.saltware.enview.components.portletregistry.PortletRegistry"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<c:if test="${fn:containsIgnoreCase( currentFragment.name, 'gridster')}">
<%@include file="../common/layoutGridster.jsp"%>
</c:if>
<c:if test="${not fn:containsIgnoreCase( currentFragment.name, 'gridster')}">
<%@include file="../common/layoutColumn.jsp"%>
</c:if>

