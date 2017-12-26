
<%@ page contentType="text/json; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
{
   "Status": "<c:out value="${inform.resultStatus}"/>"
  ,"Reason": "<c:out value="${inform.failureReason}"/>"

  ,"principalId": "<c:out value="${userMenu.principalId}"/>"
  ,"pageId": "<c:out value="${userMenu.pageId}"/>"
  ,"parentId": "<c:out value="${userMenu.parentId}"/>"
  ,"menuType": "<c:out value="${userMenu.menuType}"/>"
  ,"menuOrder": "<c:out value="${userMenu.menuOrder}"/>"
}
