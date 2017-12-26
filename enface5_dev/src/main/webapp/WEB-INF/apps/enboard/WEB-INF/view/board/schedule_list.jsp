<%@ page contentType="text/json; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
{
  "Status": "<c:out value="${form.status}"/>",
  "Reason": "<c:out value="${form.reason}"/>",
  "Data":
  [
<c:forEach items="${bltnList}" var="bltnSchedule" varStatus="status">
	{
		"id": "s_<c:out value="${bltnSchedule.bltnNo}"/>",
		"scheduleId": "<c:out value="${bltnSchedule.bltnNo}"/>",
		"calendarId": "<c:out value="${bltnSchedule.boardId}"/>",
		"allDay": <c:out value="${bltnSchedule.allday}"/>,
		"start": "<c:out value="${bltnSchedule.start}"/>",
		"startTime": "<c:out value="${bltnSchedule.startTime}"/>",
		"end": "<c:out value="${bltnSchedule.end}"/>",
		"endTime": "<c:out value="${bltnSchedule.endTime}"/>",
		"title": "<c:out value="${bltnSchedule.title}" escapeXml="false"/>",
		"contents": "<c:out value="${bltnSchedule.bltnCntt}"/>",
		"place": "<c:out value="${bltnSchedule.bltnPlace}"/>",
		"backgroundColor": "#3a87ad",
		"textColor": "white",
		"className": "boardSchedule"
	} <c:if test="${!status.last}">,</c:if>
</c:forEach>
  ]
}
