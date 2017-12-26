<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<div class="p_21">
<%--
	<div id="Portlet_tab_normal" class="Portlet_tab_normal m1">
		<ul>
			<c:forEach items="${boardIdList}" var="boardId" varStatus="boardIdSeq">
			<li class="m${boardIdSeq.count}"><a href="#" <c:if test="${boardIdSeq.first}">class='first'</c:if> url='<portlet:resourceURL id="${boardId}"/>'><span><util:message key='ev.prop.boards.${boardId}'/></span></a>
				<ul>
				<c:if test="${boardIdSeq.first}">
				<c:forEach items="${bltnList}" var="bltn" varStatus="status" end="5">
					<li><a href="/portal${domainPath}${menuPath}${boardId}.page?bltnNo=${bltn.bltnNo}" class="title ellipsis">${bltn.bltnSubj}</a><img src="${themePath}/images/main/new_icon.gif" alt="새로운 글" /><span class="date">${bltn.regDatim}</span></li>
				</c:forEach>
				</c:if>
				</ul>
				<span class="more"><a href="/portal${domainPath}${menuPath}${boardId}.page"><util:message key='ev.prop.more'/></a></span>                        
			 </li>
			 </c:forEach>
		</ul>
	</div>
 --%>	
	<div id="Portlet_tab_normal" class="Portlet_tab_normal m1">
		<ul>
			<c:forEach items="${boardIdList}" var="boardId" varStatus="boardIdSeq">
			<li class="m${boardIdSeq.count}"><a href="#" <c:if test="${boardIdSeq.first}">class='first'</c:if> url='<portlet:resourceURL id="${boardId}"/>'><span><util:message key='ev.prop.boards.${boardId}'/></span></a>
				<ul>
				<c:forEach items="${boardBltnList[boardIdSeq.index]}" var="bltn" varStatus="status" end="5">
					<li><a href="/portal${domainPath}${menuPath}${boardId}.page?bltnNo=${bltn.bltnNo}" class="title ellipsis">${bltn.bltnSubj}</a><img src="${themePath}/images/main/new_icon.gif" alt="새로운 글" /><span class="date">${bltn.regDatim}</span></li>
				</c:forEach>
				</ul>
				<span class="more"><a href="/portal${domainPath}${menuPath}${boardId}.page"><util:message key='ev.prop.more'/></a></span>                        
			 </li>
			 </c:forEach>
		</ul>
	</div>
</div>