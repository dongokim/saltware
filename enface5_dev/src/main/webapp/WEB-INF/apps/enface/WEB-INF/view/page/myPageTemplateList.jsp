<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Iterator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<c:forEach items="${results}" var="template" varStatus="status">
<div class="templatePane">
	<p><img title='<c:out value="${template.title}"/>' src='<%=request.getContextPath()%>/images/template/mypage/<c:out value="${template.name}"/>.png' width='70' height='70' onclick="portalMyPageEditor.selectTemplate('<c:out value="${template.name}"/>')"></p>
	<p><input type='radio' id='<c:out value="${template.name}"/>' name='templetCheck' value='<c:out value="${template.path}"/>' class='webcheckbox'/><c:out value="${template.name}"/></p>
</div>
</c:forEach>
