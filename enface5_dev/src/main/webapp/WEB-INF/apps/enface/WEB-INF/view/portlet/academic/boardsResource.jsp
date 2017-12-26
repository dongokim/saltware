<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<ul>
<c:forEach items="${bltnList}" var="bltn" varStatus="status" end="5">
	<li><a href="/portal${domainPath}${menuPath}${boardId}.page?bltnNo=${bltn.bltnNo}" class="title ellipsis">${bltn.bltnSubj}</a><img src="${themePath}/images/main/new_icon.gif" alt="새로운 글" /><span class="date">${bltn.regDatim}</span></li>
</c:forEach>
</ul>
