
<%@ page contentType="text/json; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
{
  "Status": "<c:out value="${inform.resultStatus}"/>",
  "Reason": "<c:out value="${inform.failureReason}"/>",
  "TotalSize": "<c:out value="${inform.totalSize}"/>",
  "ResultSize": "<c:out value="${inform.resultSize}"/>",
  "Data":
  [
<c:forEach items="${results}" var="sample" varStatus="status">
	{
"principalId": "<c:out value="${sample.principalId}"/>"
,"parentId": "<c:out value="${sample.parentId}"/>"
,"shortPath": "<c:out value="${sample.shortPath}"/>"
,"principalName": "<c:out value="${sample.principalName}"/>"
,"theme": "<c:out value="${sample.theme}"/>"
,"defaultPage": "<c:out value="${sample.defaultPage}"/>"
,"principalType": "<c:out value="${sample.principalType}"/>"
,"principalOrder": "<c:out value="${sample.principalOrder}"/>"
,"creationDate": "<c:out value="${sample.creationDateByFormat}"/>"
,"modifiedDate": "<c:out value="${sample.modifiedDateByFormat}"/>"
,"principalInfo01": "<c:out value="${sample.principalInfo01}"/>"
,"principalInfo02": "<c:out value="${sample.principalInfo02}"/>"
,"principalInfo03": "<c:out value="${sample.principalInfo03}"/>"
,"principalDesc": "<c:out value="${sample.principalDesc}"/>"

	} <c:if test="${!status.last}">,</c:if>
</c:forEach>
  ]
}
