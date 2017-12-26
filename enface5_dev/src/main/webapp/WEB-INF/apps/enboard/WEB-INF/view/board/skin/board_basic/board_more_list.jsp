<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <c:choose>
  <c:when test="${isMobile==true}">
	<jsp:include page="mobile_more_list.jsp"/>
  </c:when>
  <c:otherwise> 
	<jsp:include page="mobile_more_list.jsp"/>
  </c:otherwise> 
  </c:choose>
</table>
