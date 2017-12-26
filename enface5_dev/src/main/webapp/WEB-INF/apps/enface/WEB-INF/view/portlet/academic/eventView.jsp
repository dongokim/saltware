<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="com.saltware.enhancer.event.service.EventUserVO"%>
<%@ page import="java.util.*" %>
<%
	String site_name = (String)request.getAttribute("site_name");
	String langKnd = (String)request.getAttribute("langKnd");
	String detailPage = request.getContextPath() + "/portal" + site_name + "/detailEvent";
 	String morePage = request.getContextPath() + "/portal" + site_name + "/selectMainEventList";
 	List<EventUserVO> eventPortletList = (List<EventUserVO>)request.getAttribute("eventPortletList");
%>

<div class ="p_11 Portlet_normal">
	<h2><util:message key='ev.prop.event.event'/></h2>
	<c:if test="${fn:length(eventPortletList) != 0}">
		<ul class="list">
			<li>
				<span class="title">
	               	<c:forEach items="${eventPortletList}" var="item" varStatus="status" begin="0" end="5">
	                	<dl <c:if test="${status.index == 0}">class="first"</c:if>>
	                        <dt>
	                       		<%-- <a href="event/detailEvent.hanc?HIST_BACK_TYPE=MAIN&EVENT_SEQ=<c:out value ="${item.eventSeq }"/>" target="_parent" ><c:out value="${item.eventTitle }"/> </a> --%>
	                       		<a href="/portal/default/main/academicCalendar/eventList.page"><c:out value="${item.eventTitle }"/> </a>
	                        </dt>
	                    </dl>
	               	</c:forEach>
               		
				</span>
			</li>
		</ul>
	</c:if>
	<c:if test="${fn:length(eventPortletList) == 0}">
		<ul class="list">
    		<util:message key='ev.prop.event.noevent'/>
    	</ul>
    </c:if>
	<c:if test="${fn:length(eventPortletList) != 0}">
		<span class="more"><a href="/portal/default/main/academicCalendar/eventList.page"><util:message key='ev.prop.more'/></a></span>
	</c:if>
</div>