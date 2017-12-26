
<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <completeCredit>

		<stuNo><![CDATA[<c:out value="${completeCredit.stuNo}"/>]]></stuNo>	
		<stuYear><![CDATA[<c:out value="${completeCredit.stuYear}"/>]]></stuYear>	
		<subjType><![CDATA[<c:out value="${completeCredit.subjType}"/>]]></subjType>	
		<subjTypeName><![CDATA[<c:out value="${completeCredit.subjTypeName}"/>]]></subjTypeName>	
		<compCredit><c:out value="${completeCredit.compCredit}"/></compCredit>	
	</completeCredit>
</js>

