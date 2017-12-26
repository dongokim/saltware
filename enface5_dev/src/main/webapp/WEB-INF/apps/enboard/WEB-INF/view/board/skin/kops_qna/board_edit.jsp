<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
try {
%>
<c:choose>
	<c:when test="${isMobile==true || isMobilePath}">
	<jsp:include page="mobile_edit.jsp"/>
  </c:when>
  <c:otherwise> 
	<jsp:include page="edit.jsp"/>
  </c:otherwise> 
</c:choose>
<%
} catch( Exception e) {
	e.printStackTrace();
}
%>