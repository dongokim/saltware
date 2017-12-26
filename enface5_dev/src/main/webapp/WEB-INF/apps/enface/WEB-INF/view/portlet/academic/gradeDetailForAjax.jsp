
<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <grade>

		<stuNo><![CDATA[<c:out value="${grade.stuNo}"/>]]></stuNo>	
		<stuYear><![CDATA[<c:out value="${grade.stuYear}"/>]]></stuYear>	
		<stuSeme><![CDATA[<c:out value="${grade.stuSeme}"/>]]></stuSeme>	
		<subjName><![CDATA[<c:out value="${grade.subjName}"/>]]></subjName>	
		<gainGrade><c:out value="${grade.gainGrade}"/></gainGrade>	
		<score><![CDATA[<c:out value="${grade.score}"/>]]></score>	
	</grade>
</js>

