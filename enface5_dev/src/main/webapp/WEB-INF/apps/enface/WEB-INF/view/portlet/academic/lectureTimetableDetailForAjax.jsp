
<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <lectureTimetable>

		<stuNo><![CDATA[<c:out value="${lectureTimetable.stuNo}"/>]]></stuNo>	
		<lectYear><![CDATA[<c:out value="${lectureTimetable.lectYear}"/>]]></lectYear>	
		<lectSeme><![CDATA[<c:out value="${lectureTimetable.lectSeme}"/>]]></lectSeme>	
		<lectDow><![CDATA[<c:out value="${lectureTimetable.lectDow}"/>]]></lectDow>	
		<strtHr><c:out value="${lectureTimetable.strtHr}"/></strtHr>	
		<endHr><c:out value="${lectureTimetable.endHr}"/></endHr>	
		<subjName><![CDATA[<c:out value="${lectureTimetable.subjName}"/>]]></subjName>	
	</lectureTimetable>
</js>

