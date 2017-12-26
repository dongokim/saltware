<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
  <c:when test="${isMobile=='t'}">
	<jsp:include page="mobile_edit.jsp"/>
  </c:when>
  <c:otherwise> 
	<jsp:include page="edit.jsp"/>
  </c:otherwise> 
</c:choose>
