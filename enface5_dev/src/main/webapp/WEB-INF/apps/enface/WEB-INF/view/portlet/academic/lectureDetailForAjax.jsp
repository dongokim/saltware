
<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <lecture>

		<userId><![CDATA[<c:out value="${lecture.userId}"/>]]></userId>	
		<lectYear><![CDATA[<c:out value="${lecture.lectYear}"/>]]></lectYear>	
		<lectSeme><![CDATA[<c:out value="${lecture.lectSeme}"/>]]></lectSeme>	
		<subjName><![CDATA[<c:out value="${lecture.subjName}"/>]]></subjName>	
		<monCount><c:out value="${lecture.monCount}"/></monCount>	
		<tusCount><c:out value="${lecture.tusCount}"/></tusCount>	
		<wedCount><c:out value="${lecture.wedCount}"/></wedCount>	
		<thuCount><c:out value="${lecture.thuCount}"/></thuCount>	
		<friCount><c:out value="${lecture.friCount}"/></friCount>	
		<satCount><c:out value="${lecture.satCount}"/></satCount>	
	</lecture>
</js>

